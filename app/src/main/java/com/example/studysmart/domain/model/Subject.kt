package com.example.studysmart.domain.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.studysmart.ui.theme.gradient0
import com.example.studysmart.ui.theme.gradient1
import com.example.studysmart.ui.theme.gradient2
import com.example.studysmart.ui.theme.gradient3
import com.example.studysmart.ui.theme.gradient4

@Entity
data class Subject(
    val name : String,
    val goalHours : Float,
    val colors : List<Int>,
    @PrimaryKey(autoGenerate = true)
    val subjectId : Int? = null
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
