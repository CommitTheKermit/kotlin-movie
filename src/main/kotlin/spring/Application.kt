package spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

data class Greeting(val id: Long, val name: String) {
    fun sayHello(): String = "Hello, $name!"
}

@RestController
class GreetingController {
    @GetMapping("/greeting")
    fun hello(name: String = "AO"): Greeting {
        return Greeting(1, name)
    }
}

@SpringBootApplication(scanBasePackages = ["spring", "repository"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
