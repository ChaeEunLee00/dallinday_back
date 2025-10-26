package com.by.dallinday.common.auth.oauth;

import com.by.dallinday.member.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private Long memberId;

    private String email;

    private String role;

    private boolean isNew;

    /**
     * Constructs a {@code DefaultOAuth2User} using the provided parameters.
     *
     * @param authorities      the authorities granted to the user
     * @param attributes       the attributes about the user
     * @param nameAttributeKey the key used to access the user's &quot;name&quot; from
     *                         {@link #getAttributes()}
     */
    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes,
                            String nameAttributeKey,
                            Long memberId,
                            String email,
                            String role,
                            boolean isNew) {
        super(authorities, attributes, nameAttributeKey);
        this.memberId = memberId;
        this.email = email;
        this.role = role;
        this.isNew = isNew;
    }

    public static CustomOAuth2User of(Member member,
                                      Map<String, Object> attributes,
                                      String nameAttributeKey,
                                      boolean isNew) {
        return new CustomOAuth2User(
                List.of(new SimpleGrantedAuthority(member.getRole())),
                attributes,
                nameAttributeKey,
                member.getMemberId(),
                member.getEmail(),
                member.getRole(),
                isNew
        );
    }
}
