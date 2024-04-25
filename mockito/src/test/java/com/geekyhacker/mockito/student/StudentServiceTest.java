package com.geekyhacker.mockito.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @InjectMocks
  private StudentService studentService;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private StudentRepository studentRepository;

  @Test
  void shouldCalculateGpaOfGivenStudent() {
    when(studentRepository.getStudentById(any(Integer.class)).getCourses())
        .thenReturn(List.of(new Student.Course("Data Structure", 14.25),
            new Student.Course("C programming", 20.0)));

    var result = studentService.calculateStudentGpa(1);
    assertEquals(17.125, result);
  }

  @Nested
  class withoutDeepStub {
    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    @Test
    void shouldCalculateGpaOfGivenStudent() {
      var student = mock(Student.class);
      when(studentRepository.getStudentById(any(Integer.class))).thenReturn(student);
      when(student.getCourses()).thenReturn(List.of(new Student.Course("Data Structure", 14.25),
          new Student.Course("C programming", 20.0)));

      var result = studentService.calculateStudentGpa(1);
      assertEquals(17.125, result);
    }
  }

  @Nested
  class deepStubWithoutAnnotation {

    private StudentService studentService;

    private StudentRepository studentRepository;

    deepStubWithoutAnnotation() {
      studentRepository = mock(StudentRepository.class, Answers.RETURNS_DEEP_STUBS);
      studentService = new StudentService(studentRepository);
    }

    @Test
    void shouldCalculateGpaOfGivenStudent() {
      when(studentRepository.getStudentById(any(Integer.class)).getCourses())
          .thenReturn(List.of(new Student.Course("Data Structure", 14.25),
              new Student.Course("C programming", 20.0)));

      var result = studentService.calculateStudentGpa(1);
      assertEquals(17.125, result);
    }
  }
}