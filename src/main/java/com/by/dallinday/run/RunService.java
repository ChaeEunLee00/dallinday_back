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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RunService {

    private final RunMapper runMapper;
    private final RunRepository runRepository;
    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;
    private final RankingRepository rankingRepository;

    // 달리기 기록 생성
    public RunResponse createRun(RunPostRequest request) {
        // dto -> entity
        Run run = runMapper.runPostRequestToRun(request);

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COURSE_NOT_FOUND));

        run.setMember(member);
        run.setCourse(course);

        // 서비스 로직
        // 멤버 기록 업데이트
        member.setTotalDistance(member.getTotalDistance() + run.getDistance());
        member.setTotalDuration(member.getTotalDuration() + run.getDuration());
        member.setAvgPace(member.getTotalDistance() / member.getTotalDuration());

        // 뱃지 조건 확인 후 업데이트

        // 월별 달리기 기록, 랭킹 업데이트
        updateMonthlyRanking(member, run.getDistance(), run.getStartTime());

        // 저장
        // 달리기 기록 저장
        runRepository.save(run);
        memberRepository.save(member);

        // entity -> dto
        return runMapper.runToRunResponse(run);
    }

    // 달리기 기록 조회
    public RunResponse findRun(Long runId) {
        Run run = runRepository.findById(runId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.RUN_NOT_FOUND));

        return runMapper.runToRunResponse(run);
    }

    @Transactional
    private void updateMonthlyRanking(Member member, double distanceKm, LocalDateTime runTime) {
        String ym = YearMonth.from(runTime).toString(); // "YYYY-MM"

        // 월별 달리기 기록 업데이트
        Ranking ranking = rankingRepository.findByMemberAndYearMonth(member, ym)
                .orElseGet(() -> {
                    Ranking r = new Ranking();
                    r.setMember(member);
                    r.setYearMonth(ym);
                    return r;
                });

        ranking.setMonthlyTotalDistance(ranking.getMonthlyTotalDistance() + distanceKm);
        ranking.setUpdatedAt(LocalDateTime.now());
        rankingRepository.save(ranking);

        // 해당 달 랭킹 재계산 (Dense Rank)
        recalcMonthRanks(ym);
    }

    @Transactional
    private void recalcMonthRanks(String yearMonth) {
        List<Ranking> list = rankingRepository.findByYearMonthOrderByMonthlyRank(yearMonth);

        double prevDist = Double.NaN;
        long currentRank = 0;
        long index = 0;
        for (Ranking r : list) {
            index++;
            if (Double.compare(r.getMonthlyTotalDistance(), prevDist) != 0) {
                currentRank = index; // Dense Rank: 값이 바뀌면 현재 index가 랭크
                prevDist = r.getMonthlyTotalDistance();
            }
            r.setMonthlyRank(currentRank);
            r.setUpdatedAt(LocalDateTime.now());
        }
        rankingRepository.saveAll(list);
    }
}
