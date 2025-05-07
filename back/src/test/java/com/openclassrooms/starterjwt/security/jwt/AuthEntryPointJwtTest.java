package com.openclassrooms.starterjwt.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Map;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class AuthEntryPointJwtTest {

    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private AuthenticationException authException;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        authException = new BadCredentialsException("Invalid credentials");
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCommence_ShouldReturnUnauthorizedResponse() throws IOException, ServletException {
        request.setServletPath("/api/test");
        authEntryPointJwt.commence(request, response, authException);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        String jsonResponse = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
        Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, Map.class);

        assertThat(responseMap).containsEntry("status", HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(responseMap).containsEntry("error", "Unauthorized");
        assertThat(responseMap).containsEntry("message", "Invalid credentials");
        assertThat(responseMap).containsEntry("path", "/api/test");
    }

    @Test
    public void testCommence_WithSpecialCharacters() throws IOException, ServletException {
        request.setServletPath("/api/auth");
        authException = new BadCredentialsException("Accès refusé");
        authEntryPointJwt.commence(request, response, authException);

        String jsonResponse = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
        Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, Map.class);

        assertThat(responseMap).containsEntry("status", HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(responseMap).containsEntry("error", "Unauthorized");
        assertThat(responseMap).containsEntry("message", "Accès refusé");
        assertThat(responseMap).containsEntry("path", "/api/auth");
    }
}
