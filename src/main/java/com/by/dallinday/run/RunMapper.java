package com.by.dallinday.run;

import com.by.dallinday.run.dto.RunPostRequest;
import com.by.dallinday.run.dto.RunResponse;
import org.springframework.stereotype.Component;

@Component
public class RunMapper {

    public Run runPostRequestToRun(RunPostRequest runPostRequest) {
        if ( runPostRequest == null ) {
            return null;
        }

        Run run = new Run();

        run.setDistance( runPostRequest.getDistance() );
        run.setDuration( runPostRequest.getDuration() );
        run.setPace( runPostRequest.getPace() );
        run.setCalorie( runPostRequest.getCalorie() );
        run.setAccuracy( runPostRequest.getAccuracy() );
        run.setStartTime( runPostRequest.getStartTime() );
        run.setEndTime( runPostRequest.getEndTime() );

        return run;
    }

    public RunResponse runToRunResponse(Run run) {
        if ( run == null ) {
            return null;
        }

        RunResponse runResponse = new RunResponse();

        runResponse.setRunId( run.getRunId());
        runResponse.setMemberId( run.getMember().getMemberId());
        runResponse.setCourseId( run.getCourse().getCourseId());
        runResponse.setDistance( run.getDistance() );
        runResponse.setDuration( run.getDuration() );
        runResponse.setPace( run.getPace() );
        runResponse.setCalorie( run.getCalorie() );
        runResponse.setAccuracy( run.getAccuracy() );
        runResponse.setStartTime( run.getStartTime() );
        runResponse.setEndTime( run.getEndTime() );

        return runResponse;
    }
}
