package client

import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.PostExchange
import spring.model.request.ReservationRequest
import spring.model.response.ReservationResponse

interface ReservationApi {
    @PostExchange("/api/reservations")
    fun reserve(
        @RequestBody
        request: ReservationRequest,
    ): ReservationResponse
}
