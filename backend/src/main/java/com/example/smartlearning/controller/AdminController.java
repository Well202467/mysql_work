package com.example.smartlearning.controller;

import com.example.smartlearning.common.ApiResponse;
import com.example.smartlearning.dto.AdminLoginDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "管理员登录接口")
public class AdminController {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "123456";
    private static final String ADMIN_TOKEN = "admin-token";

    @PostMapping("/login")
    @Operation(summary = "管理员登录")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody AdminLoginDTO loginDTO) {
        if (loginDTO != null
                && ADMIN_USERNAME.equals(loginDTO.getUsername())
                && ADMIN_PASSWORD.equals(loginDTO.getPassword())) {
            Map<String, String> data = Map.of("token", ADMIN_TOKEN);
            return ResponseEntity.ok(new ApiResponse<>(200, "登录成功", data));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.unauthorized("用户名或密码错误"));
    }
}
