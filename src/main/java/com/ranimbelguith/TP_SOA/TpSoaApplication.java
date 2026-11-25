package com.ranimbelguith.TP_SOA;

import com.ranimbelguith.TP_SOA.entity.StudentEntity;
import com.ranimbelguith.TP_SOA.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TpSoaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TpSoaApplication.class, args);
	}

	@Bean
	CommandLineRunner printStudentsAtStartup(StudentRepository studentRepository) {
		return args -> {
			System.out.println("Etudiants dans la base de donnees :");
			for (StudentEntity student : studentRepository.findAll()) {
				System.out.println(student);
			}
		};
	}

}
