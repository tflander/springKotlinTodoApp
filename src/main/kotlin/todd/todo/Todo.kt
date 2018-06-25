package todd.todo

import java.time.LocalDate
import javax.validation.constraints.Size

data class Todo(
        val id: Int = 0,
        val user: String,

        @get:Size(min = 10, message = "Enter at least 10 chars")
        val desc: String,
        val targetDate: LocalDate,
        val isDone: Boolean
)