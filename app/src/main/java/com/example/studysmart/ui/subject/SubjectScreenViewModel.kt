package com.example.studysmart.ui.subject

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studysmart.domain.model.Subject
import com.example.studysmart.domain.repository.SessionRepository
import com.example.studysmart.domain.repository.SubjectRepository
import com.example.studysmart.domain.repository.TaskRepository
import com.example.studysmart.ui.navArgs
import com.example.studysmart.util.SnackBarEvent
import com.example.studysmart.util.toHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SubjectScreenViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val taskRepository: TaskRepository,
    private val sessionRepository : SessionRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val navArgs : SubjectScreenNavArgs = savedStateHandle.navArgs()



    private val _state = MutableStateFlow(SubjectState())
    //update the state as SubjectState()
    val state = combine(
        _state,
        taskRepository.getUpcomingTasksForSubject(navArgs.subjectId),
        taskRepository.getCompletedTaskForSubject(navArgs.subjectId),
        sessionRepository.getRecentTenSessionForSubject(navArgs.subjectId),
        sessionRepository.getTotalSessionDurationBySubject(navArgs.subjectId),
    ){state , upComingTask, completedTask , recentSession , totalSessionDuration->
        state.copy(
            upcomingTask = upComingTask,
            completedTask = completedTask,
            recentSession = recentSession,
            studiedHours = totalSessionDuration.toHours()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = SubjectState()
    )

    private val _snackBarEventFlow : MutableSharedFlow<SnackBarEvent> = MutableSharedFlow()
    val snackBarEventFlow = _snackBarEventFlow.asSharedFlow()

    init {
        fetchSubject()
    }
    fun onEvent(event : SubjectEvent){
        when(event){
            is SubjectEvent.OnSubjectCardColorChange -> {
                _state.update {subjectState ->
                    subjectState.copy(
                        subjectCardColor = event.color
                    )
                }
            }
            is SubjectEvent.OnSubjectNameChange -> {
                _state.update { subjectState ->
                    subjectState.copy(
                        subjectName = event.name
                    )
                }
            }
            is SubjectEvent.OnGoalStudyHourChange -> {
                _state.update {subjectState ->
                    subjectState.copy(
                        goalStudyHour = event.hours
                    )
                }
            }
            SubjectEvent.UpdateSubject -> updateSubject()
            SubjectEvent.UpdateProgress -> {
                val goalStudyHours = state.value.goalStudyHour.toFloatOrNull() ?: 1f
                _state.update {subjectState->
                    subjectState.copy(
                        progress = (state.value.studiedHours / goalStudyHours).coerceIn(0f,1f)
                    )
                }
            }
            SubjectEvent.DeleteSubject -> deleteSubject()
            SubjectEvent.DeleteSession -> TODO()
            is SubjectEvent.OnDeleteSessionButtonClick -> TODO()
            is SubjectEvent.OnTaskCompleteChange -> TODO()
        }
    }

    private fun updateSubject() {
        viewModelScope.launch {
            try {
                subjectRepository.upsertSubject(
                    subject = Subject(
                        subjectId = state.value.currentSubjectId,
                        name = state.value.subjectName,
                        goalHours = state.value.goalStudyHour.toFloatOrNull() ?: 1f,
                        colors = state.value.subjectCardColor.map { it.toArgb() }
                    )
                )
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "SuccessFully Updated"
                    )
                )
            }
            catch (e : Exception){
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Couldn't update ${e.message}",
                        SnackbarDuration.Long

                    )
                )
            }
        }
    }

    private fun fetchSubject(){
        viewModelScope.launch {
            subjectRepository.getSubjectById(navArgs.subjectId)?.let { subject->
                _state.update { subjectState ->
                    subjectState.copy(
                        subjectName = subject.name,
                        goalStudyHour = subject.goalHours.toString(),
                        subjectCardColor = subject.colors.map { Color(it) },
                        currentSubjectId = subject.subjectId
                    )
                }
            }
        }
    }


    private fun deleteSubject(){
        viewModelScope.launch {
            try {
                val currentSubjectId = state.value.currentSubjectId
                if (currentSubjectId != null) {
                    withContext(Dispatchers.IO){
                        subjectRepository.deleteSubject(subjectId = currentSubjectId)
                    }
                    _snackBarEventFlow.emit(
                        SnackBarEvent.ShowSnackBar(
                            message = "Successfully Deleted Subject"
                        )
                    )
                    _snackBarEventFlow.emit(SnackBarEvent.NavigateUp)
                }
                else{
                    _snackBarEventFlow.emit(
                        SnackBarEvent.ShowSnackBar(
                            message = "No Subject To Delete"
                        )
                    )
                }
            }
            catch (e : Exception){
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Couldn't delete subject {${e.message}}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }


}