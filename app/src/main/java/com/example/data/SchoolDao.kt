package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SchoolDao {

    // --- Users ---
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): User?

    @Query("SELECT * FROM users ORDER BY name ASC")
    fun getAllUsersFlow(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)


    // --- Students ---
    @Query("SELECT * FROM students ORDER BY lastName, firstName ASC")
    fun getAllStudentsFlow(): Flow<List<Student>>

    @Query("SELECT * FROM students WHERE userId = :userId LIMIT 1")
    suspend fun getStudentByUserId(userId: Int): Student?

    @Query("SELECT * FROM students WHERE id = :id LIMIT 1")
    suspend fun getStudentById(id: Int): Student?

    @Query("SELECT * FROM students WHERE classId = :classId")
    fun getStudentsByClass(classId: Int): Flow<List<Student>>

    @Query("SELECT * FROM students WHERE parentId = :parentId")
    fun getStudentsByParent(parentId: Int): Flow<List<Student>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student): Long

    @Update
    suspend fun updateStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)


    // --- Teachers ---
    @Query("SELECT * FROM teachers ORDER BY lastName, firstName ASC")
    fun getAllTeachersFlow(): Flow<List<Teacher>>

    @Query("SELECT * FROM teachers WHERE userId = :userId LIMIT 1")
    suspend fun getTeacherByUserId(userId: Int): Teacher?

    @Query("SELECT * FROM teachers WHERE id = :id LIMIT 1")
    suspend fun getTeacherById(id: Int): Teacher?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacher(teacher: Teacher): Long

    @Update
    suspend fun updateTeacher(teacher: Teacher)

    @Delete
    suspend fun deleteTeacher(teacher: Teacher)


    // --- Parents ---
    @Query("SELECT * FROM parents ORDER BY lastName, firstName ASC")
    fun getAllParentsFlow(): Flow<List<ParentEntity>>

    @Query("SELECT * FROM parents WHERE userId = :userId LIMIT 1")
    suspend fun getParentByUserId(userId: Int): ParentEntity?

    @Query("SELECT * FROM parents WHERE id = :id LIMIT 1")
    suspend fun getParentById(id: Int): ParentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParent(parent: ParentEntity): Long

    @Update
    suspend fun updateParent(parent: ParentEntity)

    @Delete
    suspend fun deleteParent(parent: ParentEntity)


    // --- Classes ---
    @Query("SELECT * FROM classes ORDER BY name ASC")
    fun getAllClassesFlow(): Flow<List<ClassEntity>>

    @Query("SELECT * FROM classes WHERE id = :id")
    suspend fun getClassById(id: Int): ClassEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClass(classEntity: ClassEntity): Long

    @Update
    suspend fun updateClass(classEntity: ClassEntity)

    @Delete
    suspend fun deleteClass(classEntity: ClassEntity)


    // --- Sections ---
    @Query("SELECT * FROM sections ORDER BY name ASC")
    fun getAllSectionsFlow(): Flow<List<Section>>

    @Query("SELECT * FROM sections WHERE classId = :classId")
    fun getSectionsForClass(classId: Int): Flow<List<Section>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSection(section: Section): Long

    @Delete
    suspend fun deleteSection(section: Section)


    // --- Subjects ---
    @Query("SELECT * FROM subjects ORDER BY name ASC")
    fun getAllSubjectsFlow(): Flow<List<Subject>>

    @Query("SELECT * FROM subjects WHERE id = :id")
    suspend fun getSubjectById(id: Int): Subject?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: Subject): Long

    @Update
    suspend fun updateSubject(subject: Subject)

    @Delete
    suspend fun deleteSubject(subject: Subject)


    // --- Teacher Subjects ---
    @Query("SELECT * FROM teacher_subjects")
    fun getAllTeacherSubjectsFlow(): Flow<List<TeacherSubject>>

    @Query("SELECT * FROM teacher_subjects WHERE teacherId = :teacherId")
    fun getTeacherSubjectsByTeacher(teacherId: Int): Flow<List<TeacherSubject>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacherSubject(ts: TeacherSubject): Long

    @Delete
    suspend fun deleteTeacherSubject(ts: TeacherSubject)


    // --- Attendance ---
    @Query("SELECT * FROM attendance ORDER BY date DESC")
    fun getAllAttendanceFlow(): Flow<List<Attendance>>

    @Query("SELECT * FROM attendance WHERE classId = :classId AND date = :date")
    fun getAttendanceByClassAndDate(classId: Int, date: String): Flow<List<Attendance>>

    @Query("SELECT * FROM attendance WHERE studentId = :studentId ORDER BY date DESC")
    fun getStudentAttendance(studentId: Int): Flow<List<Attendance>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: Attendance): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendanceList(list: List<Attendance>)


    // --- Exams ---
    @Query("SELECT * FROM exams ORDER BY startDate DESC")
    fun getAllExamsFlow(): Flow<List<Exam>>

    @Query("SELECT * FROM exams WHERE id = :id")
    suspend fun getExamById(id: Int): Exam?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExam(exam: Exam): Long

    @Update
    suspend fun updateExam(exam: Exam)

    @Delete
    suspend fun deleteExam(exam: Exam)


    // --- Exam Results ---
    @Query("SELECT * FROM exam_results")
    fun getAllExamResultsFlow(): Flow<List<ExamResult>>

    @Query("SELECT * FROM exam_results WHERE studentId = :studentId")
    fun getExamResultsForStudent(studentId: Int): Flow<List<ExamResult>>

    @Query("SELECT * FROM exam_results WHERE examId = :examId AND subjectId = :subjectId")
    fun getExamResultsForClass(examId: Int, subjectId: Int): Flow<List<ExamResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamResult(result: ExamResult): Long

    @Update
    suspend fun updateExamResult(result: ExamResult)


    // --- Assignments ---
    @Query("SELECT * FROM assignments ORDER BY dueDate ASC")
    fun getAllAssignmentsFlow(): Flow<List<Assignment>>

    @Query("SELECT * FROM assignments WHERE classId = :classId ORDER BY dueDate ASC")
    fun getAssignmentsForClass(classId: Int): Flow<List<Assignment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: Assignment): Long

    @Delete
    suspend fun deleteAssignment(assignment: Assignment)


    // --- Assignment Submissions ---
    @Query("SELECT * FROM assignment_submissions")
    fun getAllSubmissionsFlow(): Flow<List<AssignmentSubmission>>

    @Query("SELECT * FROM assignment_submissions WHERE assignmentId = :assignmentId")
    fun getSubmissionsForAssignment(assignmentId: Int): Flow<List<AssignmentSubmission>>

    @Query("SELECT * FROM assignment_submissions WHERE studentId = :studentId")
    fun getStudentSubmissions(studentId: Int): Flow<List<AssignmentSubmission>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubmission(submission: AssignmentSubmission): Long

    @Update
    suspend fun updateSubmission(submission: AssignmentSubmission)


    // --- Fees ---
    @Query("SELECT * FROM fees ORDER BY dueDate ASC")
    fun getAllFeesFlow(): Flow<List<Fee>>

    @Query("SELECT * FROM fees WHERE studentId = :studentId ORDER BY dueDate ASC")
    fun getStudentFees(studentId: Int): Flow<List<Fee>>

    @Query("SELECT * FROM fees WHERE id = :id")
    suspend fun getFeeById(id: Int): Fee?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFee(fee: Fee): Long

    @Update
    suspend fun updateFee(fee: Fee)


    // --- Payments ---
    @Query("SELECT * FROM payments ORDER BY paymentDate DESC")
    fun getAllPaymentsFlow(): Flow<List<Payment>>

    @Query("SELECT * FROM payments WHERE studentId = :studentId ORDER BY paymentDate DESC")
    fun getStudentPayments(studentId: Int): Flow<List<Payment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: Payment): Long


    // --- Announcements ---
    @Query("SELECT * FROM announcements ORDER BY createdAt DESC")
    fun getAllAnnouncementsFlow(): Flow<List<Announcement>>

    @Query("SELECT * FROM announcements WHERE targetRole IN (:roles, 'All') OR classId = :classId ORDER BY createdAt DESC")
    fun getAnnouncementsForUser(roles: String, classId: Int?): Flow<List<Announcement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: Announcement): Long


    // --- Events ---
    @Query("SELECT * FROM events ORDER BY eventDate ASC")
    fun getAllEventsFlow(): Flow<List<Event>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event): Long


    // --- Messages ---
    @Query("SELECT * FROM messages ORDER BY createdAt DESC")
    fun getAllMessagesFlow(): Flow<List<Message>>

    @Query("SELECT * FROM messages WHERE senderId = :userId OR receiverId = :userId ORDER BY createdAt DESC")
    fun getMessagesForUser(userId: Int): Flow<List<Message>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message): Long


    // --- Notifications ---
    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY createdAt DESC")
    fun getNotificationsForUser(userId: Int): Flow<List<Notification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: Notification): Long

    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    suspend fun markNotificationsRead(userId: Int)
}
