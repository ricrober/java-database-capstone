**Title:**
_As an Admin, I want to log into the portal with my username and password, so that I can manage the platform securely._

**Acceptance Criteria:**
1. System validates credentials against the encrypted database.
2. Users are redirected to the Admin Dashboard upon successful login.
3. System displays a clear error message for incorrect credentials and locks the account after 5 failed attempts.

**Priority:** High
**Story Points:** 3
**Notes:**
- Must use HTTPS for data transmission.
- Session tokens should expire after 30 minutes of inactivity.

**Title:**
_As an Admin, I want to log into the portal with my username and password, so that I can manage the platform securely._

**Acceptance Criteria:**
1. System validates credentials against the encrypted database.
2. Users are redirected to the Admin Dashboard upon successful login.
3. System displays a clear error message for incorrect credentials and locks the account after 5 failed attempts.

**Priority:** High
**Story Points:** 3
**Notes:**

**Title:**
_As an Admin, I want to add new doctors to the portal, so that they can begin managing their patient schedules._

**Acceptance Criteria:**
1. Admin can input doctor details (Name, Specialization, Email, License Number).
2. System prevents duplicate entries based on License Number or Email.
3. An automated "Welcome" email is sent to the doctor with temporary login credentials.

**Priority:** Medium
**Story Points:** 5
**Notes:**
- Specialization should be a selectable dropdown to maintain data integrity.


- Must use HTTPS for data transmission.
- Session tokens should expire after 30 minutes of inactivity.**Title:**
_As an Admin, I want to delete a doctor's profile from the portal, so that inactive or former staff no longer have system access._

**Acceptance Criteria:**
1. Admin can search for and select a doctor profile for deletion.
2. System triggers a "Confirm Deletion" warning to prevent accidental data loss.
3. All associated future appointments for that doctor are flagged for rescheduling.

**Priority:** Medium
**Story Points:** 3
**Notes:**
- Consider implementing a "Soft Delete" (deactivation) instead of permanent removal to preserve historical medical records.

**Title:**
_As an Admin, I want to run a stored procedure to get the number of appointments per month, so that I can track usage statistics and resource needs._

**Acceptance Criteria:**
1. Admin can trigger the report via a secure command or dashboard button.
2. The output displays a month-by-month breakdown of total appointments.
3. Data is pulled accurately using the Count function within the MySQL environment.

**Priority:** Low
**Story Points:** 5
**Notes:**
- The SQL query should be optimized to handle large datasets without locking the Appointments table.
- Future iteration: Visualize this data in a bar chart on the Admin Dashboard.

**Title:**
_As a Patient, I want to view a list of doctors without logging in, so that I can explore my options before registering._

**Acceptance Criteria:**
1. Users can access a public "Find a Doctor" page without an account.
2. The list displays the doctor’s name, specialization, and professional bio.
3. Users can filter the list by medical specialty.

**Priority:** Medium
**Story Points:** 3
**Notes:**
- Contact details and booking buttons should remain hidden until the user logs in.

**Title:**
_As a Patient, I want to sign up using my email and password, so that I can book appointments and access the portal._

**Acceptance Criteria:**
1. System collects name, email, and a password that meets security complexity requirements.
2. System checks for existing accounts to prevent duplicate email registrations.
3. A verification email is sent to the user to activate the account.

**Priority:** High
**Story Points:** 5
**Notes:**
- Passwords must be hashed using a secure algorithm (e.g., BCrypt) before storage.

**Title:**
_As a Patient, I want to log into the portal, so that I can securely manage my bookings and personal health info._

**Acceptance Criteria:**
1. Patient can authenticate using their registered email and password.
2. "Forgot Password" functionality is available on the login page.
3. Successful login redirects the user to the "My Appointments" dashboard.

**Priority:** High
**Story Points:** 3
**Notes:**
- Implement "Remember Me" functionality using secure cookies if desired.

**Title:**
_As a Patient, I want to log out of the portal, so that I can ensure my account remains secure on shared devices._

**Acceptance Criteria:**
1. Logout option is easily accessible from the user profile menu.
2. The system terminates the session immediately upon clicking.
3. The user is redirected to the home page or login screen.

**Priority:** High
**Story Points:** 1
**Notes:**
- Crucial for HIPAA/GDPR compliance regarding sensitive health data.

**Title:**
_As a Patient, I want to book an hour-long appointment, so that I can consult with a doctor of my choice._

**Acceptance Criteria:**
1. Patient can select a doctor and view available 60-minute time slots.
2. System prevents double-booking the same time slot for a doctor.
3. Patient receives a confirmation message and email once the booking is successful.

**Priority:** High
**Story Points:** 8
**Notes:**
- Logic must account for the doctor's specific working hours and existing appointments.

**Title:**
_As a Patient, I want to view my upcoming appointments, so that I can prepare accordingly and remember my visit times._

**Acceptance Criteria:**
1. A "My Appointments" section displays a list of all future bookings.
2. Each entry shows the doctor’s name, date, time, and location (or virtual link).
3. The list is sorted chronologically, with the soonest appointment first.

**Priority:** Medium
**Story Points:** 3
**Notes:**
- Include a "Cancel Appointment" button next to each entry (subject to a 24-hour notice policy).

**Title:**
_As a Doctor, I want to log into the portal, so that I can manage my appointments and patient interactions._

**Acceptance Criteria:**
1. System authenticates the doctor using their unique professional email and password.
2. Upon login, the system identifies the user role and redirects them to the Doctor Dashboard.
3. System provides a secure "Forgot Password" flow specifically for staff.

**Priority:** High
**Story Points:** 3
**Notes:**
- Login attempts should be logged for security auditing.

**Title:**
_As a Doctor, I want to log out of the portal, so that I can protect my data and patient confidentiality._

**Acceptance Criteria:**
1. Logout button is accessible from the sidebar/header on all pages.
2. System clears the active session and prevents access to patient records via the "back" button.
3. Redirects the user to the main landing page or staff login portal.

**Priority:** High
**Story Points:** 1
**Notes:**
- Critical for maintaining compliance with health data protection regulations.

**Title:**
_As a Doctor, I want to view my appointment calendar, so that I can stay organized and manage my daily workflow._

**Acceptance Criteria:**
1. Calendar displays daily, weekly, and monthly views of scheduled appointments.
2. Clicking an appointment block displays a summary of the patient and the visit time.
3. The calendar distinguishes between different types of appointments (e.g., initial consult vs. follow-up).

**Priority:** High
**Story Points:** 5
**Notes:**
- Interface should be optimized for both desktop and tablet use.

**Title:**
_As a Doctor, I want to mark my unavailability, so that patients are only shown available slots for booking._

**Acceptance Criteria:**
1. Doctor can select specific dates or time ranges to mark as "Blocked" or "Out of Office."
2. The booking system automatically hides these slots from the patient-facing view.
3. Existing appointments cannot be blocked without first being rescheduled or canceled.

**Priority:** Medium
**Story Points:** 5
**Notes:**
- Should allow for recurring unavailability (e.g., "Every Wednesday from 2 PM - 4 PM").

**Title:**
_As a Doctor, I want update my profile with specialization and contact information, so that patients have accurate and up-to-date information._

**Acceptance Criteria:**
1. Doctor can edit their biography, specialization, and professional contact details.
2. Profile updates are reflected in real-time on the public "Find a Doctor" list.
3. A profile picture upload feature is available and functional.

**Priority:** Medium
**Story Points:** 3
**Notes:**
- Certain fields (like License Number) may require Admin approval before updates are finalized.

**Title:**
_As a Doctor, I want view patient details for upcoming appointments, so that I can be prepared with their medical history and context._

**Acceptance Criteria:**
1. Doctor can click on an upcoming appointment to view the patient’s basic profile.
2. System displays the "Reason for Visit" provided by the patient during booking.
3. Access is restricted so doctors can only see details for patients scheduled with them.

**Priority:** High
**Story Points:** 5
**Notes:**
- Ensure data access complies with the principle of least privilege.



