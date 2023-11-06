package com.josejordan.simonsays

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.josejordan.simonsays.ui.theme.SimonSaysTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimonSaysGame()
        }
    }
}

@Composable
fun SimonSaysGame() {
    var sequence by remember { mutableStateOf(listOf<Int>()) }
    var currentInput by remember { mutableStateOf(listOf<Int>()) }
    var gameStarted by remember { mutableStateOf(false) }
    var activeColor by remember { mutableStateOf(-1) }
    var gameMessage by remember { mutableStateOf("Press 'Start' to play Simon Says!") }
    var sequenceKey by remember { mutableStateOf(0) } // Agregado para forzar la recomposición
    val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow)
    var score by remember { mutableStateOf(0) } // Variable para la puntuación


// Este efecto se reinicia cada vez que 'sequenceKey' cambia y 'gameStarted' es true
    LaunchedEffect(sequenceKey, gameStarted) {
        if (gameStarted) {
            sequence += (0..3).random()
            activeColor = -1
            gameMessage = "Watch the sequence!"
            delay(1000L)

            sequence.forEach { colorIndex ->
                activeColor = colorIndex
                delay(600L) // Show color for a while
                activeColor = -1
                delay(400L) // Pause between colors
            }

            gameMessage = "Your turn!"
            delay(500L) // Short pause before the user's turn begins
        }
    }

// Este efecto verifica la entrada del usuario
    LaunchedEffect(currentInput.size, sequence) {
        if (currentInput.isNotEmpty() && currentInput.size == sequence.size) {
            if (currentInput == sequence) {
                gameMessage = "Correct! Keep going."
                score++ // Incrementa la puntuación por secuencia correcta
                delay(2000L) // Wait before starting the next sequence
                currentInput = listOf()
                sequenceKey++ // Incrementar la clave para forzar la recomposición y reiniciar la secuencia
            } else {
                gameMessage = "Wrong sequence! Try again."
                score = 0 // Reinicia la puntuación cuando el usuario se equivoca
                delay(2000L)
                gameStarted = false
                sequence = listOf()
                currentInput = listOf()
                sequenceKey++ // También reiniciar cuando el usuario se equivoca
                gameMessage = "Press 'Start' to play Simon Says!"
            }
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Score: $score", style = MaterialTheme.typography.headlineMedium) // Mostrar la puntuación
        Text(text = gameMessage, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        if (!gameStarted) {
            Button(onClick = { gameStarted = true }) {
                Text(text = "Start")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row {
            colors.forEachIndexed { index, color ->
                val isCurrent = index == activeColor
                Button(
                    onClick = {
                        if (gameStarted && activeColor == -1 && currentInput.size < sequence.size) {
                            currentInput += index
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCurrent) color else color.copy(
                            alpha = 0.3f
                        )
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .height(100.dp)
                ) {
                    Spacer(modifier = Modifier.background(if (isCurrent) color else color.copy(alpha = 0.3f)))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimonSaysTheme {
        SimonSaysGame()
    }
}