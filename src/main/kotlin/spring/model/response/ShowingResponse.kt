package spring.model.response

import domain.cinema.Movie
import domain.cinema.Screen
import domain.cinema.Showing

data class ShowingResponse(val id: Int, val startTime: String, val movieId: Int, val screenId: Int) {

    companion object {
        fun from(
            showing: Showing,
            movie: Movie,
            screen: Screen,
        ) = ShowingResponse(
            id = showing.id.value,
            startTime = showing.startTime.value.toString(),
            movieId = movie.id.value,
            screenId = screen.id.value,
        )
    }
}
