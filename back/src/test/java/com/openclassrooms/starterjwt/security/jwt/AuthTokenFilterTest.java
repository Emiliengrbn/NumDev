package com.openclassrooms.starterjwt.security.jwt;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    public void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }

    @Test
    public void doFilterInternal_withValidJwt_setsAuthentication() throws ServletException, IOException {
        // Arrange
        String jwt = "valid.jwt.token";
        String username = "user1";
        request.addHeader("Authorization", "Bearer " + jwt);

        when(jwtUtils.validateJwtToken(jwt)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(jwt)).thenReturn(username);

        UserDetails userDetails = new User(username, "password", Collections.emptyList());
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(userDetails);
        assertThat(authentication.isAuthenticated()).isTrue();

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterInternal_withInvalidJwt_doesNotSetAuthentication() throws ServletException, IOException {
        // Arrange
        String jwt = "invalid.jwt.token";
        request.addHeader("Authorization", "Bearer " + jwt);

        when(jwtUtils.validateJwtToken(jwt)).thenReturn(false);

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterInternal_withoutAuthorizationHeader_doesNotSetAuthentication() throws ServletException, IOException {
        // Arrange : pas de header Authorization

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterInternal_withException_logsErrorAndContinues() throws ServletException, IOException {
        // Arrange
        String jwt = "jwt";
        request.addHeader("Authorization", "Bearer " + jwt);

        when(jwtUtils.validateJwtToken(jwt)).thenThrow(new RuntimeException("JWT error"));

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain, times(1)).doFilter(request, response);
        // Ici, on ne teste pas le logger, mais on vérifie que le filtre continue malgré l'exception
    }
}
