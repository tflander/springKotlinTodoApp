package todd.todo

import java.time.LocalDate

data class Todo(
        val id: Int = 0,
        val user: String,
        val desc: String,
        val targetDate: LocalDate,
        val isDone: Boolean
)