package todd.utilities

import org.springframework.core.io.ClassPathResource
import java.io.IOException
import org.apache.commons.io.IOUtils

class JsonFileReader {
    @Throws(IOException::class)
    fun readJsonFromClasspath(path: String): String {

        ClassPathResource(path).inputStream.use { stream -> return IOUtils.toString(stream) }
    }

}