# 🏥 Patient Queue Management System
### Spring Boot + Spring Data JPA + MySQL | No Lombok

A clean, beginner-friendly REST API that manages a **priority-based patient queue**.
Patients with higher priority values are always served before those with lower values.

---

## 📁 Project Structure

```
Patient_Queue_Management_System/
│
├── pom.xml                                         ← Maven dependencies
├── schema.sql                                      ← MySQL tables + sample data
│
└── src/main/
    ├── resources/
    │   └── application.properties                  ← DB connection config
    │
    └── java/com/yash/Patient_Queue_Management_System/
        │
        ├── PatientQueueManagementSystemApplication.java  ← App entry point
        │
        ├── entity/
        │   ├── Patient.java        ← Maps to `patient` table
        │   └── QueueVisit.java     ← Maps to `queue_visit` table
        │
        ├── repository/
        │   ├── PatientRepository.java       ← extends JpaRepository
        │   └── QueueVisitRepository.java    ← extends JpaRepository
        │
        ├── service/
        │   └── QueueService.java   ← PriorityQueue logic lives here
        │
        └── controller/
            └── QueueController.java  ← REST endpoints
```

---

## 🧠 How the PriorityQueue Works

Java's `PriorityQueue` is a **min-heap** by default — lowest number exits first.
We **reverse** the comparator to make it a **max-heap** — highest priority exits first.

```java
// In QueueService.java
private final PriorityQueue<Patient> patientQueue =
    new PriorityQueue<>(Comparator.comparingInt(Patient::getPriority).reversed());
```

### Serving Order Example

| Patient       | Priority | Served Order |
|---------------|----------|-------------|
| Pooja Shah    | 1        | 5th (last)  |
| Mohan Das     | 3        | 4th         |
| Sita Devi     | 5        | 3rd         |
| Arjun Mehta   | 7        | 2nd         |
| Ravi Kumar    | **10**   | **1st ✅**  |

`poll()` always removes and returns the patient at the top of the max-heap.

---

## 🗄️ Database Design

```
patient
────────────────────────────────
id          BIGINT  PK AUTO_INC
name        VARCHAR NOT NULL
priority    INT     NOT NULL

queue_visit
────────────────────────────────
id          BIGINT   PK AUTO_INC
patient_id  BIGINT   FK → patient.id
status      VARCHAR  (WAITING | SERVED | CANCELLED)
visit_time  DATETIME NOT NULL
```

**Relationship:** One `Patient` → Many `QueueVisit` records

---

## ▶️ How to Run

### Step 1 — Set up MySQL
```sql
CREATE DATABASE patient_queue_db;
```
Then run `schema.sql` in your MySQL client to create tables and load sample data.

### Step 2 — Configure DB in `application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/patient_queue_db
spring.datasource.username=root
spring.datasource.password=your_password
```

### Step 3 — Start the Application
```bash
mvn spring-boot:run
```
App runs at: `http://localhost:8080`

---

## 📡 API Endpoints

### 1. Add a Patient
```
POST /api/queue/add
Content-Type: application/json

{
  "name": "Ravi Kumar",
  "priority": 10
}
```
```json
200 OK
{
  "id": 1,
  "name": "Ravi Kumar",
  "priority": 10
}
```

---

### 2. Serve Next Patient (highest priority)
```
GET /api/queue/serve
```
```json
200 OK — patient returned and removed from queue
{
  "id": 1,
  "name": "Ravi Kumar",
  "priority": 10
}

204 No Content — queue is empty
```

---

### 3. Get All Patients
```
GET /api/queue/patients
```
```json
200 OK
[
  { "id": 1, "name": "Ravi Kumar",  "priority": 10 },
  { "id": 2, "name": "Arjun Mehta", "priority": 7  },
  { "id": 3, "name": "Sita Devi",   "priority": 5  }
]
```

---

### 4. Get Visit History for a Patient
```
GET /api/queue/visits/1
```
```json
200 OK
[
  { "id": 1, "status": "WAITING", "visitTime": "2024-06-01T09:00:00" },
  { "id": 6, "status": "SERVED",  "visitTime": "2024-06-01T09:20:00" }
]
```

---

### 5. Get Current Queue Size
```
GET /api/queue/size
```
```json
200 OK
{
  "queueSize": 3
}
```

---

## 🔁 Visit Status Values

| Status      | When Set                                      |
|-------------|-----------------------------------------------|
| `WAITING`   | When a patient is added via `/add`            |
| `SERVED`    | When a patient is removed via `/serve`        |
| `CANCELLED` | Can be set manually if visit is cancelled     |

---

## ⚙️ Tech Stack

| Layer      | Technology                  |
|------------|-----------------------------|
| Framework  | Spring Boot 3.2             |
| Language   | Java 17                     |
| Database   | MySQL 8                     |
| ORM        | Spring Data JPA / Hibernate |
| Build Tool | Maven                       |
| Lombok     | ❌ Not used                  |

---

## 📝 Notes for Beginners

- **No Lombok** — all getters, setters, and constructors are written manually in entity classes.
- **PriorityQueue is in-memory** — it resets when the app restarts. Database records persist.
- **No DTOs** — request/response uses entity classes directly (keeps it simple).
- **No authentication** — this is a bare REST API focused on queue logic.
