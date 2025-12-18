package com.escalated.quickly.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.escalated.quickly.data.Question
import com.escalated.quickly.ui.components.GradientButton
import com.escalated.quickly.ui.components.QuestionCard
import com.escalated.quickly.ui.theme.*

@Composable
fun QuestionScreen(
    question: Question?,
    onSkipQuestion: () -> Unit,
    onAddQuestion: () -> Unit,
    onStartRound: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(OrangePrimary.copy(alpha = 0.15f), Cream, White)
                )
            )
    ) {
        // Add question button in corner
        FloatingActionButton(
            onClick = onAddQuestion,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            containerColor = TealAccent,
            contentColor = White,
            shape = CircleShape
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add custom question"
            )
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(60.dp))
                
                Text(
                    text = "Your Question",
                    style = MaterialTheme.typography.headlineSmall,
                    color = OrangeBurnt,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            // Question card
            if (isLoading) {
                CircularProgressIndicator(
                    color = OrangePrimary,
                    modifier = Modifier.size(48.dp)
                )
            } else if (question != null) {
                QuestionCard(
                    question = question,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            
            // Action buttons
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                GradientButton(
                    text = "Start Round",
                    onClick = onStartRound,
                    enabled = question != null && !isLoading,
                    modifier = Modifier.fillMaxWidth()
                )
                
                TextButton(
                    onClick = onSkipQuestion,
                    enabled = !isLoading
                ) {
                    Icon(
                        Icons.Default.SkipNext,
                        contentDescription = null,
                        tint = OrangePrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Skip Question",
                        style = MaterialTheme.typography.labelLarge,
                        color = OrangePrimary
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddQuestionDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var questionText by remember { mutableStateOf("") }
    var meaning1 by remember { mutableStateOf("") }
    var meaning10 by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.clip(RoundedCornerShape(24.dp))
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add Your Question",
                    style = MaterialTheme.typography.headlineSmall,
                    color = OrangeBurnt,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                OutlinedTextField(
                    value = questionText,
                    onValueChange = { questionText = it },
                    label = { Text("Your question") },
                    placeholder = { Text("What's the situation?") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OrangePrimary,
                        focusedLabelColor = OrangePrimary,
                        cursorColor = OrangePrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 3
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(TealAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "1",
                                style = MaterialTheme.typography.titleMedium,
                                color = White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = meaning1,
                            onValueChange = { meaning1 = it },
                            label = { Text("1 means") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TealAccent,
                                focusedLabelColor = TealAccent,
                                cursorColor = TealAccent
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                    
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(OrangePrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "10",
                                style = MaterialTheme.typography.titleMedium,
                                color = White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = meaning10,
                            onValueChange = { meaning10 = it },
                            label = { Text("10 means") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = OrangePrimary,
                                focusedLabelColor = OrangePrimary,
                                cursorColor = OrangePrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel")
                    }
                    
                    Button(
                        onClick = {
                            if (questionText.isNotBlank() && meaning1.isNotBlank() && meaning10.isNotBlank()) {
                                onConfirm(questionText, meaning1, meaning10)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = questionText.isNotBlank() && meaning1.isNotBlank() && meaning10.isNotBlank(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                    ) {
                        Text("Use Question")
                    }
                }
            }
        }
    }
}
