package com.by.dallinday.theme;

import com.by.dallinday.course.Course;
import com.by.dallinday.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "themes")
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String image_url;

    @Column
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "spot")
    private List<Course> runList = new ArrayList<>();
}
