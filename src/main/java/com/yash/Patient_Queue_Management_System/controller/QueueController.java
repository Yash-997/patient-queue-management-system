package com.yash.Patient_Queue_Management_System.controller;

import com.yash.Patient_Queue_Management_System.entity.Patient;
import com.yash.Patient_Queue_Management_System.entity.QueueVisit;
import com.yash.Patient_Queue_Management_System.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/queue")
public class QueueController {

    @Autowired
    private QueueService queueService;

    // -------------------------------------------------------
    // POST /api/queue/add
    // Add a new patient to the queue
    // -------------------------------------------------------

    @PostMapping("/add")
    public ResponseEntity<Patient> addPatient(@RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        int priority = (Integer) body.get("priority");

        Patient saved = queueService.addPatient(name, priority);
        return ResponseEntity.ok(saved);
    }

    // -------------------------------------------------------
    // GET /api/queue/serve
    // Serve the next highest-priority patient
    // -------------------------------------------------------

    @GetMapping("/serve")
    public ResponseEntity<?> serveNext() {
        Patient patient = queueService.serveNextPatient();

        if (patient == null) {
            return ResponseEntity
                    .noContent()
                    .build(); // 204: Queue is empty
        }

        return ResponseEntity.ok(patient);
    }

    // -------------------------------------------------------
    // GET /api/queue/patients
    // Get all patients in the database
    // -------------------------------------------------------

    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(queueService.getAllPatients());
    }

    // -------------------------------------------------------
    // GET /api/queue/visits/{patientId}
    // Get visit history for a specific patient
    // -------------------------------------------------------

    @GetMapping("/visits/{patientId}")
    public ResponseEntity<List<QueueVisit>> getVisits(@PathVariable Long patientId) {
        return ResponseEntity.ok(queueService.getVisitsByPatient(patientId));
    }

    // -------------------------------------------------------
    // GET /api/queue/size
    // Get current number of patients waiting in queue
    // -------------------------------------------------------

    @GetMapping("/size")
    public ResponseEntity<Map<String, Integer>> getQueueSize() {
        return ResponseEntity.ok(Map.of("queueSize", queueService.getQueueSize()));
    }
}