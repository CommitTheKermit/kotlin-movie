package spring.model.response

import domain.cinema.Movie
import domain.cinema.Showings

data class MovieResponse(val id: Int, val title: String, val runningTimeMinutes: Int, val showings: List<ShowingResponse>) {
    companion object {
        fun from(
            movie: Movie,
            showings: Showings,
        ) = MovieResponse(
            id = movie.id.value,
            title = movie.title,
            runningTimeMinutes = movie.runningTime,
            showings = showings.showings.map {
                ShowingResponse.from(
                    it,
                    movie = movie,
                    screen = it.screen,
                )
            },
        )
    }
}
