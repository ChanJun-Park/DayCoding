package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {

	private lateinit var tasksViewModel: TasksViewModel

	@get:Rule
	var instantExecutorRule = InstantTaskExecutorRule()

	@Before
	fun setupViewModel() {
		// Given a fresh ViewModel
		tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
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