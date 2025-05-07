package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher1;
    private Teacher teacher2;

    @BeforeEach
    void setUp() {
        teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setLastName("Dupont");

        teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setLastName("Martin");
    }

    @Test
    void findAll_shouldReturnAllTeachers() {
        when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher1, teacher2));

        List<Teacher> result = teacherService.findAll();

        assertEquals(2, result.size());
        assertEquals("Dupont", result.get(0).getLastName());
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void findById_shouldReturnTeacherWhenExists() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher1));

        Teacher result = teacherService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Dupont", result.getLastName());
        verify(teacherRepository, times(1)).findById(1L);
    }

    @Test
    void findById_shouldReturnNullWhenNotFound() {
        when(teacherRepository.findById(999L)).thenReturn(Optional.empty());

        Teacher result = teacherService.findById(999L);

        assertNull(result);
        verify(teacherRepository, times(1)).findById(999L);
    }
}
