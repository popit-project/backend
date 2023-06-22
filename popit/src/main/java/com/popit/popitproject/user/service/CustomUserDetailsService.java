package com.popit.popitproject.user.service;

import com.popit.popitproject.user.entity.UserEntity; // UserEntity 클래스의 패키지에 맞게 임포트해주세요.
import com.popit.popitproject.user.repository.UserRepository; // UserRepository 인터페이스의 패키지에 맞게 임포트해주세요.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUserId(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(user.getUserId(), user.getPassword(), getAuthorities(user));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(UserEntity user) {
        if (user.getRoles() != null) {
            String[] roles = user.getRoles().stream()
                    .map(UserEntity.Role::name)
                    .toArray(String[]::new);
            return AuthorityUtils.createAuthorityList(roles);
        }
        return Collections.emptyList();
    }
}
