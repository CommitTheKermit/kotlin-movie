package client

import domain.cinema.MovieTheater
import domain.cinema.Movies
import domain.cinema.Showings
import domain.reservation.ReservationInfos
import domain.seat.Seats

class MovieTheaterLoader(private val movieApi: MovieApi, private val showingApi: ShowingApi, private val seatApi: SeatApi) {
    fun load(): MovieTheater {
        val movieResponses = movieApi.fetchMovies()
        val movies = Movies(movieResponses.map { it.toMovie() })

        val seats = Seats(seatApi.getAllSeats().map { it.toSeat() })

        val showingResponses = showingApi.getAllShowings()
        val showings = Showings(
            showingResponses.map {
                val targetIndex = movieResponses.indexOfFirst { movie -> movie.id == it.movieId }
                it.toShowing(movie = movieResponses[targetIndex], seats = seats)
            },
        )

        return MovieTheater(
            movies = movies,
            showings = showings,
            reservationInfos = ReservationInfos(emptyList()),
        )
    }
}
