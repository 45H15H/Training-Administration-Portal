# 🧾 API Development Status Report



## 📊 Entity-wise API Status Table with Endpoints

|                   Entity | Endpoint Base Path                              |
|-------------------------:|-------------------------------------------------|
|                     User | `/api/users/{user-id}`                          |
|                  Student | `/api/students{id}`                             |
|        StudentPreference | `/api/students{student-id}/preferences`         |
|        StudentBankDetail | `/api/students{student-id}/bankDetails`         |
|           StudentPayment | `/api/students{student-id}/payments`            |
| StudentCourseEnrollments | `POST: /api/enrollments`, `GET: /api/enrollments/student/{studentId}`, `Udate progress PUT: /api/enrollments/{enrollmentId}/progress`                                             | 
|          StudentBookings | `POST: /api/bookings`                                             |
|               Instructor | `/api/instructors/{id}`                         |
|         InstructorResume | `/api/instrcutors/{instructor-id}/resume`       |
|    InstructorBankDetails | `/api/instructors/{instructor-id}/bank-details` |
|        InstructorEarning | `/api/instructors/{instructor-id}/earnings`     |
|  InstructorQualification | `/api/instructors/{instructorId}/qualification` |
|          InstructorSkill | ` /api/instructors/{instructorId}/skills`       |
|         ProficiencyLevel | `/api/proficiency-levels`                       |
|       InstructorTimeSlot | `/api/instructors/{instructorId}/slots`         | 
|                   Course | `/api/courses/{courseId}`                       | 

## 👥 Team Members: 3

---

### 👤 **Member 1: Ashish**

#### Assigned Entities:
- **Users**
    - [x] GET
    - [x] POST
    - [x] GETALL
    - [x] PUT
    - [ ] DELETE

- **Students**
    - [x] GET
    - [x] POST
    - [x] GETALL
    - [x] PUT
    - [ ] DELETE

- **StudentPreferences**
    - [x] GET
    - [x] POST
    - [ ] GETALL
    - [x] PUT
    - [ ] DELETE

- **Instructors**
    - [x] GET
    - [x] POST
    - [x] GETALL
    - [x] PUT
    - [ ] DELETE

- **InstructorResumes**
    - [x] GET
    - [x] POST
    - [ ] GETALL
    - [x] PUT
    - [ ] DELETE

#### ✅ Status: _

---

### 👤 **Member 2: Barsha**

#### Assigned Entities:
- **StudentPayments**
    - [x] GET
    - [x] POST
    - [ ] GETALL
    - [ ] PUT
    - [ ] DELETE

- **StudentBankDetails**
    - [x] GET
    - [x] POST
    - [ ] GETALL
    - [ ] PUT
    - [x] DELETE

- **StudentCourseEnrollments**
    - [x] GET
    - [x] POST
    - [ ] GETALL
    - [x] PUT
    - [x] DELETE


- **StudentBookings**
    - [ ] GET
    - [ ] POST
    - [ ] GETALL
    - [ ] PUT
    - [ ] DELETE

- **Course**
    - [ ] GET
    - [ ] POST
    - [ ] GETALL
    - [ ] PUT
    - [ ] DELETE

#### ✅ Status: _

---

### 👤 **Member 3: Muskan**

#### Assigned Entities:
- **InstructorQualifications**
    - [x] GET
    - [x] POST
    - [ ] GETALL
    - [x] PUT
    - [x] DELETE

- **InstructorEarnings**
    - [x] GET
    - [x] POST
    - [ ] GETALL
    - [x] PUT
    - [x] DELETE

- **InstructorSkills**
    - [x] GET
    - [x] POST
    - [ ] GETALL
    - [x] PUT
    - [x] DELETE

- **ProficiencyLevel**
    - [x] GET
    - [x] POST
    - [ ] GETALL
    - [x] PUT
    - [x] DELETE

- **InstructorTimeSlots**
    - [x] GET
    - [x] POST
    - [ ] GETALL
    - [x] PUT
    - [x] DELETE

- **InstructorBankDetails**
    - [ ] GET
    - [ ] POST
    - [ ] GETALL
    - [ ] PUT
    - [ ] DELETE



#### ✅ Status: _
