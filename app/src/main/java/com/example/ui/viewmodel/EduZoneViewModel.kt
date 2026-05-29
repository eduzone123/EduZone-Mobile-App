package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.database.Course
import com.example.data.database.UserAccount
import com.example.data.repository.EduZoneRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class EduZoneViewModel(private val repository: EduZoneRepository) : ViewModel() {

    // Current State flow for auth session
    private val _currentUser = MutableStateFlow<UserAccount?>(null)
    val currentUser: StateFlow<UserAccount?> = _currentUser.asStateFlow()

    // Courses List mapped dynamically for the logged-in user to prevent crossed enrollments
    val courses: StateFlow<List<Course>> = currentUser.flatMapLatest { user ->
        if (user == null) {
            repository.allCourses.map { list ->
                list.map { it.copy(enrolled = false, progress = 0, enrolledUserEmail = null) }
            }
        } else {
            combine(
                repository.allCourses,
                repository.getUserProgressFlow(user.email)
            ) { baseCourses, userProgress ->
                val progressMap = userProgress.associateBy { it.courseId }
                baseCourses.map { course ->
                    val record = progressMap[course.id]
                    course.copy(
                        enrolled = record?.enrolled ?: false,
                        progress = record?.progress ?: 0,
                        enrolledUserEmail = user.email
                    )
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Current filtered Category
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Interactive details states
    private val _selectedCourseId = MutableStateFlow<Int?>(null)
    val selectedCourseId: StateFlow<Int?> = _selectedCourseId.asStateFlow()

    init {
        // Automatically populate defaults to give user a rich experience out of the box
        viewModelScope.launch {
            repository.populateDefaults()
        }
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun selectCourse(id: Int?) {
        _selectedCourseId.value = id
    }

    // Login function
    fun login(email: String, passwordText: String, onResult: (success: Boolean, message: String) -> Unit) {
        viewModelScope.launch {
            if (email.isBlank() || passwordText.isBlank()) {
                onResult(false, "Please fill in all credentials.")
                return@launch
            }
            val user = repository.getUserByEmail(email.trim())
            if (user != null && user.passwordHash == passwordText) {
                _currentUser.value = user
                onResult(true, "Welcome back, ${user.fullName}!")
            } else {
                onResult(false, "Invalid email or password.")
            }
        }
    }

    // Signup function
    fun signUp(email: String, nameText: String, passwordText: String, onResult: (success: Boolean, message: String) -> Unit) {
        viewModelScope.launch {
            if (email.isBlank() || nameText.isBlank() || passwordText.isBlank()) {
                onResult(false, "All fields are required to sign up.")
                return@launch
            }
            val existing = repository.getUserByEmail(email.trim())
            if (existing != null) {
                onResult(false, "An account with this email already exists.")
                return@launch
            }
            val newUser = UserAccount(email = email.trim(), fullName = nameText.trim(), passwordHash = passwordText)
            repository.registerUser(newUser)
            _currentUser.value = newUser
            onResult(true, "Account created successfully!")
        }
    }

    fun logout() {
        _currentUser.value = null
        _selectedCourseId.value = null
    }

    fun enrollInCourse(courseId: Int) {
        viewModelScope.launch {
            val user = _currentUser.value ?: return@launch
            repository.enrollInCourse(courseId, user.email)
        }
    }

    fun unenrollFromCourse(courseId: Int) {
        viewModelScope.launch {
            val user = _currentUser.value ?: return@launch
            repository.unenrollFromCourse(courseId, user.email)
        }
    }

    fun updateCourseProgress(courseId: Int, progress: Int) {
        viewModelScope.launch {
            val user = _currentUser.value ?: return@launch
            repository.updateCourseProgress(courseId, user.email, progress)
        }
    }
}

class EduZoneViewModelFactory(private val repository: EduZoneRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EduZoneViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EduZoneViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
