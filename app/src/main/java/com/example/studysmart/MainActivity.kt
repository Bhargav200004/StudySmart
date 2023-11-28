package com.example.studysmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.studysmart.domain.model.Session
import com.example.studysmart.domain.model.Subject
import com.example.studysmart.domain.model.Task
import com.example.studysmart.ui.NavGraphs
import com.example.studysmart.ui.theme.StudySmartTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudySmartTheme {
                // A surface container using the 'background' color from the theme
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}

val subject = listOf(
    Subject(
        name = "Maths",
        goalHours = "20",
        colors = Subject.subjectCardColor[0],
        subjectId = 0
    ),
    Subject(
        name = "Physics",
        goalHours = "5",
        colors = Subject.subjectCardColor[1],
        subjectId = 0
    ),
    Subject(
        name = "Chemistry",
        goalHours = "20",
        colors = Subject.subjectCardColor[2],
        subjectId = 0
    ),
    Subject(
        name = "Bio",
        goalHours = "60",
        colors = Subject.subjectCardColor[3],
        subjectId = 0
    ),
    Subject(
        name = "DSA",
        goalHours = "100",
        colors = Subject.subjectCardColor[4],
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