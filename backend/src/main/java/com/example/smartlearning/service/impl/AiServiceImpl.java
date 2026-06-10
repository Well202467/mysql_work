package com.example.smartlearning.service.impl;

import com.example.smartlearning.common.Result;
import com.example.smartlearning.config.DeepSeekProperties;
import com.example.smartlearning.dto.AiChatRequest;
import com.example.smartlearning.service.AiService;
import com.example.smartlearning.vo.AiChatVO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
public class AiServiceImpl implements AiService {

    private static final String SYSTEM_PROMPT = "你是软件学院职业导航网站的AI学习助手。你的任务是帮助学生理解专业、课程、知识点、职业方向和学习路径。回答要面向大学生，语言清晰、具体、有学习建议。不要编造学校不存在的政策或数据。如果问题超出专业学习和职业规划范围，可以简要回答，但要尽量引导回学习、课程、职业发展。";
    private static final String EMPTY_MESSAGE_TIP = "请输入你想咨询的问题。";
    private static final int MAX_ERROR_BODY_LENGTH = 800;

    private final DeepSeekProperties deepSeekProperties;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public AiServiceImpl(DeepSeekProperties deepSeekProperties, ObjectMapper objectMapper) {
        this.deepSeekProperties = deepSeekProperties;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }

    @Override
    public Result<AiChatVO> chat(AiChatRequest request) {
        String message = normalize(request == null ? null : request.getMessage());
        if (message.isEmpty()) {
            return Result.success(new AiChatVO(EMPTY_MESSAGE_TIP));
        }

        String apiKey = normalize(deepSeekProperties.getApiKey());
        if (apiKey.isEmpty()) {
            return Result.error("DeepSeek API Key 未配置，请设置环境变量 DEEPSEEK_API_KEY。");
        }

        String model = normalize(deepSeekProperties.getModel());
        if (model.isEmpty()) {
            return Result.error("DeepSeek model 未配置，请检查 application.yml 中 deepseek.model。");
        }

        String baseUrl = normalize(deepSeekProperties.getBaseUrl());
        if (baseUrl.isEmpty()) {
            return Result.error("DeepSeek base-url 未配置，请检查 application.yml 中 deepseek.base-url。");
        }

        try {
            String requestBody = buildRequestBody(model, message);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(buildChatCompletionsUrl(baseUrl)))
                    .timeout(Duration.ofSeconds(60))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                return Result.error("DeepSeek 调用失败，HTTP " + response.statusCode() + "：" + extractErrorMessage(response.body()));
            }

            String answer = extractAnswer(response.body());
            if (answer.isEmpty()) {
                return Result.error("DeepSeek 响应解析失败：未找到 choices[0].message.content。原始响应：" + truncate(response.body()));
            }

            return Result.success(new AiChatVO(answer));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Result.error("DeepSeek 调用被中断：" + e.getMessage());
        } catch (IllegalArgumentException e) {
            return Result.error("DeepSeek 请求地址配置错误：" + e.getMessage());
        } catch (IOException e) {
            return Result.error("DeepSeek 网络调用失败：" + e.getMessage());
        }
    }

    private String buildRequestBody(String model, String message) throws IOException {
        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("model", model);

        ArrayNode messages = payload.putArray("messages");
        ObjectNode systemMessage = messages.addObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", SYSTEM_PROMPT);

        ObjectNode userMessage = messages.addObject();
        userMessage.put("role", "user");
        userMessage.put("content", message);

        return objectMapper.writeValueAsString(payload);
    }

    private String extractAnswer(String responseBody) throws IOException {
        JsonNode root = objectMapper.readTree(responseBody);
        return normalize(root.path("choices").path(0).path("message").path("content").asText());
    }

    private String extractErrorMessage(String responseBody) {
        String body = normalize(responseBody);
        if (body.isEmpty()) {
            return "响应体为空";
        }

        try {
            String message = normalize(objectMapper.readTree(body).path("error").path("message").asText());
            return message.isEmpty() ? truncate(body) : message;
        } catch (IOException e) {
            return truncate(body);
        }
    }

    private String buildChatCompletionsUrl(String baseUrl) {
        return removeTrailingSlash(baseUrl) + "/chat/completions";
    }

    private String removeTrailingSlash(String value) {
        String result = value;
        while (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private String truncate(String value) {
        String text = normalize(value);
        if (text.length() <= MAX_ERROR_BODY_LENGTH) {
            return text;
        }
        return text.substring(0, MAX_ERROR_BODY_LENGTH) + "...";
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
