package domain.cinema

import domain.reservation.ReservationInfos
import view.message.CinemaMessages

class Showings(val showings: List<Showing>) {
    operator fun get(index: Int): Showing = showings[index]

    fun first(): Showing = showings.first()

    fun findByMovieAndDate(
        movie: Movie,
        movieTime: MovieTime,
    ): Showings {
        val filtered = showings.filter { it.movie == movie && it.startTime.isOnSameDate(movieTime) }
        require(filtered.isNotEmpty()) { CinemaMessages.ERROR_NO_SHOWINGS_ON_DATE }
        return Showings(filtered)
    }

    fun findByIndex(input: String): Showing {
        require(input.toIntOrNull() != null && input.toInt() <= showings.size) { CinemaMessages.ERROR_INVALID_SHOWING_NUMBER }
        return showings[input.toInt() - 1]
    }

    fun findAvailableShowing(
        movie: Movie,
        movieTime: MovieTime,
        number: String,
        reservationInfos: ReservationInfos,
    ): Showing {
        val showings = findByMovieAndDate(movie, movieTime)
        val selected = showings.findByIndex(number)
        reservationInfos.checkReservationHistory(selected)

        return selected
    }
}
