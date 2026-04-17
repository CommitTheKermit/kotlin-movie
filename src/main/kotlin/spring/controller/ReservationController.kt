package spring.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import spring.model.request.ReservationRequest
import spring.service.ReservationService

@RestController
class ReservationController(private val reservationService: ReservationService) {
    @PostMapping("/api/reservations")
    fun reserve(
        @RequestBody
        request: ReservationRequest,
    ): ResponseEntity<Any> = try {
        ResponseEntity.status(HttpStatus.CREATED).body(reservationService.reserve(request))
    } catch (e: IllegalArgumentException) {
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message ?: "")
    }
}
