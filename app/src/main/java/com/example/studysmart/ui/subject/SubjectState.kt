package com.example.studysmart.ui.subject

import androidx.compose.ui.graphics.Color
import com.example.studysmart.domain.model.Session
import com.example.studysmart.domain.model.Subject
import com.example.studysmart.domain.model.Task

data class SubjectState(
    val currentSubjectId: Int? = null,
    val subjectName: String = "",
    val goalStudyHour: String = "",
    val studiedHours: Float = 0f,
    val subjectCardColor: List<Color> = Subject.subjectCardColor.random(),
    val recentSession: List<Session> = emptyList(),
    val upcomingTask: List<Task> = emptyList(),
    val completedTask: List<Task> = emptyList(),
    val session: Session? = null,
    val progress: Float = 0f,
)
