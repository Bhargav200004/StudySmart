package com.example.studysmart.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.studysmart.domain.model.Subject
import com.example.studysmart.domain.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Upsert
    suspend fun upsertTask(task: Task)

    @Query("DELETE FROM TASK WHERE taskId =:taskId")
    suspend fun deleteTask(taskId : Int)

    @Query("DELETE FROM TASK WHERE taskSubjectId =:subjectId")
    suspend fun deleteTaskBySubjectId(subjectId : Int)

    @Query("SELECT * FROM TASK WHERE taskId =:taskId")
    suspend fun getTaskById(taskId : Int): Task?

    @Query("SELECT * FROM TASK WHERE taskSubjectId=:subjectId")
    fun getTasksForSubject(subjectId: Int) : Flow<List<Task>>

    @Query("SELECT * FROM TASK")
    fun getAllTask() : Flow<List<Task>>

}