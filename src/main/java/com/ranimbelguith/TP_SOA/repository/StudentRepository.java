package com.ranimbelguith.TP_SOA.repository;

import com.ranimbelguith.TP_SOA.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
}
