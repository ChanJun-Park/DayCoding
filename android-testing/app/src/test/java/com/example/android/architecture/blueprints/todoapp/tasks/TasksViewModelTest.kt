package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTasksRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TasksViewModelTest {

	private lateinit var tasksRepository: FakeTasksRepository

	private lateinit var tasksViewModel: TasksViewModel

	@get:Rule
	var instantExecutorRule = InstantTaskExecutorRule()

	@Before
	fun setupViewModel() {
		// Given a fresh ViewModel
		tasksRepository = FakeTasksRepository()
		val task1 = Task("Title1", "Description1")
		val task2 = Task("Title2", "Description2")
		val task3 = Task("Title3", "Description3")
		tasksRepository.addTasks(task1, task2, task3)

		tasksViewModel = TasksViewModel(tasksRepository)
	}

	@Test
	fun addNewTask_setsNewTaskEvent() {

		// When adding a new task
		tasksViewModel.addNewTask()

		// Then the new task event is triggered
		assertThat(tasksViewModel.newTaskEvent.getOrAwaitValue(), not(nullValue()))
	}

	@Test
	fun setFilterAllTasks_tasksAddViewVisible() {

		// When the filter type is ALL_TASKS
		tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)

		// Then the "Add task" action is visible
		assertThat(tasksViewModel.tasksAddViewVisible.getOrAwaitValue(), `is`(true))
	}
}