package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnUserDto_whenValidIdProvided() throws Exception {

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("yoga@studio.com"));
    }

    @Test
    void shouldReturnNotFound_whenUserDoesNotExist() throws Exception {

        mockMvc.perform(get("/api/user/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequest_whenIdIsInvalid() throws Exception {
        mockMvc.perform(get("/api/user/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    void shouldDeleteUser_whenAuthorizedUser() throws Exception {

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "yoga@studio.com", "test!1234", Collections.singletonList(new SimpleGrantedAuthority("ADMIN"))
        );
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isOk());

    }

    @Test
    void shouldReturnNotFound_whenDeletingNonExistentUser() throws Exception {

        mockMvc.perform(delete("/api/user/42"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequest_whenDeletingWithInvalidId() throws Exception {
        mockMvc.perform(delete("/api/user/invalid"))
                .andExpect(status().isBadRequest());
    }
}
