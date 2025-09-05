package com.by.dallinday.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class OAuthRefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, length = 2048)
    private String encryptedRefreshToken;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;
}
