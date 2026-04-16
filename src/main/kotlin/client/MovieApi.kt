package client

import org.springframework.web.service.annotation.GetExchange
import spring.model.response.MovieResponse

interface MovieApi {
    @GetExchange("/api/movies")
    fun fetchMovies(): List<MovieResponse>
}
