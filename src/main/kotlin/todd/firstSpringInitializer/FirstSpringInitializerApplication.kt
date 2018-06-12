package todd.firstSpringInitializer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FirstSpringInitializerApplication

fun main(args: Array<String>) {
    runApplication<FirstSpringInitializerApplication>(*args)
}
