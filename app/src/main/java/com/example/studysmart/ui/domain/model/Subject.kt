package com.example.studysmart.ui.domain.model

import androidx.compose.ui.graphics.Color
import com.example.studysmart.ui.theme.gradient0
import com.example.studysmart.ui.theme.gradient1
import com.example.studysmart.ui.theme.gradient2
import com.example.studysmart.ui.theme.gradient3
import com.example.studysmart.ui.theme.gradient4

data class Subject(
    val name : String,
    val goalHours : String,
    val colors : List<Color>
){
    companion object{
        val subjectCardColor = listOf(
            gradient0,
            gradient1,
            gradient2,
            gradient3,
            gradient4
        )
    }

}
