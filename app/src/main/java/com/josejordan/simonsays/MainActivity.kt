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
// Esta función comprueba si la primera lista es un prefijo de la segunda
fun startsWith(prefix: List<Int>, list: List<Int>): Boolean {
    if (prefix.size > list.size) return false
    return prefix.indices.all { index -> prefix[index] == list[index] }
}

@Composable
fun SimonSaysGame() {
    var sequence by remember { mutableStateOf(listOf<Int>()) }
    var currentInput by remember { mutableStateOf(listOf<Int>()) }
    var gameActive by remember { mutableStateOf(false) }
    var gameStarted by remember { mutableStateOf(false) }
    var showSequence by remember { mutableStateOf(false) }
    var userTurn by remember { mutableStateOf(false) }
    var gameMessage by remember { mutableStateOf("Press 'Start' to play Simon Says!") }

    LaunchedEffect(gameActive) {
        if (gameActive) {
            sequence += listOf((0..3).random())
            currentInput = listOf()
            showSequence = true
            var index = 0
            delay(1000L)
            while (index < sequence.size) {
                currentInput = sequence.take(index + 1)
                gameMessage = "Watch the sequence!"
                delay(600L)
                currentInput = listOf()
                delay(400L)
                index++
            }
            showSequence = false
            userTurn = true
            gameMessage = "Your turn!"
        }
    }

    LaunchedEffect(currentInput) {
        if (userTurn && currentInput.isNotEmpty()) {
            if (!startsWith(currentInput, sequence)) {
                gameMessage = "Wrong sequence! Try again."
                sequence = listOf()
                gameActive = false
                gameStarted = false
                userTurn = false
                delay(2000L)
                gameMessage = "Press 'Start' to play Simon Says!"
            } else if (sequence.size == currentInput.size) {
                userTurn = false
                gameMessage = "Correct! Keep going."
                delay(2000L)
                gameActive = true
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
        Text(text = gameMessage, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        if (!gameStarted) {
            Button(onClick = {
                gameStarted = true
                gameActive = true
            }) {
                Text(text = "Start")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow)
        Row {
            colors.forEachIndexed { index, color ->
                val isCurrent = currentInput.contains(index)
                Button(
                    onClick = {
                        if (userTurn && !showSequence) {
                            currentInput += index
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = if (isCurrent && showSequence) color else color.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .height(100.dp)
                ) {
                    Spacer(modifier = Modifier.background(if (isCurrent && showSequence) color else color.copy(alpha = 0.3f)))
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