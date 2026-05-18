package com.example.smartlearning.controller;

import com.example.smartlearning.common.Result;
import com.example.smartlearning.dto.AiChatRequest;
import com.example.smartlearning.service.AiService;
import com.example.smartlearning.vo.AiChatVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "AI 学习助手接口")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/chat")
    @Operation(summary = "首页 AI 学习助手对话")
    public Result<AiChatVO> chat(@RequestBody(required = false) AiChatRequest request) {
        return aiService.chat(request);
    }
}
