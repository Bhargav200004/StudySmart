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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studysmart.ui.components.AddSubjectDialog
import com.example.studysmart.ui.components.CountCard
import com.example.studysmart.ui.components.DeleteDialog
import com.example.studysmart.ui.components.studySessionList
import com.example.studysmart.ui.components.taskList
import com.example.studysmart.ui.destinations.TaskScreenRouteDestination
import com.example.studysmart.ui.task.TaskScreenNavArgs
import com.example.studysmart.util.SnackBarEvent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

data class SubjectScreenNavArgs(
    val subjectId : Int
)


@Destination(navArgsDelegate = SubjectScreenNavArgs::class )
@Composable
fun SubjectScreenRoute(
    navigator : DestinationsNavigator
) {

    val viewModel : SubjectScreenViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    SubjectScreen(
        state = state,
        onEvent = viewModel::onEvent,
        snackBarEvent = viewModel.snackBarEventFlow,
        onBackButtonClick = {
            navigator.navigateUp()
        },
        onAddTaskButtonClick = {
            val navArgs = TaskScreenNavArgs(taskId = null, subjectId = -1)
            navigator.navigate(TaskScreenRouteDestination(navArgs)) },
        onTaskCardClick = {taskId ->
            val navArgs = TaskScreenNavArgs(taskId = taskId, subjectId = null)
            navigator.navigate(TaskScreenRouteDestination(navArgs))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectScreen(
    state : SubjectState,
    onEvent : (SubjectEvent) -> Unit,
    snackBarEvent : SharedFlow<SnackBarEvent>,
    onBackButtonClick : () -> Unit,
    onAddTaskButtonClick : () -> Unit,
    onTaskCardClick : (Int?) -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState()
    val isFABExpanded by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }


    var isEditSubjectButton by rememberSaveable { mutableStateOf(false) }
    var isDeleteButton by rememberSaveable { mutableStateOf(false) }
    var isDeleteSubjectButton by rememberSaveable { mutableStateOf(false) }

    val snackBarHost = remember { SnackbarHostState()}


    AddSubjectDialog(
        isOpen = isEditSubjectButton,
        subjectName = state.subjectName,
        goalHour = state.goalStudyHour,
        selectedColor = state.subjectCardColor,
        onSubjectChange = {  onEvent(SubjectEvent.OnSubjectNameChange(it)) },
        onGoalHourChange = { onEvent(SubjectEvent.OnGoalStudyHourChange(it)) },
        onColorChange = { onEvent(SubjectEvent.OnSubjectCardColorChange(it)) },
        onDismissRequest = { isEditSubjectButton = false },
        onSaveRequest = {
            onEvent(SubjectEvent.UpdateSubject)
            isEditSubjectButton = false
        }
    )

    DeleteDialog(
        isOpen = isDeleteSubjectButton,
        title = "Delete Subject",
        bodyText = "Are you sure, you want to delete Subject All related task" +
                " and study session will be permanently remove , this action can not be undone. ",
        onDismissRequest = { isDeleteSubjectButton = false },
        onSaveRequest = {
            onEvent(SubjectEvent.DeleteSubject)
            isDeleteSubjectButton = false
        }
    )


    DeleteDialog(
        isOpen = isDeleteButton,
        title = "Delete Session",
        bodyText = "Are you sure, you want to delete session? Your study hour will reduce " +
                "by this session time . this action cannot be undo",
        onDismissRequest = { isDeleteButton = false },
        onSaveRequest = {
            onEvent(SubjectEvent.DeleteSession)
            isDeleteButton = false
        }
    )

    LaunchedEffect(key1 = true){
        snackBarEvent.collectLatest {event->
            when(event){
                is SnackBarEvent.ShowSnackBar ->{
                    snackBarHost.showSnackbar(
                        message = event.message,
                        duration = event.duration
                    )
                }
            }

        }
    }

    Scaffold(
        snackbarHost = {SnackbarHost(hostState = snackBarHost)},
        topBar = {
            SubjectScreenTopBar(
                title = state.subjectName,
                onBackButtonClick = onBackButtonClick,
                onDeleteButtonClick = { isDeleteSubjectButton = true},
                onEditButtonClick = { isEditSubjectButton = true},
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton ={
            ExtendedFloatingActionButton(
                onClick = onAddTaskButtonClick,
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
                    goalHour = state.goalStudyHour,
                    studyHour =state.studiedHours.toString(),
                    progress = state.progress
                )
            }
            taskList(
                sectionHeading = "Upcoming Task",
                emptyText = "You don't have upcoming task\n" +
                        "Click + in Subject task to add new task",
                tasks = state.upcomingTask,
                onCheckBoxClick = { onEvent(SubjectEvent.OnTaskCompleteChange(it))},
                onTaskCardClick = onTaskCardClick
            )
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
            taskList(
                sectionHeading = "Completed Task",
                emptyText = "You don't have complete task\n" +
                        "Click the check box on completion of task",
                tasks = state.completedTask,
                onCheckBoxClick = { onEvent(SubjectEvent.OnTaskCompleteChange(it))},
                onTaskCardClick = onTaskCardClick
            )
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
            studySessionList(
                sectionHeading = "Resent Study session",
                emptyText = "You don't have resent study Session\n" +
                        "Start a study session to begin the recording",
                sessions = state.recentSession,
                onDeleteClick = {
                    isDeleteButton = true
                    onEvent(SubjectEvent.OnDeleteSessionButtonClick(it))
                }
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

