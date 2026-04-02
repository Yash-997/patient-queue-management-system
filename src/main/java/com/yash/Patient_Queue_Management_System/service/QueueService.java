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
import java.util.Optional;
import java.util.PriorityQueue;

@Service
public class QueueService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private QueueVisitRepository queueVisitRepository;

    private final PriorityQueue<Patient> patientQueue =
            new PriorityQueue<>(Comparator.comparingInt(Patient::getPriority).reversed());

    private static final int MINUTES_PER_PATIENT = 5;

    // 1. ADD PATIENT  (with deduplication fix)

    public Patient addPatient(String name, int priority) {

        // Step 1: Check if a patient with this name already exists
        Optional<Patient> existingPatient = patientRepository.findByName(name);

        Patient patient;

        if (existingPatient.isPresent()) {
            // Step 2a: Reuse existing patient — update priority for this visit
            patient = existingPatient.get();
            patient.setPriority(priority);
            patient = patientRepository.save(patient);   // persist priority update

        } else {
            // Step 2b: First-time patient — create and save a new record
            patient = new Patient(name, priority);
            patient = patientRepository.save(patient);
        }

        // Step 3: Add (or re-add) to in-memory priority queue
        // offer() places the patient in the correct heap position
        patientQueue.offer(patient);

        // Step 4: Log a WAITING visit — always happens regardless of new/existing
        QueueVisit visit = new QueueVisit(patient, "WAITING", LocalDateTime.now());
        queueVisitRepository.save(visit);

        return patient;
    }

    // 2. SERVE NEXT PATIENT

    public Patient serveNextPatient() {

        // Remove the top of the max-heap
        Patient patient = patientQueue.poll();

        if (patient == null) {
            return null;    // Queue was empty — nothing to serve
        }

        // Log the served event in DB
        QueueVisit visit = new QueueVisit(patient, "SERVED", LocalDateTime.now());
        queueVisitRepository.save(visit);

        return patient;
    }

    // 3. GET ALL PATIENTS
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // 4. GET VISIT HISTORY FOR ONE PATIENT
    public List<QueueVisit> getVisitsByPatient(Long patientId) {
        return queueVisitRepository.findByPatientIdOrderByVisitTimeDesc(patientId);
    }

    // 5. GET ALL VISITS (new — Enhancement 1)

    public List<QueueVisit> getAllVisits() {
        return queueVisitRepository.findAllByOrderByVisitTimeDesc();
    }

    // 6. GET QUEUE SIZE + ESTIMATED WAIT TIME (Enhancement 3)

    public int getQueueSize() {
        return patientQueue.size();
    }

    public int getEstimatedWaitTime() {
        return patientQueue.size() * MINUTES_PER_PATIENT;
    }
}