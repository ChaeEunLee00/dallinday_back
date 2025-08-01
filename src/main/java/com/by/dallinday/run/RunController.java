package com.by.dallinday.run;

import com.by.dallinday.run.dto.RunPostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/runs")
public class RunController {
    private final RunService runService;

    // 달리기 기록 생성
    @PostMapping
    public ResponseEntity postRun(@RequestBody RunPostRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> principal = (Map<String, Object>) authentication.getPrincipal();
        Long memberId = Long.valueOf(principal.get("memberId").toString());

        Run run = runService.createRun(memberId, request);
        return new ResponseEntity<>(run, HttpStatus.OK);
    }

    // 달리기 기록 조회
    @GetMapping("/{run-id}")
    public ResponseEntity getRun() {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
