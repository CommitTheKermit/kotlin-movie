package client

import org.springframework.web.service.annotation.GetExchange
import spring.model.response.SeatResponse

interface SeatApi {
    @GetExchange("/api/seats")
    fun getAllSeats(): List<SeatResponse>
}
