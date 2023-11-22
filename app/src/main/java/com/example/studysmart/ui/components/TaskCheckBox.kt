package com.example.studysmart.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TaskCheckBox(
    isCompleted: Boolean,
    boarderColor: Color,
    onCheckBoxClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(25.dp)
            .clip(CircleShape)
            .border(2.dp, boarderColor, CircleShape)
            .clickable { onCheckBoxClick() },
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visible = isCompleted) {
            Icon(
                modifier = Modifier
                    .size(20.dp),
                imageVector = Icons.Default.Check,
                contentDescription = null

            )
        }
    }
}