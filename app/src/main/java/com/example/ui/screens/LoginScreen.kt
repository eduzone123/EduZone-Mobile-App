package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.SecondaryPurple

@Composable
fun LoginScreen(
    onLoginSuccess: (email: String, passwordText: String, onResult: (Boolean, String) -> Unit) -> Unit,
    onSignUpSuccess: (email: String, fullName: String, passwordText: String, onResult: (Boolean, String) -> Unit) -> Unit
) {
    var isSignUp by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var feedbackMessage by remember { mutableStateOf("") }
    var feedbackTypeSuccess by remember { mutableStateOf(false) }

    val isDark = isSystemInDarkTheme()
    val gradientBrush = remember(isDark) {
        Brush.verticalGradient(
            colors = if (isDark) {
                listOf(Color(0xFF221142), Color(0xFF121115))
            } else {
                listOf(Color(0xFFE8DEF8), Color(0xFFFBFCFF))
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Branded Logo Card (Custom EduZone "Ez" Brand matching launcher icon)
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0xFF0035B1)), // Brilliant Royal Blue matching launcher
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // White Dot in upper-right quadrant
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 22.dp, end = 22.dp)
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    
                    // Main layout with overlapping E and z
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 14.dp, top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "E",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                fontSize = 42.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(1.dp))
                        Text(
                            text = "z",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFF6D00), // Vibrant Orange
                                fontSize = 28.sp
                            ),
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "EduZone",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-1.0).sp
                ),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Learn Programming Interactively",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Credentials Card conforming to the layout
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("auth_form_card"),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF1E1C24) else Color.White
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isDark) Color(0xFF332F42) else Color(0xFFCAC4D0)
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isSignUp) "Create Account" else "Welcome Back",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Text(
                        text = if (isSignUp) "Sign up to track course achievements" else "Enter credentials to access interactive lectures",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(top = 4.dp, bottom = 20.dp)
                    )

                    // Error Message Visualizer
                    if (feedbackMessage.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (feedbackTypeSuccess) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                            border = BorderStroke(1.dp, if (feedbackTypeSuccess) Color(0xFF81C784) else Color(0xFFEF5350)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Text(
                                text = feedbackMessage,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = if (feedbackTypeSuccess) Color(0xFF2E7D32) else Color(0xFFC2185B),
                                modifier = Modifier.padding(12.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Fields
                    if (isSignUp) {
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("Full Name") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryPurple) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("name_input"),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedBorderColor = PrimaryPurple,
                                unfocusedBorderColor = if (isDark) Color(0xFF38334E) else Color(0xFFCAC4D0),
                                focusedLabelColor = PrimaryPurple,
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = PrimaryPurple) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("username_input"),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = if (isDark) Color(0xFF38334E) else Color(0xFFCAC4D0),
                            focusedLabelColor = PrimaryPurple,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = PrimaryPurple) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle password visibility",
                                    tint = PrimaryPurple
                                )
                            }
                        },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("password_input"),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = if (isDark) Color(0xFF38334E) else Color(0xFFCAC4D0),
                            focusedLabelColor = PrimaryPurple,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Authenticate Button
                    Button(
                        onClick = {
                            feedbackMessage = ""
                            if (isSignUp) {
                                onSignUpSuccess(email, fullName, password) { success, msg ->
                                    feedbackMessage = msg
                                    feedbackTypeSuccess = success
                                    if (success) {
                                        feedbackMessage = "Account registered successfully!"
                                    }
                                }
                            } else {
                                onLoginSuccess(email, password) { success, msg ->
                                    feedbackMessage = msg
                                    feedbackTypeSuccess = success
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("submit_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryPurple,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(
                            text = if (isSignUp) "Register & Start" else "Sign In",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Black)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(
                        onClick = {
                            isSignUp = !isSignUp
                            feedbackMessage = ""
                        }
                    ) {
                        Text(
                            text = if (isSignUp) "Already registered? Log In" else "New to EduZone? Create Account",
                            color = PrimaryPurple,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Demo Automated Sign-In Quick-Access Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("demo_quick_access_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF140F30) // Deep ink purple
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "⚡ SANDBOX DEMO QUICK ACCESS ⚡",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = SecondaryPurple
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Skip typing. Press the button below to instantly login with the built-in demo administrator account.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF8A82AE),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            email = "student@eduzone.com"
                            password = "password"
                            feedbackMessage = ""
                            // auto trigger
                            onLoginSuccess(email, password) { success, msg ->
                                feedbackMessage = msg
                                feedbackTypeSuccess = success
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("demo_sign_in_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.linearGradient(listOf(SecondaryPurple, Color(0xFFC0AFFF)))
                        )
                    ) {
                        Text(
                            text = "Auto Sign In as student@eduzone.com",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}
