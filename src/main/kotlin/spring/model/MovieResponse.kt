package spring.model

import domain.cinema.Movie
import domain.cinema.Showing
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
            showings = showings.showings.map { ShowingResponse.from(it) },
        )
    }
}

data class ShowingResponse(val id: Int, val startTime: String) {
    companion object {
        fun from(showing: Showing) = ShowingResponse(
            id = showing.id.value,
            startTime = showing.startTime.value.toString(),
        )
    }
}
