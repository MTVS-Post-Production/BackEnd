package com.alal.backend.config.security.handler;


import com.alal.backend.advice.assertThat.DefaultAssert;
import com.alal.backend.config.security.OAuth2Config;
import com.alal.backend.config.security.token.TokenResponse;
import com.alal.backend.config.security.util.CustomCookie;
import com.alal.backend.domain.entity.user.Token;
import com.alal.backend.domain.mapping.TokenMapping;
import com.alal.backend.repository.auth.CustomAuthorizationRequestRepository;
import com.alal.backend.repository.auth.TokenRepository;
import com.alal.backend.service.auth.CustomTokenProviderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static com.alal.backend.repository.auth.CustomAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;


@RequiredArgsConstructor
@Component
public class CustomSimpleUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

    private final CustomTokenProviderService customTokenProviderService;
    private final OAuth2Config oAuth2Config;
    private final TokenRepository tokenRepository;
    private final CustomAuthorizationRequestRepository customAuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultAssert.isAuthentication(!response.isCommitted());

//        String targetUrl = determineTargetUrl(request, response, authentication);
//        clearAuthenticationAttributes(request, response);
//        getRedirectStrategy().sendRedirect(request, response, targetUrl);

        sendTokenResponse(request, response, authentication);
    }

    protected void sendTokenResponse(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Optional<String> redirectUri = CustomCookie.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue);

        DefaultAssert.isAuthentication(!(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())));

        TokenMapping tokenMapping = customTokenProviderService.createToken(authentication);
        Token token = Token.builder()
                .userEmail(tokenMapping.getUserEmail())
                .refreshToken(tokenMapping.getRefreshToken())
                .build();
        tokenRepository.save(token);

//        TokenResponse tokenResponse = TokenResponse.toTokenMapping(tokenMapping);

        Cookie accessTokenCookie = new Cookie("accessToken", tokenMapping.getAccessToken());
        Cookie refreshTokenCookie = new Cookie("refreshToken", tokenMapping.getAccessToken());

        accessTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setHttpOnly(true);

        accessTokenCookie.setSecure(true);
        refreshTokenCookie.setSecure(true);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        // ObjectMapper를 사용하여 TokenResponse 객체를 JSON 문자열로 변환
//        ObjectMapper objectMapper = new ObjectMapper();
//        String tokenResponseJson = objectMapper.writeValueAsString(tokenResponse);
//
//        response.setContentType("application/json");
//        response.getWriter().write(tokenResponseJson);

        clearAuthenticationAttributes(request, response);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CustomCookie.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue);

        DefaultAssert.isAuthentication( !(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) );

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        TokenMapping tokenMapping = customTokenProviderService.createToken(authentication);
        Token token = Token.builder()
                .userEmail(tokenMapping.getUserEmail())
                .refreshToken(tokenMapping.getRefreshToken())
                .build();
        tokenRepository.save(token);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", tokenMapping.getAccessToken())
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        customAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return oAuth2Config.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }
}
