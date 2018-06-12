package todd.todo

import java.time.LocalDate

data class Todo(
        val id: Int = 0,
        val user: String? = null,
        val desc: String? = null,
        val targetDate: LocalDate? = null,
        val isDone: Boolean = false
)