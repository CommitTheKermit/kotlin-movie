package spring.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import spring.model.response.SeatResponse
import spring.repository.SeatRepository

@RestController
class SeatController(private val seatRepository: SeatRepository) {
    @GetMapping("/api/seats")
    fun getSeats(): List<SeatResponse> = seatRepository.findAllSeats().seats.map { SeatResponse.from(it) }
}
