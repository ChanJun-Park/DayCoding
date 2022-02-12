package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Test

class StatisticsUtilsTest {

	@Test
	fun getActiveAndCompleteStats_noCompleted_returnsHundredZero() {

		val tasks = listOf(
			Task("title", "desc", isCompleted = false)
		)

		val result = getActiveAndCompletedStats(tasks)

		assertThat(result.completedTasksPercent, `is`(0f))
		assertThat(result.activeTasksPercent, `is`(100f))
	}

	@Test
	fun getActiveAndCompleteStats_both_4060() {

		val tasks = listOf(
			Task("title", "desc", isCompleted = true),
			Task("title", "desc", isCompleted = true),
			Task("title", "desc", isCompleted = false),
			Task("title", "desc", isCompleted = false),
			Task("title", "desc", isCompleted = false)
		)

		val result = getActiveAndCompletedStats(tasks)

		assertThat(result.completedTasksPercent, `is`(40f))
		assertThat(result.activeTasksPercent, `is`(60f))
	}

	@Test
	fun getActiveAndCompleteStats_empty_returnZeros() {

		val tasks = emptyList<Task>()

		val result = getActiveAndCompletedStats(tasks)

		assertThat(result.completedTasksPercent, `is`(0f))
		assertThat(result.activeTasksPercent, `is`(0f))
	}

	@Test
	fun getActiveAndCompleteStats_errors_returnZeros() {

		val tasks = null

		val result = getActiveAndCompletedStats(tasks)

		assertThat(result.completedTasksPercent, `is`(0f))
		assertThat(result.activeTasksPercent, `is`(0f))
	}
}