package com.example.android.architecture.blueprints.todoapp.tasks

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.ServiceLocator
import com.example.android.architecture.blueprints.todoapp.TodoApplication
import com.example.android.architecture.blueprints.todoapp.addedittask.AddEditTaskFragment
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeAndroidTasksRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class TasksFragmentTest {

	private lateinit var repository: TasksRepository

	@Before
	fun initRepository() {
		repository = FakeAndroidTasksRepository()
		ServiceLocator.tasksRepository = repository
	}

	@After
	fun cleanupDb() = runBlockingTest {
		ServiceLocator.resetRepository()
	}

	@Test
	fun clickTask_navigateToDetailFragmentOne() = runBlockingTest {
		repository.saveTask(Task("Title1", "Description1", false, "id1"))
		repository.saveTask(Task("Title2", "Description2", false, "id2"))

		val scenario = launchFragmentInContainer<TasksFragment>(Bundle(), R.style.AppTheme)

		val navController = mock(NavController::class.java)

		scenario.onFragment {
			Navigation.setViewNavController(it.view!!, navController)
		}

		onView(withId(R.id.tasks_list))
			.perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
				hasDescendant(withText("Title1")), click()))

		verify(navController).navigate(
			TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment("id1")
		)
	}

	@Test
	fun clickAddTaskButton_navigateToAddEditFragment() {
		val scenario = launchFragmentInContainer<TasksFragment>(Bundle(), R.style.AppTheme)
		val navController = mock(NavController::class.java)
		val application = ApplicationProvider.getApplicationContext<TodoApplication>()

		scenario.onFragment {
			Navigation.setViewNavController(it.view!!, navController)
		}

		onView(withId(R.id.add_task_fab)).perform(click())

		verify(navController).navigate(
			TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment(null, application.resources.getString(R.string.add_task))
		)
	}
}