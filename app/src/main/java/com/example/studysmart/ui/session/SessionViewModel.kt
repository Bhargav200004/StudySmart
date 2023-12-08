package com.example.studysmart.ui.session

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studysmart.domain.model.Session
import com.example.studysmart.domain.repository.SessionRepository
import com.example.studysmart.domain.repository.SubjectRepository
import com.example.studysmart.util.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject


@HiltViewModel
class SessionViewModel @Inject constructor(
    subjectRepository: SubjectRepository,
    private val sessionRepository: SessionRepository
): ViewModel() {

    private val _state = MutableStateFlow(SessionStates())
    val state = combine(
        _state,
        subjectRepository.getAllSubject(),
        sessionRepository.getAllSession()
    ){state , subjects, session->
        state.copy(
            subjects = subjects,
            sessions = session
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = SessionStates()
    )

    private val _snackBarEventFlow = MutableSharedFlow<SnackBarEvent>()
    val snackBarEventFlow  = _snackBarEventFlow.asSharedFlow()

    fun onEvent(event : SessionEvents){
        when (event){
            SessionEvents.CheckSubject -> TODO()
            SessionEvents.DeleteSession -> deleteSession()
            is SessionEvents.OnDeleteButtonClick -> {
                _state.update {
                    it.copy(session = event.session)
                }
            }
            is SessionEvents.OnRelatedSubjectChange -> {
                _state.update {
                    it.copy(
                        relatedToSubject = event.subject.name,
                        subjectId = event.subject.subjectId
                    )
                }
            }
            is SessionEvents.SaveSession -> insertSession(event.duration)
            is SessionEvents.UpdateSubjectIdAndRelatedSubject -> TODO()
        }
    }

    private fun deleteSession(){
        viewModelScope.launch {
            try {
                state.value.session?.let { session ->
                    sessionRepository.deleteSession(session = session)
                    _snackBarEventFlow.emit(
                        SnackBarEvent.ShowSnackBar(message = "session delete successfully")
                    )
                }
            }
            catch (e : Exception){
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(message = "couldn't delete session", duration = SnackbarDuration.Long),
                )
            }
        }
    }

    private fun insertSession(duration: Long) {
        viewModelScope.launch {
            if (duration < 36){
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Single session cannot be less than 36 second"
                    )
                )
                return@launch
            }
            try {
                sessionRepository.insertSession(
                    session = Session(
                        sessionSubjectId = state.value.subjectId ?: -1,
                        relatedToSubject = state.value.relatedToSubject ?: "",
                        date = Instant.now().toEpochMilli(),
                        duration = duration

                    )
                )
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Session Save Successfully"
                    )
                )
            }
            catch (e :Exception){
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Couldn't save session ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }
}