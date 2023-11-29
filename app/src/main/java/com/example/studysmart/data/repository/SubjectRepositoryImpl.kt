package com.example.studysmart.data.repository

import com.example.studysmart.data.local.SubjectDao
import com.example.studysmart.domain.model.Subject
import com.example.studysmart.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubjectRepositoryImpl @Inject constructor(
    private val subjectDoa : SubjectDao
) : SubjectRepository {
    override suspend fun upsertSubject(subject: Subject) {
        subjectDoa.upsertSubject(subject)
    }

    override fun getTotalSubjectCount(): Flow<Int> {
        return subjectDoa.getTotalSubjectCount()
    }

    override fun getTotalGoalHour(): Flow<Float> {
        return subjectDoa.getTotalGoalHour()
    }

    override suspend fun deleteSubject(subjectInt: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getSubjectById(subjectInt: Int): Subject? {
        TODO("Not yet implemented")
    }

    override fun getAllSubject(): Flow<List<Subject>> {
        return subjectDoa.getAllSubjects()
    }
}