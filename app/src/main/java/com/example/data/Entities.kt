package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val passwordHash: String,
    val role: String, // admin, teacher, student, parent
    val status: String = "Active", // Active, Inactive
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val studentId: String, // Custom registration ID, e.g. "STD-2026-001"
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val gender: String,
    val dateOfBirth: String,
    val phone: String,
    val address: String,
    val classId: Int,
    val sectionId: Int,
    val parentId: Int, // Refers to parents.id
    val admissionDate: String,
    val profilePhoto: String? = null,
    val status: String = "Active",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "teachers")
data class Teacher(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val teacherId: String, // Custom registration ID, e.g. "TCH-2026-001"
    val firstName: String,
    val lastName: String,
    val gender: String,
    val phone: String,
    val address: String,
    val qualification: String,
    val specialization: String,
    val hireDate: String,
    val profilePhoto: String? = null,
    val status: String = "Active",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "parents")
data class ParentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val parentId: String, // Custom registration ID, e.g. "PRN-2026-001"
    val firstName: String,
    val lastName: String,
    val relationship: String, // Father, Mother, Guardian
    val phone: String,
    val address: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "classes")
data class ClassEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val classTeacherId: Int? = null, // Refers to teachers.id
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "sections")
data class Section(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val classId: Int,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "subjects")
data class Subject(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val code: String,
    val description: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "teacher_subjects")
data class TeacherSubject(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val teacherId: Int,
    val subjectId: Int,
    val classId: Int,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "attendance")
data class Attendance(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: Int,
    val classId: Int,
    val date: String, // YYYY-MM-DD
    val status: String, // Present, Absent, Late, Excused
    val remarks: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "exams")
data class Exam(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val academicYear: String,
    val term: String,
    val startDate: String,
    val endDate: String,
    val status: String = "Scheduled", // Scheduled, Ongoing, Completed
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "exam_results")
data class ExamResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val examId: Int,
    val studentId: Int,
    val subjectId: Int,
    val marks: Double,
    val grade: String,
    val remarks: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "assignments")
data class Assignment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val teacherId: Int,
    val classId: Int,
    val subjectId: Int,
    val title: String,
    val description: String,
    val dueDate: String,
    val file: String? = null, // Path or name
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "assignment_submissions")
data class AssignmentSubmission(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val assignmentId: Int,
    val studentId: Int,
    val file: String? = null, // Simulated upload path
    val submissionDate: String,
    val marks: Double? = null,
    val feedback: String? = null,
    val status: String = "Submitted", // Submitted, Graded, Late
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "fees")
data class Fee(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: Int,
    val feeType: String, // Tuition Fee, Registration Fee, Transport Fee, Library Fee, Laboratory Fee
    val amount: Double,
    val dueDate: String,
    val status: String = "Unpaid", // Paid, Unpaid, Partially Paid
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "payments")
data class Payment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: Int,
    val feeId: Int,
    val amount: Double,
    val paymentMethod: String, // Cash, Card, Bank Transfer
    val paymentDate: String,
    val receiptNumber: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "announcements")
data class Announcement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val targetRole: String, // All, Admin, Teacher, Student, Parent
    val classId: Int? = null, // Null means all classes
    val createdBy: Int, // user_id
    val status: String = "Published", // Published, Draft
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val eventDate: String,
    val location: String,
    val createdBy: Int, // user_id
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val senderId: Int, // user_id
    val receiverId: Int, // user_id
    val subject: String,
    val message: String,
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int, // user_id
    val title: String,
    val message: String,
    val type: String, // Info, Alert, Success
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
