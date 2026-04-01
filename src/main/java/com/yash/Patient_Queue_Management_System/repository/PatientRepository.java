package com.yash.Patient_Queue_Management_System.repository;

import com.yash.Patient_Queue_Management_System.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // JpaRepository provides: save(), findById(), findAll(), deleteById(), etc.
}