package spring.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import spring.model.request.ReservationRequest
import spring.model.response.ReservationResponse
import spring.service.ReservationService

@RestController
class ReservationController(private val reservationService: ReservationService) {
    @PostMapping("/api/reservations")
    fun reserve(
        @RequestBody
        request: ReservationRequest,
    ): ReservationResponse = reservationService.reserve(request)
}
