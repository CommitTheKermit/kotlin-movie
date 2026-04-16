package client

import org.springframework.web.service.annotation.GetExchange
import spring.model.response.ShowingResponse

interface ShowingApi {
    @GetExchange("/api/showings")
    fun getAllShowings(): List<ShowingResponse>
}
