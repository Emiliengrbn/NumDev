package com.openclassrooms.starterjwt.security.services;

import static org.assertj.core.api.Assertions.*;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UserDetailsImplTest {

    @Test
    public void testBuilderAndGetters() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(42L)
                .username("jdoe")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .password("secret")
                .build();

        assertThat(user.getId()).isEqualTo(42L);
        assertThat(user.getUsername()).isEqualTo("jdoe");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getAdmin()).isTrue();
        assertThat(user.getPassword()).isEqualTo("secret");
    }

    @Test
    public void testAuthoritiesIsEmpty() {
        UserDetailsImpl user = UserDetailsImpl.builder().build();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertThat(authorities).isNotNull();
        assertThat(authorities).isEmpty();
    }

    @Test
    public void testAccountStatusMethods() {
        UserDetailsImpl user = UserDetailsImpl.builder().build();
        assertThat(user.isAccountNonExpired()).isTrue();
        assertThat(user.isAccountNonLocked()).isTrue();
        assertThat(user.isCredentialsNonExpired()).isTrue();
        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    public void testEqualsAndHashCode() {
        UserDetailsImpl user1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl user2 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl user3 = UserDetailsImpl.builder().id(2L).build();

        assertThat(user1).isEqualTo(user2);
        assertThat(user1).isNotEqualTo(user3);
    }

    @Test
    public void testJsonIgnorePassword() throws Exception {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("jdoe")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("secret")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user);

        assertThat(json).doesNotContain("secret");
        assertThat(json).contains("jdoe");
    }
}
