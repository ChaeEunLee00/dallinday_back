package com.by.dallinday.common.auth.oauth;

import com.by.dallinday.common.auth.oauth.userinfo.GoogleUserInfo;
import com.by.dallinday.common.auth.oauth.userinfo.KakaoUserInfo;
import com.by.dallinday.common.auth.oauth.userinfo.NaverUserInfo;
import com.by.dallinday.common.auth.oauth.userinfo.OAuth2UserInfo;
import com.by.dallinday.common.exception.BusinessLogicException;
import com.by.dallinday.common.exception.ExceptionCode;
import com.by.dallinday.member.Member;
import com.by.dallinday.member.MemberRepository;
import com.by.dallinday.member.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 -> OAuth2 로그인 요청 진입");

        // 액세스 토큰을 통해 사용자 정보 요청
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        log.info("provider : {} ", provider);
        log.info("providerId : {}", providerId);
        log.info("attributes : {}", attributes);

        // UserInfo 가져오기 - provider 별로 사용자 데이터 구조, 필드 이름 등이 다르기 때문에 공통 인터페이스 정의
        OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(provider, attributes);

        // member 존재 여부 확인
        Optional<Member> existing = memberRepository.findByEmailAndProvider(oAuth2UserInfo.getEmail(), provider);
        boolean isNew = existing.isEmpty();

        // 저장되어있는 member 정보 가져오기, 없으면 회원가입 진행
        Member member = existing.orElseGet(() -> createUser(oAuth2UserInfo));

        log.info("dallinday memberId : {}", member.getMemberId());
        log.info("dallinday email : {}", member.getEmail());
        log.info("dallinday username : {}", member.getUsername());

        // 커스텀 OAuth2User 전달
        log.info("CustomOAuth2UserSerivce.loadUser() 종료");
        return CustomOAuth2User.of(member, attributes, providerId, isNew);
    }

    private OAuth2UserInfo getOAuth2UserInfo(String provider, Map<String, Object> attributes) {
        if (provider.equals("google")) {
            return new GoogleUserInfo(provider,attributes);
        }
        else if (provider.equals("kakao")) {
            return new KakaoUserInfo(provider,attributes);
        }
        else if (provider.equals("naver")) {
            return new NaverUserInfo(provider,attributes);
        }
        else throw new BusinessLogicException(ExceptionCode.PROVIDER_NOT_FOUND);
    }

    // 저장되어있는 member 정보 가져오기
//    private Member getMember(OAuth2UserInfo oAuth2UserInfo, String provider) {
//        return memberRepository.findByEmailAndProvider(oAuth2UserInfo.getEmail(), provider)
//                .orElseGet(() -> createUser(oAuth2UserInfo));
//    }

    // 회원가입 진행
    private Member createUser(OAuth2UserInfo oAuth2UserInfo) {
        Member member = new Member();
        member.setEmail(oAuth2UserInfo.getEmail());
        member.setUsername(oAuth2UserInfo.getName());
        member.setProvider(oAuth2UserInfo.getProvider());
        member.setProviderId(oAuth2UserInfo.getProviderId());
        member.setImageUrl(oAuth2UserInfo.getImageUrl());
        member.setRole(Role.USER.getRole());
        member.setCreatedAt(LocalDateTime.now());
        return memberRepository.save(member);
    }
}

