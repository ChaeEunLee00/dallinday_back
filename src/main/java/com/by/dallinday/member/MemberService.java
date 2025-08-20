package com.by.dallinday.member;

import com.by.dallinday.common.exception.BusinessLogicException;
import com.by.dallinday.common.exception.ExceptionCode;
import com.by.dallinday.course.Course;
import com.by.dallinday.course.CourseMapper;
import com.by.dallinday.course.dto.CourseListResponse;
import com.by.dallinday.favorite.Favorite;
import com.by.dallinday.favorite.FavoriteRepository;
import com.by.dallinday.member.dto.MemberGetResponse;
import com.by.dallinday.member.dto.MyPageGetResponse;
import com.by.dallinday.member.dto.MyRankingDetailResponse;
import com.by.dallinday.member.dto.MyRankingResponse;
import com.by.dallinday.ranking.dto.RankingHistoryResponse;
import com.by.dallinday.ranking.dto.RankingResponse;
import com.by.dallinday.ranking.Ranking;
import com.by.dallinday.ranking.RankingMapper;
import com.by.dallinday.ranking.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper memberMapper;
    private final CourseMapper courseMapper;
    private final RankingMapper rankingMapper;

    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;
    private final RankingRepository rankingRepository;

    // 마이 페이지 조회
    public MyPageGetResponse findMyPage(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        // 개인 기록, 뱃지, 달리기 리스트
        MyPageGetResponse myPageGetResponse = memberMapper.memberToMyPageGetResponse(member);

        // 랭킹 계산 (현재 월 기준)
        myPageGetResponse.setRanking(getMyRanking(memberId));

        return myPageGetResponse;
    }

    // 찜한 코스 리스트 조회
    public List<CourseListResponse> findFavorites(Long memberId) {
        // 멤버가 존재하는지 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        // 찜 레파지토리에서 멤버의 찜 목록 조회
        List<Favorite> favorites = favoriteRepository.findByMember(member);

        // 찜 목록을 통해 코스 목록 가져오기
        List<Course> favoriteCourses = favorites.stream()
                .map(favorite -> favorite.getCourse()) // eager이기 때문에 1번의 쿼리 예상 (확인 필요)
                .toList();

        return favoriteCourses.stream()
                .map(course -> courseMapper.courseToCourseListResponse(course))
                .toList();
    }

    // 멤버 탈퇴
    public void removeMember(Long memberId) {
        // 멤버가 존재하는지 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        memberRepository.delete(member);
    }

    // 순위 조회
    public MyRankingDetailResponse findMyRanking(Long memberId) {

        // 랭킹 계산 (현재 월 기준)
        return getMyDetailRanking(memberId);
    }

    // 멤버 조회
    public MemberGetResponse findMember(Long memberId){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        return memberMapper.memberToMemberGetResponse(member);
    }

    private MyRankingResponse getMyRanking(Long memberId) {
        MyRankingResponse myRankingResponse = new MyRankingResponse();

        // 1. 현재 월 기준 Ranking 기록을 가진 전체 멤버 등수 순으로 조회
        String ym = YearMonth.from(LocalDateTime.now()).toString(); // "YYYY-MM"
        List<Ranking> monthlyRankings = rankingRepository.findByYearMonthOrderByMonthlyRank(ym);

        // 2. memberId로 해당 멤버의 순위 조회
        int idx = -1;
        for (int i = 0; i < monthlyRankings.size(); i++) {
            if (monthlyRankings.get(i).getMember().getMemberId().equals(memberId)) {
                idx = i;
                break;
            }
        }

        // yearMonth
        myRankingResponse.setYearMonth(ym);

        // totalNum
        myRankingResponse.setMonthlyTotalNum((long) monthlyRankings.size());

        if(idx != -1){
            // my
            RankingResponse myRanking = rankingMapper.rankingToRankingResponse(monthlyRankings.get(idx));
            myRankingResponse.setMy(myRanking);

            // prev
            if(idx-1 >= 0){
                RankingResponse prevRanking = rankingMapper.rankingToRankingResponse(monthlyRankings.get(idx-1));
                myRankingResponse.setPrev(prevRanking);
            }

            // next
            if(idx+1 < monthlyRankings.size()){
                RankingResponse nextRanking = rankingMapper.rankingToRankingResponse(monthlyRankings.get(idx+1));
                myRankingResponse.setNext(nextRanking);
            }
        }

        return myRankingResponse;
    }

    private MyRankingDetailResponse getMyDetailRanking(Long memberId) {
        MyRankingDetailResponse myRankingDetailResponse = new MyRankingDetailResponse();

        // 1. 현재 월 기준 Ranking 기록을 가진 전체 멤버 등수 순으로 조회
        String ym = YearMonth.from(LocalDateTime.now()).toString(); // "YYYY-MM"
        List<Ranking> monthlyRankings = rankingRepository.findByYearMonthOrderByMonthlyRank(ym);

        // 2. memberId로 해당 멤버의 순위 조회
        int idx = -1;
        for (int i = 0; i < monthlyRankings.size(); i++) {
            if (monthlyRankings.get(i).getMember().getMemberId().equals(memberId)) {
                idx = i;
                break;
            }
        }

        // yearMonth
        myRankingDetailResponse.setYearMonth(ym);

        // totalNum
        myRankingDetailResponse.setMonthlyTotalNum((long) monthlyRankings.size());

        if(idx != -1){
            // my
            RankingResponse myRanking = rankingMapper.rankingToRankingResponse(monthlyRankings.get(idx));
            myRankingDetailResponse.setMy(myRanking);

            // prev
            if(idx-1 >= 0){
                RankingResponse prevRanking = rankingMapper.rankingToRankingResponse(monthlyRankings.get(idx-1));
                myRankingDetailResponse.setPrev(prevRanking);
            }

            // next
            if(idx+1 < monthlyRankings.size()){
                RankingResponse nextRanking = rankingMapper.rankingToRankingResponse(monthlyRankings.get(idx+1));
                myRankingDetailResponse.setNext(nextRanking);
            }
        }

        // total ranking
        List<RankingResponse> totalRanking = monthlyRankings.stream()
                .map(rank -> rankingMapper.rankingToRankingResponse(rank))
                .toList();
        myRankingDetailResponse.setTotalRanking(totalRanking);

        // ranking history
        List<Ranking> history = rankingRepository.findByMember_MemberIdOrderByYearMonthDesc(memberId);
        List<RankingHistoryResponse> rankingHistory = history.stream()
                .map(rank -> rankingMapper.rankingToRankingHistroyResponse(rank))
                .toList();

        return myRankingDetailResponse;
    }
}
