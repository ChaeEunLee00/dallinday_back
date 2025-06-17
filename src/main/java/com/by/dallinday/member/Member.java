package com.by.dallinday.member;

import com.by.dallinday.run.Run;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

//    @Column(nullable = false, unique = true)
    private String email;

//    @Column(nullable = false, unique = true)
    private String username;

//    @Column(nullable = false)
    private String provider;

//    @Column(nullable = false)
    private String providerId;

    private String imageUrl;

    private String role;

    @Column
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "member")
    private List<Run> runList = new ArrayList<>();
}
