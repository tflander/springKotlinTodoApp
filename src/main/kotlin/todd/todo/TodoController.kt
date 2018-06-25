package todd.todo

import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class TodoController {

    @Autowired
    lateinit var todoService: TodoService

    @GetMapping("users/{name}/todos")
    @ApiOperation(
            value = "Retrieve all Todos for a user",
            notes = "pagination is not supported",
            produces = "application/json"
    )
    fun retrieveForName(@PathVariable name: String): List<Todo> {
        return todoService.todosForName(name)
    }


    @GetMapping("users/{name}/todo/{id}")
    @ApiOperation(
            value = "Retrieve todo by id",
            produces = "application/json"
    )
    fun retrieveById(@PathVariable name: String, @PathVariable id: Int): Any {
        val todoOptional = todoService.byId(id)

        if (!todoOptional.isPresent) {
            throw TodoNotFoundException("Todo with id $id not found")
        }

        // Note: this HATEOAS snippet breaks being able to do get from browser.
//        val resource = Resource<Todo>(todoOptional.get())
//        val linkbyId: ControllerLinkBuilder = linkTo(methodOn(this::class.java).retrieveForName(name))
//        resource.add(linkbyId.withRel("parent"))
//        return resource

        return todoOptional.get();
    }

    @PostMapping("/users/{name}/todo")
    fun addTodoForUser(@PathVariable name: String, @Valid @RequestBody todo: Todo): ResponseEntity<*> {
        val (id) = todoService.add(todo.user, todo.desc, todo.targetDate)
                ?: return ResponseEntity.noContent().build<Any>()
        val uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri()

        return ResponseEntity.created(uri).build<Any>()
    }
}

