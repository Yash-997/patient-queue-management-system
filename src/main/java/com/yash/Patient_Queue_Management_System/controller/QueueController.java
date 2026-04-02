package com.yash.Patient_Queue_Management_System.controller;

import com.yash.Patient_Queue_Management_System.entity.Patient;
import com.yash.Patient_Queue_Management_System.entity.QueueVisit;
import com.yash.Patient_Queue_Management_System.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/queue")
public class QueueController {

    @Autowired
    private QueueService queueService;


    // POST /api/queue/add
    // Add a new patient to the queue

    @PostMapping("/add")
    public ResponseEntity<Patient> addPatient(@RequestBody Map<String, Object> requestBody) {
        String name = (String) requestBody.get("name");
        int priority = (Integer) requestBody.get("priority");

        Patient savedPatient = queueService.addPatient(name, priority);
        return ResponseEntity.ok(savedPatient);
    }

    // GET /api/queue/serve
    // Serve the next highest-priority patient

    @GetMapping("/serve")
    public ResponseEntity<?> serveNextPatient() {
        Patient patient = queueService.serveNextPatient();

        if (patient == null) {
            // Queue is empty — return 204 No Content
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(patient);
    }

    // GET /api/queue/patients
    // Get all registered patients from database

    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = queueService.getAllPatients();
        return ResponseEntity.ok(patients);
    }


    // GET /api/queue/visits/{patientId}
    // Get all visit records for a specific patient

    @GetMapping("/visits/{patientId}")
    public ResponseEntity<List<QueueVisit>> getVisitHistory(@PathVariable Long patientId) {
        List<QueueVisit> visits = queueService.getVisitsByPatient(patientId);
        return ResponseEntity.ok(visits);
    }

    // GET /api/queue/size
    // Get current number of patients in the in-memory queue

    @GetMapping("/size")
    public ResponseEntity<Map<String, Integer>> getQueueSize() {
        Map<String, Integer> response = new HashMap<>();
        response.put("queueSize", queueService.getQueueSize());
        return ResponseEntity.ok(response);
    }
}