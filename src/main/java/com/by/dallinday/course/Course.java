package com.by.dallinday.course;

import com.by.dallinday.spot.Spot;
import com.by.dallinday.theme.Theme;
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
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Long distance;

    @Column
    private Long duration;

    @Column
    private String difficulty;

    @Column
    private LocalDateTime createdAt;

    @Column
    private String type;

    @ManyToOne
    @JoinColumn(name = "ID")
    private Spot spot;

    @ManyToOne
    @JoinColumn(name = "ID")
    private Theme theme;

    @OneToMany(mappedBy = "spot")
    private List<Course> runList = new ArrayList<>();
}
