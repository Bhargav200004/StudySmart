package com.example.studysmart.ui.session

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.studysmart.sessionLists
import com.example.studysmart.subject
import com.example.studysmart.ui.components.DeleteDialog
import com.example.studysmart.ui.components.SubjectListBottomSheet
import com.example.studysmart.ui.components.studySessionList
import com.example.studysmart.util.Constants.ACTION_SERVICE_CANCEL
import com.example.studysmart.util.Constants.ACTION_SERVICE_START
import com.example.studysmart.util.Constants.ACTION_SERVICE_STOP
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination(deepLinks =[
    DeepLink(
        action = Intent.ACTION_VIEW,
        uriPattern = "study_smart://dashboard/session"
    )
] )
@Composable
fun SessionScreenRoute(
    navigator: DestinationsNavigator,
    timerService: StudySessionTimerService

) {

    val viewModel : SessionViewModel = hiltViewModel()

    SessionScreen(
        onBackButtonClick = {
            navigator.navigateUp()
        },
        timerService = timerService
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionScreen(
    onBackButtonClick : () -> Unit,
    timerService: StudySessionTimerService
) {


    val hours by timerService.hours
    val minute by timerService.minutes
    val seconds by timerService.seconds
    val currentTimeState by timerService.currentTimerState



    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var isDismissSubjectListBottomSheet by rememberSaveable { mutableStateOf(false) }

    var isDeleteDialog by rememberSaveable { mutableStateOf(false) }

    SubjectListBottomSheet(
        sheetState = sheetState,
        isOpen = isDismissSubjectListBottomSheet,
        subjects = subject,
        onSubjectClick = { isDismissSubjectListBottomSheet = false },
        onDismissRequest = {
            scope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                if (!sheetState.isVisible) isDismissSubjectListBottomSheet = false
            }
        })

    DeleteDialog(
        isOpen = isDeleteDialog,
        title = "Delete Session?",
        bodyText = "Are you sure you want to delete session?"
                + "This action cannot be undone",
        onDismissRequest = { isDeleteDialog = false },
        onSaveRequest = { isDeleteDialog = false }
    )


    Scaffold(
        topBar = {
            SessionScreenTopBar(
                onClickBack = onBackButtonClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentPadding = paddingValues
        ) {
            item {
                TimerSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    hours = hours,
                    minutes = minute,
                    seconds = seconds
                )
            }
            item {
                RelatedToStudySession(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    relatedToSubject = "Maths",
                    selectSubjectButtonClick = { isDismissSubjectListBottomSheet = true }
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    ButtonSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        cancelButtonClick = {
                            ServiceHelper.triggerForeGroundServiceI(
                                context = context,
                                action = ACTION_SERVICE_CANCEL
                            ) },
                        startButtonClick = {
                            ServiceHelper.triggerForeGroundServiceI(
                                context = context,
                                action = if (currentTimeState == TimerState.STARTED) {
                                    ACTION_SERVICE_STOP
                                }
                                else {
                                    ACTION_SERVICE_START
                                }
                            )
                        },
                        finishButtonClick = {
                            ServiceHelper.triggerForeGroundServiceI(
                                context = context,
                                action = ACTION_SERVICE_STOP
                            )
                        },
                        timerState = currentTimeState,
                        seconds = seconds
                    )
                }
            }

            studySessionList(
                sectionHeading = "STUDY SESSION HISTORY",
                emptyText = "You don't have resent study Session\n" +
                        "Start a study session to begin the recording",
                sessions = sessionLists,
                onDeleteClick = { isDeleteDialog = true }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionScreenTopBar(
    onClickBack: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onClickBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Navigation"
                )
            }
        },
        title = {
            Text(
                text = "Session",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    )
}


@Composable
private fun TimerSection(
    modifier: Modifier,
    hours : String,
    minutes : String,
    seconds : String
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .border(
                    5.dp,
                    MaterialTheme.colorScheme.surfaceVariant,
                    CircleShape
                )
        )
        Row {
            AnimatedContent(
                targetState = hours,
                label = hours
            ) { hours ->
                Text(
                    text = "$hours:",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
                )
            }
            AnimatedContent(
                targetState = minutes,
                label = minutes
            ) { minutes ->
                Text(
                    text = "$minutes:",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
                )
            }
            AnimatedContent(
                targetState = seconds,
                label = seconds
            ) { seconds ->
                Text(
                    text = seconds,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
                )
            }
        }

    }
}

@Composable
private fun RelatedToStudySession(
    modifier: Modifier,
    relatedToSubject: String,
    selectSubjectButtonClick: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
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
                text = relatedToSubject,
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(onClick = selectSubjectButtonClick) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Arrow Drop down"
                )
            }
        }
    }
}

@Composable
private fun ButtonSection(
    modifier: Modifier,
    startButtonClick: () -> Unit,
    cancelButtonClick: () -> Unit,
    finishButtonClick: () -> Unit,
    timerState: TimerState,
    seconds: String

) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
            Button(
                onClick = startButtonClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor =  if (timerState==TimerState.STARTED) Color.Red
                    else MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    text = when(timerState){
                        TimerState.STOPPED -> "Resume"
                        TimerState.STARTED -> "Stop"
                        else -> "Start"
                    }
                )
            }
            Button(
                onClick = cancelButtonClick,
                enabled = seconds !="00" && timerState != TimerState.STARTED
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    text = "Cancel"
                )
            }
            Button(
                onClick = finishButtonClick,
                enabled = seconds != "00" && timerState != TimerState.STARTED
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    text = "finish"
                )
            }
    }
}
