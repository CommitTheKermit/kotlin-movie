package spring.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import spring.model.response.ShowingResponse
import spring.service.ShowingService

@RestController
class ShowingController(private val showingService: ShowingService) {
    @GetMapping("/api/showings")
    fun getAllShowings(): ResponseEntity<Any> = try {
        ResponseEntity.ok(
            showingService.findAll().map {
                ShowingResponse.from(
                    showing = it,
                    movie = it.movie,
                    screen = it.screen,
                )
            },
        )
    } catch (e: IllegalArgumentException) {
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message ?: "")
    }
}
