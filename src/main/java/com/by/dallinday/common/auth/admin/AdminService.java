package com.by.dallinday.common.auth.admin;

import com.by.dallinday.common.auth.jwt.JwtTokenizer;
import com.by.dallinday.common.exception.BusinessLogicException;
import com.by.dallinday.common.exception.ExceptionCode;
import com.by.dallinday.course.Course;
import com.by.dallinday.course.CourseRepository;
import com.by.dallinday.member.Member;
import com.by.dallinday.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;

    // 관리자 로그인
    public LoginResponse login(LoginRequest request) {
        // 아이디 불일치
        Member member = memberRepository.findByEmail(request.getId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        // 비밀번호 불일치
        if (!passwordEncoder.matches(request.getPassword(), member.getProviderId())) {
            throw new BusinessLogicException(ExceptionCode.PASSWORD_MISMATCH);
        }

        // 관리자 로그인 성공 - JWT 생성
        String base64EncodedSecretKey = jwtTokenizer.encodedBase64SecretKey(jwtTokenizer.getSecretKey());
        String accessToken = jwtTokenizer.generateAccessToken(member.getMemberId(), base64EncodedSecretKey);
        String refreshToken = jwtTokenizer.generateRefreshToken(member.getMemberId(), base64EncodedSecretKey);

        log.info("accessToken : {}", accessToken);
        log.info("refreshToken : {}", refreshToken);

        return new LoginResponse(accessToken, refreshToken, member.getMemberId());
    }

    // 코스 생성 (관리자용)
    public Course createCourse(Course course) {
        // 이미 존재하는 코스 아이디인지 확인
        Optional<Course> existingCourse = courseRepository.findById(course.getCourseId());
        if (existingCourse.isPresent()) throw new BusinessLogicException(ExceptionCode.COURSE_EXIST);

        return courseRepository.save(course);
    }

    // 코스 수정 (관리자용)
    public Course updateCourse(String courseId, Course updatedCourse) {
        // 해당 코스가 존재하는지 확인
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COURSE_NOT_FOUND));

        // Optional.ofNullable 패턴으로 일부 필드만 수정
        Optional.ofNullable(updatedCourse.getCrsKorNm())
                .ifPresent(existingCourse::setCrsKorNm);

        Optional.ofNullable(updatedCourse.getCrsDstnc())
                .ifPresent(existingCourse::setCrsDstnc);

        Optional.ofNullable(updatedCourse.getCrsTotlRqrmMin())
                .ifPresent(existingCourse::setCrsTotlRqrmMin);

        Optional.ofNullable(updatedCourse.getCrsLevel())
                .ifPresent(existingCourse::setCrsLevel);

        Optional.ofNullable(updatedCourse.getCrsSummary())
                .ifPresent(existingCourse::setCrsSummary);

        Optional.ofNullable(updatedCourse.getCrsTourInfo())
                .ifPresent(existingCourse::setCrsTourInfo);

        Optional.ofNullable(updatedCourse.getGpxpath())
                .ifPresent(existingCourse::setGpxpath);

        Optional.ofNullable(updatedCourse.getModifiedtime())
                .ifPresent(existingCourse::setModifiedtime);

        return courseRepository.save(existingCourse);
    }


    // 코스 삭제 (관리자용)
    public void removeCourse(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COURSE_NOT_FOUND));

        courseRepository.delete(course);
    }
}
