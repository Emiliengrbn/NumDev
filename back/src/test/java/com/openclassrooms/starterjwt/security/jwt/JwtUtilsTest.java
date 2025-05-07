package com.openclassrooms.starterjwt.security.jwt;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

public class JwtUtilsTest {

    private JwtUtils jwtUtils;

    private final String jwtSecret = "testSecretKey1234567890";
    private final int jwtExpirationMs = 1000 * 60 * 60; // 1 heure

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        jwtUtils = new JwtUtils();

        // Injection des valeurs @Value via la réflexion
        java.lang.reflect.Field secretField = JwtUtils.class.getDeclaredField("jwtSecret");
        secretField.setAccessible(true);
        secretField.set(jwtUtils, jwtSecret);

        java.lang.reflect.Field expField = JwtUtils.class.getDeclaredField("jwtExpirationMs");
        expField.setAccessible(true);
        expField.set(jwtUtils, jwtExpirationMs);
    }

    @Test
    public void testGenerateAndValidateJwtToken() {
        // Arrange
        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L,           // id
                "testuser",   // username
                "Test",       // firstName
                "User",       // lastName
                false,        // admin
                "password"    // password
        );when(authentication.getPrincipal()).thenReturn(userDetails);

        // Act
        String token = jwtUtils.generateJwtToken(authentication);

        // Assert
        assertThat(token).isNotBlank();
        assertThat(jwtUtils.validateJwtToken(token)).isTrue();

        String username = jwtUtils.getUserNameFromJwtToken(token);
        assertThat(username).isEqualTo("testuser");
    }

    @Test
    public void testValidateJwtToken_invalidToken() {
        // Arrange
        String invalidToken = "this.is.not.a.jwt";

        // Act
        boolean isValid = jwtUtils.validateJwtToken(invalidToken);

        // Assert
        assertThat(isValid).isFalse();
    }

    @Test
    public void testValidateJwtToken_expiredToken() throws Exception {
        // Arrange
        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L,           // id
                "testuser",   // username
                "Test",       // firstName
                "User",       // lastName
                false,        // admin
                "password"    // password
        );
        // Génère un token déjà expiré
        String expiredToken = io.jsonwebtoken.Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis() - 2000))
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        // Act
        boolean isValid = jwtUtils.validateJwtToken(expiredToken);

        // Assert
        assertThat(isValid).isFalse();
    }
}
