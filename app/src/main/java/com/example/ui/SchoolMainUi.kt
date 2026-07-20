package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.ExperimentalMaterial3Api
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.*

@Composable
fun SchoolMainUi(viewModel: SchoolViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
            when (screen) {
                "login" -> LoginScreen(viewModel)
                "admin_dashboard" -> AdminDashboard(viewModel)
                "teacher_dashboard" -> TeacherDashboard(viewModel)
                "student_dashboard" -> StudentDashboard(viewModel)
                "parent_dashboard" -> ParentDashboard(viewModel)
                else -> LoginScreen(viewModel)
            }
        }
    }
}

// ==========================================
// 1. LOGIN SCREEN WITH HERO BANNER & QUICK-LOGIN
// ==========================================
@Composable
fun LoginScreen(viewModel: SchoolViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginError by viewModel.loginError.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Hero Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_school_hero),
                contentDescription = "Dangla City School Campus",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                            startY = 100f
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Dangla City School",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Smart Management System",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome Back",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("username_input"),
                singleLine = true
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("password_input"),
                singleLine = true
            )

            if (loginError != null) {
                Text(
                    text = loginError ?: "",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Button(
                onClick = { viewModel.login(email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("login_button"),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Login to Dashboard", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            var showDemoAccounts by remember { mutableStateOf(false) }

            TextButton(
                onClick = { showDemoAccounts = !showDemoAccounts },
                modifier = Modifier.testTag("toggle_demo_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = if (showDemoAccounts) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (showDemoAccounts) "Hide Demo Access" else "Reveal Demo Credentials",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            AnimatedVisibility(
                visible = showDemoAccounts,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Demo Accounts (Quick Select)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val demoUsers = listOf(
                            Triple("Admin", "admin@school.com", "admin123"),
                            Triple("Teacher", "abebe@school.com", "teacher123")
                        )
                        demoUsers.forEach { (label, demEmail, demPass) ->
                            OutlinedButton(
                                onClick = {
                                    email = demEmail
                                    password = demPass
                                    viewModel.login(demEmail, demPass)
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                            ) {
                                Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val demoUsers2 = listOf(
                            Triple("Student", "daniel@school.com", "student123"),
                            Triple("Parent", "kebede@school.com", "parent123")
                        )
                        demoUsers2.forEach { (label, demEmail, demPass) ->
                            OutlinedButton(
                                onClick = {
                                    email = demEmail
                                    password = demPass
                                    viewModel.login(demEmail, demPass)
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                            ) {
                                Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 2. ADMIN DASHBOARD WITH CRUD FOR ALL TABLES
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboard(viewModel: SchoolViewModel) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val students by viewModel.allStudents.collectAsStateWithLifecycle()
    val teachers by viewModel.allTeachers.collectAsStateWithLifecycle()
    val parents by viewModel.allParents.collectAsStateWithLifecycle()
    val classes by viewModel.allClasses.collectAsStateWithLifecycle()
    val subjects by viewModel.allSubjects.collectAsStateWithLifecycle()
    val announcements by viewModel.allAnnouncements.collectAsStateWithLifecycle()
    val events by viewModel.allEvents.collectAsStateWithLifecycle()

    var activeTab by remember { mutableStateOf("Dashboard") }
    val tabs = listOf("Dashboard", "Students", "Teachers", "Parents", "Classes", "Subjects", "Announcements")

    Row(modifier = Modifier.fillMaxSize()) {
        CleanSideNavigation(
            tabs = tabs,
            activeTab = activeTab,
            onTabSelected = { activeTab = it },
            tabIcon = { tab ->
                when (tab) {
                    "Dashboard" -> Icons.Default.Dashboard
                    "Students" -> Icons.Default.School
                    "Teachers" -> Icons.Default.Person
                    "Parents" -> Icons.Default.People
                    "Classes" -> Icons.Default.Class
                    "Subjects" -> Icons.Default.Book
                    else -> Icons.Default.Announcement
                }
            },
            username = currentUser?.name ?: "Admin",
            role = "Administrator",
            onLogout = { viewModel.logout() }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.background)
        ) {
            CleanHeader(
                title = currentUser?.name ?: "Admin",
                subtitle = "Admin Panel - $activeTab",
                onLogout = { viewModel.logout() }
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (activeTab) {
                    "Dashboard" -> AdminStatsOverview(students.size, teachers.size, parents.size, classes.size, subjects.size, announcements, events)
                    "Students" -> AdminStudentsTab(viewModel, students, classes, parents)
                    "Teachers" -> AdminTeachersTab(viewModel, teachers)
                    "Parents" -> AdminParentsTab(viewModel, parents)
                    "Classes" -> AdminClassesTab(viewModel, classes, teachers)
                    "Subjects" -> AdminSubjectsTab(viewModel, subjects)
                    "Announcements" -> AdminAnnouncementsTab(viewModel, announcements)
                }
            }
        }
    }
}

@Composable
fun AdminStatsOverview(
    studentCount: Int, teacherCount: Int, parentCount: Int,
    classCount: Int, subjectCount: Int, announcements: List<Announcement>, events: List<Event>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("School Statistics", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("Students", studentCount.toString(), Icons.Default.School, Modifier.weight(1f))
                StatCard("Teachers", teacherCount.toString(), Icons.Default.Person, Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("Parents", parentCount.toString(), Icons.Default.People, Modifier.weight(1f))
                StatCard("Classes", classCount.toString(), Icons.Default.Class, Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("Subjects", subjectCount.toString(), Icons.Default.Book, Modifier.weight(1f))
                StatCard("Events", events.size.toString(), Icons.Default.Event, Modifier.weight(1f))
            }
        }

        item {
            Text("Latest Announcements", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }

        items(announcements.take(3)) { announcement ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(announcement.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(announcement.content, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
            Column {
                Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

// ADMIN STUDENTS CRUD
@Composable
fun AdminStudentsTab(
    viewModel: SchoolViewModel,
    students: List<Student>,
    classes: List<ClassEntity>,
    parents: List<ParentEntity>
) {
    var showDialog by remember { mutableStateOf(false) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var classId by remember { mutableStateOf(classes.firstOrNull()?.id ?: 1) }
    var parentId by remember { mutableStateOf(parents.firstOrNull()?.id ?: 1) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (students.isEmpty()) {
            EmptyState("No Students Registered")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(students) { std ->
                    val clsName = classes.find { it.id == std.classId }?.name ?: "Unknown Class"
                    val parentName = parents.find { it.id == std.parentId }?.let { "${it.firstName} ${it.lastName}" } ?: "No Parent"
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("${std.firstName} ${std.lastName}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("ID: ${std.studentId} | $clsName", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                Text("Parent: $parentName", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                Text("Phone: ${std.phone}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            }
                            IconButton(onClick = { viewModel.deleteStudent(std) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete student", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Student", tint = MaterialTheme.colorScheme.onPrimary)
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Add New Student", fontWeight = FontWeight.Bold) },
                text = {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") })
                        OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") })
                        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email (for login)") })
                        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") })
                        OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Address") })

                        // Simple Gender selection
                        Text("Gender:", fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = gender == "Male", onClick = { gender = "Male" })
                                Text("Male")
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = gender == "Female", onClick = { gender = "Female" })
                                Text("Female")
                            }
                        }

                        // Class selection
                        Text("Class:", fontWeight = FontWeight.SemiBold)
                        classes.forEach { cls ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { classId = cls.id }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = classId == cls.id, onClick = { classId = cls.id })
                                Text(cls.name, modifier = Modifier.padding(start = 8.dp))
                            }
                        }

                        // Parent selection
                        Text("Parent/Guardian:", fontWeight = FontWeight.SemiBold)
                        parents.forEach { p ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { parentId = p.id }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = parentId == p.id, onClick = { parentId = p.id })
                                Text("${p.firstName} ${p.lastName}", modifier = Modifier.padding(start = 8.dp))
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.addStudent(
                                firstName = firstName, middleName = "", lastName = lastName,
                                gender = gender, dob = "2010-01-01", phone = phone, email = email,
                                address = address, classId = classId, sectionId = 1, parentId = parentId,
                                admissionDate = "2026-09-01"
                            )
                            showDialog = false
                            // Clear inputs
                            firstName = ""; lastName = ""; email = ""; phone = ""; address = ""
                        }
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}

// ADMIN TEACHERS CRUD
@Composable
fun AdminTeachersTab(viewModel: SchoolViewModel, teachers: List<Teacher>) {
    var showDialog by remember { mutableStateOf(false) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var qualification by remember { mutableStateOf("") }
    var specialization by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        if (teachers.isEmpty()) {
            EmptyState("No Teachers Registered")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(teachers) { t ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("${t.firstName} ${t.lastName}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("ID: ${t.teacherId} | ${t.qualification}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                Text("Specialization: ${t.specialization}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                Text("Phone: ${t.phone}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            }
                            IconButton(onClick = { viewModel.deleteTeacher(t) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete teacher", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Teacher", tint = MaterialTheme.colorScheme.onPrimary)
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Add New Teacher", fontWeight = FontWeight.Bold) },
                text = {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") })
                        OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") })
                        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email (login)") })
                        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") })
                        OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Address") })
                        OutlinedTextField(value = qualification, onValueChange = { qualification = it }, label = { Text("Qualification") })
                        OutlinedTextField(value = specialization, onValueChange = { specialization = it }, label = { Text("Specialization") })
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.addTeacher(
                                firstName = firstName, lastName = lastName, gender = "Female",
                                phone = phone, email = email, address = address, qualification = qualification,
                                specialization = specialization, hireDate = "2026-07-20"
                            )
                            showDialog = false
                            firstName = ""; lastName = ""; email = ""; phone = ""; address = ""; qualification = ""; specialization = ""
                        }
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}

// ADMIN PARENTS CRUD
@Composable
fun AdminParentsTab(viewModel: SchoolViewModel, parents: List<ParentEntity>) {
    var showDialog by remember { mutableStateOf(false) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var relationship by remember { mutableStateOf("Father") }
    var address by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        if (parents.isEmpty()) {
            EmptyState("No Parents Registered")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(parents) { p ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("${p.firstName} ${p.lastName}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("ID: ${p.parentId} | ${p.relationship}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                Text("Phone: ${p.phone}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                Text("Address: ${p.address}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            }
                            IconButton(onClick = { viewModel.deleteParent(p) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete parent", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Parent", tint = MaterialTheme.colorScheme.onPrimary)
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Add New Parent", fontWeight = FontWeight.Bold) },
                text = {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") })
                        OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") })
                        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email (login)") })
                        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") })
                        OutlinedTextField(value = relationship, onValueChange = { relationship = it }, label = { Text("Relationship (e.g. Father, Mother)") })
                        OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Address") })
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.addParent(
                                firstName = firstName, lastName = lastName, relationship = relationship,
                                phone = phone, email = email, address = address
                            )
                            showDialog = false
                            firstName = ""; lastName = ""; email = ""; phone = ""; relationship = "Father"; address = ""
                        }
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}

// ADMIN CLASSES CRUD
@Composable
fun AdminClassesTab(viewModel: SchoolViewModel, classes: List<ClassEntity>, teachers: List<Teacher>) {
    var showDialog by remember { mutableStateOf(false) }
    var className by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var teacherId by remember { mutableStateOf<Int?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (classes.isEmpty()) {
            EmptyState("No Classes Registered")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(classes) { cls ->
                    val classTName = teachers.find { it.id == cls.classTeacherId }?.let { "${it.firstName} ${it.lastName}" } ?: "Unassigned"
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(cls.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(cls.description, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                Text("Class Teacher: $classTName", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.primary)
                            }
                            IconButton(onClick = { viewModel.deleteClass(cls) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete class", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Class", tint = MaterialTheme.colorScheme.onPrimary)
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Create New Class", fontWeight = FontWeight.Bold) },
                text = {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(value = className, onValueChange = { className = it }, label = { Text("Class Name (e.g. Grade 11-A)") })
                        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })

                        Text("Assign Class Teacher:", fontWeight = FontWeight.SemiBold)
                        teachers.forEach { t ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { teacherId = t.id }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = teacherId == t.id, onClick = { teacherId = t.id })
                                Text("${t.firstName} ${t.lastName}", modifier = Modifier.padding(start = 8.dp))
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.addClass(className, description, teacherId)
                            showDialog = false
                            className = ""; description = ""; teacherId = null
                        }
                    ) {
                        Text("Create")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}

// ADMIN SUBJECTS CRUD
@Composable
fun AdminSubjectsTab(viewModel: SchoolViewModel, subjects: List<Subject>) {
    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        if (subjects.isEmpty()) {
            EmptyState("No Subjects Added")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(subjects) { sub ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(sub.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("Code: ${sub.code}", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.primary)
                                Text(sub.description, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            }
                            IconButton(onClick = { viewModel.deleteSubject(sub) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete subject", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Subject", tint = MaterialTheme.colorScheme.onPrimary)
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Add Subject", fontWeight = FontWeight.Bold) },
                text = {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Subject Name") })
                        OutlinedTextField(value = code, onValueChange = { code = it }, label = { Text("Subject Code") })
                        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.addSubject(name, code, description)
                            showDialog = false
                            name = ""; code = ""; description = ""
                        }
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}

// ADMIN ANNOUNCEMENTS
@Composable
fun AdminAnnouncementsTab(viewModel: SchoolViewModel, announcements: List<Announcement>) {
    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var targetRole by remember { mutableStateOf("All") }

    Box(modifier = Modifier.fillMaxSize()) {
        if (announcements.isEmpty()) {
            EmptyState("No Announcements Published")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(announcements) { ann ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(ann.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("To: ${ann.targetRole}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(ann.content, fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Announcement", tint = MaterialTheme.colorScheme.onPrimary)
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Publish Announcement", fontWeight = FontWeight.Bold) },
                text = {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                        OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Message Content") }, modifier = Modifier.height(100.dp))

                        Text("Target Audience:", fontWeight = FontWeight.SemiBold)
                        val roles = listOf("All", "Teacher", "Student", "Parent")
                        roles.forEach { r ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { targetRole = r }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = targetRole == r, onClick = { targetRole = r })
                                Text(r, modifier = Modifier.padding(start = 8.dp))
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.addAnnouncement(title, content, targetRole, null)
                            showDialog = false
                            title = ""; content = ""; targetRole = "All"
                        }
                    ) {
                        Text("Publish")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}

// ==========================================
// 3. TEACHER DASHBOARD
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDashboard(viewModel: SchoolViewModel) {
    val currentTeacher by viewModel.currentTeacher.collectAsStateWithLifecycle()
    val classes by viewModel.allClasses.collectAsStateWithLifecycle()
    val students by viewModel.allStudents.collectAsStateWithLifecycle()
    val assignments by viewModel.allAssignments.collectAsStateWithLifecycle()
    val submissions by viewModel.allSubmissions.collectAsStateWithLifecycle()
    val announcements by viewModel.allAnnouncements.collectAsStateWithLifecycle()

    var activeTab by remember { mutableStateOf("Attendance") }
    val tabs = listOf("Attendance", "Assignments", "Exams & Marks", "Messages")

    Row(modifier = Modifier.fillMaxSize()) {
        CleanSideNavigation(
            tabs = tabs,
            activeTab = activeTab,
            onTabSelected = { activeTab = it },
            tabIcon = { tab ->
                when (tab) {
                    "Attendance" -> Icons.Default.HowToReg
                    "Assignments" -> Icons.Default.Assignment
                    "Exams & Marks" -> Icons.Default.Grade
                    else -> Icons.Default.Message
                }
            },
            username = "${currentTeacher?.firstName} ${currentTeacher?.lastName}",
            role = "Teacher Portal",
            onLogout = { viewModel.logout() }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.background)
        ) {
            CleanHeader(
                title = "${currentTeacher?.firstName} ${currentTeacher?.lastName}",
                subtitle = "Teacher Portal - $activeTab",
                onLogout = { viewModel.logout() }
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (activeTab) {
                    "Attendance" -> TeacherAttendanceTab(viewModel, students, classes)
                    "Assignments" -> TeacherAssignmentsTab(viewModel, assignments, submissions, classes)
                    "Exams & Marks" -> TeacherMarksTab(viewModel, students, classes)
                    "Messages" -> TeacherMessagesTab(viewModel)
                }
            }
        }
    }
}

@Composable
fun TeacherAttendanceTab(viewModel: SchoolViewModel, students: List<Student>, classes: List<ClassEntity>) {
    var selectedClass by remember { mutableStateOf(classes.firstOrNull()?.id ?: 1) }
    val filteredStudents = students.filter { it.classId == selectedClass }
    val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Mark Today's Attendance", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text("Date: $todayStr", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))

        // Class Selection Filter
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            classes.forEach { cls ->
                FilterChip(
                    selected = selectedClass == cls.id,
                    onClick = { selectedClass = cls.id },
                    label = { Text(cls.name) }
                )
            }
        }

        if (filteredStudents.isEmpty()) {
            EmptyState("No Students Enrolled in this Class")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredStudents) { std ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("${std.firstName} ${std.lastName}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("ID: ${std.studentId}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                val options = listOf("Present", "Absent", "Late", "Excused")
                                options.forEach { opt ->
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.markAttendance(std.id, std.classId, opt, todayStr, "Marked by Teacher")
                                        },
                                        modifier = Modifier.weight(1.5f),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = Color.Transparent
                                        ),
                                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                                    ) {
                                        Text(opt, fontSize = 11.sp, maxLines = 1)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TeacherAssignmentsTab(
    viewModel: SchoolViewModel,
    assignments: List<Assignment>,
    submissions: List<AssignmentSubmission>,
    classes: List<ClassEntity>
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var classId by remember { mutableStateOf(classes.firstOrNull()?.id ?: 1) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Assignments", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Button(onClick = { showCreateDialog = true }) {
                        Text("Create")
                    }
                }
            }

            items(assignments) { assign ->
                val clsName = classes.find { it.id == assign.classId }?.name ?: "Unknown"
                val subCount = submissions.count { it.assignmentId == assign.id }
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(assign.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                        Text("Class: $clsName | Due: ${assign.dueDate}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(assign.description, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Submissions: $subCount", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }

        if (showCreateDialog) {
            AlertDialog(
                onDismissRequest = { showCreateDialog = false },
                title = { Text("Create Assignment", fontWeight = FontWeight.Bold) },
                text = {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })

                        Text("Target Class:", fontWeight = FontWeight.SemiBold)
                        classes.forEach { cls ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { classId = cls.id }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = classId == cls.id, onClick = { classId = cls.id })
                                Text(cls.name, modifier = Modifier.padding(start = 8.dp))
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.createAssignment(classId, 1, title, description, "2026-08-01")
                            showCreateDialog = false
                            title = ""; description = ""
                        }
                    ) {
                        Text("Publish")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCreateDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}

@Composable
fun TeacherMarksTab(viewModel: SchoolViewModel, students: List<Student>, classes: List<ClassEntity>) {
    var selectedClass by remember { mutableStateOf(classes.firstOrNull()?.id ?: 1) }
    val filteredStudents = students.filter { it.classId == selectedClass }
    var selectedStudentId by remember { mutableStateOf<Int?>(null) }
    var marksInput by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Enter Exam Marks", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            classes.forEach { cls ->
                FilterChip(
                    selected = selectedClass == cls.id,
                    onClick = { selectedClass = cls.id },
                    label = { Text(cls.name) }
                )
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(filteredStudents) { std ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("${std.firstName} ${std.lastName}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("ID: ${std.studentId}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                        Button(onClick = {
                            selectedStudentId = std.id
                            showDialog = true
                        }) {
                            Text("Enter Marks")
                        }
                    }
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Enter Term Final Marks", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Enter marks out of 100:")
                        OutlinedTextField(
                            value = marksInput,
                            onValueChange = { marksInput = it },
                            label = { Text("Marks") }
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val score = marksInput.toDoubleOrNull() ?: 0.0
                            selectedStudentId?.let { stdId ->
                                viewModel.enterExamMarks(examId = 1, studentId = stdId, subjectId = 1, marks = score)
                            }
                            showDialog = false
                            marksInput = ""
                        }
                    ) {
                        Text("Save Marks")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}

@Composable
fun TeacherMessagesTab(viewModel: SchoolViewModel) {
    var recEmail by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var messageText by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Compose Message", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

        OutlinedTextField(
            value = recEmail,
            onValueChange = { recEmail = it },
            label = { Text("Receiver Email (e.g. parent1@school.com)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = subject,
            onValueChange = { subject = it },
            label = { Text("Subject") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = messageText,
            onValueChange = { messageText = it },
            label = { Text("Message Body") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        Button(
            onClick = {
                viewModel.sendMessage(recEmail, subject, messageText)
                statusMessage = "Message sent successfully!"
                recEmail = ""; subject = ""; messageText = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send Message")
        }

        if (statusMessage.isNotEmpty()) {
            Text(statusMessage, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
        }
    }
}

// ==========================================
// 4. STUDENT DASHBOARD
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboard(viewModel: SchoolViewModel) {
    val currentStudent by viewModel.currentStudent.collectAsStateWithLifecycle()
    val allAnnouncements by viewModel.allAnnouncements.collectAsStateWithLifecycle()
    val allAssignments by viewModel.allAssignments.collectAsStateWithLifecycle()
    val examResults by viewModel.allExamResults.collectAsStateWithLifecycle()
    val fees by viewModel.allFees.collectAsStateWithLifecycle()

    var activeTab by remember { mutableStateOf("Academic") }
    val tabs = listOf("Academic", "Assignments", "Fees", "Announcements")

    Row(modifier = Modifier.fillMaxSize()) {
        CleanSideNavigation(
            tabs = tabs,
            activeTab = activeTab,
            onTabSelected = { activeTab = it },
            tabIcon = { tab ->
                when (tab) {
                    "Academic" -> Icons.Default.Assessment
                    "Assignments" -> Icons.Default.Assignment
                    "Fees" -> Icons.Default.AttachMoney
                    else -> Icons.Default.Announcement
                }
            },
            username = "${currentStudent?.firstName} ${currentStudent?.lastName}",
            role = "Student Portal",
            onLogout = { viewModel.logout() }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.background)
        ) {
            CleanHeader(
                title = "${currentStudent?.firstName} ${currentStudent?.lastName}",
                subtitle = "Student Portal - $activeTab",
                onLogout = { viewModel.logout() }
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (activeTab) {
                    "Academic" -> StudentAcademicTab(currentStudent, examResults)
                    "Assignments" -> StudentAssignmentsTab(viewModel, currentStudent, allAssignments)
                    "Fees" -> StudentFeesTab(currentStudent, fees)
                    "Announcements" -> StudentAnnouncementsTab(allAnnouncements)
                }
            }
        }
    }
}

@Composable
fun StudentAcademicTab(student: Student?, examResults: List<ExamResult>) {
    val results = examResults.filter { it.studentId == student?.id }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Academic Record", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

        if (results.isEmpty()) {
            EmptyState("No Grades Released Yet")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(results) { res ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Term Exam", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("Score: ${res.marks}%", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            }
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(res.grade, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StudentAssignmentsTab(viewModel: SchoolViewModel, student: Student?, assignments: List<Assignment>) {
    var textSubmission by remember { mutableStateOf("") }
    var activeAssignmentId by remember { mutableStateOf<Int?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("My Homework Assignments", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

        if (assignments.isEmpty()) {
            EmptyState("No Pending Assignments")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(assignments) { assign ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(assign.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                            Text("Due Date: ${assign.dueDate}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(assign.description, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    activeAssignmentId = assign.id
                                    showDialog = true
                                },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Submit Work")
                            }
                        }
                    }
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Submit Homework", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Type or paste your answers:")
                        OutlinedTextField(
                            value = textSubmission,
                            onValueChange = { textSubmission = it },
                            label = { Text("Homework Submission Text") },
                            modifier = Modifier.height(120.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            student?.let { std ->
                                activeAssignmentId?.let { assignId ->
                                    viewModel.submitAssignment(assignId, std.id, textSubmission)
                                }
                            }
                            showDialog = false
                            textSubmission = ""
                        }
                    ) {
                        Text("Submit")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}

@Composable
fun StudentFeesTab(student: Student?, fees: List<Fee>) {
    val myFees = fees.filter { it.studentId == student?.id }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Fee Status", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

        if (myFees.isEmpty()) {
            EmptyState("No School Fees Assigned")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(myFees) { fee ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(fee.feeType, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("Amount: $${fee.amount}", fontSize = 14.sp)
                                Text("Due: ${fee.dueDate}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (fee.status == "Paid") MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                                        else MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    fee.status,
                                    fontWeight = FontWeight.Bold,
                                    color = if (fee.status == "Paid") MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StudentAnnouncementsTab(announcements: List<Announcement>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("School Announcements", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

        if (announcements.isEmpty()) {
            EmptyState("No Announcements")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(announcements) { ann ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(ann.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(ann.content, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 5. PARENT DASHBOARD
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentDashboard(viewModel: SchoolViewModel) {
    val currentParent by viewModel.currentParent.collectAsStateWithLifecycle()
    val students by viewModel.allStudents.collectAsStateWithLifecycle()
    val selectedChildId by viewModel.selectedChildId.collectAsStateWithLifecycle()
    val attendanceLogs by viewModel.allAttendance.collectAsStateWithLifecycle()
    val examResults by viewModel.allExamResults.collectAsStateWithLifecycle()
    val fees by viewModel.allFees.collectAsStateWithLifecycle()

    val myChildren = students.filter { it.parentId == currentParent?.id }
    val selectedChild = myChildren.find { it.id == selectedChildId }

    Row(modifier = Modifier.fillMaxSize()) {
        CleanSideNavigation(
            tabs = listOf("Overview"),
            activeTab = "Overview",
            onTabSelected = { },
            tabIcon = { Icons.Default.Dashboard },
            username = "${currentParent?.firstName} ${currentParent?.lastName}",
            role = "Parent Portal",
            onLogout = { viewModel.logout() }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.background)
        ) {
            CleanHeader(
                title = "${currentParent?.firstName} ${currentParent?.lastName}",
                subtitle = "Parent Portal - Overview",
                onLogout = { viewModel.logout() }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Switch Child Monitor", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

                // Child selector chips
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    myChildren.forEach { child ->
                        FilterChip(
                            selected = selectedChildId == child.id,
                            onClick = { viewModel.selectChild(child.id) },
                            label = { Text("${child.firstName} ${child.lastName}") }
                        )
                    }
                }

                if (selectedChild == null) {
                    EmptyState("No Children Linked to Account")
                } else {
                    Text(
                        "Monitoring Active: ${selectedChild.firstName}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    // 1. Attendance Widget
                    val childAttendance = attendanceLogs.filter { it.studentId == selectedChild.id }
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Attendance Report", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(8.dp))
                            if (childAttendance.isEmpty()) {
                                Text("No Attendance Logs Recorded", fontSize = 14.sp)
                            } else {
                                childAttendance.forEach { att ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(att.date, fontSize = 14.sp)
                                        Text(att.status, fontWeight = FontWeight.Bold, color = if (att.status == "Present") MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    }

                    // 2. Grades Widget
                    val childResults = examResults.filter { it.studentId == selectedChild.id }
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Exam & Grades", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(8.dp))
                            if (childResults.isEmpty()) {
                                Text("No Grades Declared", fontSize = 14.sp)
                            } else {
                                childResults.forEach { res ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Term Exam Score", fontSize = 14.sp)
                                        Text("${res.marks}% (Grade: ${res.grade})", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }
                        }
                    }

                    // 3. Fee Monitoring Widget
                    val childFees = fees.filter { it.studentId == selectedChild.id }
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Outstanding Fees", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(8.dp))
                            if (childFees.isEmpty()) {
                                Text("No Fees Assigned", fontSize = 14.sp)
                            } else {
                                childFees.forEach { fee ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(fee.feeType, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                            Text("Due: ${fee.dueDate}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                                        }
                                        Text(
                                            "$${fee.amount} (${fee.status})",
                                            fontWeight = FontWeight.Bold,
                                            color = if (fee.status == "Paid") MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// COMMON PLACEHOLDER / EMPTY STATE UI
@Composable
fun EmptyState(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.HourglassEmpty,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CleanHeader(
    title: String,
    subtitle: String = "Dangla City School",
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = subtitle.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.5.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                    .clickable { onLogout() }
                    .border(1.5.dp, MaterialTheme.colorScheme.surface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        HorizontalDivider(
            color = Color(0xFFE2E8F0),
            thickness = 1.dp
        )
    }
}

@Composable
fun CleanSideNavigation(
    tabs: List<String>,
    activeTab: String,
    onTabSelected: (String) -> Unit,
    tabIcon: (String) -> ImageVector,
    username: String,
    role: String,
    onLogout: () -> Unit
) {
    BoxWithConstraints {
        val isExpanded = maxWidth >= 540.dp
        val sidebarWidth = if (isExpanded) 200.dp else 72.dp
        
        Surface(
            modifier = Modifier
                .width(sidebarWidth)
                .fillMaxHeight(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp
        ) {
            Row(modifier = Modifier.fillMaxHeight()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        horizontalAlignment = if (isExpanded) Alignment.Start else Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // School Logo / Icon
                        Box(
                            modifier = Modifier
                                .padding(horizontal = if (isExpanded) 16.dp else 8.dp)
                                .padding(bottom = 24.dp)
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = "School Logo",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        
                        if (isExpanded) {
                            Column(modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 24.dp)) {
                                Text(
                                    text = username,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                )
                                Text(
                                    text = role,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary,
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Navigation Items
                        tabs.forEach { tab ->
                            val isSelected = activeTab == tab
                            val activeBg = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                            val textColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            val iconColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) activeBg else Color.Transparent)
                                    .clickable { onTabSelected(tab) }
                                    .padding(horizontal = if (isExpanded) 12.dp else 10.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = if (isExpanded) Arrangement.Start else Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = tabIcon(tab),
                                    contentDescription = tab,
                                    tint = iconColor,
                                    modifier = Modifier.size(22.dp)
                                )
                                if (isExpanded) {
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = tab,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = textColor,
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                    
                    // Logout Section
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        HorizontalDivider(
                            color = Color(0xFFE2E8F0),
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onLogout() }
                                .padding(horizontal = if (isExpanded) 12.dp else 10.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = if (isExpanded) Arrangement.Start else Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = "Logout",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(22.dp)
                            )
                            if (isExpanded) {
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Logout",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
                HorizontalDivider(
                    color = Color(0xFFE2E8F0),
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
            }
        }
    }
}

