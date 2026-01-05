package com.by.dallinday.member;

import com.by.dallinday.common.auth.oauth.refresh.OAuthAPIClient;
import com.by.dallinday.common.exception.BusinessLogicException;
import com.by.dallinday.common.exception.ExceptionCode;
import com.by.dallinday.course.Course;
import com.by.dallinday.course.CourseMapper;
import com.by.dallinday.course.CourseService;
import com.by.dallinday.course.dto.CourseListResponse;
import com.by.dallinday.favorite.Favorite;
import com.by.dallinday.favorite.FavoriteRepository;
import com.by.dallinday.member.dto.*;
import com.by.dallinday.ranking.Ranking;
import com.by.dallinday.ranking.RankingMapper;
import com.by.dallinday.ranking.RankingRepository;
import com.by.dallinday.ranking.dto.RankingResponse;
import com.by.dallinday.run.Run;
import com.by.dallinday.run.RunMapper;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberService 단위 테스트")
class MemberServiceTest {

    @Mock
    private OAuthAPIClient oAuthAPIClient;

    @Mock
    private RunMapper runMapper;

    @Mock
    private MemberMapper memberMapper;

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private RankingMapper rankingMapper;

    @Mock
    private CourseService courseService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private RankingRepository rankingRepository;

    @InjectMocks
    private MemberService memberService;

    private Member testMember;
    private MemberResponse testMemberResponse;
    private MyPageGetResponse testMyPageResponse;

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
        testMember.setBadges("100000000");
        testMember.setRunList(new ArrayList<>());
        testMember.setFavoriteList(new ArrayList<>());
        testMember.setCreatedAt(LocalDateTime.now());

        testMemberResponse = new MemberResponse();
        testMemberResponse.setMemberId(1L);
        testMemberResponse.setEmail("test@test.com");
        testMemberResponse.setUsername("테스트유저");

        testMyPageResponse = new MyPageGetResponse();
        testMyPageResponse.setMemberId(1L);
        testMyPageResponse.setUsername("테스트유저");
        testMyPageResponse.setTotalDistance(100.0);
        testMyPageResponse.setTotalDuration(600.0);
    }

    @Nested
    @DisplayName("findMember 메서드")
    class FindMemberTest {

        @Test
        @DisplayName("존재하는 멤버 ID로 조회하면 멤버 정보를 반환한다")
        void findMember_withValidId_returnsMemberResponse() {
            // Given
            Long memberId = 1L;
            given(memberRepository.findById(memberId)).willReturn(Optional.of(testMember));
            given(memberMapper.memberToMemberResponse(testMember)).willReturn(testMemberResponse);

            // When
            MemberResponse result = memberService.findMember(memberId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getMemberId()).isEqualTo(memberId);
            assertThat(result.getUsername()).isEqualTo("테스트유저");
            verify(memberRepository, times(1)).findById(memberId);
        }

        @Test
        @DisplayName("존재하지 않는 멤버 ID로 조회하면 예외가 발생한다")
        void findMember_withInvalidId_throwsException() {
            // Given
            Long invalidMemberId = 999L;
            given(memberRepository.findById(invalidMemberId)).willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> memberService.findMember(invalidMemberId))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.MEMBER_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("updateMember 메서드")
    class UpdateMemberTest {

        @Test
        @DisplayName("유효한 요청으로 멤버 정보를 수정한다")
        void updateMember_withValidRequest_updatesMember() {
            // Given
            Long memberId = 1L;
            MemberPatchRequest request = new MemberPatchRequest();
            request.setUsername("수정된이름");

            given(memberRepository.findById(memberId)).willReturn(Optional.of(testMember));
            given(memberMapper.memberToMemberResponse(any(Member.class))).willReturn(testMemberResponse);

            // When
            MemberResponse result = memberService.updateMember(memberId, request);

            // Then
            assertThat(result).isNotNull();
            verify(memberRepository, times(1)).save(testMember);
        }

        @Test
        @DisplayName("username이 null이면 업데이트하지 않는다")
        void updateMember_withNullUsername_doesNotUpdate() {
            // Given
            Long memberId = 1L;
            MemberPatchRequest request = new MemberPatchRequest();
            request.setUsername(null);

            given(memberRepository.findById(memberId)).willReturn(Optional.of(testMember));
            given(memberMapper.memberToMemberResponse(any(Member.class))).willReturn(testMemberResponse);

            // When
            MemberResponse result = memberService.updateMember(memberId, request);

            // Then
            assertThat(result).isNotNull();
            verify(memberRepository, never()).save(any(Member.class));
        }

        @Test
        @DisplayName("존재하지 않는 멤버 수정 시 예외가 발생한다")
        void updateMember_memberNotFound_throwsException() {
            // Given
            Long invalidMemberId = 999L;
            MemberPatchRequest request = new MemberPatchRequest();
            given(memberRepository.findById(invalidMemberId)).willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> memberService.updateMember(invalidMemberId, request))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.MEMBER_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("removeMember 메서드")
    class RemoveMemberTest {

        @Test
        @DisplayName("일반 사용자를 성공적으로 삭제한다")
        void removeMember_withValidUser_removesSuccessfully() {
            // Given
            Long memberId = 1L;
            given(memberRepository.findById(memberId)).willReturn(Optional.of(testMember));
            doNothing().when(oAuthAPIClient).revoke(testMember);

            // When
            memberService.removeMember(memberId);

            // Then
            verify(oAuthAPIClient, times(1)).revoke(testMember);
            verify(memberRepository, times(1)).delete(testMember);
        }

        @Test
        @DisplayName("관리자 계정 삭제 시 예외가 발생한다")
        void removeMember_adminAccount_throwsException() {
            // Given
            Long memberId = 1L;
            testMember.setRole("ADMIN");
            given(memberRepository.findById(memberId)).willReturn(Optional.of(testMember));

            // When & Then
            assertThatThrownBy(() -> memberService.removeMember(memberId))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.ADMIN_CANNOT_LEAVE);
        }

        @Test
        @DisplayName("존재하지 않는 멤버 삭제 시 예외가 발생한다")
        void removeMember_memberNotFound_throwsException() {
            // Given
            Long invalidMemberId = 999L;
            given(memberRepository.findById(invalidMemberId)).willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> memberService.removeMember(invalidMemberId))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.MEMBER_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("findMyPage 메서드")
    class FindMyPageTest {

        @Test
        @DisplayName("마이페이지 정보를 성공적으로 조회한다")
        void findMyPage_withValidId_returnsMyPageResponse() {
            // Given
            Long memberId = 1L;
            String currentYearMonth = YearMonth.from(LocalDateTime.now()).toString();

            given(memberRepository.findById(memberId)).willReturn(Optional.of(testMember));
            given(memberMapper.memberToMyPageGetResponse(testMember)).willReturn(testMyPageResponse);
            given(rankingRepository.findByRankingMonthOrderByMonthlyRank(currentYearMonth))
                    .willReturn(new ArrayList<>());

            // When
            MyPageGetResponse result = memberService.findMyPage(memberId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getMemberId()).isEqualTo(memberId);
            verify(memberRepository, times(1)).findById(memberId);
        }

        @Test
        @DisplayName("랭킹 정보가 있으면 마이페이지에 포함한다")
        void findMyPage_withRankingData_includesRanking() {
            // Given
            Long memberId = 1L;
            String currentYearMonth = YearMonth.from(LocalDateTime.now()).toString();

            Ranking ranking = new Ranking();
            ranking.setMember(testMember);
            ranking.setRankingMonth(currentYearMonth);
            ranking.setMonthlyRank(1L);
            ranking.setMonthlyTotalDistance(50.0);

            RankingResponse rankingResponse = new RankingResponse();
            rankingResponse.setMemberId(1L);
            rankingResponse.setMonthlyRank(1L);

            given(memberRepository.findById(memberId)).willReturn(Optional.of(testMember));
            given(memberMapper.memberToMyPageGetResponse(testMember)).willReturn(testMyPageResponse);
            given(rankingRepository.findByRankingMonthOrderByMonthlyRank(currentYearMonth))
                    .willReturn(List.of(ranking));
            given(rankingMapper.rankingToRankingResponse(ranking)).willReturn(rankingResponse);

            // When
            MyPageGetResponse result = memberService.findMyPage(memberId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getRanking()).isNotNull();
        }

        @Test
        @DisplayName("존재하지 않는 멤버 조회 시 예외가 발생한다")
        void findMyPage_memberNotFound_throwsException() {
            // Given
            Long invalidMemberId = 999L;
            given(memberRepository.findById(invalidMemberId)).willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> memberService.findMyPage(invalidMemberId))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.MEMBER_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("findMyRanking 메서드")
    class FindMyRankingTest {

        @Test
        @DisplayName("내 랭킹 상세 정보를 성공적으로 조회한다")
        void findMyRanking_withValidId_returnsRankingDetail() {
            // Given
            Long memberId = 1L;
            String currentYearMonth = YearMonth.from(LocalDateTime.now()).toString();

            Ranking ranking = new Ranking();
            ranking.setMember(testMember);
            ranking.setRankingMonth(currentYearMonth);
            ranking.setMonthlyRank(1L);

            RankingResponse rankingResponse = new RankingResponse();
            rankingResponse.setMemberId(1L);
            rankingResponse.setMonthlyRank(1L);

            given(rankingRepository.findByRankingMonthOrderByMonthlyRank(currentYearMonth))
                    .willReturn(List.of(ranking));
            given(rankingRepository.findByMember_MemberIdOrderByRankingMonthDesc(memberId))
                    .willReturn(List.of(ranking));
            given(rankingMapper.rankingToRankingResponse(any(Ranking.class))).willReturn(rankingResponse);
            given(rankingMapper.rankingToRankingHistroyResponse(any(Ranking.class))).willReturn(null);

            // When
            MyRankingDetailResponse result = memberService.findMyRanking(memberId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getYearMonth()).isEqualTo(currentYearMonth);
        }

        @Test
        @DisplayName("랭킹이 없는 멤버도 조회할 수 있다")
        void findMyRanking_withNoRanking_returnsEmptyRanking() {
            // Given
            Long memberId = 1L;
            String currentYearMonth = YearMonth.from(LocalDateTime.now()).toString();

            given(rankingRepository.findByRankingMonthOrderByMonthlyRank(currentYearMonth))
                    .willReturn(new ArrayList<>());
            given(rankingRepository.findByMember_MemberIdOrderByRankingMonthDesc(memberId))
                    .willReturn(new ArrayList<>());

            // When
            MyRankingDetailResponse result = memberService.findMyRanking(memberId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getMy()).isNull();
        }
    }

    @Nested
    @DisplayName("findFavorites 메서드")
    class FindFavoritesTest {

        @Test
        @DisplayName("찜한 코스 목록을 성공적으로 조회한다")
        void findFavorites_withValidId_returnsFavoriteList() {
            // Given
            Long memberId = 1L;

            Course course = new Course();
            course.setCourseId(1L);
            course.setName("테스트 코스");
            course.setCourseSpotList(new ArrayList<>());

            Favorite favorite = new Favorite();
            favorite.setMember(testMember);
            favorite.setCourse(course);

            CourseListResponse courseListResponse = new CourseListResponse();
            courseListResponse.setCourseId(1L);
            courseListResponse.setCourseSpotList(new ArrayList<>());

            given(memberRepository.findById(memberId)).willReturn(Optional.of(testMember));
            given(favoriteRepository.findByMember(testMember)).willReturn(List.of(favorite));
            given(courseMapper.courseToCourseListResponse(any(Course.class))).willReturn(courseListResponse);
            given(courseService.getCourseImageUrl(anyList())).willReturn("https://example.com/image.jpg");

            // When
            List<CourseListResponse> result = memberService.findFavorites(memberId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("찜한 코스가 없으면 빈 목록을 반환한다")
        void findFavorites_noFavorites_returnsEmptyList() {
            // Given
            Long memberId = 1L;
            given(memberRepository.findById(memberId)).willReturn(Optional.of(testMember));
            given(favoriteRepository.findByMember(testMember)).willReturn(new ArrayList<>());

            // When
            List<CourseListResponse> result = memberService.findFavorites(memberId);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 멤버 조회 시 예외가 발생한다")
        void findFavorites_memberNotFound_throwsException() {
            // Given
            Long invalidMemberId = 999L;
            given(memberRepository.findById(invalidMemberId)).willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> memberService.findFavorites(invalidMemberId))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.MEMBER_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("findRuns 메서드")
    class FindRunsTest {

        @Test
        @DisplayName("달리기 기록 목록을 성공적으로 조회한다")
        void findRuns_withValidId_returnsRunList() {
            // Given
            Long memberId = 1L;

            Run run = new Run();
            run.setRunId(1L);
            run.setMember(testMember);
            run.setDistance(5.0);

            testMember.setRunList(List.of(run));

            RunResponse runResponse = new RunResponse();
            runResponse.setRunId(1L);
            runResponse.setDistance(5.0);

            given(memberRepository.findById(memberId)).willReturn(Optional.of(testMember));
            given(runMapper.runToRunResponse(any(Run.class))).willReturn(runResponse);

            // When
            List<RunResponse> result = memberService.findRuns(memberId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getDistance()).isEqualTo(5.0);
        }

        @Test
        @DisplayName("달리기 기록이 없으면 빈 목록을 반환한다")
        void findRuns_noRuns_returnsEmptyList() {
            // Given
            Long memberId = 1L;
            testMember.setRunList(new ArrayList<>());
            given(memberRepository.findById(memberId)).willReturn(Optional.of(testMember));

            // When
            List<RunResponse> result = memberService.findRuns(memberId);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 멤버 조회 시 예외가 발생한다")
        void findRuns_memberNotFound_throwsException() {
            // Given
            Long invalidMemberId = 999L;
            given(memberRepository.findById(invalidMemberId)).willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> memberService.findRuns(invalidMemberId))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.MEMBER_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("랭킹 계산 테스트")
    class RankingCalculationTest {

        @Test
        @DisplayName("이전 순위와 다음 순위가 있을 때 올바르게 반환한다")
        void ranking_withPrevAndNext_returnsCorrectly() {
            // Given
            Long memberId = 2L;
            String currentYearMonth = YearMonth.from(LocalDateTime.now()).toString();

            Member member1 = new Member();
            member1.setMemberId(1L);
            Member member2 = new Member();
            member2.setMemberId(2L);
            Member member3 = new Member();
            member3.setMemberId(3L);

            Ranking ranking1 = new Ranking();
            ranking1.setMember(member1);
            ranking1.setMonthlyRank(1L);
            ranking1.setRankingMonth(currentYearMonth);

            Ranking ranking2 = new Ranking();
            ranking2.setMember(member2);
            ranking2.setMonthlyRank(2L);
            ranking2.setRankingMonth(currentYearMonth);

            Ranking ranking3 = new Ranking();
            ranking3.setMember(member3);
            ranking3.setMonthlyRank(3L);
            ranking3.setRankingMonth(currentYearMonth);

            RankingResponse response1 = new RankingResponse();
            response1.setMemberId(1L);
            response1.setMonthlyRank(1L);

            RankingResponse response2 = new RankingResponse();
            response2.setMemberId(2L);
            response2.setMonthlyRank(2L);

            RankingResponse response3 = new RankingResponse();
            response3.setMemberId(3L);
            response3.setMonthlyRank(3L);

            given(rankingRepository.findByRankingMonthOrderByMonthlyRank(currentYearMonth))
                    .willReturn(List.of(ranking1, ranking2, ranking3));
            given(rankingRepository.findByMember_MemberIdOrderByRankingMonthDesc(memberId))
                    .willReturn(List.of(ranking2));
            given(rankingMapper.rankingToRankingResponse(ranking1)).willReturn(response1);
            given(rankingMapper.rankingToRankingResponse(ranking2)).willReturn(response2);
            given(rankingMapper.rankingToRankingResponse(ranking3)).willReturn(response3);
            given(rankingMapper.rankingToRankingHistroyResponse(any())).willReturn(null);

            // When
            MyRankingDetailResponse result = memberService.findMyRanking(memberId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getMy()).isNotNull();
            assertThat(result.getMy().getMonthlyRank()).isEqualTo(2L);
            assertThat(result.getPrev()).isNotNull();
            assertThat(result.getPrev().getMonthlyRank()).isEqualTo(1L);
            assertThat(result.getNext()).isNotNull();
            assertThat(result.getNext().getMonthlyRank()).isEqualTo(3L);
        }
    }
}
