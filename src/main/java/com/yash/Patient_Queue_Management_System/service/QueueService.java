package com.yash.Patient_Queue_Management_System.service;

import com.yash.Patient_Queue_Management_System.entity.Patient;
import com.yash.Patient_Queue_Management_System.entity.QueueVisit;
import com.yash.Patient_Queue_Management_System.repository.PatientRepository;
import com.yash.Patient_Queue_Management_System.repository.QueueVisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

@Service
public class QueueService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private QueueVisitRepository queueVisitRepository;


    private final PriorityQueue<Patient> patientQueue =
            new PriorityQueue<>(Comparator.comparingInt(Patient::getPriority).reversed());


    // 1. ADD PATIENT
    public Patient addPatient(String name, int priority) {

        // Step 1: Build and save patient to database
        Patient patient = new Patient();
        patient.setName(name);
        patient.setPriority(priority);
        Patient savedPatient = patientRepository.save(patient);
        // After save(), savedPatient.getId() is now populated by MySQL AUTO_INCREMENT

        // Step 2: Add to in-memory queue
        // offer() inserts and re-arranges the heap so the highest priority stays at the top
        patientQueue.offer(savedPatient);

        // Step 3: Record a WAITING entry in queue_visit
        QueueVisit visit = new QueueVisit();
        visit.setPatient(savedPatient);
        visit.setStatus("WAITING");
        visit.setVisitTime(LocalDateTime.now());
        queueVisitRepository.save(visit);

        return savedPatient;
    }
    // 2. SERVE NEXT PATIENT

    public Patient serveNextPatient() {

        // Step 1: Remove highest-priority patient from queue
        // poll() returns null if queue is empty (no exception thrown)
        Patient patient = patientQueue.poll();

        // Step 2: Nothing to serve
        if (patient == null) {
            return null;
        }

        // Step 3: Record SERVED visit in database
        QueueVisit visit = new QueueVisit();
        visit.setPatient(patient);
        visit.setStatus("SERVED");
        visit.setVisitTime(LocalDateTime.now());
        queueVisitRepository.save(visit);

        return patient;
    }
    // 3. GET ALL PATIENTS
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }


    // 4. GET VISIT HISTORY FOR A PATIENT
    public List<QueueVisit> getVisitsByPatient(Long patientId) {
        return queueVisitRepository.findByPatientId(patientId);
    }
    // 5. GET CURRENT QUEUE SIZE

    public int getQueueSize() {
        return patientQueue.size();
    }
}