package com.by.dallinday.run;

import com.by.dallinday.common.exception.BusinessLogicException;
import com.by.dallinday.common.exception.ExceptionCode;
import com.by.dallinday.course.Course;
import com.by.dallinday.course.CourseRepository;
import com.by.dallinday.member.Member;
import com.by.dallinday.member.MemberRepository;
import com.by.dallinday.run.dto.RunMapper;
import com.by.dallinday.run.dto.RunPostRequest;
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
    public Run createRun(Long memberId, RunPostRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COURSE_NOT_FOUND));

        Run run = runMapper.runPostRequestToRun(request);
        run.setMember(member);
        run.setCourse(course);

        runRepository.save(run);
        return run;
    }

    // 달리기 기록 조회
    public Run findRun() {
        return new Run();
    }
}
