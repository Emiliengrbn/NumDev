package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session session;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        session = new Session();
        session.setId(1L);
        session.setUsers(new ArrayList<>());
    }

    @Test
    void create_shouldSaveSession() {
        when(sessionRepository.save(session)).thenReturn(session);

        Session created = sessionService.create(session);

        assertEquals(session, created);
        verify(sessionRepository).save(session);
    }

    @Test
    void delete_shouldCallDeleteById() {
        sessionService.delete(1L);

        verify(sessionRepository).deleteById(1L);
    }

    @Test
    void findAll_shouldReturnAllSessions() {
        List<Session> sessions = List.of(session);
        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> result = sessionService.findAll();

        assertEquals(1, result.size());
        assertEquals(session, result.get(0));
    }

    @Test
    void getById_shouldReturnSessionIfFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        Session result = sessionService.getById(1L);

        assertEquals(session, result);
    }

    @Test
    void getById_shouldReturnNullIfNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        Session result = sessionService.getById(1L);

        assertNull(result);
    }

    @Test
    void update_shouldSetIdAndSave() {
        Session newSession = new Session();
        newSession.setUsers(new ArrayList<>());

        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        Session updated = sessionService.update(1L, newSession);

        assertEquals(1L, updated.getId());
        verify(sessionRepository).save(newSession);
    }

    @Test
    void participate_shouldAddUserToSession() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        sessionService.participate(1L, 1L);

        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }

    @Test
    void participate_shouldThrowNotFoundIfSessionMissing() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
    }

    @Test
    void participate_shouldThrowNotFoundIfUserMissing() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
    }

    @Test
    void participate_shouldThrowBadRequestIfAlreadyParticipating() {
        session.getUsers().add(user);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 1L));
    }

    @Test
    void noLongerParticipate_shouldRemoveUser() {
        session.getUsers().add(user);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        sessionService.noLongerParticipate(1L, 1L);

        assertFalse(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }

    @Test
    void noLongerParticipate_shouldThrowNotFoundIfSessionMissing() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }

    @Test
    void noLongerParticipate_shouldThrowBadRequestIfUserNotInSession() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }
}
