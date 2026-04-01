# 🏥 Patient Queue Management System

A Spring Boot REST API for managing a **priority-based patient queue** in a clinic or hospital setting.
Patients with higher priority values are served before those with lower priority.

---

## 📁 Project Structure

```
Patient_Queue_Management_System/
├── pom.xml
├── schema.sql                          ← MySQL setup + sample data
└── src/main/
    ├── resources/
    │   └── application.properties      ← DB config
    └── java/com/yash/Patient_Queue_Management_System/
        ├── PatientQueueManagementSystemApplication.java
        ├── controller/
        │   └── QueueController.java    ← REST endpoints
        ├── service/
        │   └── QueueService.java       ← Business logic + PriorityQueue
        ├── entity/
        │   ├── Patient.java            ← Patient table mapping
        │   └── QueueVisit.java         ← Visit tracking table
        └── repository/
            ├── PatientRepository.java
            └── QueueVisitRepository.java
```

---

## ⚙️ How PriorityQueue Works

Java's `PriorityQueue` is a **min-heap** by default — smallest number polled first.

We flip this using:
```java
new PriorityQueue<>(Comparator.comparingInt(Patient::getPriority).reversed());
```

This makes it a **max-heap**, so the **highest priority** patient is always served first.

### Example:
| Patient     | Priority | Order Served |
|-------------|----------|--------------|
| Pooja Shah  | 1        | 4th          |
| Mohan Das   | 3        | 3rd          |
| Sita Devi   | 5        | 2nd          |
| Arjun Mehta | 7        | (skipped)    |
| Ravi Kumar  | 10       | **1st** ✅   |

---

## 🚀 Setup & Run

### 1. Create MySQL Database
```sql
CREATE DATABASE patient_queue_db;
```
Then run `schema.sql` to create tables and insert sample data.

### 2. Configure application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/patient_queue_db
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3. Run the App
```bash
mvn spring-boot:run
```
Server starts at: `http://localhost:8080`

---

## 📡 API Endpoints

### ➕ Add a Patient
**POST** `/api/queue/add`

```json
Request Body:
{
  "name": "Ravi Kumar",
  "priority": 10
}
```
```json
Response (200 OK):
{
  "id": 1,
  "name": "Ravi Kumar",
  "priority": 10
}
```

---

### 🔔 Serve Next Patient
**GET** `/api/queue/serve`

```json
Response (200 OK) — Returns highest-priority patient:
{
  "id": 1,
  "name": "Ravi Kumar",
  "priority": 10
}
```
```
Response (204 No Content) — If queue is empty
```

---

### 👥 Get All Patients
**GET** `/api/queue/patients`

```json
Response (200 OK):
[
  { "id": 1, "name": "Ravi Kumar",  "priority": 10 },
  { "id": 2, "name": "Sita Devi",   "priority": 5  },
  { "id": 3, "name": "Arjun Mehta", "priority": 7  }
]
```

---

### 📋 Get Visit History for a Patient
**GET** `/api/queue/visits/{patientId}`

Example: `GET /api/queue/visits/1`

```json
Response (200 OK):
[
  {
    "id": 1,
    "status": "WAITING",
    "visitTime": "2024-06-01T09:00:00"
  },
  {
    "id": 6,
    "status": "SERVED",
    "visitTime": "2024-06-01T09:45:00"
  }
]
```

---

### 🔢 Get Queue Size
**GET** `/api/queue/size`

```json
Response (200 OK):
{
  "queueSize": 3
}
```

---

## 🗄️ Entity Relationships

```
patient (id, name, priority)
    │
    └──< queue_visit (id, patient_id FK, status, visit_time)
```

- One `Patient` → Many `QueueVisit` records
- `queue_visit.patient_id` is a foreign key referencing `patient.id`

---

## 📝 Visit Status Values

| Status      | Meaning                              |
|-------------|--------------------------------------|
| `WAITING`   | Patient added to queue, not yet seen |
| `SERVED`    | Patient was called and served        |
| `CANCELLED` | Visit was cancelled (manual update)  |

---

## 🛠️ Tech Stack

| Layer      | Technology              |
|------------|-------------------------|
| Framework  | Spring Boot 3.2         |
| Language   | Java 17                 |
| Database   | MySQL 8                 |
| ORM        | Spring Data JPA / Hibernate |
| Build Tool | Maven                   |
