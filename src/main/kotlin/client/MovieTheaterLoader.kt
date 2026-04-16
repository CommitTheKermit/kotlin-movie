package client

import domain.cinema.MovieTheater
import domain.cinema.Movies
import domain.cinema.Showings
import domain.reservation.ReservationInfos

class MovieTheaterLoader(private val movieApi: MovieApi, private val showingApi: ShowingApi) {
    fun load(): MovieTheater {
        val movieResponses = movieApi.fetchMovies()
        val movies = Movies(movieResponses.map { it.toMovie() })

        val showingResponses = showingApi.getAllShowings()
        val showings = Showings(
            showingResponses.map {
                val targetIndex = movieResponses.indexOfFirst { movie -> movie.id == it.movieId }
                it.toShowing(movie = movieResponses[targetIndex])
            },
        )

        return MovieTheater(
            movies = movies,
            showings = showings,
            reservationInfos = ReservationInfos(emptyList()),
        )
    }
}
