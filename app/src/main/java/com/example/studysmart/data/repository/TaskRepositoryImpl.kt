package com.example.studysmart.data.repository

import com.example.studysmart.data.local.TaskDao
import com.example.studysmart.domain.model.Task
import com.example.studysmart.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDoa : TaskDao
) : TaskRepository {
    override suspend fun upsertTask(task: Task) {
        taskDoa.upsertTask(task)
    }

    override suspend fun deleteTask(taskId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskById(taskId: Int): Task? {
        TODO("Not yet implemented")
    }

    override fun getUpcomingTasksForSubject(subjectInt: Int): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getCompletedTaskForSubject(subjectInt: Int): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getAllUpcomingTask(): Flow<List<Task>> {
        return taskDoa.getAllTask()
            .map { tasks ->
                tasks.filter {
                    it.isCompleted.not()
                }
            }
            .map {tasks->
                sortTask(tasks)
            }
    }

    private fun sortTask(tasks : List<Task>) : List<Task>{
        return tasks.sortedWith(compareBy<Task> {it.dueDate}.thenByDescending { it.priority })
    }

}