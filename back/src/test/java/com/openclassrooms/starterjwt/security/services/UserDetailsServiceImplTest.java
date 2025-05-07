package com.openclassrooms.starterjwt.security.services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void loadUserByUsername_userExists_returnsUserDetails() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("test@mail.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("secret");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));

        // Act
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername("test@mail.com");

        // Assert
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getId()).isEqualTo(1L);
        assertThat(userDetails.getUsername()).isEqualTo("test@mail.com");
        assertThat(userDetails.getFirstName()).isEqualTo("John");
        assertThat(userDetails.getLastName()).isEqualTo("Doe");
        assertThat(userDetails.getPassword()).isEqualTo("secret");
    }

    @Test
    public void loadUserByUsername_userDoesNotExist_throwsException() {
        // Arrange
        when(userRepository.findByEmail("notfound@mail.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("notfound@mail.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User Not Found with email: notfound@mail.com");
    }
}
