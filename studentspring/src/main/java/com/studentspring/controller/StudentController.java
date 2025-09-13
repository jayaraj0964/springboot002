package com.studentspring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.studentspring.entity.Student;
import com.studentspring.service.StudentService;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService service;
        private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    // @PostMapping("/post")
    // public ResponseEntity<Student> create(@RequestBody Student student) {
    //      logger.debug("🔐 detailes " );
    //     return ResponseEntity.ok(service.createStudent(student));
    // }

    @PostMapping("/post")
public ResponseEntity<Student> create(@RequestBody Student student) {
    logger.debug("🔐 Creating student: {}", student.getName());
    Student savedStudent = service.createStudent(student);
    return ResponseEntity.ok(savedStudent);
}


    @PatchMapping("/{id}")
    public ResponseEntity<Student> partialUpdate(@PathVariable Long id, @RequestBody Student student) {
        return ResponseEntity.ok(service.updateStudent(id, student));
    }

    @GetMapping("/get")
    public ResponseEntity<List<Student>> getAll() {
        return ResponseEntity.ok(service.getAllStudents());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}

