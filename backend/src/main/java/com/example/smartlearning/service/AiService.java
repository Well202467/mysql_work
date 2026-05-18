package com.example.smartlearning.service;

import com.example.smartlearning.common.Result;
import com.example.smartlearning.dto.AiChatRequest;
import com.example.smartlearning.vo.AiChatVO;

public interface AiService {

    Result<AiChatVO> chat(AiChatRequest request);
}
