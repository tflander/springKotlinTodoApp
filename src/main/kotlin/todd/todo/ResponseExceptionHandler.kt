package todd.todo

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*

@ControllerAdvice
@RestController
class ResponseExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(TodoNotFoundException::class)
    fun handleTodoNotFound(exception: TodoNotFoundException, locale: Locale): ResponseEntity<TodoExceptionResponse> {
        val todoExceptionResponse = TodoExceptionResponse(message = exception.localizedMessage, details = "Not Found")
        return ResponseEntity<TodoExceptionResponse>(todoExceptionResponse, HttpHeaders(), HttpStatus.NOT_FOUND)
    }

}