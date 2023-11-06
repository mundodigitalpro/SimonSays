package com.josejordan.simonsays
/*
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
    var gameActive by remember { mutableStateOf(false) }
    var gameStarted by remember { mutableStateOf(false) }
    var showSequence by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = gameActive) {
        if (gameActive) {
            showSequence = true
            sequence += (0..3).random()
            delay(1000L)
            for (colorIndex in sequence) {
                currentInput = listOf(colorIndex)
                delay(500L)
            }
            currentInput = emptyList()
            showSequence = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = if (gameStarted) "¡Repite la secuencia!" else "¡Bienvenido a Simon Says!")

        Spacer(modifier = Modifier.height(16.dp))

        if (gameStarted && !gameActive) {
            Button(onClick = { gameActive = true }) {
                Text(text = "Iniciar Secuencia")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botones de colores para la entrada del usuario
        val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow)
        Row {
            colors.forEachIndexed { index, color ->
                Button(
                    onClick = {
                        if (!showSequence) {
                            currentInput += index
                            if (sequence.size == currentInput.size) {
                                if (sequence == currentInput) {
                                    gameActive = true
                                } else {
                                    gameStarted = false
                                    sequence = emptyList()
                                    currentInput = emptyList()
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = if (showSequence && currentInput.contains(index)) color else Color.Gray),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(if (showSequence && sequence.contains(index)) color else Color.Transparent))
                }
            }
        }

        if (!gameStarted) {
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { gameStarted = true; gameActive = true }) {
                Text(text = "Comenzar Juego")
            }
        }
    }
}
*/
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
    var gameActive by remember { mutableStateOf(false) }
    var gameStarted by remember { mutableStateOf(false) }
    var showSequence by remember { mutableStateOf(false) }
    var currentIndexToShow by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = gameActive) {
        if (gameActive) {
            sequence = sequence + listOf((0..3).random())
            currentIndexToShow = 0
            showSequence = true
            gameActive = false
        }
    }

    LaunchedEffect(key1 = showSequence) {
        if (showSequence && currentIndexToShow < sequence.size) {
            delay(500L) // Wait half a second between colors
            currentInput = sequence.take(currentIndexToShow + 1)
            currentIndexToShow += 1
            delay(500L) // Show color for half a second
            if (currentIndexToShow >= sequence.size) {
                showSequence = false
                currentInput = emptyList()
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
        Text(text = if (!gameStarted) "¡Bienvenido a Simon Says!" else if (!showSequence) "Tu turno" else "Observa la secuencia")

        Spacer(modifier = Modifier.height(16.dp))

        if (!gameStarted) {
            Button(onClick = { gameStarted = true; gameActive = true }) {
                Text(text = "Comenzar Juego")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botones de colores para la entrada del usuario
        val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow)
        Row {
            colors.forEachIndexed { index, color ->
                val isCurrent = currentInput.lastOrNull() == index
                Button(
                    onClick = {
                        if (!gameStarted || showSequence) return@Button
                        currentInput += index
                        if (currentInput != sequence.take(currentInput.size)) {
                            // Incorrect sequence
                            gameStarted = false
                            sequence = emptyList()
                            currentInput = emptyList()
                            gameActive = false
                        } else if (currentInput.size == sequence.size) {
                            // Correct sequence, wait for next round
                            gameActive = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = if (isCurrent && showSequence) colors[index] else color.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .height(100.dp)
                ) {
                    if (isCurrent && showSequence) {
                        Spacer(modifier = Modifier.background(color))
                    } else {
                        Spacer(modifier = Modifier.background(color.copy(alpha = 0.3f)))
                    }
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