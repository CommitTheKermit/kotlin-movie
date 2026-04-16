package spring.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import repository.MovieRepository
import repository.ShowingRepository
import spring.model.MovieResponse

@RestController
class MovieController(private val movieRepository: MovieRepository, private val showingRepository: ShowingRepository) {
    @GetMapping("/api/movies")
    fun getMovies(): List<MovieResponse> {
        val movies = movieRepository.getAllMovies().movies.map {
            val showings = showingRepository.findByMovieId(it.id)
            MovieResponse.from(it, showings)
        }
        return movies
    }
}
