package com.edosoft.vertexengine.editor.features.inspector.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Vector3Input(
    label: String,
    vector: org.joml.Vector3f,
    onChanged: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            AxisInput("X", vector.x) { vector.x = it; onChanged() }
            AxisInput("Y", vector.y) { vector.y = it; onChanged() }
            AxisInput("Z", vector.z) { vector.z = it; onChanged() }
        }
    }
}

@Composable
private fun RowScope.AxisInput(
    label: String,
    value: Float,
    onValueValid: (Float) -> Unit
) {
    fun formatFloat(f: Float) = "%.3f".format(f).replace(",", ".")
    var textValue by remember(value) { mutableStateOf(formatFloat(value)) }

    LaunchedEffect(value) {
        val currentTextAsFloat = textValue.replace(",", ".").toFloatOrNull()
        if (currentTextAsFloat == null || kotlin.math.abs(currentTextAsFloat - value) > 0.001f) {
            textValue = formatFloat(value)
        }
    }

    OutlinedTextField(
        value = textValue,
        onValueChange = { newValue ->
            textValue = newValue
            newValue.replace(",", ".").toFloatOrNull()?.let {
                onValueValid(it)
            }
        },
        label = { Text(label) },
        modifier = Modifier.weight(1f),
        singleLine = true,
        textStyle = MaterialTheme.typography.bodySmall
    )
}