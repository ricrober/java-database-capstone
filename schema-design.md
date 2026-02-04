## MySQL Database Design
### Table: patients
- id: INT, Primary Key, Auto Increment
- name: VARCHAR(100), Not Null
### Table: doctors
- id: INT, Primary Key, Auto Increment
- name: VARCHAR(100), Not Null
- ### Table: clinic_locations
- id: INT, Primary Key, Auto Increment
- address: VARCHAR(100), Not Null
### Table: appointments
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id)
- patient_id: INT, Foreign Key → patients(id)
- appointment_time: DATETIME, Not Null
- status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled)

## MongoDB Collection Design
### Collection: prescriptions
```json
{
  "_id": "ObjectId('')",
  "patientName": "",
  "appointmentId": 0,
  "medication": "",
  "dosage": "",
  "doctorNotes": "",
  "refillCount": 0,
  "pharmacy": {
    "name": "",
    "location": ""
  }
}
```
### Collection: reviews
```json
{
  "_id": "ObjectId('')",
  "patientName": "",
  "review": "",
  "rating": 0
}
```
### Collection: logs
```json
{
  "_id": "ObjectId('')",
  "message": ""
}
```
