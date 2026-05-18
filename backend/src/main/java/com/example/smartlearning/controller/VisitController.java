package com.example.smartlearning.controller;

import com.example.smartlearning.common.Result;
import com.example.smartlearning.dto.VisitRecordDTO;
import com.example.smartlearning.service.VisitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/visit")
public class VisitController {

    private static final Logger log = LoggerFactory.getLogger(VisitController.class);

    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping("/record")
    public Result<Void> record(@RequestBody(required = false) VisitRecordDTO dto, HttpServletRequest request) {
        try {
            visitService.recordVisit(dto, request);
        } catch (Exception e) {
            log.warn("Record visit failed", e);
        }
        return Result.success();
    }
}
