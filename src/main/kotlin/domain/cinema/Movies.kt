package domain.cinema

import domain.Id
import view.message.CinemaMessages

class Movies(val movies: List<Movie>) {
    fun findMovieById(id: Id): Movie {
        val movieIndex = movies.indexOfFirst { it.id.value == id.value }
        require(movieIndex != -1) { CinemaMessages.ERROR_MOVIE_NOT_FOUND_BY_ID }
        return movies[movieIndex]
    }

    fun findMovieByTitle(title: String): Movie {
        val movieIndex = movies.indexOfFirst { it.title == title }
        require(movieIndex != -1) { CinemaMessages.ERROR_MOVIE_NOT_FOUND_BY_TITLE }
        return movies[movieIndex]
    }
}
