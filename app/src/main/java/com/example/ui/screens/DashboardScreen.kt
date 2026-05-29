package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.Course
import com.example.data.database.UserAccount
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.SecondaryPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    userAccount: UserAccount,
    courses: List<Course>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onCourseSelected: (Int) -> Unit,
    onLogoutPressed: () -> Unit
) {
    // Calculative statistics for the visual widgets
    val enrolledCourses = remember(courses) { courses.filter { it.enrolled } }
    val totalEnrolled = enrolledCourses.size
    val averageProgress = remember(enrolledCourses) {
        if (enrolledCourses.isEmpty()) 0 else enrolledCourses.map { it.progress }.sum() / enrolledCourses.size
    }

    val rankStatus = when {
        averageProgress >= 75 -> "Elite Scholar"
        averageProgress >= 40 -> "Programming Padawan"
        totalEnrolled >= 1 -> "Active Apprentice"
        else -> "New Cadet"
    }

    // Filtered courses based on current category
    val filteredCourses = remember(courses, selectedCategory) {
        if (selectedCategory == "All") {
            courses
        } else {
            courses.filter { it.language.lowercase() == selectedCategory.lowercase() }
        }
    }

    val categories = listOf("All", "Kotlin", "Python", "JavaScript", "AI")

    var showProfileDialog by remember { mutableStateOf(false) }

    if (showProfileDialog) {
        ProfileDialog(
            userAccount = userAccount,
            enrolledCourses = enrolledCourses,
            rankStatus = rankStatus,
            averageProgress = averageProgress,
            onClose = { showProfileDialog = false },
            onCourseSelected = onCourseSelected
        )
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(PrimaryPurple)
                            .clickable { showProfileDialog = true }
                            .testTag("profile_avatar_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userAccount.fullName.trim().firstOrNull()?.uppercase() ?: "E",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "EduZone",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryPurple
                        )
                    )
                }

                // Logout Button
                IconButton(
                    onClick = onLogoutPressed,
                    modifier = Modifier.testTag("logout_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Logout",
                        tint = if (isSystemInDarkTheme()) Color(0xFFFFA5A5) else Color(0xFFC62828)
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                // Bold Typography Greeting block from HTML template
                Column {
                    Text(
                        text = "Welcome back,",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            letterSpacing = (-1.0).sp
                        )
                    )
                    Text(
                        text = userAccount.fullName,
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Black,
                            color = PrimaryPurple,
                            letterSpacing = (-1.5).sp
                        )
                    )
                    Text(
                        text = "You've active pathways in school with $averageProgress% learning index.",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            item {
                // Featured Course Card mimicking HTML statistics card
                StatsCard(
                    totalEnrolled = totalEnrolled,
                    averageProgress = averageProgress,
                    rankStatus = rankStatus
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Available Tracks",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "See all",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryPurple
                        )
                    )
                }
            }

            // Categories horizontal filter chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { category ->
                        val isSelected = selectedCategory == category
                        val isDark = isSystemInDarkTheme()
                        FilterChip(
                            selected = isSelected,
                            onClick = { onCategorySelected(category) },
                            label = { 
                                Text(
                                    text = category,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                ) 
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = if (isDark) Color(0xFF211D2C) else Color(0xFFF3EDF7),
                                labelColor = if (isDark) Color(0xFFC0AFFF) else Color(0xFF49454F),
                                selectedContainerColor = PrimaryPurple,
                                selectedLabelColor = Color.White
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isDark) Color(0xFF38334E) else Color(0xFFCAC4D0),
                                selectedBorderColor = Color.Transparent
                            )
                        )
                    }
                }
            }

            // Dynamic Empty State
            if (filteredCourses.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "No courses",
                            tint = if (isSystemInDarkTheme()) Color(0xFF3B336A) else Color(0xFFCAC4D0),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No tracks available in this category yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isSystemInDarkTheme()) Color(0xFFA59EC6) else Color(0xFF49454F)
                        )
                    }
                }
            } else {
                items(filteredCourses) { course ->
                    CourseCardItem(
                        course = course,
                        onClick = { onCourseSelected(course.id) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun StatsCard(
    totalEnrolled: Int,
    averageProgress: Int,
    rankStatus: String
) {
    val isDark = isSystemInDarkTheme()
    val containerBg = if (isDark) Color(0xFF2C1E5C) else Color(0xFFEADDFF)
    val textColor = if (isDark) Color(0xFFEADDFF) else Color(0xFF21005D)
    val buttonBg = PrimaryPurple
    val buttonText = Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("student_stats_card"),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerBg
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "ACADEMIC RANK",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.5.sp
                        ),
                        color = textColor.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = rankStatus,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = textColor
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$totalEnrolled Pathways Enrolled",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = textColor.copy(alpha = 0.8f)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(if (isDark) Color(0xFF3F3577) else Color(0xFFD0BCFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Rank Status",
                        tint = if (isDark) Color(0xFFF7D54F) else PrimaryPurple,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Progress Indicators from Design layout
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "$averageProgress%",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Black,
                            color = textColor,
                            letterSpacing = (-1.0).sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isDark) Color(0xFF1D173C) else Color(0xFFD0BCFF))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(averageProgress / 100f)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(buttonBg)
                        )
                    }
                }

                if (totalEnrolled > 0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(buttonBg)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Resume",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = buttonText
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CourseCardItem(
    course: Course,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val levelColor = when (course.difficulty.lowercase()) {
        "beginner" -> if (isDark) Color(0xFFC2F0C2) else Color(0xFF1B5E20)
        "intermediate" -> if (isDark) Color(0xFFFFD180) else Color(0xFFE65100)
        else -> if (isDark) Color(0xFFFF80AB) else Color(0xFFC2185B)
    }

    val iconVector = when (course.language.lowercase()) {
        "kotlin" -> Icons.Default.Code
        "python" -> Icons.Default.Terminal
        "javascript" -> Icons.Default.Bookmark
        "ai" -> Icons.Default.Psychology
        else -> Icons.Default.Code
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("course_card_${course.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF1E1C24) else Color.White
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isDark) Color(0xFF332F42) else Color(0xFFCAC4D0)
        ),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left column: Language badge & Course details
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(levelColor.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = course.difficulty.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = levelColor
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = course.language,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = PrimaryPurple
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = course.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.3).sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Right column: circular language visual representation
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isDark) Color(0xFF2E2B38) else Color(0xFFF3EDF7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = null,
                        tint = PrimaryPurple,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = course.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Flow indicating enrollment status and progress
            if (course.enrolled) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Enrolled",
                                tint = if (isDark) Color(0xFF81C784) else Color(0xFF2E7D32),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Enrolled (Progress: ${course.progress}%)",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isDark) Color(0xFF81C784) else Color(0xFF2E7D32)
                            )
                        }
                        Text(
                            text = course.duration,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { course.progress / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = PrimaryPurple,
                        trackColor = if (isDark) Color(0xFF221F2B) else Color(0xFFEADDFF)
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onClick() }
                    ) {
                        Text(
                            text = "START STUDY TRACK",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            ),
                            color = PrimaryPurple
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = PrimaryPurple,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = "Estimate: ${course.duration}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileDialog(
    userAccount: UserAccount,
    enrolledCourses: List<Course>,
    rankStatus: String,
    averageProgress: Int,
    onClose: () -> Unit,
    onCourseSelected: (Int) -> Unit
) {
    val isDark = isSystemInDarkTheme()
    Dialog(onDismissRequest = onClose) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("profile_dialog_card"),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) Color(0xFF1E1C24) else Color.White
            ),
            border = BorderStroke(
                width = 1.dp,
                color = if (isDark) Color(0xFF332F42) else Color(0xFFCAC4D0)
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                // Header row with title and close icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "My Profile",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close profile",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Avatar and User details Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(PrimaryPurple),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userAccount.fullName.trim().firstOrNull()?.uppercase() ?: "E",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column {
                        Text(
                            text = userAccount.fullName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = userAccount.email,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Statistics Grid Container
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Rank Card
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDark) Color(0xFF2C1E5C) else Color(0xFFEADDFF)
                        ),
                        border = BorderStroke(1.dp, if (isDark) Color(0xFF3F3577) else Color(0xFFD0BCFF))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "RANK",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                                color = (if (isDark) Color(0xFFEADDFF) else Color(0xFF21005D)).copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = rankStatus,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Black),
                                color = if (isDark) Color(0xFFEADDFF) else Color(0xFF21005D),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    
                    // Progress Card
                    Card(
                        modifier = Modifier.weight(1.0f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDark) Color(0xFF16152F) else Color(0xFFF3EDF7)
                        ),
                        border = BorderStroke(1.dp, if (isDark) Color(0xFF2B2844) else Color(0xFFEADDFF))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "INDEX PROGRESS",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$averageProgress%",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Black),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Enrolled courses list
                Text(
                    text = "My Enrolled Paths (${enrolledCourses.size})",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.3).sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(10.dp))
                
                if (enrolledCourses.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .background(
                                color = if (isDark) Color(0xFF16141D) else Color(0xFFF9F9FA),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No active paths enrolled yet.\nStart learning below!",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(enrolledCourses) { course ->
                            val levelColor = when (course.difficulty.lowercase()) {
                                "beginner" -> if (isDark) Color(0xFFC2F0C2) else Color(0xFF1B5E20)
                                "intermediate" -> if (isDark) Color(0xFFFFD180) else Color(0xFFE65100)
                                else -> if (isDark) Color(0xFFFF80AB) else Color(0xFFC2185B)
                            }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onCourseSelected(course.id)
                                        onClose()
                                    },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isDark) Color(0xFF17151D) else Color(0xFFF9F9FA)
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = if (isDark) Color(0xFF2B2835) else Color(0xFFEADDFF)
                                )
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = course.title,
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f)
                                        )
                                        
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = "Go to track",
                                            tint = PrimaryPurple,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.height(6.dp))
                                    
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Progress: ${course.progress}%",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = PrimaryPurple
                                        )
                                        Text(
                                            text = course.difficulty.uppercase(),
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = levelColor
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.height(6.dp))
                                    
                                    LinearProgressIndicator(
                                        progress = { course.progress / 100f },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(4.dp)
                                            .clip(RoundedCornerShape(2.dp)),
                                        color = PrimaryPurple,
                                        trackColor = if (isDark) Color(0xFF211E2A) else Color(0xFFEADDFF)
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
