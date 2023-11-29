package com.example.studysmart.ui.dashboard

import androidx.compose.ui.graphics.Color
import com.example.studysmart.domain.model.Session
import com.example.studysmart.domain.model.Task


sealed class DashBoardEvents{
    data object SaveSubject : DashBoardEvents()

    data object DeleteSession : DashBoardEvents()

    data class OnDeleteSessionButtonClick(val session: Session) : DashBoardEvents()

    data class OnTaskIsCompleteChange(val task: Task) : DashBoardEvents()

    data class OnSubjectCardColorChange(val colors : List<Color>) : DashBoardEvents()

    data class OnSubjectNameChange(val name : String) : DashBoardEvents()

    data class OnGoalStudyHoursChange(val hours : String) : DashBoardEvents()

}
