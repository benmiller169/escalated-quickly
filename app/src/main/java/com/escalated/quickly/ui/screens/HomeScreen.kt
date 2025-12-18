package com.escalated.quickly.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.escalated.quickly.ui.components.GradientButton
import com.escalated.quickly.ui.theme.*

@Composable
fun HomeScreen(
    playerCount: Int,
    onPlayerCountChange: (Int) -> Unit,
    onStartGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animations
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient"
    )
    
    val titleScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "titleScale"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        OrangePrimary.copy(alpha = 0.1f + gradientOffset * 0.1f),
                        Cream,
                        OrangeLight.copy(alpha = 0.2f)
                    )
                )
            )
    ) {
        // Decorative circles
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-100).dp, y = (-100).dp)
                .clip(RoundedCornerShape(50))
                .background(OrangePrimary.copy(alpha = 0.1f))
        )
        
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 50.dp, y = 50.dp)
                .clip(RoundedCornerShape(50))
                .background(OrangeBurnt.copy(alpha = 0.1f))
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            
            // Title section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🔥",
                    fontSize = 64.sp,
                    modifier = Modifier.scale(titleScale)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "That Escalated",
                    style = MaterialTheme.typography.displayMedium,
                    color = OrangeBurnt,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = "Quickly!",
                    style = MaterialTheme.typography.displayLarge,
                    color = OrangePrimary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.scale(titleScale)
                )
            }
            
            // Player count selector
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "How many players?",
                        style = MaterialTheme.typography.titleLarge,
                        color = DarkText
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Decrease button
                        FilledIconButton(
                            onClick = { if (playerCount > 2) onPlayerCountChange(playerCount - 1) },
                            enabled = playerCount > 2,
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = TealAccent,
                                disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier.size(56.dp)
                        ) {
                            Icon(
                                Icons.Rounded.Remove,
                                contentDescription = "Decrease",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        
                        // Player count display
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(OrangePrimary, OrangeBurnt)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = playerCount.toString(),
                                style = MaterialTheme.typography.displayLarge,
                                color = White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        // Increase button
                        FilledIconButton(
                            onClick = { if (playerCount < 9) onPlayerCountChange(playerCount + 1) },
                            enabled = playerCount < 9,
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = TealAccent,
                                disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier.size(56.dp)
                        ) {
                            Icon(
                                Icons.Rounded.Add,
                                contentDescription = "Increase",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "(2-9 players)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LightText
                    )
                }
            }
            
            // Start button
            GradientButton(
                text = "Start Game",
                onClick = onStartGame,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
