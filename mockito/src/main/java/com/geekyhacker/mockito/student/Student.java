package com.geekyhacker.mockito.student;

import java.util.List;

public class Student {

  private String firstName;

  private String lastName;
  private List<Course> courses;

  public Student(String firstName, String lastName, List<Course> courses) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.courses = courses;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public List<Course> getCourses() {
    return courses;
  }

  public static class Course {

    private String name;

    private double score;

    public Course(String name, double score) {
      this.name = name;
      this.score = score;
    }

    public String getName() {
      return name;
    }

    public double getScore() {
      return score;
    }
  }
}
