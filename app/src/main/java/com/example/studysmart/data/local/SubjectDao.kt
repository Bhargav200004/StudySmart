package com.example.studysmart.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.studysmart.domain.model.Subject
import kotlinx.coroutines.flow.Flow


//Store all function of the for database
@Dao
interface SubjectDao {

    @Upsert
    suspend fun upsertSubject(subject: Subject)/*It is use for both INSERT and UPDATE the subject*/

    @Query("SELECT COUNT(*) FROM SUBJECT")
    fun getTotalSubjectCount() : Flow<Int>

    @Query("SELECT SUM(goalHours) FROM SUBJECT")
    fun getTotalGoalHour() : Flow<Float>

    @Query("SELECT * FROM SUBJECT WHERE subjectId =:subjectId")
    suspend fun getSubjectById(subjectId : Int) : Subject?

    @Query("DELETE FROM SUBJECT WHERE subjectId =:subjectId")
    suspend fun deleteSubject(subjectId: Int)

    @Query("SELECT * FROM SUBJECT")
    fun getAllSubjects() : Flow<List<Subject>>

}