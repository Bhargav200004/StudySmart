package com.example.studysmart.domain.model

data class StudySession(
    val sessionSubjectId : Int,
    val relatedToSubject : String ,
    val date : Long,
    val duration : Long,
    val sessionId : Int,

)
