package com.by.dallinday.run;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/runs")
public class RunController {
    private final RunService runService;

    // 달리기 기록 생성
    @PostMapping
    public ResponseEntity postRun() {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 달리기 기록 조회
    @GetMapping("/{run-id}")
    public ResponseEntity getRun() {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
