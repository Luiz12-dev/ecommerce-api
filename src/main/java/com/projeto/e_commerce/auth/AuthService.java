package com.projeto.e_commerce.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto.e_commerce.config.security.JwtUtils;
import com.projeto.e_commerce.exception.DuplicateResourceException;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public RegisterResponseDto register(RegisterRequestDto req) {
        if (appUserRepository.findByEmail(req.email()).isPresent()) {
            throw new DuplicateResourceException("Já existe um usuário com o e-mail: " + req.email());
        }

        AppUser user = AppUser.builder()
            .fullName(req.fullName())
            .email(req.email())
            .password(passwordEncoder.encode(req.password()))
            .role(req.role())
            .build();

        AppUser savedUser = appUserRepository.save(user);

        return new RegisterResponseDto(savedUser);
    }

    public AuthResponseDto login(LoginRequestDto req) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.generateToken(authentication);

        return new AuthResponseDto(accessToken);
    }
}
