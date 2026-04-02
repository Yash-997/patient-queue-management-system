package com.yash.Patient_Queue_Management_System.entity;

import jakarta.persistence.*;
import java.util.List;

/**
 * Represents a patient registered in the system.
 *
 * priority: higher number = served first
 * Example: priority 10 (Critical) is served before priority 1 (General)
 *
 * No Lombok — all getters, setters, and constructors are written manually.
 */
@Entity
@Table(name = "patient")
public class Patient {

    // -------------------------------------------------------
    // Fields
    // -------------------------------------------------------

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    /**
     * Priority level.
     * Higher value = higher urgency = served sooner.
     * e.g. 10 = Critical, 5 = Urgent, 1 = General
     */
    @Column(nullable = false)
    private int priority;

    /**
     * One patient can have many visits over time.
     * mappedBy = "patient" refers to the field name in QueueVisit.
     */
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QueueVisit> visits;

    // -------------------------------------------------------
    // Constructors
    // -------------------------------------------------------

    /** Default no-arg constructor — required by JPA */
    public Patient() {
    }

    /** Constructor with all fields (excluding visits) */
    public Patient(Long id, String name, int priority) {
        this.id = id;
        this.name = name;
        this.priority = priority;
    }

    // -------------------------------------------------------
    // Getters
    // -------------------------------------------------------

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public List<QueueVisit> getVisits() {
        return visits;
    }

    // -------------------------------------------------------
    // Setters
    // -------------------------------------------------------

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setVisits(List<QueueVisit> visits) {
        this.visits = visits;
    }

    // -------------------------------------------------------
    // toString (useful for logs/debugging)
    // -------------------------------------------------------

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", priority=" + priority +
                '}';
    }
}