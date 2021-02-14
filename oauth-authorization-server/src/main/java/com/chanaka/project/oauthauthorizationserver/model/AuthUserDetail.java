package com.chanaka.project.oauthauthorizationserver.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AuthUserDetail extends User implements UserDetails {

    public AuthUserDetail() {
    }

    public AuthUserDetail(User user) {
        super(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthority = new ArrayList<>();
        if(isReadAccess()){
            grantedAuthority.add(new SimpleGrantedAuthority("read"));
        }
        if(isWriteAccess()){
            grantedAuthority.add(new SimpleGrantedAuthority("write"));
        }
        if(isUpdateAccess()){
            grantedAuthority.add(new SimpleGrantedAuthority("update"));
        }
        if(isDeleteAccess()){
            grantedAuthority.add(new SimpleGrantedAuthority("delete"));
        }
        grantedAuthority.add(new SimpleGrantedAuthority(getRole()));
        return grantedAuthority;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }
}
