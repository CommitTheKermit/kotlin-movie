package spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["spring", "repository"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
