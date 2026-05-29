package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.Course
import com.example.data.repository.CourseLessonsData
import com.example.data.repository.LessonQuiz
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.SecondaryPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailsScreen(
    course: Course,
    onBackClicked: () -> Unit,
    onEnrollClicked: () -> Unit,
    onUnenrollClicked: () -> Unit,
    onProgressUpdated: (Int) -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val lessonsList = remember(course.id) { CourseLessonsData.getLessonsForCourse(course.id) }

    // Active lesson selection (defaults to lesson 0 or the first uncompleted lesson)
    var selectedLessonIndex by remember(course.id) {
        // Find index of first uncompleted lesson based on course progress (each lesson is 25% progress)
        val targetIndex = (course.progress / 25).coerceIn(0, 3)
        mutableStateOf(targetIndex)
    }

    val activeLesson = lessonsList.getOrNull(selectedLessonIndex)

    // Interactive Quiz Option states
    var selectedOptionIndex by remember(selectedLessonIndex) { mutableStateOf<Int?>(null) }
    var answerChecked by remember(selectedLessonIndex) { mutableStateOf(false) }
    var answerIsCorrect by remember(selectedLessonIndex) { mutableStateOf<Boolean?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "EduZone Curriculum",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClicked,
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back",
                            tint = PrimaryPurple
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Course Info Block
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF1E1C24) else Color.White
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isDark) Color(0xFF332F42) else Color(0xFFCAC4D0)
                ),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PrimaryPurple.copy(alpha = 0.15f),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text(
                                text = course.language.uppercase(),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = PrimaryPurple
                            )
                        }

                        Text(
                            text = course.difficulty.uppercase(),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = when (course.difficulty.lowercase()) {
                                "beginner" -> if (isDark) Color(0xFF81C784) else Color(0xFF2E7D32)
                                "intermediate" -> if (isDark) Color(0xFFFFB74D) else Color(0xFFE65100)
                                else -> if (isDark) Color(0xFFFF8A80) else Color(0xFFC2185B)
                            }
                        )
                    }

                    Text(
                        text = course.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = course.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Enrollment controller Row
                    if (!course.enrolled) {
                        Button(
                            onClick = onEnrollClicked,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("enroll_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryPurple,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Enroll & Access Sandbox",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Black)
                            )
                        }
                    } else {
                        Column {
                            // Progress bar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Syllabus Progress: ${course.progress}% Completed",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (isDark) Color(0xFF81C784) else Color(0xFF2E7D32)
                                )
                                Text(
                                    text = "Duration: ${course.duration}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { course.progress / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = PrimaryPurple,
                                trackColor = if (isDark) Color(0xFF221F2B) else Color(0xFFEADDFF)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedButton(
                                onClick = onUnenrollClicked,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("unenroll_button"),
                                shape = RoundedCornerShape(24.dp),
                                border = BorderStroke(1.dp, Color(0xFFEF5350)),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFFEF5350)
                                )
                            ) {
                                Text(
                                    text = "Unenroll (Reset Achievements)",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }

            // Lessons List
            Text(
                text = "Course Curriculum",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            if (!course.enrolled) {
                // Blur/Locked placeholder content
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) Color(0xFF1E1C24) else Color(0xFFF3EDF7)
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isDark) Color(0xFF332F42) else Color(0xFFCAC4D0)
                    ),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked curriculum",
                            tint = PrimaryPurple,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Sandbox Environment Locked",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-0.5).sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Press the Enroll button above to start your course track, complete standard multiple-choice challenges, and construct your skills.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                // Interactive Lessons layout
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    lessonsList.forEachIndexed { index, lesson ->
                        val isCompleted = course.progress >= ((index + 1) * 25)
                        val isSelected = selectedLessonIndex == index

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedLessonIndex = index }
                                .testTag("lesson_card_$index"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) {
                                    if (isDark) Color(0xFF2C1E5C) else Color(0xFFEADDFF)
                                } else {
                                    if (isDark) Color(0xFF1E1C24) else Color.White
                                }
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (isSelected) {
                                    PrimaryPurple
                                } else {
                                    if (isDark) Color(0xFF2A2633) else Color(0xFFEADDFF)
                                }
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (isCompleted) {
                                            if (isDark) Color(0xFF1B5E20) else Color(0xFFC8E6C9)
                                        } else {
                                            if (isDark) Color(0xFF2A2633) else Color(0xFFF3EDF7)
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            if (isCompleted) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Completed",
                                                    tint = if (isDark) Color(0xFFE8F5E9) else Color(0xFF1B5E20),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            } else {
                                                Text(
                                                    text = "${index + 1}",
                                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                                    color = if (isDark) Color.White else Color(0xFF49454F)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column {
                                        Text(
                                            text = lesson.title,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "Lecture ${index + 1}",
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                    }
                                }

                                if (isCompleted) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Verified completed",
                                        tint = if (isDark) Color(0xFF81C784) else Color(0xFF2E7D32),
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Ready",
                                        tint = PrimaryPurple,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Simulator Playground Interactive Card (Always-Dark high tech terminal aesthetic)
                if (activeLesson != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("terminal_sandbox_card"),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF14121E) // High contrast premium obsidian terminal color
                        ),
                        border = BorderStroke(1.dp, if (isDark) Color(0xFF2C2548) else Color(0xFFC0AFFF))
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            // Playground header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFF5F56))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFFBD2E))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF27C93F))
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Interactive Sandbox Lesson ${selectedLessonIndex + 1}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 0.5.sp
                                    ),
                                    color = Color(0xFFD0BCFF)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = activeLesson.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Interactive course theoretical explanation block
                            Text(
                                text = activeLesson.explanation,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFEADDFF)
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            // Question box
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF0F0E13),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "QUIZ CHALLENGE",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp),
                                        color = Color(0xFFD0BCFF)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = activeLesson.question,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace),
                                        color = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Options selector
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                activeLesson.options.forEachIndexed { optIndex, option ->
                                    val isSelected = selectedOptionIndex == optIndex
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (isSelected) Color(0xFF2C2152) else Color(0xFF1D1B22))
                                            .clickable {
                                                if (!answerChecked) {
                                                    selectedOptionIndex = optIndex
                                                }
                                            }
                                            .padding(vertical = 4.dp, horizontal = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = {
                                                if (!answerChecked) {
                                                    selectedOptionIndex = optIndex
                                                }
                                            },
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = Color(0xFFD0BCFF),
                                                unselectedColor = Color(0xFF49454F)
                                            ),
                                            modifier = Modifier.testTag("radio_option_$optIndex")
                                        )
                                        Text(
                                            text = option,
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Action verification button
                            if (!answerChecked) {
                                Button(
                                    onClick = {
                                        if (selectedOptionIndex != null) {
                                            val correct = selectedOptionIndex == activeLesson.correctIndex
                                            answerIsCorrect = correct
                                            answerChecked = true

                                            if (correct) {
                                                // If this is correct, increment progress mathematically!
                                                // Lesson index 0 completed -> progress = 25
                                                // Lesson index 1 completed -> progress = 50
                                                // Lesson index 2 completed -> progress = 75
                                                // Lesson index 3 completed -> progress = 100
                                                val requiredProgress = (selectedLessonIndex + 1) * 25
                                                // only increase, do not reduce progress
                                                val nextProgress = maxOf(course.progress, requiredProgress)
                                                onProgressUpdated(nextProgress)
                                            }
                                        }
                                    },
                                    enabled = selectedOptionIndex != null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("submit_answer_button"),
                                    shape = RoundedCornerShape(24.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PrimaryPurple,
                                        disabledContainerColor = Color(0xFF2F2B36),
                                        disabledContentColor = Color(0xFF8A82AE)
                                    )
                                ) {
                                    Text(
                                        text = "Execute Code & Run Assessment",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Black)
                                    )
                                }
                            } else {
                                // Feedback visual layouts
                                Column {
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = if (answerIsCorrect == true) Color(0xFF1B5E20) else Color(0xFFC2185B).copy(alpha = 0.2f),
                                        border = BorderStroke(1.dp, if (answerIsCorrect == true) Color(0xFF81C784) else Color(0xFFEF5350)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(16.dp),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Info,
                                                contentDescription = null,
                                                tint = if (answerIsCorrect == true) Color(0xFF81C784) else Color(0xFFFF8A80),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column {
                                                Text(
                                                    text = if (answerIsCorrect == true) "SUCCESS: COMPILATION PASSED" else "ERROR: COMPILATION FAILED",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = if (answerIsCorrect == true) Color(0xFF81C784) else Color(0xFFFF8A80)
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = if (answerIsCorrect == true) {
                                                        activeLesson.successExplanation
                                                    } else {
                                                        "That choice is incorrect. Review standard documentation guidelines and try compiling again."
                                                    },
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = if (answerIsCorrect == true) Color(0xFFC8E6C9) else Color(0xFFFFCDD2)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Button(
                                        onClick = {
                                            if (answerIsCorrect == true && selectedLessonIndex < 3) {
                                                selectedLessonIndex += 1
                                            }
                                            selectedOptionIndex = null
                                            answerChecked = false
                                            answerIsCorrect = null
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(24.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (answerIsCorrect == true) PrimaryPurple else Color(0xFF2C2456)
                                        )
                                    ) {
                                        Text(
                                            text = if (answerIsCorrect == true) "Unlock Next Lesson" else "Try Re-Compiling",
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
