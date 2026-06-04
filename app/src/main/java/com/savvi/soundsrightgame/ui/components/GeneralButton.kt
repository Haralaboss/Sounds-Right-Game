package com.savvi.soundsrightgame.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.savvi.soundsrightgame.ui.theme.Cyan
import com.savvi.soundsrightgame.ui.theme.YellowText

@Composable
fun GeneralButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(0.8f)
            .height(72.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(Cyan),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Bold,
                shadow = Shadow(
                    color = YellowText,
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            )
        )
    }
}