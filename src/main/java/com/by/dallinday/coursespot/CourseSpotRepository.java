package com.by.dallinday.coursespot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseSpotRepository  extends JpaRepository<CourseSpot, Long> {
    List<CourseSpot> findBySpotId(Long spotId);
}
