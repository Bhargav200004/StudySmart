package com.example.studysmart.ui.session

import com.example.studysmart.domain.model.Session
import com.example.studysmart.domain.model.Subject

data class SessionStates(
    val subjects: List<Subject> = emptyList(),
    val sessions: List<Session> = emptyList(),
    val relatedToSubject: String? = null,
    val subjectId: Int? = null,
    val session: Session? = null
)
