package controller

import domain.cinema.Movie
import domain.cinema.MovieTheater
import domain.cinema.Showing
import domain.reservation.ReservationInfo
import domain.seat.Seats
import view.InputView
import view.OutputView
import view.retryUntilValid

class ReservationController(private val movieTheater: MovieTheater) {
    fun run(): ReservationInfo {
        val movie = retryUntilValid { chooseMovie() }
        val (movieTime, candidates) = retryUntilValid {
            val at = InputView.readDate()
            at to movieTheater.showings.findByMovieAndDate(movie, at)
        }
        OutputView.printShowing(candidates)
        val showing = retryUntilValid {
            movieTheater.showings.findAvailableShowing(
                movie,
                movieTime,
                InputView.readShowingNumber(),
                movieTheater.reservationInfos,
            )
        }
        val seats = retryUntilValid { chooseSeat(showing) }

        return ReservationInfo(showing, seats)
    }

    private fun chooseMovie(): Movie {
        val input = InputView.readMovieTitle()
        return movieTheater.movies.findMovieByTitle(input)
    }

    private fun chooseSeat(showing: Showing): Seats {
        OutputView.printSeats(showing.screen)
        val inputs = InputView.readSeat()
        return showing.screen.selectSeats(inputs)
    }
}
