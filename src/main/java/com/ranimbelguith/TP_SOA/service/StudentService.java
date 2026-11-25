package com.ranimbelguith.TP_SOA.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ranimbelguith.TP_SOA.entity.StudentEntity;
import com.ranimbelguith.TP_SOA.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<StudentEntity> findAll() {
        return studentRepository.findAll();
    }

    public StudentEntity findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Étudiant introuvable"));
    }

    public StudentEntity create(StudentEntity student) {
        student.setId(null);
        return studentRepository.save(student);
    }

    public StudentEntity update(Long id, StudentEntity student) {
        StudentEntity existingStudent = findById(id);
        existingStudent.setNom(student.getNom());
        existingStudent.setLevel(student.getLevel());
        return studentRepository.save(existingStudent);
    }

    public void delete(Long id) {
        StudentEntity student = findById(id);
        studentRepository.delete(student);
    }
}
