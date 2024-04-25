package com.geekyhacker.mockito.student;

import java.util.Objects;

public class StudentService {
  private final StudentRepository studentRepository;

  public StudentService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  public double calculateStudentGpa(int studentId) {
    var student = studentRepository.getStudentById(studentId);
    if (Objects.isNull(student)) {
      return 0;
    }
    return student.getCourses().stream()
        .mapToDouble(Student.Course::getScore)
        .average().orElse(0);
  }
}
