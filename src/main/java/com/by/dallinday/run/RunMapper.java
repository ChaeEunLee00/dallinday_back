package com.by.dallinday.run;

import com.by.dallinday.run.dto.RunPostRequest;
import com.by.dallinday.run.dto.RunPostResponse;
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

    public RunPostResponse runToRunPostResponse(Run run) {
        if ( run == null ) {
            return null;
        }

        RunPostResponse runPostResponse = new RunPostResponse();

        runPostResponse.setRunId( run.getRunId());
        runPostResponse.setMemberId( run.getMember().getMemberId());
        runPostResponse.setCourseId( run.getCourse().getCourseId());
        runPostResponse.setDistance( run.getDistance() );
        runPostResponse.setDuration( run.getDuration() );
        runPostResponse.setPace( run.getPace() );
        runPostResponse.setCalorie( run.getCalorie() );
        runPostResponse.setAccuracy( run.getAccuracy() );
        runPostResponse.setStartTime( run.getStartTime() );
        runPostResponse.setEndTime( run.getEndTime() );

        return runPostResponse;
    }
}
