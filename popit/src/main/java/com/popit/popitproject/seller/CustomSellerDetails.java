package com.popit.popitproject.seller;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

@Data
public class CustomSellerDetails implements UserDetails {

    private final SellerEntity seller;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 사용자의 권한 정보를 반환하는 로직을 구현해야 합니다.
        // 예시: user.getAuthorities();
        return null;
    }

    @Override
    public String getPassword() {
        // 사용자의 암호화된 비밀번호를 반환합니다.
        return seller.getPassword();
    }

    @Override
    public String getUsername() {
        // 사용자의 식별자(예: 이메일)를 반환합니다.
        return seller.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        // 사용자 계정의 유효 기간이 만료되었는지 여부를 반환합니다.
        // 예시: user.isAccountNonExpired();
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 사용자 계정이 잠겨 있는지 여부를 반환합니다.
        // 예시: user.isAccountNonLocked();
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 사용자의 자격 증명(비밀번호)의 유효 기간이 만료되었는지 여부를 반환합니다.
        // 예시: user.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 사용자 계정이 활성화되었는지 여부를 반환합니다.
        // 예시: user.isEnabled();
        return true;
    }
}