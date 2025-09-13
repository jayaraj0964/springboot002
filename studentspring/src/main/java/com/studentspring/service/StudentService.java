package com.studentspring.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studentspring.entity.Student;
import com.studentspring.repository.StudentRepository;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private StudentRepository studentRepository;

    // Create
    // public Student createStudent(Student student) {
    //     logger.info("Creating new student: {}", student.getName());
    //     return studentRepository.save(student);
    // }

    public Student createStudent(Student student) {
    logger.info("Creating new student: {}", student.getName());
    Student saved = studentRepository.save(student);
    logger.info("✅ Student saved with ID: {}", saved.getStudentId());
    return saved;
}

    // Read All
    public List<Student> getAllStudents() {
        logger.debug("Fetching all students from database");
        return studentRepository.findAll();
    }

    // Read One
    public Student getStudentById(Long id) {
        logger.debug("Fetching student with ID: {}", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Student not found with ID: {}", id);
                    return new RuntimeException("Student not found");
                });
    }

    // Update (Full)
    public Student updateStudent(Long id, Student updatedStudent) {
        logger.info("Updating student with ID: {}", id);
        Student existing = getStudentById(id);

        existing.setName(updatedStudent.getName());
        existing.setAge(updatedStudent.getAge());
        existing.setSection(updatedStudent.getSection());
        existing.setBloodGroup(updatedStudent.getBloodGroup());

        return studentRepository.save(existing);
    }

    // Partial Update (CRUP)
    public Student partialUpdateStudent(Long id, Student updatedStudent) {
        logger.info("Partially updating student with ID: {}", id);
        Student existing = getStudentById(id);

        if (updatedStudent.getName() != null) {
            existing.setName(updatedStudent.getName());
            logger.debug("Updated name to {}", updatedStudent.getName());
        }
        if (updatedStudent.getAge() != 0) {
            existing.setAge(updatedStudent.getAge());
            logger.debug("Updated age to {}", updatedStudent.getAge());
        }
        if (updatedStudent.getSection() != null) {
            existing.setSection(updatedStudent.getSection());
            logger.debug("Updated section to {}", updatedStudent.getSection());
        }
        if (updatedStudent.getBloodGroup() != null) {
            existing.setBloodGroup(updatedStudent.getBloodGroup());
            logger.debug("Updated blood group to {}", updatedStudent.getBloodGroup());
        }

        return studentRepository.save(existing);
    }

    // Delete
    public void deleteStudent(Long id) {
        logger.warn("Deleting student with ID: {}", id);
        studentRepository.deleteById(id);
    }
}
