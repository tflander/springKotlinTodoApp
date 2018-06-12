package todd.todo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class TodoController {

    @Autowired
    lateinit var todoService: TodoService

    @GetMapping("users/{name}/todos")
    fun retrieveForName(@PathVariable name: String): List<Todo> {
        return todoService.todosForName(name)
    }

}

