package com.yash.Patient_Queue_Management_System.repository;

import com.yash.Patient_Queue_Management_System.entity.QueueVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueueVisitRepository extends JpaRepository<QueueVisit, Long> {


    List<QueueVisit> findByPatientIdOrderByVisitTimeDesc(Long patientId);

    List<QueueVisit> findAllByOrderByVisitTimeDesc();
}