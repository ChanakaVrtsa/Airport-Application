package com.chanaka.project.oauthauthorizationserver.service;

import com.chanaka.project.oauthauthorizationserver.model.AuthUserDetail;
import com.chanaka.project.oauthauthorizationserver.model.User;
import com.chanaka.project.oauthauthorizationserver.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<User> user = userDetailsRepository.findByUsername(name);
        user.orElseThrow(() -> new UsernameNotFoundException("Username or Password wrong"));

        UserDetails userDetails = new AuthUserDetail(user.get());
        new AccountStatusUserDetailsChecker().check(userDetails);
        return userDetails;
    }
}
