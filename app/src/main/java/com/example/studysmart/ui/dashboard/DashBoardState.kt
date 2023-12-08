package com.example.studysmart.ui.dashboard

import androidx.compose.ui.graphics.Color
import com.example.studysmart.domain.model.Session
import com.example.studysmart.domain.model.Subject

data class DashBoardState(
    val totalSubjectCount: Int = 0,
    val totalStudiedHour: Float = 0f,
    val totalGoalStudyHour: Float = 0f,
    val subjects: List<Subject> = emptyList(),
    val subjectName: String = "",
    val goalStudyHour: String = "",
    val subjectCardColors: List<Color> = Subject.subjectCardColor.random(),
    val session: Session? = null
)
