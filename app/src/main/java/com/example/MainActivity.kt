package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.database.EduZoneDatabase
import com.example.data.repository.EduZoneRepository
import com.example.ui.screens.CourseDetailsScreen
import com.example.ui.theme.EduZoneTheme
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.viewmodel.EduZoneViewModel
import com.example.ui.viewmodel.EduZoneViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Initialize local SQL Database using Room Singleton patterns
        val database = EduZoneDatabase.getDatabase(this)
        
        // 2. Instantiate repository carrying courselist defaults and mock admin account population logic
        val repository = EduZoneRepository(database.courseDao(), database.userDao(), database.userCourseProgressDao())
        
        // 3. Bind VM carrying state-machine variables
        val viewModel: EduZoneViewModel by lazy {
            ViewModelProvider(this, EduZoneViewModelFactory(repository))[EduZoneViewModel::class.java]
        }

        setContent {
            EduZoneTheme {
                EduZoneApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun EduZoneApp(viewModel: EduZoneViewModel) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val courses by viewModel.courses.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedCourseId by viewModel.selectedCourseId.collectAsStateWithLifecycle()

    val currentCourse = selectedCourseId?.let { id ->
        courses.find { it.id == id }
    }

    // Capture standard software back presses
    BackHandler(enabled = selectedCourseId != null) {
        viewModel.selectCourse(null)
    }

    // Animation transition container based on logged-in states
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = currentUser,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "AuthRouting"
        ) { user ->
            if (user == null) {
                LoginScreen(
                    onLoginSuccess = { email, password, onResult ->
                        viewModel.login(email, password, onResult)
                    },
                    onSignUpSuccess = { email, fullName, password, onResult ->
                        viewModel.signUp(email, fullName, password, onResult)
                    }
                )
            } else {
                AnimatedContent(
                    targetState = currentCourse,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "WorkspaceDetailsRouting"
                ) { targetCourse ->
                    if (targetCourse != null) {
                        CourseDetailsScreen(
                            course = targetCourse,
                            onBackClicked = { viewModel.selectCourse(null) },
                            onEnrollClicked = { viewModel.enrollInCourse(targetCourse.id) },
                            onUnenrollClicked = { viewModel.unenrollFromCourse(targetCourse.id) },
                            onProgressUpdated = { nextProgress ->
                                viewModel.updateCourseProgress(targetCourse.id, nextProgress)
                            }
                        )
                    } else {
                        DashboardScreen(
                            userAccount = user,
                            courses = courses,
                            selectedCategory = selectedCategory,
                            onCategorySelected = { category ->
                                viewModel.setCategory(category)
                            },
                            onCourseSelected = { id ->
                                viewModel.selectCourse(id)
                            },
                            onLogoutPressed = {
                                viewModel.logout()
                            }
                        )
                    }
                }
            }
        }
    }
}
