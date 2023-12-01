package com.example.studysmart.domain.repository

import com.example.studysmart.domain.model.Subject
import kotlinx.coroutines.flow.Flow

interface SubjectRepository {

    suspend fun upsertSubject(subject: Subject)

     fun getTotalSubjectCount():Flow<Int>

     fun getTotalGoalHour () : Flow<Float>

     suspend fun deleteSubject(subjectId : Int)

     suspend fun getSubjectById(subjectId : Int) : Subject?

     fun getAllSubject() : Flow<List<Subject>>
}