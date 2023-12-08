package com.example.studysmart.data.repository


import com.example.studysmart.data.local.SessionDao
import com.example.studysmart.domain.model.Session
import com.example.studysmart.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionDoa  : SessionDao
) : SessionRepository {
    override suspend fun insertSession(session: Session) {
        sessionDoa.insertSession(session)
    }

    override suspend fun deleteSession(session: Session) {
        sessionDoa.deleteSession(session)
    }

    override fun getAllSession(): Flow<List<Session>> {
       return sessionDoa.getAllSessions().map {session ->
           session.sortedByDescending {it.date  }
       }
    }

    override fun getRecentFiveSession(): Flow<List<Session>> {
        return sessionDoa.getAllSessions()
            .map { session ->
                session.sortedByDescending { it.date }
            }.take(count = 5)
    }

    override fun getRecentTenSessionForSubject(subjectId: Int): Flow<List<Session>> {
        return sessionDoa.getRecentSessionsForSubject(subjectId)
            .map {session ->
                session.sortedByDescending {it.date  }
            }
            .take(count = 10)
    }

    override fun getTotalSessionDuration(): Flow<Long> {
        return sessionDoa.getTotalSessionsDuration()
    }

    override fun getTotalSessionDurationBySubject(subjectId: Int): Flow<Long> {
        return sessionDoa.getTotalSessionsDurationBySubject(subjectId)
    }

}