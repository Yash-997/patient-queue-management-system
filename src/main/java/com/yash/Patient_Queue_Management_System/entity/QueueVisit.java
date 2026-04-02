package com.yash.Patient_Queue_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "queue_visit")
public class QueueVisit {

    // ─────────────────────────────────────────────────────────
    // Fields
    // ─────────────────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnoreProperties({"visits", "hibernateLazyInitializer", "handler"})
    private Patient patient;

    @Column(nullable = false)
    private String status;

    @Column(name = "visit_time", nullable = false)
    private LocalDateTime visitTime;

    // Constructors

    public QueueVisit() {
    }

    public QueueVisit(Patient patient, String status, LocalDateTime visitTime) {
        this.patient = patient;
        this.status = status;
        this.visitTime = visitTime;
    }

    public QueueVisit(Long id, Patient patient, String status, LocalDateTime visitTime) {
        this.id = id;
        this.patient = patient;
        this.status = status;
        this.visitTime = visitTime;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getVisitTime() {
        return visitTime;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setVisitTime(LocalDateTime visitTime) {
        this.visitTime = visitTime;
    }

    // toString

    @Override
    public String toString() {
        return "QueueVisit{" +
                "id=" + id +
                ", patientId=" + (patient != null ? patient.getId() : null) +
                ", status='" + status + '\'' +
                ", visitTime=" + visitTime +
                '}';
    }
}