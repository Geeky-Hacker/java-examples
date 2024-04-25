package com.geekyhacker.mockito.student;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StudentRepository {
  Map<Integer, Student> students;

  public StudentRepository() {
    students = Map.of(
        1, new Student("James", "Doe", List.of(new Student.Course("Physics", 19.5), new Student.Course("Chemistry", 18.25))),
        2, new Student("Myra", "Ellis", List.of(new Student.Course("Biology", 15.75), new Student.Course("Political Science", 15.5))),
        3, new Student("Hazel", "Brewer", List.of(new Student.Course("Data Structure", 14.25), new Student.Course("C programming", 20.0)))
    );
  }

  public Student getStudentById(int studentId) {
    return students.get(studentId);
  }
}
