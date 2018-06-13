package todd.todo

import assertk.assert
import assertk.assertions.isEqualTo
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoControllerTest {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var todoService: TodoService

    @Test
    fun retrieveForName() {

        val mockList = listOf(
                Todo(
                        id = 0,
                        user = "todd",
                        desc = "descr",
                        targetDate = LocalDate.now(),
                        isDone = false
                )
        )

        `when`(todoService.todosForName("Todd")).thenReturn(mockList)

        val result = testRestTemplate.getForEntity("/api/users/Todd/todos",
                String::class.java)
        assert(result.statusCode).isEqualTo(HttpStatus.OK)

        JSONAssert.assertEquals(
                objectMapper.writeValueAsString(mockList),
                result.body,
                true
        )

    }

    @Test
    fun retrieveById() {

        val todo = Todo(0, "todd", "descr", LocalDate.now(), false)
        `when`(todoService.byId(0)).thenReturn(Optional.of(todo))

        val result = testRestTemplate.getForEntity("/api/users/Todd/todo/0",
                String::class.java)
        assert(result.statusCode).isEqualTo(HttpStatus.OK)

        JSONAssert.assertEquals(
                objectMapper.writeValueAsString(todo),
                result.body,
                true
        )
    }

    @Test
    fun addNewTodo() {
        val now = LocalDate.now()
        val todo = Todo(0, "todd", "descr", now, false)
        val todoWithIdAdded = todo.copy(id=4)
        val jsonNode = objectMapper.valueToTree<JsonNode>(todo)
        println(jsonNode.toString())
        `when`<Todo>(todoService.add("todd", "descr", now)).thenReturn(todoWithIdAdded)

        val result = testRestTemplate.postForEntity("/api/users/Todd/todo", jsonNode, JsonNode::class.java)
        assert(result.statusCode).isEqualTo(HttpStatus.CREATED)
        assert(result.headers.get("location")).equals("/users/Todd/todo/4")
    }

}