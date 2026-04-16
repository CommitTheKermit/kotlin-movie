package spring.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import spring.model.MovieResponse

@RestController
class Movie {

    @GetMapping("/api/movies")
    fun getMovies(): List<MovieResponse> {

//        return Movies.movies.map { MovieResponse.from(it) }
        return emptyList()
    }
}
