package com.yash.Patient_Queue_Management_System.service;

import com.yash.Patient_Queue_Management_System.entity.QueueVisit;
import com.yash.Patient_Queue_Management_System.entity.Patient;
import com.yash.Patient_Queue_Management_System.repository.PatientRepository;
import com.yash.Patient_Queue_Management_System.repository.QueueVisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Core service for Patient Queue Management.
 *
 * How PriorityQueue works here:
 * -----------------------------------------------
 * Java's PriorityQueue is a min-heap by default (smallest first).
 * We reverse it using Comparator.comparingInt(...).reversed()
 * so that higher priority patients come out FIRST.
 *
 * Example:
 *   Patient A: priority 3 (general)
 *   Patient B: priority 7 (urgent)
 *   Patient C: priority 10 (critical)
 *   → serveNext() returns Patient C (priority 10) first.
 */
@Service
public class QueueService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private QueueVisitRepository queueVisitRepository;

    /**
     * In-memory priority queue: patients with higher priority are served first.
     * Comparator.comparingInt(Patient::getPriority).reversed()
     * → turns min-heap into max-heap (highest priority = polled first)
     */
    private final PriorityQueue<Patient> patientQueue =
            new PriorityQueue<>(Comparator.comparingInt(Patient::getPriority).reversed());

    // -------------------------------------------------------
    // 1. Add a patient (save to DB + add to in-memory queue)
    // -------------------------------------------------------

    /**
     * Registers a new patient and adds them to the priority queue.
     * Also creates a QueueVisit with status = "WAITING".
     *
     * @param name     Patient's full name
     * @param priority Integer priority (higher = served sooner)
     * @return Saved Patient object
     */
    public Patient addPatient(String name, int priority) {
        // Save patient to DB
        Patient patient = new Patient();
        patient.setName(name);
        patient.setPriority(priority);
        Patient savedPatient = patientRepository.save(patient);

        // Add to in-memory priority queue
        patientQueue.offer(savedPatient);

        // Record a WAITING visit entry
        QueueVisit visit = new QueueVisit();
        visit.setPatient(savedPatient);
        visit.setStatus("WAITING");
        visit.setVisitTime(LocalDateTime.now());
        queueVisitRepository.save(visit);

        return savedPatient;
    }

    // -------------------------------------------------------
    // 2. Serve next patient (poll from queue + update DB)
    // -------------------------------------------------------

    /**
     * Removes and returns the highest-priority patient from the queue.
     * Records a QueueVisit with status = "SERVED".
     *
     * @return The next Patient to be served, or null if queue is empty
     */
    public Patient serveNextPatient() {
        // Poll removes the head (highest priority) from the queue
        Patient patient = patientQueue.poll();

        if (patient == null) {
            return null; // Queue is empty
        }

        // Log the visit as SERVED
        QueueVisit visit = new QueueVisit();
        visit.setPatient(patient);
        visit.setStatus("SERVED");
        visit.setVisitTime(LocalDateTime.now());
        queueVisitRepository.save(visit);

        return patient;
    }

    // -------------------------------------------------------
    // 3. Get all patients from DB
    // -------------------------------------------------------

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // -------------------------------------------------------
    // 4. Get visit history for a patient
    // -------------------------------------------------------


    public List<QueueVisit> getVisitsByPatient(Long patientId) {
        return queueVisitRepository.findByPatientId(patientId);
    }

    // -------------------------------------------------------
    // 5. Get current queue size
    // -------------------------------------------------------


    public int getQueueSize() {
        return patientQueue.size();
    }
}