package com.by.dallinday.course;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByName(String name);

    @Query("select c from Course c left join c.runList r group by c order by count(r) desc, c.id asc")
    List<Course> findTopNByRunCount(Pageable pageable);
}
