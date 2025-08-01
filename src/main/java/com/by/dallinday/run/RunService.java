package com.by.dallinday.run;

import com.by.dallinday.common.exception.BusinessLogicException;
import com.by.dallinday.common.exception.ExceptionCode;
import com.by.dallinday.course.Course;
import com.by.dallinday.course.CourseRepository;
import com.by.dallinday.member.Member;
import com.by.dallinday.member.MemberRepository;
import com.by.dallinday.run.dto.RunPostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RunService {
    private final RunRepository runRepository;
    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;

    // 달리기 기록 생성
    public Run createRun(Long memberId, RunPostRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COURSE_NOT_FOUND));

        Run run = new Run();
        run.setMember(member);
        run.setCourse(course);
        run.setDistance(request.getDistance());
        run.setDuration(request.getDuration());
        run.setCalorie(request.getCalorie());
        run.setAccuracy(request.getAccuracy());
        run.setStartTime(request.getStartTime());
        run.setEndTime(request.getEndTime());

        runRepository.save(run);
        return run;
    }

    // 달리기 기록 조회
    public Run findRun() {
        return new Run();
    }
}
