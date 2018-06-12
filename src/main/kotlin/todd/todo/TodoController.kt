package todd.todo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api")
class TodoController {

    @Autowired
    lateinit var todoService: TodoService

    @GetMapping("users/{name}/todos")
    fun retrieveForName(@PathVariable name: String): List<Todo> {
        return todoService.todosForName(name)
    }


    @GetMapping("users/{name}/todo/{id}")
    fun retrieveById(@PathVariable name: String, @PathVariable id: Int): Any {
        val todoOptional = todoService.byId(id)
        return if (todoOptional.isPresent) {
            todoOptional.get()
        } else {
            "Todo with id $id not found"
        }
    }

    @PostMapping("/users/{name}/todo")
    fun addTodoForUser(@PathVariable name: String, @RequestBody todo: Todo): ResponseEntity<*> {
        val (id) = todoService.add(todo.user!!, todo.desc!!, todo.targetDate!!)
                ?: return ResponseEntity.noContent().build<Any>()
        val uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri()
        return ResponseEntity.created(uri).build<Any>()
    }
}

