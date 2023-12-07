package com.example.studysmart

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import com.example.studysmart.domain.model.Session
import com.example.studysmart.domain.model.Subject
import com.example.studysmart.domain.model.Task
import com.example.studysmart.ui.NavGraphs
import com.example.studysmart.ui.destinations.SessionScreenRouteDestination
import com.example.studysmart.ui.session.StudySessionTimerService
import com.example.studysmart.ui.theme.StudySmartTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var isBound by mutableStateOf(false)
    private lateinit var timeService : StudySessionTimerService

    private val connection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as StudySessionTimerService.StudySessionTimeBinder
            timeService = binder.getServices()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }

    }

    override fun onStart() {
        super.onStart()
        Intent(this,StudySessionTimerService::class.java).also {intent ->
            bindService(intent,connection, Context.BIND_AUTO_CREATE)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (isBound) {
                StudySmartTheme {
                    // A surface container using the 'background' color from the theme
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        dependenciesContainerBuilder = {
                            dependency(SessionScreenRouteDestination){
                                timeService
                            }
                        }
                    )
                }
            }
        }
        requestPermission()
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
    }
}

val subject = listOf(
    Subject(
        name = "Maths",
        goalHours = 20f,
        colors = Subject.subjectCardColor[0].map { it.toArgb() },
        subjectId = 0
    ),
    Subject(
        name = "Physics",
        goalHours = 5f,
        colors = Subject.subjectCardColor[1].map { it.toArgb() },
        subjectId = 0
    ),
    Subject(
        name = "Chemistry",
        goalHours = 20f,
        colors = Subject.subjectCardColor[2].map { it.toArgb() },
        subjectId = 0
    ),
    Subject(
        name = "Bio",
        goalHours = 60f,
        colors = Subject.subjectCardColor[3].map { it.toArgb() },
        subjectId = 0
    ),
    Subject(
        name = "DSA",
        goalHours = 100f,
        colors = Subject.subjectCardColor[4].map { it.toArgb() },
        subjectId = 0
    )
)

val task = listOf(
    Task(
        title = "playing",
        description = "Playing game",
        dueDate = 1323,
        priority = 1,
        relatedToSubject = "physical education",
        isCompleted = true,
        taskSubjectId = 0,
        taskId = 1
    ),
    Task(
        title = "playing",
        description = "Playing game",
        dueDate = 1323,
        priority = 2,
        relatedToSubject = "physical education",
        isCompleted = true,
        taskSubjectId = 0,
        taskId = 1
    ),
    Task(
        title = "playing",
        description = "Playing game",
        dueDate = 1323,
        priority = 1,
        relatedToSubject = "physical education",
        isCompleted = false,
        taskSubjectId = 0,
        taskId = 1
    ),
    Task(
        title = "playing",
        description = "Playing game",
        dueDate = 1323,
        priority = 1,
        relatedToSubject = "physical education",
        isCompleted = false,
        taskSubjectId = 0,
        taskId = 1
    ),
    Task(
        title = "playing",
        description = "Playing game",
        dueDate = 1323,
        priority = 0,
        relatedToSubject = "physical education",
        isCompleted = true,
        taskSubjectId = 0,
        taskId = 1
    ),
    Task(
        title = "playing",
        description = "Playing game",
        dueDate = 1323,
        priority = 0,
        relatedToSubject = "physical education",
        isCompleted = true,
        taskSubjectId = 0,
        taskId = 1
    ),
)

val sessionLists = listOf(
    Session(
        sessionSubjectId = 1,
        relatedToSubject = "English",
        date = 0L,
        duration = 0L,
        sessionId = 0,
    ),
    Session(
        sessionSubjectId = 1,
        relatedToSubject = "English",
        date = 0L,
        duration = 0L,
        sessionId = 0,
    ),
    Session(
        sessionSubjectId = 1,
        relatedToSubject = "English",
        date = 0L,
        duration = 0L,
        sessionId = 0,
    ),
    Session(
        sessionSubjectId = 1,
        relatedToSubject = "English",
        date = 0L,
        duration = 0L,
        sessionId = 0,
    ),
    Session(
        sessionSubjectId = 1,
        relatedToSubject = "English",
        date = 0L,
        duration = 0L,
        sessionId = 0,
    )

)