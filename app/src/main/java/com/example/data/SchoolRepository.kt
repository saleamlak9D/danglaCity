package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.security.MessageDigest

class SchoolRepository(private val schoolDao: SchoolDao) {

    // Helper to hash password
    fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    // Seed Data Routine
    suspend fun seedDataIfEmpty() {
        val users = schoolDao.getAllUsersFlow().firstOrNull()
        if (users.isNullOrEmpty()) {
            // Seed Users
            val adminId = schoolDao.insertUser(
                User(name = "Mrs. Saleamlak Dege", email = "admin@school.com", passwordHash = hashPassword("admin123"), role = "admin")
            ).toInt()

            val teacher1Id = schoolDao.insertUser(
                User(name = "Abebe Kebede", email = "abebe@school.com", passwordHash = hashPassword("teacher123"), role = "teacher")
            ).toInt()

            val teacher2Id = schoolDao.insertUser(
                User(name = "Marta Hailu", email = "marta@school.com", passwordHash = hashPassword("teacher123"), role = "teacher")
            ).toInt()

            val student1UserId = schoolDao.insertUser(
                User(name = "Daniel Abebe", email = "daniel@school.com", passwordHash = hashPassword("student123"), role = "student")
            ).toInt()

            val student2UserId = schoolDao.insertUser(
                User(name = "Aster Marta", email = "aster@school.com", passwordHash = hashPassword("student123"), role = "student")
            ).toInt()

            val parent1UserId = schoolDao.insertUser(
                User(name = "Kebede Chala", email = "kebede@school.com", passwordHash = hashPassword("parent123"), role = "parent")
            ).toInt()

            val parent2UserId = schoolDao.insertUser(
                User(name = "Hailu Bekele", email = "hailu@school.com", passwordHash = hashPassword("parent123"), role = "parent")
            ).toInt()

            // Seed Teachers
            val t1Id = schoolDao.insertTeacher(
                Teacher(
                    userId = teacher1Id,
                    teacherId = "TCH-2026-001",
                    firstName = "Abebe",
                    lastName = "Kebede",
                    gender = "Male",
                    phone = "+251911234567",
                    address = "Dangla, Kebele 02",
                    qualification = "M.Sc. in Mathematics",
                    specialization = "Algebra & Calculus",
                    hireDate = "2020-09-11"
                )
            ).toInt()

            val t2Id = schoolDao.insertTeacher(
                Teacher(
                    userId = teacher2Id,
                    teacherId = "TCH-2026-002",
                    firstName = "Marta",
                    lastName = "Hailu",
                    gender = "Female",
                    phone = "+251911987654",
                    address = "Dangla, Kebele 03",
                    qualification = "B.Ed. in English",
                    specialization = "English Literature",
                    hireDate = "2021-10-05"
                )
            ).toInt()

            // Seed Parents
            val p1Id = schoolDao.insertParent(
                ParentEntity(
                    userId = parent1UserId,
                    parentId = "PRN-2026-001",
                    firstName = "Kebede",
                    lastName = "Chala",
                    relationship = "Father",
                    phone = "+251912445566",
                    address = "Dangla, Kebele 01"
                )
            ).toInt()

            val p2Id = schoolDao.insertParent(
                ParentEntity(
                    userId = parent2UserId,
                    parentId = "PRN-2026-002",
                    firstName = "Hailu",
                    lastName = "Bekele",
                    relationship = "Father",
                    phone = "+251912778899",
                    address = "Dangla, Kebele 04"
                )
            ).toInt()

            // Seed Classes & Sections
            val c1Id = schoolDao.insertClass(
                ClassEntity(name = "Grade 9 - A", description = "Freshman Secondary Education Class A", classTeacherId = t1Id)
            ).toInt()

            val c2Id = schoolDao.insertClass(
                ClassEntity(name = "Grade 10 - A", description = "Sophomore Secondary Education Class A", classTeacherId = t2Id)
            ).toInt()

            schoolDao.insertSection(Section(classId = c1Id, name = "Section A"))
            schoolDao.insertSection(Section(classId = c2Id, name = "Section A"))

            // Seed Students
            val std1Id = schoolDao.insertStudent(
                Student(
                    userId = student1UserId,
                    studentId = "STD-2026-001",
                    firstName = "Daniel",
                    middleName = "Abebe",
                    lastName = "Kebede",
                    gender = "Male",
                    dateOfBirth = "2011-04-12",
                    phone = "+251913112233",
                    address = "Dangla, Kebele 02",
                    classId = c1Id,
                    sectionId = 1,
                    parentId = p1Id,
                    admissionDate = "2025-09-01"
                )
            ).toInt()

            val std2Id = schoolDao.insertStudent(
                Student(
                    userId = student2UserId,
                    studentId = "STD-2026-002",
                    firstName = "Aster",
                    middleName = "Marta",
                    lastName = "Hailu",
                    gender = "Female",
                    dateOfBirth = "2010-08-25",
                    phone = "+251913445566",
                    address = "Dangla, Kebele 03",
                    classId = c1Id,
                    sectionId = 1,
                    parentId = p2Id,
                    admissionDate = "2025-09-01"
                )
            ).toInt()

            // Seed Subjects
            val sub1Id = schoolDao.insertSubject(
                Subject(name = "Mathematics", code = "MATH-9", description = "Secondary Algebra and Geometry")
            ).toInt()

            val sub2Id = schoolDao.insertSubject(
                Subject(name = "English Language", code = "ENGL-9", description = "Grammar and Literature")
            ).toInt()

            val sub3Id = schoolDao.insertSubject(
                Subject(name = "Biology", code = "BIOL-9", description = "General Life Sciences")
            ).toInt()

            // Teacher Subjects
            schoolDao.insertTeacherSubject(TeacherSubject(teacherId = t1Id, subjectId = sub1Id, classId = c1Id))
            schoolDao.insertTeacherSubject(TeacherSubject(teacherId = t2Id, subjectId = sub2Id, classId = c1Id))

            // Attendance
            schoolDao.insertAttendance(Attendance(studentId = std1Id, classId = c1Id, date = "2026-07-15", status = "Present", remarks = "On time"))
            schoolDao.insertAttendance(Attendance(studentId = std1Id, classId = c1Id, date = "2026-07-16", status = "Present", remarks = "On time"))
            schoolDao.insertAttendance(Attendance(studentId = std1Id, classId = c1Id, date = "2026-07-17", status = "Late", remarks = "10 mins late"))
            schoolDao.insertAttendance(Attendance(studentId = std2Id, classId = c1Id, date = "2026-07-15", status = "Present", remarks = "On time"))
            schoolDao.insertAttendance(Attendance(studentId = std2Id, classId = c1Id, date = "2026-07-16", status = "Absent", remarks = "Family trip"))

            // Exams
            val exam1Id = schoolDao.insertExam(
                Exam(name = "First Term Midterm", academicYear = "2026", term = "Term 1", startDate = "2026-04-15", endDate = "2026-04-20", status = "Completed")
            ).toInt()

            val exam2Id = schoolDao.insertExam(
                Exam(name = "First Term Final Exam", academicYear = "2026", term = "Term 1", startDate = "2026-06-10", endDate = "2026-06-18", status = "Scheduled")
            ).toInt()

            // Exam Results
            schoolDao.insertExamResult(ExamResult(examId = exam1Id, studentId = std1Id, subjectId = sub1Id, marks = 92.5, grade = "A+", remarks = "Excellent!"))
            schoolDao.insertExamResult(ExamResult(examId = exam1Id, studentId = std1Id, subjectId = sub2Id, marks = 84.0, grade = "A", remarks = "Great job"))
            schoolDao.insertExamResult(ExamResult(examId = exam1Id, studentId = std2Id, subjectId = sub1Id, marks = 76.0, grade = "B", remarks = "Improvement needed"))
            schoolDao.insertExamResult(ExamResult(examId = exam1Id, studentId = std2Id, subjectId = sub2Id, marks = 95.0, grade = "A+", remarks = "Brilliant"))

            // Assignments
            val assign1Id = schoolDao.insertAssignment(
                Assignment(
                    teacherId = t1Id,
                    classId = c1Id,
                    subjectId = sub1Id,
                    title = "Quadratic Equations",
                    description = "Complete exercises 4.1 to 4.4 from page 82. Write steps clearly.",
                    dueDate = "2026-07-25"
                )
            ).toInt()

            val assign2Id = schoolDao.insertAssignment(
                Assignment(
                    teacherId = t2Id,
                    classId = c1Id,
                    subjectId = sub2Id,
                    title = "Poetry Review",
                    description = "Write a 500-word analysis on 'The Road Not Taken' by Robert Frost.",
                    dueDate = "2026-07-28"
                )
            ).toInt()

            // Assignment Submissions
            schoolDao.insertSubmission(
                AssignmentSubmission(
                    assignmentId = assign1Id,
                    studentId = std1Id,
                    file = "daniel_hw1.pdf",
                    submissionDate = "2026-07-22",
                    marks = 95.0,
                    feedback = "Excellent work on quadratic solutions!",
                    status = "Graded"
                )
            )

            // Fees
            val fee1Id = schoolDao.insertFee(Fee(studentId = std1Id, feeType = "Tuition Fee", amount = 1500.0, dueDate = "2026-08-01", status = "Unpaid")).toInt()
            val fee2Id = schoolDao.insertFee(Fee(studentId = std1Id, feeType = "Transport Fee", amount = 300.0, dueDate = "2026-08-01", status = "Paid")).toInt()
            val fee3Id = schoolDao.insertFee(Fee(studentId = std2Id, feeType = "Tuition Fee", amount = 1500.0, dueDate = "2026-08-01", status = "Unpaid")).toInt()

            // Payments
            schoolDao.insertPayment(Payment(studentId = std1Id, feeId = fee2Id, amount = 300.0, paymentMethod = "Bank Transfer", paymentDate = "2026-07-10", receiptNumber = "REC-45892"))

            // Announcements
            schoolDao.insertAnnouncement(
                Announcement(
                    title = "Welcome to Dangla City School!",
                    content = "We are thrilled to welcome all students, parents, and teachers to our smart School Management System.",
                    targetRole = "All",
                    createdBy = adminId
                )
            )
            schoolDao.insertAnnouncement(
                Announcement(
                    title = "PTA Meeting Scheduled",
                    content = "There will be a Parent-Teacher Association meeting this Saturday at 10:00 AM in the school auditorium.",
                    targetRole = "Parent",
                    createdBy = adminId
                )
            )

            // Events
            schoolDao.insertEvent(
                Event(
                    title = "Annual Science Exhibition",
                    description = "Students showcase their innovative science experiments and projects in the main hall.",
                    eventDate = "2026-08-15",
                    location = "Main Hall",
                    createdBy = adminId
                )
            )

            // Messages
            schoolDao.insertMessage(
                Message(
                    senderId = teacher1Id,
                    receiverId = parent1UserId,
                    subject = "Daniel's Performance",
                    message = "Dear Kebede, Daniel has been performing exceptionally well in Mathematics. Keep up the encouragement!"
                )
            )
        }
    }

    // --- User login & authentication ---
    suspend fun getUserByEmail(email: String): User? = schoolDao.getUserByEmail(email)
    suspend fun getUserById(id: Int): User? = schoolDao.getUserById(id)
    fun getAllUsersFlow(): Flow<List<User>> = schoolDao.getAllUsersFlow()
    suspend fun insertUser(user: User): Long = schoolDao.insertUser(user)
    suspend fun updateUser(user: User) = schoolDao.updateUser(user)
    suspend fun deleteUser(user: User) = schoolDao.deleteUser(user)

    // --- Students ---
    fun getAllStudentsFlow(): Flow<List<Student>> = schoolDao.getAllStudentsFlow()
    suspend fun getStudentByUserId(userId: Int): Student? = schoolDao.getStudentByUserId(userId)
    suspend fun getStudentById(id: Int): Student? = schoolDao.getStudentById(id)
    fun getStudentsByClass(classId: Int): Flow<List<Student>> = schoolDao.getStudentsByClass(classId)
    fun getStudentsByParent(parentId: Int): Flow<List<Student>> = schoolDao.getStudentsByParent(parentId)
    suspend fun insertStudent(student: Student): Long = schoolDao.insertStudent(student)
    suspend fun updateStudent(student: Student) = schoolDao.updateStudent(student)
    suspend fun deleteStudent(student: Student) = schoolDao.deleteStudent(student)

    // --- Teachers ---
    fun getAllTeachersFlow(): Flow<List<Teacher>> = schoolDao.getAllTeachersFlow()
    suspend fun getTeacherByUserId(userId: Int): Teacher? = schoolDao.getTeacherByUserId(userId)
    suspend fun getTeacherById(id: Int): Teacher? = schoolDao.getTeacherById(id)
    suspend fun insertTeacher(teacher: Teacher): Long = schoolDao.insertTeacher(teacher)
    suspend fun updateTeacher(teacher: Teacher) = schoolDao.updateTeacher(teacher)
    suspend fun deleteTeacher(teacher: Teacher) = schoolDao.deleteTeacher(teacher)

    // --- Parents ---
    fun getAllParentsFlow(): Flow<List<ParentEntity>> = schoolDao.getAllParentsFlow()
    suspend fun getParentByUserId(userId: Int): ParentEntity? = schoolDao.getParentByUserId(userId)
    suspend fun getParentById(id: Int): ParentEntity? = schoolDao.getParentById(id)
    suspend fun insertParent(parent: ParentEntity): Long = schoolDao.insertParent(parent)
    suspend fun updateParent(parent: ParentEntity) = schoolDao.updateParent(parent)
    suspend fun deleteParent(parent: ParentEntity) = schoolDao.deleteParent(parent)

    // --- Classes ---
    fun getAllClassesFlow(): Flow<List<ClassEntity>> = schoolDao.getAllClassesFlow()
    suspend fun getClassById(id: Int): ClassEntity? = schoolDao.getClassById(id)
    suspend fun insertClass(classEntity: ClassEntity): Long = schoolDao.insertClass(classEntity)
    suspend fun updateClass(classEntity: ClassEntity) = schoolDao.updateClass(classEntity)
    suspend fun deleteClass(classEntity: ClassEntity) = schoolDao.deleteClass(classEntity)

    // --- Sections ---
    fun getAllSectionsFlow(): Flow<List<Section>> = schoolDao.getAllSectionsFlow()
    fun getSectionsForClass(classId: Int): Flow<List<Section>> = schoolDao.getSectionsForClass(classId)
    suspend fun insertSection(section: Section): Long = schoolDao.insertSection(section)
    suspend fun deleteSection(section: Section) = schoolDao.deleteSection(section)

    // --- Subjects ---
    fun getAllSubjectsFlow(): Flow<List<Subject>> = schoolDao.getAllSubjectsFlow()
    suspend fun getSubjectById(id: Int): Subject? = schoolDao.getSubjectById(id)
    suspend fun insertSubject(subject: Subject): Long = schoolDao.insertSubject(subject)
    suspend fun updateSubject(subject: Subject) = schoolDao.updateSubject(subject)
    suspend fun deleteSubject(subject: Subject) = schoolDao.deleteSubject(subject)

    // --- Teacher Subjects ---
    fun getAllTeacherSubjectsFlow(): Flow<List<TeacherSubject>> = schoolDao.getAllTeacherSubjectsFlow()
    fun getTeacherSubjectsByTeacher(teacherId: Int): Flow<List<TeacherSubject>> = schoolDao.getTeacherSubjectsByTeacher(teacherId)
    suspend fun insertTeacherSubject(ts: TeacherSubject): Long = schoolDao.insertTeacherSubject(ts)
    suspend fun deleteTeacherSubject(ts: TeacherSubject) = schoolDao.deleteTeacherSubject(ts)

    // --- Attendance ---
    fun getAllAttendanceFlow(): Flow<List<Attendance>> = schoolDao.getAllAttendanceFlow()
    fun getAttendanceByClassAndDate(classId: Int, date: String): Flow<List<Attendance>> = schoolDao.getAttendanceByClassAndDate(classId, date)
    fun getStudentAttendance(studentId: Int): Flow<List<Attendance>> = schoolDao.getStudentAttendance(studentId)
    suspend fun insertAttendance(attendance: Attendance): Long = schoolDao.insertAttendance(attendance)
    suspend fun insertAttendanceList(list: List<Attendance>) = schoolDao.insertAttendanceList(list)

    // --- Exams ---
    fun getAllExamsFlow(): Flow<List<Exam>> = schoolDao.getAllExamsFlow()
    suspend fun getExamById(id: Int): Exam? = schoolDao.getExamById(id)
    suspend fun insertExam(exam: Exam): Long = schoolDao.insertExam(exam)
    suspend fun updateExam(exam: Exam) = schoolDao.updateExam(exam)
    suspend fun deleteExam(exam: Exam) = schoolDao.deleteExam(exam)

    // --- Exam Results ---
    fun getAllExamResultsFlow(): Flow<List<ExamResult>> = schoolDao.getAllExamResultsFlow()
    fun getExamResultsForStudent(studentId: Int): Flow<List<ExamResult>> = schoolDao.getExamResultsForStudent(studentId)
    fun getExamResultsForClass(examId: Int, subjectId: Int): Flow<List<ExamResult>> = schoolDao.getExamResultsForClass(examId, subjectId)
    suspend fun insertExamResult(result: ExamResult): Long = schoolDao.insertExamResult(result)
    suspend fun updateExamResult(result: ExamResult) = schoolDao.updateExamResult(result)

    // --- Assignments ---
    fun getAllAssignmentsFlow(): Flow<List<Assignment>> = schoolDao.getAllAssignmentsFlow()
    fun getAssignmentsForClass(classId: Int): Flow<List<Assignment>> = schoolDao.getAssignmentsForClass(classId)
    suspend fun insertAssignment(assignment: Assignment): Long = schoolDao.insertAssignment(assignment)
    suspend fun deleteAssignment(assignment: Assignment) = schoolDao.deleteAssignment(assignment)

    // --- Assignment Submissions ---
    fun getAllSubmissionsFlow(): Flow<List<AssignmentSubmission>> = schoolDao.getAllSubmissionsFlow()
    fun getSubmissionsForAssignment(assignmentId: Int): Flow<List<AssignmentSubmission>> = schoolDao.getSubmissionsForAssignment(assignmentId)
    fun getStudentSubmissions(studentId: Int): Flow<List<AssignmentSubmission>> = schoolDao.getStudentSubmissions(studentId)
    suspend fun insertSubmission(submission: AssignmentSubmission): Long = schoolDao.insertSubmission(submission)
    suspend fun updateSubmission(submission: AssignmentSubmission) = schoolDao.updateSubmission(submission)

    // --- Fees ---
    fun getAllFeesFlow(): Flow<List<Fee>> = schoolDao.getAllFeesFlow()
    fun getStudentFees(studentId: Int): Flow<List<Fee>> = schoolDao.getStudentFees(studentId)
    suspend fun getFeeById(id: Int): Fee? = schoolDao.getFeeById(id)
    suspend fun insertFee(fee: Fee): Long = schoolDao.insertFee(fee)
    suspend fun updateFee(fee: Fee) = schoolDao.updateFee(fee)

    // --- Payments ---
    fun getAllPaymentsFlow(): Flow<List<Payment>> = schoolDao.getAllPaymentsFlow()
    fun getStudentPayments(studentId: Int): Flow<List<Payment>> = schoolDao.getStudentPayments(studentId)
    suspend fun insertPayment(payment: Payment): Long = schoolDao.insertPayment(payment)

    // --- Announcements ---
    fun getAllAnnouncementsFlow(): Flow<List<Announcement>> = schoolDao.getAllAnnouncementsFlow()
    fun getAnnouncementsForUser(roles: String, classId: Int?): Flow<List<Announcement>> = schoolDao.getAnnouncementsForUser(roles, classId)
    suspend fun insertAnnouncement(announcement: Announcement): Long = schoolDao.insertAnnouncement(announcement)

    // --- Events ---
    fun getAllEventsFlow(): Flow<List<Event>> = schoolDao.getAllEventsFlow()
    suspend fun insertEvent(event: Event): Long = schoolDao.insertEvent(event)

    // --- Messages ---
    fun getAllMessagesFlow(): Flow<List<Message>> = schoolDao.getAllMessagesFlow()
    fun getMessagesForUser(userId: Int): Flow<List<Message>> = schoolDao.getMessagesForUser(userId)
    suspend fun insertMessage(message: Message): Long = schoolDao.insertMessage(message)

    // --- Notifications ---
    fun getNotificationsForUser(userId: Int): Flow<List<Notification>> = schoolDao.getNotificationsForUser(userId)
    suspend fun insertNotification(notification: Notification): Long = schoolDao.insertNotification(notification)
    suspend fun markNotificationsRead(userId: Int) = schoolDao.markNotificationsRead(userId)
}
