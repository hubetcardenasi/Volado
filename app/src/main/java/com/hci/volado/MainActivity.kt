package com.hci.volado

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hci.volado.ui.theme.VoladoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VoladoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    VoladoScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun VoladoScreen(modifier: Modifier = Modifier) {
    var isFlipping by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<String?>(null) }
    var currentSide by remember { mutableStateOf("cara") }

    // Animación de rotación
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(isFlipping) {
        if (isFlipping) {
            result = null

            // Número de vueltas
            val vueltas = (6..12).random()

            rotation.animateTo(
                targetValue = 360f * vueltas,
                animationSpec = tween(
                    durationMillis = 1500,
                    easing = LinearEasing
                )
            )

            // Determinar resultado final
            val finalSide = if (listOf(true, false).random()) "cara" else "aguila"
            currentSide = finalSide
            result = if (finalSide == "cara") "Cayó: Cara" else "Cayó: Águila"

            // Reset rotación
            rotation.snapTo(0f)

            isFlipping = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Imagen de la moneda
        val imageRes = if (currentSide == "cara") {
            R.drawable.moneda_cara
        } else {
            R.drawable.moneda_aguila
        }

        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Moneda",
            modifier = Modifier
                .size(200.dp)
                .graphicsLayer {
                    rotationY = rotation.value
                    cameraDistance = 12 * density
                }
                .clickable(enabled = !isFlipping) {
                    isFlipping = true
                }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Resultado
        result?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VoladoPreview() {
    VoladoTheme {
        VoladoScreen()
    }
}