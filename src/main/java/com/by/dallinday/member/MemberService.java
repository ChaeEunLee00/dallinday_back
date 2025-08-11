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
import com.by.dallinday.member.dto.MyRankingResponse;
import com.by.dallinday.member.dto.RankingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper memberMapper;
    private final CourseMapper courseMapper;
    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;

    // 마이 페이지 조회
    public MyPageGetResponse findMyPage(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        // 개인 기록, 뱃지, 달리기 리스트
        MyPageGetResponse myPageGetResponse = memberMapper.memberToMyPageGetResponse(member);

        // 랭킹 계산
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

        // Course -> CourseListResponse 맵핑
        return favoriteCourses.stream()
                .map(course -> courseMapper.courseCourseListResponse(course))
                .toList();
    }

    // 개인 기록 조회
    public Member findMyRecord() {
        return new Member();
    }

    // 순위 조회
    public Member findMyRanking() {
        return new Member();
    }

    // 멤버 조회
    public MemberGetResponse findMember(Long memberId){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        return memberMapper.memberToMemberGetResponse(member);
    }

    public MyRankingResponse getMyRanking(Long memberId) {
        MyRankingResponse myRankingResponse = new MyRankingResponse();

        // 1. 전체 멤버를 totalDistance 내림차순으로 조회
        List<Member> sortedMembers = memberRepository.findAll().stream()
                .sorted(Comparator.comparingDouble(Member::getTotalDistance).reversed())
                .toList();

        // 2. memberId로 해당 멤버의 순위 조회
        int rank = 0;
        for (int i = 0; i < sortedMembers.size(); i++) {
            if (sortedMembers.get(i).getMemberId().equals(memberId)) {
                rank = i;
                break;
            }
        }

        System.out.println(sortedMembers.size());
        // totalNum
        myRankingResponse.setTotalNum((long) sortedMembers.size());

        // my
        RankingResponse myRanking = getRanking(rank, sortedMembers);
        myRankingResponse.setMy(myRanking);

        // prev
        RankingResponse prevRanking = getRanking(rank-1, sortedMembers);
        myRankingResponse.setPrev(prevRanking);

        // next
        RankingResponse nextRanking = getRanking(rank+1,sortedMembers);
        myRankingResponse.setNext(nextRanking);

        return myRankingResponse;
    }

    private RankingResponse getRanking(int rank, List<Member> sortedMembers) {
        if(rank < 0 || rank >= sortedMembers.size()) return null;

        Member prevMember = sortedMembers.get(rank);
        RankingResponse prevRanking = new RankingResponse();
        prevRanking.setMemberId(prevMember.getMemberId());
        prevRanking.setUsername(prevMember.getUsername());
        prevRanking.setRanking((long) rank+1);
        prevRanking.setTotalDistance(prevMember.getTotalDistance());
        return prevRanking;
    }
}
