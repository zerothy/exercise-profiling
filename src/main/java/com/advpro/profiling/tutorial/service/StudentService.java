package com.advpro.profiling.tutorial.service;

import com.advpro.profiling.tutorial.model.Student;
import com.advpro.profiling.tutorial.model.StudentCourse;
import com.advpro.profiling.tutorial.repository.StudentCourseRepository;
import com.advpro.profiling.tutorial.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author muhammad.khadafi
 */
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    public List<StudentCourse> getAllStudentsWithCourses() {
        List<Student> students = studentRepository.findAll();

        Map<Long, List<StudentCourse>> coursesMap = students.stream()
                .collect(Collectors.toMap(
                        Student::getId,
                        student -> studentCourseRepository.findByStudentId(student.getId())
                ));

        return students.stream()
                .flatMap(student -> coursesMap.get(student.getId()).stream()
                        .map(sc -> {
                            StudentCourse newSc = new StudentCourse();
                            newSc.setStudent(student);
                            newSc.setCourse(sc.getCourse());
                            return newSc;
                        }))
                .collect(Collectors.toList());
    }

    public Optional<Student> findStudentWithHighestGpa() {
        return studentRepository.findAll().stream()
                .max(Comparator.comparingDouble(Student::getGpa));
    }

    public String joinStudentNames() {
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .collect(Collectors.joining(", "));
    }
}

