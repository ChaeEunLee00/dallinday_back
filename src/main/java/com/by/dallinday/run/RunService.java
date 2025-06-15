package com.by.dallinday.run;

import org.springframework.stereotype.Service;

@Service
public class RunService {
    private final RunRepository runRepository;

    public RunService(RunRepository runRepository) {
        this.runRepository = runRepository;
    }

    // 달리기 기록 생성
    public Run createRun() {
        return new Run();
    }

    // 달리기 기록 조회
    public Run findRun() {
        return new Run();
    }
}
