package com.kiet.mse2sample;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
public class StudentController {

    private List<Student> studentList = new ArrayList<>();

    public StudentController() {
        // Initializing at least 5 students for in-memory storage[cite: 1]
        studentList.add(new Student(101, "Rahul Sharma", "rahul@kiet.edu", "CSE(AI & ML)", 2, 8.65));
        studentList.add(new Student(102, "Priya Singh", "priya@kiet.edu", "CSE", 3, 9.12));
        studentList.add(new Student(103, "Amit Verma", "amit.v@kiet.edu", "IT", 1, 7.80));
        studentList.add(new Student(104, "Sneha Gupta", "sneha@kiet.edu", "CSE(AI)", 2, 9.40));
        studentList.add(new Student(105, "Rohit Kumar", "rohit@kiet.edu", "ECE", 4, 8.10));
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return new ResponseEntity<>(studentList, HttpStatus.OK);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Object> getStudentById(@PathVariable int studentId) {
        for (Student s : studentList) {
            if (s.getStudentId() == studentId) {
                return new ResponseEntity<>(s, HttpStatus.OK);
            }
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", "Student not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        studentList.add(student);
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<Object> updateStudent(@PathVariable int studentId, @RequestBody Student updatedStudent) {
        for (int i = 0; i < studentList.size(); i++) {
            Student s = studentList.get(i);
            if (s.getStudentId() == studentId) {
                updatedStudent.setStudentId(studentId);
                studentList.set(i, updatedStudent);
                return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
            }
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", "Student not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Object> deleteStudent(@PathVariable int studentId) {
        boolean removed = studentList.removeIf(s -> s.getStudentId() == studentId);
        Map<String, String> response = new HashMap<>();
        if (removed) {
            response.put("message", "Student deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "Student not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Student>> searchStudents(@RequestParam String branch, @RequestParam int year) {
        List<Student> filtered = studentList.stream()
                .filter(s -> s.getBranch().equalsIgnoreCase(branch) && s.getYear() == year)
                .collect(Collectors.toList());
        return new ResponseEntity<>(filtered, HttpStatus.OK);
    }

    @GetMapping("/topper")
    public ResponseEntity<List<Student>> getTopperStudents(@RequestParam double cgpa) {
        List<Student> filtered = studentList.stream()
                .filter(s -> s.getCgpa() >= cgpa)
                .collect(Collectors.toList());
        return new ResponseEntity<>(filtered, HttpStatus.OK);
    }
}