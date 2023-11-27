package com.example.studysmart.ui.task

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studysmart.subject
import com.example.studysmart.ui.components.DeleteDialog
import com.example.studysmart.ui.components.SubjectListBottomSheet
import com.example.studysmart.ui.components.TaskCheckBox
import com.example.studysmart.ui.components.TaskDatePickerDialog
import com.example.studysmart.ui.theme.Green
import com.example.studysmart.ui.theme.Red
import com.example.studysmart.util.Priority
import com.example.studysmart.util.changeMillsToDateString
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import java.time.Instant


data class TaskScreenNavArgs(
    val taskId : Int?,
    val subjectId : Int?
)

@Destination(navArgsDelegate = TaskScreenNavArgs::class)
@Composable
fun TaskScreenRoute(navigator : DestinationsNavigator) {
    TaskScreen (
        onBackButtonClick = {
            navigator.navigateUp()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskScreen(
    onBackButtonClick : () -> Unit
) {

    val scope = rememberCoroutineScope()

    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    var isDeleteDialog by rememberSaveable { mutableStateOf(false)}

    val sheetState = rememberModalBottomSheetState()
    var isDismissSubjectListBottomSheet by rememberSaveable { mutableStateOf(false)}

    var isOpenDatePicker by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()
    )



    var taskTitleError by rememberSaveable { mutableStateOf<String?>(null) }
    taskTitleError = when {
        title.isBlank() -> "Please Enter Task Title"
        title.length < 4 -> "Title is too small"
        title.length > 40 -> "Tittle is too long"
        else -> null
    }

    DeleteDialog(
        isOpen = isDeleteDialog,
        title = "Delete Task?",
        bodyText = "Are you sure you want to delete task?"
        +"This action cannot be undone",
        onDismissRequest = { isDeleteDialog = false },
        onSaveRequest = {isDeleteDialog = false}
    )

    TaskDatePickerDialog(
        state = datePickerState,
        isOpen = isOpenDatePicker,
        onDismissRequest = { isOpenDatePicker = false },
        onConfirmButtonClicked = { isOpenDatePicker = false }
    )

    SubjectListBottomSheet(
        sheetState = sheetState,
        isOpen = isDismissSubjectListBottomSheet,
        subjects = subject,
        onSubjectClick ={isDismissSubjectListBottomSheet = false},
        onDismissRequest = {
            scope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                if (!sheetState.isVisible) isDismissSubjectListBottomSheet = false
            }
        })



    Scaffold(
        topBar = {
            TaskScreenTopBar(
                isTaskExit = true,
                isComplete = false,
                checkBoxBoarderColor = Red,
                onBackButtonClick = onBackButtonClick,
                onDeleteButtonClick = { isDeleteDialog = true },
                onCheckBoxButtonClick = {})
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(state = rememberScrollState())
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = title,
                onValueChange = { title = it },
                label = { Text(text = "Title") },
                singleLine = true,
                isError = taskTitleError != null && title.isNotBlank(),
                supportingText = { Text(text = taskTitleError.orEmpty()) }
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = description,
                onValueChange = { description = it },
                label = { Text(text = "Description(optional)") },
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Due Date",
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = datePickerState.selectedDateMillis.changeMillsToDateString(),
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = { isOpenDatePicker = true  }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date Button"
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Priority",
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Priority.entries.forEach { priority ->
                    PriorityButton(
                        modifier = Modifier
                            .weight(1f),
                        label = priority.title,
                        backgroundColor = priority.color,
                        boarderColor = if (priority == Priority.MEDIUM) {
                            Color.White
                        } else Color.Transparent,
                        labelColor = if (priority == Priority.MEDIUM) {
                            Color.White
                        } else Color.White.copy(alpha = 0.7f),
                        onClick = {}
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Related to subject",
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "English",
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = { isDismissSubjectListBottomSheet = true }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Arrow Drop down"
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Button(
                enabled = taskTitleError == null,
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Text(text = "Save")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskScreenTopBar(
    isTaskExit: Boolean,
    isComplete: Boolean,
    checkBoxBoarderColor: Color,
    onBackButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    onCheckBoxButtonClick: () -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Navigation BackButton"
                )
            }
        },
        title = {
            Text(
                text = "Task",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        actions = {
            if (isTaskExit) {
                TaskCheckBox(
                    isCompleted = isComplete,
                    boarderColor = checkBoxBoarderColor,
                    onCheckBoxClick = onCheckBoxButtonClick
                )
                IconButton(onClick = onDeleteButtonClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Task"
                    )
                }

            }
        }
    )
}

@Composable
private fun PriorityButton(
    modifier: Modifier = Modifier,
    label: String,
    backgroundColor: Color,
    boarderColor: Color,
    labelColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(5.dp)
            .border(1.dp, boarderColor, RoundedCornerShape(5.dp))
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = labelColor
        )
    }
}








