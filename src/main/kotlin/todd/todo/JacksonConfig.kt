package todd.todo

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@Configuration
class JacksonConfig {

    @Bean
    @Primary
    fun objectMapper(builder: Jackson2ObjectMapperBuilder): ObjectMapper {
        println("Config is starting.")
        val objectMapper = builder.createXmlMapper(false).build<ObjectMapper>()
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        return objectMapper
    }
}