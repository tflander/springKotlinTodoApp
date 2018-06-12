package todd.firstSpringInitializer

import org.springframework.stereotype.Service
import todd.todo.Todo
import java.time.LocalDate
import java.util.*

@Service
class TodoService {

    private lateinit var todos: MutableList<Todo>
    private var todoCount: Int = 0

    init {
        init()
    }

    fun init() {
        todos = mutableListOf()
        todos.add(createTodo("Todd", "better spring"))
        todos.add(createTodo("Todd", "spring Kotlin"))
        todos.add(createTodo("Jake", "better job"))
        todoCount = todos.size
    }

    fun todosForName(name: String): List<Todo> {
        return todos.filter { todo -> todo.user?.toLowerCase() ==(name.toLowerCase()) }
    }

    fun add(name: String, desc: String, targetDate: LocalDate): Todo {
        val todo = Todo(++todoCount, name, desc, targetDate, false)
        todos.add(todo)
        return todo
    }

    fun byId(id: Int): Optional<Todo> {
        val firstOrNull = todos
                .filter { todo -> todo.id === id }
                .firstOrNull()

        return if(firstOrNull == null) Optional.empty() else Optional.of(firstOrNull)
    }

    fun remove(id: Int) {
        todos = todos.filter { todo -> todo.id != id }.toMutableList()
    }

    private fun createTodo(name: String, desc: String): Todo {
        return Todo(todos.size, name, desc, LocalDate.now(), false)
    }
}