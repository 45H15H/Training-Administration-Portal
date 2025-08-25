
# Training Administration Portal (TAP)

Comprehensive backend service for managing a training administration portal, where instructors publish courses and offer one-on-one training sessions to students.

---

## Key Features

* Student self-service (/api/students/me/**): profile, preferences, bank details, payments, enrollments, bookings
* Instructor self-service (/api/instructors/me/**): profile (registration), skills, qualifications, bank details, time slots, earnings, course creation & management
* Course lifecycle: instructor creates, updates, publishes (students only see published)
* Booking flow: student books an instructor time slot for a published course (auto-enroll + slot availability check)
* Payments: student records payments; instructor records earnings
* Admin (in‑memory) management: create/delete users (base users), toggle authorization (also syncs isVerified for student/instructor), list all users
* Role-based security with Basic/Form login and stateless session policy
* DTO mapping layer isolating entities from API payloads

---

## Roles & Access

| Role | How Created | Granted Authorities | Core Capabilities |
|------|-------------|---------------------|-------------------|
| ADMIN | In-memory (hardcoded) | ROLE_ADMIN | Full /api/admin/** user management |
| STUDENT | POST /api/students/register | ROLE_STUDENT | /api/students/me/**, browse published courses, bookings, payments |
| INSTRUCTOR | POST /api/instructors/register | ROLE_INSTRUCTOR | /api/instructors/me/**, manage courses, slots, earnings |


Authorization flags: `authorization` (User) mirrored to `isVerified` for Student/Instructor when admin toggles.

---

## Security

Spring Security configuration:

* In-memory admin credentials (hardcoded in `SecurityConfig`):
    * username: `admin`
    * password: `admin`
* HTTP Basic + form login enabled
* Path rules (simplified):
    * `/api/students/register`, `/api/instructors/register` – permitAll
    * `/api/admin/**` – ROLE_ADMIN
    * `/api/students/**` – ROLE_STUDENT
    * `/api/instructors/**` – ROLE_INSTRUCTOR

---

## High-Level Domain Model


Entities (simplified):

* User (base) → Student, Instructor (JOINED inheritance)
* StudentPreference, StudentBankDetails, StudentPayment, StudentBooking, StudentCourseEnrollment
* InstructorSkill, InstructorQualification, InstructorBankDetail, InstructorTimeSlot, InstructorEarning, InstructorResume (if present)
* Course (links Instructor, Skill, Level)

Operational flows:

1. Registration (student/instructor) → password hashed.
2. Instructor creates time slots & skills → creates course(s).
3. Student browses published courses → books a slot referencing instructor & course  → enrollment auto-created.
4. Student records payment; Instructor records earning (separate sides of financial record).
5. Admin toggles authorization (verification) using /api/admin endpoints.

---

## Core Endpoints (Representative)

### Admin (`ROLE_ADMIN`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/admin/users | Create base user (admin-managed) |
| GET | /api/admin/users | List all users |
| PATCH | /api/admin/users/{userId}/authorization | Toggle authorization (updates isVerified) |
| DELETE | /api/admin/users/{userId} | Delete user & subtype cascade |

### Student (`ROLE_STUDENT`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/students/register | Register new student |
| GET | /api/students/me | Get own profile |
| PUT | /api/students/me | Update own profile |
| POST | /api/students/me/preferences | Create/update preferences |
| GET | /api/students/me/preferences | Get preferences |
| POST | /api/students/me/bankDetails | Create/update bank details |
| GET | /api/students/me/bankDetails | Fetch bank details |
| DELETE | /api/students/me/bankDetails | Remove bank details |
| POST | /api/students/me/payments | Record payment |
| GET | /api/students/me/payments | List payments |
| GET | /api/students/me/payments/{id} | Get single payment (ownership enforced) |
| GET | /api/students/instructors | List all instructors (student view) |
| (Bookings) | /api/bookings/me (POST) | Create booking (student context) |
| (Enrollments) | /api/enrollments/me | List enrollments / update progress |

### Instructor (`ROLE_INSTRUCTOR`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/instructors/register | Register new instructor |
| (Profile) | /api/instructors/me | (Assumed GET/PUT if implemented) |
| CRUD | /api/instructors/me/skills | Manage skills |
| CRUD | /api/instructors/me/qualification | Manage qualification |
| CRUD | /api/instructors/me/bank-details | Manage bank details |
| CRUD | /api/instructors/me/slots | Manage availability slots |
| CRUD | /api/instructors/me/earnings | Manage earnings records |
| CRUD | /api/courses/instructors/me | Manage owned courses |

### Courses (Mixed)

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| GET | /api/courses/students/me | STUDENT | List published courses |
| GET | /api/courses/{courseId} | STUDENT/INSTRUCTOR | Published or owner access |

### Bookings

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| POST | /api/bookings/me | STUDENT | Book instructor slot (auto enrollment) |

---

## Validation & Business Rules (Selected)

* Password length & basic constraints validated on registration
* Student age ≥ 15 (enforced elsewhere in code if implemented)
* Booking only allowed for published courses
* Slot must belong to the instructor of the course and be unbooked
* Authorization toggle cascades to Student/Instructor `isVerified`
* Payments enforce ownership; earnings tie to instructor

---



### Sample Booking Flow (Student)

```http
# Register student
POST /api/students/register
{ "username":"stud1", "password":"Pass123!", "email":"stud1@example.com", "age":18 }

 # Register instructor
 POST /api/instructors/register
 { "username":"inst1", "password":"Pass123!", "email":"inst1@example.com" }

 # Instructor creates skill, slot, course (simplified in practice multiple calls)
 POST /api/instructors/me/skills ...
 POST /api/instructors/me/slots ...
 POST /api/courses/instructors/me ... (course initially or then published)

 # Student lists published courses
 GET /api/courses/students/me

 # Student books slot
 POST /api/bookings/me
 { "instructorId":"...", "timeSlotId":"...", "courseId":"..." }

```

---

## Tech Stack

* Java 17, Spring Boot
* Spring Data JPA (JOINED inheritance for User hierarchy)
* Spring Security (Basic, form login, role-based access)
* H2/PostgreSQL (configure in `application.properties`)
* Maven build

---

## Future Enhancements

* Replace Basic Auth with JWT / refresh tokens
* Persistent ADMIN role instead of in-memory credentials
* Global exception handler + error contract


---

## Project Structure

```text
Training-Administration-Portal/
├── pom.xml
├── mvnw / mvnw.cmd
├── README.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/tap/
│   │   │       ├── TrainingAdminstratorPortalApplication.java
│   │   │       ├── controllers/
│   │   │       │   └── All controllers
│   │   │       ├── dto/
│   │   │       │   └── All DTOs
│   │   │       ├── entities/
│   │   │       │   └── All entities
│   │   │       ├── mappers/
│   │   │       │   └── All mappers
│   │   │       ├── repositories/
│   │   │       │   └── All repositories
│   │   │       └── services/
│   │   │           └── All services
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/tap/
│               └── controllers/
│                   └── All controllers
│               └── exceptions/
│                   └── All exceptions
│               └── services/
│                   └── All services
