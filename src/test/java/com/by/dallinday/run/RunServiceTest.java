package com.by.dallinday.run;

import com.by.dallinday.common.exception.BusinessLogicException;
import com.by.dallinday.common.exception.ExceptionCode;
import com.by.dallinday.course.Course;
import com.by.dallinday.course.CourseRepository;
import com.by.dallinday.member.Member;
import com.by.dallinday.member.MemberRepository;
import com.by.dallinday.ranking.Ranking;
import com.by.dallinday.ranking.RankingRepository;
import com.by.dallinday.run.dto.RunPostRequest;
import com.by.dallinday.run.dto.RunResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RunService 단위 테스트")
class RunServiceTest {

    @Mock
    private RunMapper runMapper;

    @Mock
    private RunRepository runRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private RankingRepository rankingRepository;

    @InjectMocks
    private RunService runService;

    private Run testRun;
    private Member testMember;
    private Course testCourse;
    private RunPostRequest testRunPostRequest;
    private RunResponse testRunResponse;

    @BeforeEach
    void setUp() {
        testMember = new Member();
        testMember.setMemberId(1L);
        testMember.setEmail("test@test.com");
        testMember.setUsername("테스트유저");
        testMember.setProvider("kakao");
        testMember.setProviderId("12345");
        testMember.setRole("USER");
        testMember.setTotalDistance(100.0);
        testMember.setTotalDuration(600.0);
        testMember.setAvgPace(6.0);

        testCourse = new Course();
        testCourse.setCourseId(1L);
        testCourse.setName("테스트 코스");
        testCourse.setDistance(5.0);
        testCourse.setDuration(60);
        testCourse.setCourseSpotList(new ArrayList<>());
        testCourse.setRunList(new ArrayList<>());

        testRun = new Run();
        testRun.setRunId(1L);
        testRun.setMember(testMember);
        testRun.setCourse(testCourse);
        testRun.setDistance(5.0);
        testRun.setDuration(30.0);
        testRun.setPace(6.0);
        testRun.setCalorie(300.0);
        testRun.setAccuracy(95.0);
        testRun.setStartTime(LocalDateTime.now().minusMinutes(30));
        testRun.setEndTime(LocalDateTime.now());

        testRunPostRequest = new RunPostRequest();
        testRunPostRequest.setMemberId(1L);
        testRunPostRequest.setCourseId(1L);
        testRunPostRequest.setDistance(5.0);
        testRunPostRequest.setDuration(30.0);
        testRunPostRequest.setPace(6.0);
        testRunPostRequest.setCalorie(300.0);
        testRunPostRequest.setAccuracy(95.0);
        testRunPostRequest.setStartTime(LocalDateTime.now().minusMinutes(30));
        testRunPostRequest.setEndTime(LocalDateTime.now());

        testRunResponse = new RunResponse();
        testRunResponse.setRunId(1L);
        testRunResponse.setMemberId(1L);
        testRunResponse.setCourseId(1L);
        testRunResponse.setDistance(5.0);
        testRunResponse.setDuration(30.0);
        testRunResponse.setPace(6.0);
    }

    @Nested
    @DisplayName("createRun 메서드")
    class CreateRunTest {

        @Test
        @DisplayName("코스 포함 달리기 기록을 성공적으로 생성한다")
        void createRun_withCourse_createsSuccessfully() {
            // Given
            Run newRun = new Run();
            newRun.setDistance(5.0);
            newRun.setDuration(30.0);
            newRun.setStartTime(LocalDateTime.now());

            String yearMonth = YearMonth.from(testRunPostRequest.getStartTime()).toString();

            given(runMapper.runPostRequestToRun(testRunPostRequest)).willReturn(newRun);
            given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
            given(courseRepository.findById(1L)).willReturn(Optional.of(testCourse));
            given(rankingRepository.findByMemberAndRankingMonth(any(Member.class), anyString()))
                    .willReturn(Optional.empty());
            given(rankingRepository.findByRankingMonthOrderByMonthlyTotalDistanceDesc(anyString()))
                    .willReturn(new ArrayList<>());
            given(runMapper.runToRunResponse(any(Run.class))).willReturn(testRunResponse);

            // When
            RunResponse result = runService.createRun(testRunPostRequest);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getRunId()).isEqualTo(1L);
            verify(runRepository, times(1)).save(any(Run.class));
            verify(memberRepository, times(1)).save(any(Member.class));
        }

        @Test
        @DisplayName("코스 없이 달리기 기록을 성공적으로 생성한다")
        void createRun_withoutCourse_createsSuccessfully() {
            // Given
            RunPostRequest requestWithoutCourse = new RunPostRequest();
            requestWithoutCourse.setMemberId(1L);
            requestWithoutCourse.setCourseId(null);
            requestWithoutCourse.setDistance(3.0);
            requestWithoutCourse.setDuration(20.0);
            requestWithoutCourse.setPace(6.67);
            requestWithoutCourse.setCalorie(200.0);
            requestWithoutCourse.setStartTime(LocalDateTime.now().minusMinutes(20));
            requestWithoutCourse.setEndTime(LocalDateTime.now());

            Run newRun = new Run();
            newRun.setDistance(3.0);
            newRun.setDuration(20.0);
            newRun.setStartTime(requestWithoutCourse.getStartTime());

            RunResponse responseWithoutCourse = new RunResponse();
            responseWithoutCourse.setRunId(2L);
            responseWithoutCourse.setMemberId(1L);
            responseWithoutCourse.setDistance(3.0);

            given(runMapper.runPostRequestToRun(requestWithoutCourse)).willReturn(newRun);
            given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
            given(rankingRepository.findByMemberAndRankingMonth(any(Member.class), anyString()))
                    .willReturn(Optional.empty());
            given(rankingRepository.findByRankingMonthOrderByMonthlyTotalDistanceDesc(anyString()))
                    .willReturn(new ArrayList<>());
            given(runMapper.runToRunResponse(any(Run.class))).willReturn(responseWithoutCourse);

            // When
            RunResponse result = runService.createRun(requestWithoutCourse);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getCourseId()).isNull();
            verify(courseRepository, never()).findById(anyLong());
        }

        @Test
        @DisplayName("멤버가 존재하지 않으면 예외가 발생한다")
        void createRun_memberNotFound_throwsException() {
            // Given
            Run newRun = new Run();
            given(runMapper.runPostRequestToRun(testRunPostRequest)).willReturn(newRun);
            given(memberRepository.findById(1L)).willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> runService.createRun(testRunPostRequest))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.MEMBER_NOT_FOUND);
        }

        @Test
        @DisplayName("코스가 존재하지 않으면 예외가 발생한다")
        void createRun_courseNotFound_throwsException() {
            // Given
            Run newRun = new Run();
            newRun.setStartTime(LocalDateTime.now());
            given(runMapper.runPostRequestToRun(testRunPostRequest)).willReturn(newRun);
            given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
            given(courseRepository.findById(1L)).willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> runService.createRun(testRunPostRequest))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.COURSE_NOT_FOUND);
        }

        @Test
        @DisplayName("달리기 생성 시 멤버 통계가 업데이트된다")
        void createRun_updatesMemberStats() {
            // Given
            double initialTotalDistance = testMember.getTotalDistance();
            double initialTotalDuration = testMember.getTotalDuration();

            Run newRun = new Run();
            newRun.setDistance(5.0);
            newRun.setDuration(30.0);
            newRun.setStartTime(LocalDateTime.now());

            given(runMapper.runPostRequestToRun(testRunPostRequest)).willReturn(newRun);
            given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
            given(courseRepository.findById(1L)).willReturn(Optional.of(testCourse));
            given(rankingRepository.findByMemberAndRankingMonth(any(Member.class), anyString()))
                    .willReturn(Optional.empty());
            given(rankingRepository.findByRankingMonthOrderByMonthlyTotalDistanceDesc(anyString()))
                    .willReturn(new ArrayList<>());
            given(runMapper.runToRunResponse(any(Run.class))).willReturn(testRunResponse);

            // When
            runService.createRun(testRunPostRequest);

            // Then
            assertThat(testMember.getTotalDistance()).isEqualTo(initialTotalDistance + 5.0);
            assertThat(testMember.getTotalDuration()).isEqualTo(initialTotalDuration + 30.0);
            verify(memberRepository, times(1)).save(testMember);
        }

        @Test
        @DisplayName("기존 월별 랭킹이 있으면 업데이트한다")
        void createRun_withExistingRanking_updatesRanking() {
            // Given
            Run newRun = new Run();
            newRun.setDistance(5.0);
            newRun.setDuration(30.0);
            newRun.setStartTime(LocalDateTime.now());

            Ranking existingRanking = new Ranking();
            existingRanking.setMember(testMember);
            existingRanking.setMonthlyTotalDistance(10.0);
            existingRanking.setMonthlyRank(1L);

            given(runMapper.runPostRequestToRun(testRunPostRequest)).willReturn(newRun);
            given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
            given(courseRepository.findById(1L)).willReturn(Optional.of(testCourse));
            given(rankingRepository.findByMemberAndRankingMonth(any(Member.class), anyString()))
                    .willReturn(Optional.of(existingRanking));
            given(rankingRepository.findByRankingMonthOrderByMonthlyTotalDistanceDesc(anyString()))
                    .willReturn(List.of(existingRanking));
            given(runMapper.runToRunResponse(any(Run.class))).willReturn(testRunResponse);

            // When
            runService.createRun(testRunPostRequest);

            // Then
            assertThat(existingRanking.getMonthlyTotalDistance()).isEqualTo(15.0);
            verify(rankingRepository, times(1)).save(existingRanking);
        }
    }

    @Nested
    @DisplayName("findRun 메서드")
    class FindRunTest {

        @Test
        @DisplayName("존재하는 달리기 기록을 성공적으로 조회한다")
        void findRun_withValidId_returnsRunResponse() {
            // Given
            Long runId = 1L;
            given(runRepository.findById(runId)).willReturn(Optional.of(testRun));
            given(runMapper.runToRunResponse(testRun)).willReturn(testRunResponse);

            // When
            RunResponse result = runService.findRun(runId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getRunId()).isEqualTo(runId);
            verify(runRepository, times(1)).findById(runId);
        }

        @Test
        @DisplayName("존재하지 않는 달리기 기록 조회 시 예외가 발생한다")
        void findRun_runNotFound_throwsException() {
            // Given
            Long invalidRunId = 999L;
            given(runRepository.findById(invalidRunId)).willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> runService.findRun(invalidRunId))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.RUN_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("removeRun 메서드")
    class RemoveRunTest {

        @Test
        @DisplayName("소유자가 달리기 기록을 삭제하면 성공한다")
        void removeRun_byOwner_removesSuccessfully() {
            // Given
            Long tokenMemberId = 1L;
            Long runId = 1L;
            given(runRepository.findById(runId)).willReturn(Optional.of(testRun));

            // When
            runService.removeRun(tokenMemberId, runId);

            // Then
            verify(runRepository, times(1)).findById(runId);
        }

        @Test
        @DisplayName("소유자가 아닌 사용자가 삭제하면 예외가 발생한다")
        void removeRun_byNonOwner_throwsException() {
            // Given
            Long tokenMemberId = 999L;
            Long runId = 1L;
            given(runRepository.findById(runId)).willReturn(Optional.of(testRun));

            // When & Then
            assertThatThrownBy(() -> runService.removeRun(tokenMemberId, runId))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.FORBIDDEN_NOT_OWNER);
        }

        @Test
        @DisplayName("존재하지 않는 달리기 기록 삭제 시 예외가 발생한다")
        void removeRun_runNotFound_throwsException() {
            // Given
            Long tokenMemberId = 1L;
            Long invalidRunId = 999L;
            given(runRepository.findById(invalidRunId)).willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> runService.removeRun(tokenMemberId, invalidRunId))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.RUN_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("랭킹 계산 테스트")
    class RankingCalculationTest {

        @Test
        @DisplayName("Dense Rank로 올바르게 랭킹이 계산된다")
        void createRun_calculatesCorrectDenseRank() {
            // Given
            Run newRun = new Run();
            newRun.setDistance(5.0);
            newRun.setDuration(30.0);
            newRun.setStartTime(LocalDateTime.now());

            Member member2 = new Member();
            member2.setMemberId(2L);
            member2.setTotalDistance(0.0);
            member2.setTotalDuration(0.0);

            Ranking ranking1 = new Ranking();
            ranking1.setMember(testMember);
            ranking1.setMonthlyTotalDistance(20.0);

            Ranking ranking2 = new Ranking();
            ranking2.setMember(member2);
            ranking2.setMonthlyTotalDistance(10.0);

            given(runMapper.runPostRequestToRun(testRunPostRequest)).willReturn(newRun);
            given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
            given(courseRepository.findById(1L)).willReturn(Optional.of(testCourse));
            given(rankingRepository.findByMemberAndRankingMonth(any(Member.class), anyString()))
                    .willReturn(Optional.of(ranking1));
            given(rankingRepository.findByRankingMonthOrderByMonthlyTotalDistanceDesc(anyString()))
                    .willReturn(List.of(ranking1, ranking2));
            given(runMapper.runToRunResponse(any(Run.class))).willReturn(testRunResponse);

            // When
            runService.createRun(testRunPostRequest);

            // Then
            assertThat(ranking1.getMonthlyRank()).isEqualTo(1L);
            assertThat(ranking2.getMonthlyRank()).isEqualTo(2L);
        }

        @Test
        @DisplayName("동점자는 같은 랭킹을 받는다")
        void createRun_sameDistanceGetsSameRank() {
            // Given
            Run newRun = new Run();
            newRun.setDistance(5.0);
            newRun.setDuration(30.0);
            newRun.setStartTime(LocalDateTime.now());

            Member member2 = new Member();
            member2.setMemberId(2L);

            // ranking1에 5.0이 추가되어 25.0이 되고, ranking2도 25.0이면 동점
            Ranking ranking1 = new Ranking();
            ranking1.setMember(testMember);
            ranking1.setMonthlyTotalDistance(20.0);

            Ranking ranking2 = new Ranking();
            ranking2.setMember(member2);
            ranking2.setMonthlyTotalDistance(25.0); // ranking1이 5.0 추가되면 동점

            given(runMapper.runPostRequestToRun(testRunPostRequest)).willReturn(newRun);
            given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
            given(courseRepository.findById(1L)).willReturn(Optional.of(testCourse));
            given(rankingRepository.findByMemberAndRankingMonth(any(Member.class), anyString()))
                    .willReturn(Optional.of(ranking1));
            given(rankingRepository.findByRankingMonthOrderByMonthlyTotalDistanceDesc(anyString()))
                    .willReturn(List.of(ranking1, ranking2)); // 정렬 후 같은 거리
            given(runMapper.runToRunResponse(any(Run.class))).willReturn(testRunResponse);

            // When
            runService.createRun(testRunPostRequest);

            // Then
            assertThat(ranking1.getMonthlyRank()).isEqualTo(ranking2.getMonthlyRank());
        }
    }

    @Nested
    @DisplayName("평균 페이스 계산 테스트")
    class AvgPaceCalculationTest {

        @Test
        @DisplayName("평균 페이스가 올바르게 계산된다")
        void createRun_calculatesCorrectAvgPace() {
            // Given
            testMember.setTotalDistance(10.0);
            testMember.setTotalDuration(60.0);

            Run newRun = new Run();
            newRun.setDistance(5.0);
            newRun.setDuration(30.0);
            newRun.setStartTime(LocalDateTime.now());

            given(runMapper.runPostRequestToRun(testRunPostRequest)).willReturn(newRun);
            given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
            given(courseRepository.findById(1L)).willReturn(Optional.of(testCourse));
            given(rankingRepository.findByMemberAndRankingMonth(any(Member.class), anyString()))
                    .willReturn(Optional.empty());
            given(rankingRepository.findByRankingMonthOrderByMonthlyTotalDistanceDesc(anyString()))
                    .willReturn(new ArrayList<>());
            given(runMapper.runToRunResponse(any(Run.class))).willReturn(testRunResponse);

            // When
            runService.createRun(testRunPostRequest);

            // Then
            double expectedTotalDistance = 15.0;
            double expectedTotalDuration = 90.0;
            double expectedAvgPace = expectedTotalDuration / expectedTotalDistance;

            assertThat(testMember.getTotalDistance()).isEqualTo(expectedTotalDistance);
            assertThat(testMember.getTotalDuration()).isEqualTo(expectedTotalDuration);
            assertThat(testMember.getAvgPace()).isEqualTo(expectedAvgPace);
        }
    }
}
