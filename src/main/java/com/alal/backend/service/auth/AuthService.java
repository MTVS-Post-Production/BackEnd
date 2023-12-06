package com.alal.backend.service.auth;


import com.alal.backend.advice.assertThat.DefaultAssert;
import com.alal.backend.advice.error.DefaultException;
import com.alal.backend.advice.payload.ErrorCode;
import com.alal.backend.config.security.token.UserPrincipal;
import com.alal.backend.domain.dto.response.JwtTokenResponse;
import com.alal.backend.domain.entity.user.Provider;
import com.alal.backend.domain.entity.user.Role;
import com.alal.backend.domain.entity.user.Token;
import com.alal.backend.domain.entity.user.User;
import com.alal.backend.domain.mapping.TokenMapping;
import com.alal.backend.payload.request.user.ProfileUpdateRequest;
import com.alal.backend.payload.request.auth.RefreshTokenRequest;
import com.alal.backend.payload.request.auth.SignInRequest;
import com.alal.backend.payload.request.auth.SignUpRequest;
import com.alal.backend.payload.response.ApiResponse;
import com.alal.backend.payload.response.AuthResponse;
import com.alal.backend.domain.dto.response.ProfileUpdateResponse;
import com.alal.backend.payload.response.Message;
import com.alal.backend.repository.auth.TokenRepository;
import com.alal.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final CustomTokenProviderService customTokenProviderService;
    
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    private static final String ACCESS_TOKEN_VALUE = "accessToken";
    private static final String REFRESH_TOKEN_VALUE = "refreshToken";
    

    public ResponseEntity<?> whoAmI(UserPrincipal userPrincipal){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isOptionalPresent(user);
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(user.get()).build();

        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> delete(UserPrincipal userPrincipal){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        Optional<Token> token = tokenRepository.findByUserEmail(user.get().getEmail());
        DefaultAssert.isTrue(token.isPresent(), "토큰이 유효하지 않습니다.");

        userRepository.delete(user.get());
        tokenRepository.delete(token.get());

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(Message.builder().message("회원 탈퇴하셨습니다.").build()).build();

        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> modify(Long userId, @Valid ProfileUpdateRequest profileUpdateRequest) {
        Optional<User> user = userRepository.findById(userId);

        user.get().updateProfile(profileUpdateRequest);

        ProfileUpdateResponse profileUpdateResponse = ProfileUpdateResponse.toEntity(user.get());

        return ResponseEntity.ok(profileUpdateResponse);
    }

    public ResponseEntity<?> signin(SignInRequest signInRequest){
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                signInRequest.getEmail(),
                signInRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenMapping tokenMapping = customTokenProviderService.createToken(authentication);
        Token token = Token.builder()
                            .refreshToken(tokenMapping.getRefreshToken())
                            .accessToken(tokenMapping.getAccessToken())
                            .userEmail(tokenMapping.getUserEmail())
                            .build();
        tokenRepository.save(token);
        AuthResponse authResponse = AuthResponse.builder().accessToken(tokenMapping.getAccessToken()).refreshToken(token.getRefreshToken()).build();
        
        return ResponseEntity.ok(authResponse);
    }

    public ResponseEntity<?> signup(SignUpRequest signUpRequest){
        DefaultAssert.isTrue(!userRepository.existsByEmail(signUpRequest.getEmail()), "해당 이메일이 존재하지 않습니다.");

        User user = User.builder()
                        .name(signUpRequest.getName())
                        .email(signUpRequest.getEmail())
                        .password(passwordEncoder.encode(signUpRequest.getPassword()))
                        .provider(Provider.local)
                        .role(Role.ADMIN)
                        .build();

        userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/auth/")
                .buildAndExpand(user.getId()).toUri();
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(Message.builder().message("회원가입에 성공하였습니다.").build()).build();

        return ResponseEntity.created(location).body(apiResponse);
    }

    public ResponseEntity<?> refresh(RefreshTokenRequest tokenRefreshRequest){
        //1차 검증
        boolean checkValid = valid(tokenRefreshRequest.getRefreshToken());
        DefaultAssert.isAuthentication(checkValid);

        Optional<Token> token = tokenRepository.findByRefreshToken(tokenRefreshRequest.getRefreshToken());
        Authentication authentication = customTokenProviderService.getAuthenticationByEmail(token.get().getUserEmail());

        //4. refresh token 정보 값을 업데이트 한다.
        //시간 유효성 확인
        TokenMapping tokenMapping;

        Long expirationTime = customTokenProviderService.getExpiration(tokenRefreshRequest.getRefreshToken());
        if(expirationTime > 0){
            tokenMapping = customTokenProviderService.refreshToken(authentication, token.get().getRefreshToken());
        }else{
            tokenMapping = customTokenProviderService.createToken(authentication);
        }

        Token updateToken = token.get().updateRefreshToken(tokenMapping.getRefreshToken());
        tokenRepository.save(updateToken);

        AuthResponse authResponse = AuthResponse.builder().accessToken(tokenMapping.getAccessToken()).refreshToken(updateToken.getRefreshToken()).build();

        return ResponseEntity.ok(authResponse);
    }

    public ResponseEntity<?> signout(RefreshTokenRequest tokenRefreshRequest){
        boolean checkValid = valid(tokenRefreshRequest.getRefreshToken());
        DefaultAssert.isAuthentication(checkValid);

        //4 token 정보를 삭제한다.
        Optional<Token> token = tokenRepository.findByRefreshToken(tokenRefreshRequest.getRefreshToken());
        tokenRepository.delete(token.get());
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(Message.builder().message("로그아웃 하였습니다.").build()).build();

        return ResponseEntity.ok(apiResponse);
    }

    private boolean valid(String refreshToken){
        //1. 토큰 형식 물리적 검증
        boolean validateCheck = customTokenProviderService.validateToken(refreshToken);
        DefaultAssert.isTrue(validateCheck, "Token 검증에 실패하였습니다.");

        //2. refresh token 값을 불러온다.
        Optional<Token> token = tokenRepository.findByRefreshToken(refreshToken);
        DefaultAssert.isTrue(token.isPresent(), "탈퇴 처리된 회원입니다.");

        //3. email 값을 통해 인증값을 불러온다
        Authentication authentication = customTokenProviderService.getAuthenticationByEmail(token.get().getUserEmail());
        DefaultAssert.isTrue(token.get().getUserEmail().equals(authentication.getName()), "사용자 인증에 실패하였습니다.");

        return true;
    }

    public JwtTokenResponse getAccessTokenAndRefreshTokenAfterOauthLogin(Cookie[] cookies) {
        if (cookies == null) {
            throw new DefaultException(ErrorCode.INVALID_AUTHENTICATION);
        }

        String accessToken = findCookieValue(cookies, ACCESS_TOKEN_VALUE);
        String refreshToken = findCookieValue(cookies, REFRESH_TOKEN_VALUE);

        return JwtTokenResponse.fromCookie(accessToken, refreshToken);
    }

    private String findCookieValue(Cookie[] cookies, String cookieName) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException());
    }
}
