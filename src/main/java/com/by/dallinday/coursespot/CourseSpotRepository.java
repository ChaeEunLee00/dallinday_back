package com.by.dallinday.coursespot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseSpotRepository  extends JpaRepository<CourseSpot, Long> {
    List<CourseSpot> findBySpotId(Long spotId);

    // 거리 오름차순
    List<CourseSpot> findBySpotIdOrderByCourse_DistanceAsc(Long spotId);

    // 난이도 오름차순
    List<CourseSpot> findBySpotIdOrderByCourse_DifficultyAsc(Long spotId);

    @Query("select distinct cs.spotId from CourseSpot cs")
    List<Long> findAllDistinctSpotIds();
}
