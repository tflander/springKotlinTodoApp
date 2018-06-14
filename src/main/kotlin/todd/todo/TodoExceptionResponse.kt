package todd.todo

import java.time.LocalDateTime

data class TodoExceptionResponse(
        val timestamp: LocalDateTime = LocalDateTime.now(),
        val message: String,
        val details: String
)