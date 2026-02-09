package com.edosoft.vertexengine.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.unit.dp
import com.edosoft.vertexengine.render.api.ViewportHost
import com.edosoft.vertexengine.render.jogl.createGlPanel

@Composable
fun App() {
    MaterialTheme {
        CubeScreen()
    }
}

@Composable
private fun CubeScreen() {
    var isRotating by remember { mutableStateOf(true) }
    var speed by remember { mutableFloatStateOf(1f) }

    Row(Modifier.fillMaxSize()) {
        SwingPanel(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            factory = { createGlPanel() },
            update = { panel ->
                val controller = (panel as ViewportHost).controller
                controller.isRotating = isRotating
                controller.speed = speed
            }
        )

        Surface(
            tonalElevation = 2.dp,
            modifier = Modifier
                .width(300.dp)
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Toolbar", style = MaterialTheme.typography.titleMedium)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { isRotating = true }) { Text("Play") }
                    OutlinedButton(onClick = { isRotating = false }) { Text("Pause") }
                }

                Text("Speed: ${"%.2f".format(speed)}")
                Slider(
                    value = speed,
                    onValueChange = { speed = it },
                    valueRange = 0f..3f
                )
            }
        }
    }
}