package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Session session;

    private Teacher teacher;

    @BeforeEach
    void setUp() {

        teacher = new Teacher();

        teacher.setFirstName("Elodie");
        teacher.setLastName("DUPONT");
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());

        teacher = teacherRepository.save(teacher);

        sessionRepository.deleteAll();

        session = new Session();
        session.setName("Yoga");
        session.setDate(new Date());
        session.setTeacher(teacher);
        session.setDescription("Cours de yoga");
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());

        session = sessionRepository.save(session);

    }

    @Test
    void shouldReturnAllSessions() throws Exception {
        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Yoga"));
    }

    @Test
    void shouldReturnSessionById() throws Exception {
        mockMvc.perform(get("/api/session/" + session.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga"));
    }

    @Test
    void shouldReturnNotFoundForUnknownId() throws Exception {
        mockMvc.perform(get("/api/session/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestForInvalidId() throws Exception {
        mockMvc.perform(get("/api/session/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateSession() throws Exception {
        String json = """
                {
                    "name": "Pilates",
                    "date": "2025-05-07T10:00:00.000+00:00",
                    "teacher_id": 2,
                    "description": "Cours de pilates",
                    "users": [],
                    "createdAt": "2025-05-07T10:00:00",
                    "updatedAt": "2025-05-07T10:00:00"
                }
                """;

        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pilates"));
    }

    @Test
    void shouldUpdateSession() throws Exception {
        String json = String.format("""
                {
                    "id": %d,
                    "name": "Yoga modifié",
                    "date": "2025-05-07T10:00:00.000+00:00",
                    "teacher_id": 1,
                    "description": "Description modifiée",
                    "users": [],
                    "createdAt": "2025-05-07T10:00:00",
                    "updatedAt": "2025-05-07T10:00:00"
                }
                """, session.getId());

        mockMvc.perform(put("/api/session/" + session.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga modifié"));
    }

    @Test
    void shouldDeleteSession() throws Exception {
        mockMvc.perform(delete("/api/session/" + session.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/session/" + session.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestOnDeleteWithInvalidId() throws Exception {
        mockMvc.perform(delete("/api/session/abc"))
                .andExpect(status().isBadRequest());
    }
}
