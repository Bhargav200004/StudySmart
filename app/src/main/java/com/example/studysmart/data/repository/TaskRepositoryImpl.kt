package com.example.studysmart.data.repository

import com.example.studysmart.data.local.TaskDao
import com.example.studysmart.domain.model.Task
import com.example.studysmart.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDoa: TaskDao
) : TaskRepository {
    override suspend fun upsertTask(task: Task) {
        taskDoa.upsertTask(task)
    }

    override suspend fun deleteTask(taskId: Int) {
        taskDoa.deleteTask(taskId)
    }

    override suspend fun getTaskById(taskId: Int): Task? {
        return taskDoa.getTaskById(taskId)
    }

    override fun getUpcomingTasksForSubject(subjectInt: Int): Flow<List<Task>> {
        return taskDoa.getTasksForSubject(subjectInt)
            .map { tasks ->
                tasks.filter {
                    it.isCompleted.not()
                }
            }
            .map { tasks ->
                sortTask(tasks)
            }
    }

    override fun getCompletedTaskForSubject(subjectInt: Int): Flow<List<Task>> {
        return taskDoa.getTasksForSubject(subjectInt)
            .map { tasks ->
                tasks.filter {
                    it.isCompleted
                }
            }
            .map { tasks ->
                sortTask(tasks)
            }
    }

    override fun getAllUpcomingTask(): Flow<List<Task>> {
        return taskDoa.getAllTask()
            .map { tasks ->
                tasks.filter {
                    it.isCompleted.not()
                }
            }
            .map { tasks ->
                sortTask(tasks)
            }
    }

    private fun sortTask(tasks: List<Task>): List<Task> {
        return tasks.sortedWith(compareBy<Task> { it.dueDate }.thenByDescending { it.priority })
    }

}