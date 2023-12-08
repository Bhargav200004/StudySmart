package com.example.studysmart.ui.session

import com.example.studysmart.domain.model.Session
import com.example.studysmart.domain.model.Subject

sealed class SessionEvents {

    data class OnRelatedSubjectChange(val subject: Subject) : SessionEvents()
    data class SaveSession(val duration : Long) : SessionEvents()
    data class  OnDeleteButtonClick(val session: Session): SessionEvents()
    data object DeleteSession : SessionEvents()
    data object NotifyToUpdateSubject : SessionEvents()
    data class UpdateSubjectIdAndRelatedSubject(
        val subjectId : Int? ,
        val relatedToSubject : String?
    ) : SessionEvents()
}
