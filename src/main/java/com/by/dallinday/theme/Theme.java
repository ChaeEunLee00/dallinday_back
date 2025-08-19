package com.by.dallinday.theme;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "themes")
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long themeId;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String imageUrl;

    @Column
    private LocalDateTime createdAt;

//    @OneToMany(mappedBy = "theme")
//    private List<Course> courseList = new ArrayList<>();
}
