package com.by.dallinday.spot;

import com.by.dallinday.course.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "spots")
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Long latitude;

    @Column
    private Long longitude;

    @Column
    private String address;

    @Column
    private String image_url;

    @OneToMany(mappedBy = "spot")
    private List<Course> runList = new ArrayList<>();
}
