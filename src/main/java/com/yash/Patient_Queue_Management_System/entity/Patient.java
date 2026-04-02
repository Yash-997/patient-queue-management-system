package com.yash.Patient_Queue_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "patient")
public class Patient {

    // Fields

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int priority;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"patient", "hibernateLazyInitializer", "handler"})
    private List<QueueVisit> visits;

    // Constructors

    public Patient() {
    }

    public Patient(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public Patient(Long id, String name, int priority) {
        this.id = id;
        this.name = name;
        this.priority = priority;
    }

    // Getters

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
    // Setters

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

    // toString

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", priority=" + priority +
                '}';
    }
}