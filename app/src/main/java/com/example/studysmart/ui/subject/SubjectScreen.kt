package com.example.studysmart.ui.subject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.studysmart.domain.model.Subject
import com.example.studysmart.task
import com.example.studysmart.studySessionList
import com.example.studysmart.ui.components.AddSubjectDialog
import com.example.studysmart.ui.components.CountCard
import com.example.studysmart.ui.components.DeleteDialog
import com.example.studysmart.ui.components.TaskDatePickerDialog
import com.example.studysmart.ui.components.studySessionList
import com.example.studysmart.ui.components.taskList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectScreen() {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState()
    val isFABExpanded by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }


    var isEditSubjectButton by rememberSaveable { mutableStateOf(false) }
    var isDeleteButton by rememberSaveable { mutableStateOf(false) }
    var isDeleteSubjectButton by rememberSaveable { mutableStateOf(false) }

    var subjectName by rememberSaveable { mutableStateOf("") }
    var goalHour by rememberSaveable { mutableStateOf("") }
    var selectedColor by rememberSaveable { mutableStateOf(Subject.subjectCardColor.random()) }

    AddSubjectDialog(
        isOpen = isEditSubjectButton,
        subjectName = subjectName,
        goalHour = goalHour,
        selectedColor = selectedColor,
        onSubjectChange = { subjectName = it },
        onGoalHourChange = { goalHour = it },
        onColorChange = { selectedColor = it },
        onDismissRequest = { isEditSubjectButton = false },
        onSaveRequest = { isEditSubjectButton = false }
    )

    DeleteDialog(
        isOpen = isDeleteSubjectButton,
        title = "Delete Subject",
        bodyText = "Are you sure, you want to delete Subject All related task" +
                " and study session will be permanently remove , this action can not be undone. ",
        onDismissRequest = { isDeleteSubjectButton = false },
        onSaveRequest = { isDeleteSubjectButton = false }
    )


    DeleteDialog(
        isOpen = isDeleteButton,
        title = "Delete Session",
        bodyText = "Are you sure, you want to delete session? Your study hour will reduce " +
                "by this session time . this action cannot be undo",
        onDismissRequest = { isDeleteButton = false },
        onSaveRequest = { isDeleteButton = false }
    )

    Scaffold(
        topBar = {
            SubjectScreenTopBar(
                title = "English",
                onBackButtonClick = {  },
                onDeleteButtonClick = { isDeleteSubjectButton = true},
                onEditButtonClick = { isEditSubjectButton = true},
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton ={
            ExtendedFloatingActionButton(
                onClick = { /*TODO*/ },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                },
                text = { Text(text = "Add Task")},
                expanded = isFABExpanded
            )


        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                SubjectOverViewSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    goalHour = "10",
                    studyHour ="20",
                    progress = 0.5f
                )
            }
            taskList(
                sectionHeading = "Upcoming Task",
                emptyText = "You don't have upcoming task\n" +
                        "Click + in Subject task to add new task",
                tasks = task,
                onCheckBoxClick = {},
                onTaskCardClick = {}
            )
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
            taskList(
                sectionHeading = "Completed Task",
                emptyText = "You don't have complete task\n" +
                        "Click the check box on completion of task",
                tasks = task,
                onCheckBoxClick = {},
                onTaskCardClick = {}
            )
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
            studySessionList(
                sectionHeading = "Resent Study session",
                emptyText = "You don't have resent study Session\n" +
                        "Start a study session to begin the recording",
                sessions = studySessionList,
                onDeleteClick = {isDeleteButton = true}
            )

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectScreenTopBar(
    title : String,
    onBackButtonClick : () -> Unit,
    onDeleteButtonClick : () -> Unit,
    onEditButtonClick : () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    LargeTopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "navigation Back"
                )
            }
        },
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        actions = {
            IconButton(onClick = onDeleteButtonClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Subject Delete Button"
                )
            }
            IconButton(onClick = onEditButtonClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Subject Edit Button"
                )
            }
        }
    )
}

@Composable
private fun SubjectOverViewSection(
    modifier: Modifier,
    goalHour :String,
    studyHour: String,
    progress : Float
) {

    val progressToPercentage = remember(progress){
        (progress * 100).toInt().coerceIn(0,100)
    }

    Row (
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CountCard(
            modifier = Modifier
                .weight(1f),
            headline = "Goal Study Hour",
            count = goalHour
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier
                .weight(1f),
            headline = "Study Hour",
            count = studyHour
        )
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier.size(75.dp),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize(),
                progress = 1f,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize(),
                progress = progress,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
            )
            Text(text = "$progressToPercentage%")

        }
    }
}

