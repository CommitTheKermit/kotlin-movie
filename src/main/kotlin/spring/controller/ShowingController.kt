package spring.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import spring.model.response.ShowingResponse
import spring.repository.ShowingRepository

@RestController
class ShowingController(private val showingRepository: ShowingRepository) {
    @GetMapping("/api/showings")
    fun getAllShowings(): ResponseEntity<Any> = try {
        ResponseEntity.ok(
            showingRepository.findAll().map {
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
