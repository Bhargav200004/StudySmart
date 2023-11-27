package com.example.studysmart.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.studysmart.R
import com.example.studysmart.domain.model.Subject
import com.example.studysmart.studySessionList
import com.example.studysmart.subject
import com.example.studysmart.task
import com.example.studysmart.ui.components.AddSubjectDialog
import com.example.studysmart.ui.components.CountCard
import com.example.studysmart.ui.components.DeleteDialog
import com.example.studysmart.ui.components.SubjectCard
import com.example.studysmart.ui.components.studySessionList
import com.example.studysmart.ui.components.taskList
import com.example.studysmart.ui.destinations.SessionScreenRouteDestination
import com.example.studysmart.ui.destinations.SubjectScreenRouteDestination
import com.example.studysmart.ui.destinations.TaskScreenRouteDestination
import com.example.studysmart.ui.subject.SubjectScreenNavArgs
import com.example.studysmart.ui.task.TaskScreenNavArgs
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination(start = true)
@Composable
fun DashBoardScreenRoute(
    navigator : DestinationsNavigator
) {
    DashboardScreen(
        onSubjectCardClick ={subjectId->
            subjectId?.let {
                val navArg = SubjectScreenNavArgs(subjectId = subjectId)
                navigator.navigate(SubjectScreenRouteDestination(navArg))
            }
        },
        onTaskCardClick ={taskId ->
            val navArg = TaskScreenNavArgs(taskId = taskId,subjectId = null)
            navigator.navigate(TaskScreenRouteDestination(navArg))
        },
        onStartSessionButtonClick ={
            navigator.navigate(SessionScreenRouteDestination())
        }
    )
}

@Composable
private fun DashboardScreen(
    onSubjectCardClick : (Int?) -> Unit,
    onTaskCardClick : (Int?) -> Unit,
    onStartSessionButtonClick : () -> Unit
) {

    var isAddSubjectButton by rememberSaveable { mutableStateOf(false) }
    var isDeleteButton by rememberSaveable { mutableStateOf(false) }

    var subjectName by rememberSaveable { mutableStateOf("") }
    var goalHour by rememberSaveable { mutableStateOf("") }
    var selectedColor by rememberSaveable { mutableStateOf(Subject.subjectCardColor.random()) }

    AddSubjectDialog(
        isOpen = isAddSubjectButton,
        subjectName = subjectName,
        goalHour = goalHour,
        selectedColor = selectedColor,
        onSubjectChange = { subjectName = it },
        onGoalHourChange = { goalHour = it },
        onColorChange = { selectedColor = it },
        onDismissRequest = { isAddSubjectButton = false },
        onSaveRequest = { isAddSubjectButton = false }
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
        topBar = { DashboardScreenTopBar() }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                CountCardsSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    subjectCount = 5,
                    studiedHourCount = "20",
                    goalStudyHourCount = "40"
                )
            }
            item {
                SubjectCardSection(
                    modifier = Modifier.fillMaxWidth(),
                    subjectList = subject,
                    isIconButtonClick = { isAddSubjectButton = true },
                    onSubjectCardClick = onSubjectCardClick
                )
            }
            item {
                Button(
                    onClick =  onStartSessionButtonClick ,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp, vertical = 20.dp)
                ) {
                    Text(text = "Start Study Session")
                }
            }

            taskList(
                sectionHeading = "Upcoming Task",
                emptyText = "You don't have upcoming task\n" +
                        "Click + in Subject task to add new task",
                tasks = task,
                onCheckBoxClick = {},
                onTaskCardClick = onTaskCardClick
            )
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
            studySessionList(
                sectionHeading = "Resent Study session",
                emptyText = "You don't have resent study Session\n" +
                        "Start a study session to begin the recording",
                sessions = studySessionList
            ) { isDeleteButton = true }

        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreenTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Study Smart",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    )
}

@Composable
private fun CountCardsSection(
    modifier: Modifier = Modifier,
    subjectCount: Int = 0,
    studiedHourCount: String = "0",
    goalStudyHourCount: String = "0"
) {
    Row(modifier = modifier) {
        CountCard(
            modifier = Modifier
                .weight(1f),
            headline = "Subject Count",
            count = "$subjectCount"
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier
                .weight(1f),
            headline = "Studied Hours",
            count = studiedHourCount
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier
                .weight(1f),
            headline = "Goal study Hour",
            count = goalStudyHourCount
        )
    }
}


@Composable
private fun SubjectCardSection(
    modifier: Modifier,
    subjectList: List<Subject>,
    emptyText: String = "Your Subject is empty. \n Press + to add the Subject",
    isIconButtonClick: () -> Unit,
    onSubjectCardClick : (Int?) -> Unit,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "SUBJECT",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(start = 12.dp)
            )
            IconButton(onClick = isIconButtonClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Subject"
                )
            }
        }
        if (subjectList.isEmpty()) {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.img_books),
                contentDescription = emptyText
            )
            Text(
                text = emptyText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ) {
            items(subjectList) { subject ->
                SubjectCard(
                    subjectName = subject.name,
                    gradientColor = subject.colors,
                    onClick = {onSubjectCardClick(subject.subjectId)}
                )
            }
        }

    }
}


