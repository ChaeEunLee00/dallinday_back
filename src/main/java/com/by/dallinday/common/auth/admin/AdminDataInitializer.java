package com.by.dallinday.common.auth.admin;

import com.by.dallinday.member.Member;
import com.by.dallinday.member.MemberRepository;
import com.by.dallinday.member.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminDataInitializer implements CommandLineRunner {

    @Value("${admin.id}")
    private String adminId;

    @Value("${admin.pw}")
    private String adminPassword;

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception { // 서버 실행 시 어드민 계정 등록

        if (memberRepository.findByEmail(adminId).isEmpty()) {
            Member admin = new Member();
            admin.setEmail(adminId); // 아이디 역할
            admin.setProviderId(passwordEncoder.encode(adminPassword)); // 비밀번호 역할
            admin.setProvider("dallinday");
            admin.setRole(Role.ADMIN.getRole());
            admin.setUsername("관리자");
            admin.setCreatedAt(LocalDateTime.now());
            admin.setImageUrl("https://wanderworld-bucket.s3.ap-northeast-2.amazonaws.com/default_profile.jpg");

            memberRepository.save(admin);
            log.info("Admin user created.");
        } else {
            log.info("Admin user already exists.");
        }
    }
}
