package com.by.dallinday.run;

import com.by.dallinday.common.exception.BusinessLogicException;
import com.by.dallinday.common.exception.ExceptionCode;
import com.by.dallinday.course.Course;
import com.by.dallinday.course.CourseRepository;
import com.by.dallinday.member.Member;
import com.by.dallinday.member.MemberRepository;
import com.by.dallinday.run.dto.RunPostRequest;
import com.by.dallinday.run.dto.RunPostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RunService {

    private final RunMapper runMapper;
    private final RunRepository runRepository;
    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;

    // 달리기 기록 생성
    public RunPostResponse createRun(RunPostRequest request) {
        // dto -> entity
        Run run = runMapper.runPostRequestToRun(request);

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COURSE_NOT_FOUND));

        run.setMember(member);
        run.setCourse(course);

        // 서비스 로직
        runRepository.save(run);

        // entity -> dto
        return runMapper.runToRunPostResponse(run);
    }

    // 달리기 기록 조회
    public Run findRun() {
        return new Run();
    }
}
