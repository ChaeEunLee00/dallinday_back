package com.by.dallinday.favorite;

import com.by.dallinday.course.Course;
import com.by.dallinday.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByMemberAndCourse(Member member, Course course);
    Optional<Favorite> findByMemberAndCourse(Member member, Course course);
}
