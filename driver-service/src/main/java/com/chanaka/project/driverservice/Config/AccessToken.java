package com.chanaka.project.driverservice.Config;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

public class AccessToken {

    public static String getAccessToken() {
        OAuth2AuthenticationDetails authDetails = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return authDetails.getTokenType().concat(" ").concat(authDetails.getTokenValue());
    }
}
