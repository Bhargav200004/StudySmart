package com.example.studysmart.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.studysmart.domain.model.Subject

@Composable
fun AddSubjectDialog(
    isOpen : Boolean,
    title : String = "Save/Update",
    subjectName : String,
    goalHour : String,
    selectedColor : List<Color>,
    onColorChange : (List<Color>) -> Unit,
    onSubjectChange : (String) -> Unit,
    onGoalHourChange : (String) -> Unit,
    onDismissRequest : () -> Unit,
    onSaveRequest : () -> Unit
) {

    var subjectNameError by rememberSaveable { mutableStateOf<String?>(null)}
    var goalHourError by rememberSaveable { mutableStateOf<String?>(null)}

    subjectNameError = when{
        subjectName.isBlank() -> "Please Enter Subject Name"
        subjectName.length < 1 -> "Subject Name To Short"
        subjectName.length > 20 -> "Subject Name To Long"
        else -> null
    }

    goalHourError = when{
        goalHour.isBlank() -> "Please Enter Goal Study Hour"
        goalHour.toFloatOrNull() == null -> "Invalid Number"
        goalHour.toFloat() < 1f -> "Please set At Least 1 Hour"
        goalHour.toFloat() > 1000f -> "please set a Maximum of 1000 Hour"
        else -> null
    }

    if (isOpen) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = title) },
            text = {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Subject.subjectCardColor.forEach { colors ->
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(shape = CircleShape)
                                    .border(
                                        width = 2.dp,
                                        color = if (colors == selectedColor) Color.Black
                                        else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .background(brush = Brush.verticalGradient(colors))
                                    .clickable { onColorChange(colors) }
                            )
                        }
                    }
                    OutlinedTextField(
                        value = subjectName,
                        onValueChange = onSubjectChange,
                        label = { Text(text = "Subject Name")},
                        singleLine = true,
                        isError = subjectNameError != null && subjectName.isNotBlank(),
                        supportingText = { Text(text = subjectNameError.orEmpty())}
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = goalHour,
                        onValueChange = onGoalHourChange,
                        label = { Text(text = "Goal Study Hour")},
                        singleLine = true,
                        isError = goalHourError != null && goalHour.isNotBlank(),
                        supportingText = { Text(text = goalHourError.orEmpty())},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = onSaveRequest,
                    enabled =subjectNameError == null && goalHourError == null
                ) {
                    Text(text = "Save")
                }
            }
        )
    }
}