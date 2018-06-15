package todd.todo

import assertk.assert
import assertk.assertions.isEqualTo
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import todd.utilities.JsonFileReader
import java.io.IOException
import java.time.LocalDate
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoControllerTest {

    @LocalServerPort
    var port: Int = 0

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var todoService: TodoService

//    @Test
//    fun retrieveTodosForUser() {
//        val response = testRestTemplate.getForEntity(createURL("/api/users/Todd/todos"), String::class.java)
//
//        val actual = ObjectMapper().readTree(response.getBody())
//        val expected = getExpectedJsonForRetrieveTodos()
//        assert(actual).isEqualTo(expected)
//    }

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
    fun retrieveByIdHappyPath() {

        val todo = Todo(0, "Todd", "descr", LocalDate.of(2018, 6,8), false)
        `when`(todoService.byId(0)).thenReturn(Optional.of(todo))

        val result = testRestTemplate.getForEntity("/api/users/Todd/todo/0",
                String::class.java)
        assert(result.statusCode).isEqualTo(HttpStatus.OK)
        val expected = objectMapper
                .readTree(JsonFileReader()
                .readJsonFromClasspath("expectedResponses/retrieveTodoById.json"))
                .toString()
                .replace("8080", port.toString())

        print(expected.toString())

        JSONAssert.assertEquals(
                expected,
                objectMapper.readTree(result.body).toString(),
                true
        )
    }

    @Test
    fun retrieveByNotFoundFailure() {

        `when`(todoService.byId(99)).thenReturn(Optional.empty())

        val result = testRestTemplate.getForEntity("/api/users/Todd/todo/99",
                String::class.java)
        assert(result.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        val body = objectMapper.readTree(result.body)
        assert(body.get("message").textValue()).isEqualTo("Todo with id 99 not found")
        assert(body.get("details").textValue()).isEqualTo("Not Found")
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

    @Throws(IOException::class)
    private fun getExpectedJsonForRetrieveTodos(): JsonNode {
        val expected = objectMapper.readTree(JsonFileReader().readJsonFromClasspath("expectedResponses/retrieveTodosForUser.json"))
        val today = LocalDate.now()
        expected.forEach { node -> (node as ObjectNode).replace("targetDate", objectMapper.valueToTree(today)) }
        return expected
    }

    private fun createURL(path: String): String {
        return "http://localhost:" + port + path
    }

}