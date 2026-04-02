package com.yash.Patient_Queue_Management_System.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Tracks each event in a patient's queue journey.
 *
 * Every time a patient is added (WAITING) or served (SERVED),
 * a new QueueVisit row is created with a timestamp and status.
 *
 * No Lombok — all getters, setters, and constructors written manually.
 */
@Entity
@Table(name = "queue_visit")
public class QueueVisit {

    // -------------------------------------------------------
    // Fields
    // -------------------------------------------------------

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private String status;

    @Column(name = "visit_time", nullable = false)
    private LocalDateTime visitTime;

    // -------------------------------------------------------
    // Constructors
    // -------------------------------------------------------

    public QueueVisit() {
    }

    public QueueVisit(Long id, Patient patient, String status, LocalDateTime visitTime) {
        this.id = id;
        this.patient = patient;
        this.status = status;
        this.visitTime = visitTime;
    }

    // -------------------------------------------------------
    // Getters
    // -------------------------------------------------------

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

    // -------------------------------------------------------
    // Setters
    // -------------------------------------------------------

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

    // -------------------------------------------------------
    // toString
    // -------------------------------------------------------

    @Override
    public String toString() {
        return "QueueVisit{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", visitTime=" + visitTime +
                '}';
    }
}