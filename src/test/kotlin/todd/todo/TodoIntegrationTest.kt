package todd.todo

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@RunWith(SpringRunner::class)
@WebMvcTest(TodoController::class)
class TodoIntegrationTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var todoService: TodoService

    @Test
    @Ignore
    @Throws(Exception::class)
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

        val result = mockMvc.perform(
                MockMvcRequestBuilders.get("/users/Todd/todos")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andReturn()

        JSONAssert.assertEquals(
                objectMapper.writeValueAsString(mockList),
                result.response.contentAsString,
                true
        )

    }
//
//    @Test
//    @Throws(Exception::class)
//    fun retrieveById() {
//
//        val todo = Todo(0, "todd", "descr", LocalDate.now(), false)
//        `when`<Optional<Todo>>(todoService!!.byId(0)).thenReturn(Optional.of(todo))
//
//        val result = mockMvc!!.perform(
//                MockMvcRequestBuilders.get("/users/Todd/todo/0")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn()
//
//        JSONAssert.assertEquals(
//                objectMapper!!.writeValueAsString(todo),
//                result.response.contentAsString,
//                true
//        )
//
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun addNewTodo() {
//        val now = LocalDate.now()
//        val todo = Todo(0, "todd", "descr", now, false)
//        val todoWithIdAdded = todo.toBuilder().id(4).build()
//        val jsonNode = objectMapper!!.valueToTree<JsonNode>(todo)
//        println(jsonNode.toString())
//        `when`<Todo>(todoService!!.add("todd", "descr", now)).thenReturn(todoWithIdAdded)
//
//        mockMvc!!.perform(
//                MockMvcRequestBuilders.post("/users/Todd/todo")
//                        .content(jsonNode.toString())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andExpect(header().string("location", containsString("/users/Todd/todo/4")))
//    }
//

}