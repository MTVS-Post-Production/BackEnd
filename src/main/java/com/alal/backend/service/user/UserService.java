package com.alal.backend.service.user;


import com.alal.backend.advice.assertThat.DefaultAssert;
import com.alal.backend.config.security.token.UserPrincipal;
import com.alal.backend.domain.entity.user.User;
import com.alal.backend.payload.response.ApiResponse;
import com.alal.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public ResponseEntity<?> readByUser(UserPrincipal userPrincipal){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isOptionalPresent(user);
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(user.get()).build();
        return ResponseEntity.ok(apiResponse);
    }
}
