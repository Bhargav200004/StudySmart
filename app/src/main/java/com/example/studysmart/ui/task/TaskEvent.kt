package com.example.studysmart.ui.task

import com.example.studysmart.domain.model.Subject
import com.example.studysmart.util.Priority

sealed class TaskEvent{
    data class OnTitleChange(val title : String) : TaskEvent()
    data class OnDescriptionChange(val description : String) : TaskEvent()
    data class OnDateChange(val date : Long?) : TaskEvent()
    data class OnPriorityChange(val priority: Priority) : TaskEvent()
    data class OnRelatedSubjectSelected(val subject: Subject) : TaskEvent()
    data object IsCompleteEvent : TaskEvent()
    data object SaveTask : TaskEvent()
    data object DeleteTask : TaskEvent()

}
