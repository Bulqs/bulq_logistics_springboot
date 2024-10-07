package com.bulq.logistics.payload.auth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenDTO {
    private String token;

    private String firstName;

    private Collection<? extends GrantedAuthority> authorities;
}
