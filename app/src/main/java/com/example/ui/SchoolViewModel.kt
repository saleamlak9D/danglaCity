package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.SchoolApplication
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SchoolViewModel(
    application: Application,
    private val repository: SchoolRepository
) : AndroidViewModel(application) {

    // --- Authentication State ---
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _currentTeacher = MutableStateFlow<Teacher?>(null)
    val currentTeacher: StateFlow<Teacher?> = _currentTeacher.asStateFlow()

    private val _currentStudent = MutableStateFlow<Student?>(null)
    val currentStudent: StateFlow<Student?> = _currentStudent.asStateFlow()

    private val _currentParent = MutableStateFlow<ParentEntity?>(null)
    val currentParent: StateFlow<ParentEntity?> = _currentParent.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    // --- Loaded/Observed Flows ---
    val allUsers = repository.getAllUsersFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allStudents = repository.getAllStudentsFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allTeachers = repository.getAllTeachersFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allParents = repository.getAllParentsFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allClasses = repository.getAllClassesFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allSections = repository.getAllSectionsFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allSubjects = repository.getAllSubjectsFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allExams = repository.getAllExamsFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allFees = repository.getAllFeesFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allPayments = repository.getAllPaymentsFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allAnnouncements = repository.getAllAnnouncementsFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allEvents = repository.getAllEventsFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allTeacherSubjects = repository.getAllTeacherSubjectsFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allExamResults = repository.getAllExamResultsFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allAssignments = repository.getAllAssignmentsFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allSubmissions = repository.getAllSubmissionsFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allAttendance = repository.getAllAttendanceFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Dynamic Navigation / Screen Flow ---
    private val _currentScreen = MutableStateFlow<String>("login")
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    // Parent Child-Monitor selection
    private val _selectedChildId = MutableStateFlow<Int?>(null)
    val selectedChildId: StateFlow<Int?> = _selectedChildId.asStateFlow()

    fun navigateTo(screen: String) {
        _currentScreen.value = screen
    }

    // --- Authentication ---
    fun login(email: String, passwordHashOrPlain: String, isPlain: Boolean = true) {
        viewModelScope.launch {
            val hashedPassword = if (isPlain) repository.hashPassword(passwordHashOrPlain) else passwordHashOrPlain
            val user = repository.getUserByEmail(email)
            if (user != null && user.passwordHash == hashedPassword) {
                _loginError.value = null
                _currentUser.value = user
                
                // Fetch associated role profile
                when (user.role) {
                    "teacher" -> {
                        _currentTeacher.value = repository.getTeacherByUserId(user.id)
                        _currentScreen.value = "teacher_dashboard"
                    }
                    "student" -> {
                        _currentStudent.value = repository.getStudentByUserId(user.id)
                        _currentScreen.value = "student_dashboard"
                    }
                    "parent" -> {
                        val parent = repository.getParentByUserId(user.id)
                        _currentParent.value = parent
                        _currentScreen.value = "parent_dashboard"
                        
                        // Select first child by default if available
                        if (parent != null) {
                            val children = repository.getStudentsByParent(parent.id).firstOrNull()
                            if (!children.isNullOrEmpty()) {
                                _selectedChildId.value = children.first().id
                            }
                        }
                    }
                    "admin" -> {
                        _currentScreen.value = "admin_dashboard"
                    }
                }
            } else {
                _loginError.value = "Invalid email or password"
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _currentTeacher.value = null
        _currentStudent.value = null
        _currentParent.value = null
        _selectedChildId.value = null
        _currentScreen.value = "login"
    }

    fun selectChild(studentId: Int) {
        _selectedChildId.value = studentId
    }

    // --- ADMIN CRUD ACTIONS ---

    // 1. Students Management
    fun addStudent(
        firstName: String, middleName: String, lastName: String, gender: String,
        dob: String, phone: String, email: String, address: String,
        classId: Int, sectionId: Int, parentId: Int, admissionDate: String
    ) {
        viewModelScope.launch {
            // Create corresponding user first
            val passHash = repository.hashPassword("student123")
            val userId = repository.insertUser(
                User(name = "$firstName $lastName", email = email, passwordHash = passHash, role = "student")
            ).toInt()

            val customRegId = "STD-${Calendar.getInstance().get(Calendar.YEAR)}-${(100..999).random()}"
            repository.insertStudent(
                Student(
                    userId = userId, studentId = customRegId, firstName = firstName, middleName = middleName,
                    lastName = lastName, gender = gender, dateOfBirth = dob, phone = phone,
                    address = address, classId = classId, sectionId = sectionId, parentId = parentId,
                    admissionDate = admissionDate
                )
            )
        }
    }

    fun updateStudent(student: Student) {
        viewModelScope.launch {
            repository.updateStudent(student)
            // Also update associated user name
            val user = repository.getUserById(student.userId)
            if (user != null) {
                repository.updateUser(user.copy(name = "${student.firstName} ${student.lastName}"))
            }
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            repository.deleteStudent(student)
            val user = repository.getUserById(student.userId)
            if (user != null) {
                repository.deleteUser(user)
            }
        }
    }

    // 2. Teachers Management
    fun addTeacher(
        firstName: String, lastName: String, gender: String, phone: String,
        email: String, address: String, qualification: String, specialization: String, hireDate: String
    ) {
        viewModelScope.launch {
            val passHash = repository.hashPassword("teacher123")
            val userId = repository.insertUser(
                User(name = "$firstName $lastName", email = email, passwordHash = passHash, role = "teacher")
            ).toInt()

            val customRegId = "TCH-${Calendar.getInstance().get(Calendar.YEAR)}-${(100..999).random()}"
            repository.insertTeacher(
                Teacher(
                    userId = userId, teacherId = customRegId, firstName = firstName, lastName = lastName,
                    gender = gender, phone = phone, address = address, qualification = qualification,
                    specialization = specialization, hireDate = hireDate
                )
            )
        }
    }

    fun updateTeacher(teacher: Teacher) {
        viewModelScope.launch {
            repository.updateTeacher(teacher)
            val user = repository.getUserById(teacher.userId)
            if (user != null) {
                repository.updateUser(user.copy(name = "${teacher.firstName} ${teacher.lastName}"))
            }
        }
    }

    fun deleteTeacher(teacher: Teacher) {
        viewModelScope.launch {
            repository.deleteTeacher(teacher)
            val user = repository.getUserById(teacher.userId)
            if (user != null) {
                repository.deleteUser(user)
            }
        }
    }

    // 3. Parents Management
    fun addParent(
        firstName: String, lastName: String, relationship: String, phone: String,
        email: String, address: String
    ) {
        viewModelScope.launch {
            val passHash = repository.hashPassword("parent123")
            val userId = repository.insertUser(
                User(name = "$firstName $lastName", email = email, passwordHash = passHash, role = "parent")
            ).toInt()

            val customRegId = "PRN-${Calendar.getInstance().get(Calendar.YEAR)}-${(100..999).random()}"
            repository.insertParent(
                ParentEntity(
                    userId = userId, parentId = customRegId, firstName = firstName, lastName = lastName,
                    relationship = relationship, phone = phone, address = address
                )
            )
        }
    }

    fun updateParent(parent: ParentEntity) {
        viewModelScope.launch {
            repository.updateParent(parent)
            val user = repository.getUserById(parent.userId)
            if (user != null) {
                repository.updateUser(user.copy(name = "${parent.firstName} ${parent.lastName}"))
            }
        }
    }

    fun deleteParent(parent: ParentEntity) {
        viewModelScope.launch {
            repository.deleteParent(parent)
            val user = repository.getUserById(parent.userId)
            if (user != null) {
                repository.deleteUser(user)
            }
        }
    }

    // 4. Class and Sections Management
    fun addClass(name: String, description: String, teacherId: Int?) {
        viewModelScope.launch {
            val classId = repository.insertClass(ClassEntity(name = name, description = description, classTeacherId = teacherId)).toInt()
            // Add a default Section
            repository.insertSection(Section(classId = classId, name = "Section A"))
        }
    }

    fun updateClass(classEntity: ClassEntity) {
        viewModelScope.launch {
            repository.updateClass(classEntity)
        }
    }

    fun deleteClass(classEntity: ClassEntity) {
        viewModelScope.launch {
            repository.deleteClass(classEntity)
        }
    }

    // 5. Subject Management
    fun addSubject(name: String, code: String, description: String) {
        viewModelScope.launch {
            repository.insertSubject(Subject(name = name, code = code, description = description))
        }
    }

    fun updateSubject(subject: Subject) {
        viewModelScope.launch {
            repository.updateSubject(subject)
        }
    }

    fun deleteSubject(subject: Subject) {
        viewModelScope.launch {
            repository.deleteSubject(subject)
        }
    }

    fun assignTeacherToSubject(teacherId: Int, subjectId: Int, classId: Int) {
        viewModelScope.launch {
            repository.insertTeacherSubject(TeacherSubject(teacherId = teacherId, subjectId = subjectId, classId = classId))
        }
    }

    // 6. Announcements & Events
    fun addAnnouncement(title: String, content: String, targetRole: String, classId: Int?) {
        viewModelScope.launch {
            repository.insertAnnouncement(
                Announcement(
                    title = title,
                    content = content,
                    targetRole = targetRole,
                    classId = classId,
                    createdBy = _currentUser.value?.id ?: 1
                )
            )
        }
    }

    fun addEvent(title: String, description: String, date: String, location: String) {
        viewModelScope.launch {
            repository.insertEvent(
                Event(
                    title = title,
                    description = description,
                    eventDate = date,
                    location = location,
                    createdBy = _currentUser.value?.id ?: 1
                )
            )
        }
    }

    // 7. Fee management
    fun assignFeeToStudent(studentId: Int, feeType: String, amount: Double, dueDate: String) {
        viewModelScope.launch {
            repository.insertFee(
                Fee(studentId = studentId, feeType = feeType, amount = amount, dueDate = dueDate)
            )
        }
    }

    fun recordPayment(studentId: Int, feeId: Int, amount: Double, method: String) {
        viewModelScope.launch {
            val fee = repository.getFeeById(feeId)
            if (fee != null) {
                val newStatus = if (amount >= fee.amount) "Paid" else "Partially Paid"
                repository.updateFee(fee.copy(status = newStatus))
                
                val customReceipt = "REC-${(10000..99999).random()}"
                val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                repository.insertPayment(
                    Payment(
                        studentId = studentId,
                        feeId = feeId,
                        amount = amount,
                        paymentMethod = method,
                        paymentDate = todayStr,
                        receiptNumber = customReceipt
                    )
                )
            }
        }
    }

    // --- TEACHER ACTIONS ---
    fun markAttendance(studentId: Int, classId: Int, status: String, date: String, remarks: String? = null) {
        viewModelScope.launch {
            repository.insertAttendance(
                Attendance(studentId = studentId, classId = classId, date = date, status = status, remarks = remarks)
            )
        }
    }

    fun addExam(name: String, academicYear: String, term: String, startDate: String, endDate: String) {
        viewModelScope.launch {
            repository.insertExam(Exam(name = name, academicYear = academicYear, term = term, startDate = startDate, endDate = endDate))
        }
    }

    fun enterExamMarks(examId: Int, studentId: Int, subjectId: Int, marks: Double) {
        viewModelScope.launch {
            val grade = when {
                marks >= 90 -> "A+"
                marks >= 80 -> "A"
                marks >= 70 -> "B"
                marks >= 60 -> "C"
                marks >= 50 -> "D"
                else -> "F"
            }
            repository.insertExamResult(
                ExamResult(
                    examId = examId,
                    studentId = studentId,
                    subjectId = subjectId,
                    marks = marks,
                    grade = grade,
                    remarks = "Recorded by Teacher"
                )
            )
        }
    }

    fun createAssignment(classId: Int, subjectId: Int, title: String, description: String, dueDate: String) {
        viewModelScope.launch {
            val teacherId = _currentTeacher.value?.id ?: 1
            repository.insertAssignment(
                Assignment(
                    teacherId = teacherId,
                    classId = classId,
                    subjectId = subjectId,
                    title = title,
                    description = description,
                    dueDate = dueDate
                )
            )
        }
    }

    fun gradeAssignmentSubmission(submissionId: Int, marks: Double, feedback: String) {
        viewModelScope.launch {
            val submissions = repository.getAllSubmissionsFlow().firstOrNull()
            val sub = submissions?.find { it.id == submissionId }
            if (sub != null) {
                repository.updateSubmission(sub.copy(marks = marks, feedback = feedback, status = "Graded"))
            }
        }
    }

    // --- STUDENT ACTIONS ---
    fun submitAssignment(assignmentId: Int, studentId: Int, textContent: String) {
        viewModelScope.launch {
            val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            repository.insertSubmission(
                AssignmentSubmission(
                    assignmentId = assignmentId,
                    studentId = studentId,
                    file = "textContent: $textContent",
                    submissionDate = todayStr,
                    status = "Submitted"
                )
            )
        }
    }

    // --- COMMUNICATION SYSTEM ---
    fun sendMessage(receiverEmail: String, subject: String, body: String) {
        viewModelScope.launch {
            val sender = _currentUser.value
            val receiver = repository.getUserByEmail(receiverEmail)
            if (sender != null && receiver != null) {
                repository.insertMessage(
                    Message(
                        senderId = sender.id,
                        receiverId = receiver.id,
                        subject = subject,
                        message = body
                    )
                )
            }
        }
    }
}

class SchoolViewModelFactory(
    private val application: Application,
    private val repository: SchoolRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SchoolViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SchoolViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
