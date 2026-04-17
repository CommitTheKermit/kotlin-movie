package client

import domain.Id
import domain.cinema.Movie
import domain.cinema.MovieTime
import domain.cinema.Screen
import domain.cinema.Showing
import domain.seat.Seat
import domain.seat.SeatCoordinate
import domain.seat.SeatGrade
import domain.seat.SeatState
import domain.seat.Seats
import kotlinx.datetime.LocalDateTime
import spring.model.response.MovieResponse
import spring.model.response.SeatResponse
import spring.model.response.ShowingResponse

fun ShowingResponse.toShowing(
    movie: MovieResponse,
    seats: Seats,
): Showing = Showing(
    id = Id(id),
    startTime = MovieTime(LocalDateTime.parse(startTime)),
    movie = Movie(title = movie.title, id = Id(movie.id), runningTime = movie.runningTimeMinutes),
    screen = Screen(seats = seats, id = Id(screenId)),
)

fun MovieResponse.toMovie(): Movie = Movie(
    title = title,
    id = Id(id),
    runningTime = runningTimeMinutes,
)

fun SeatResponse.toSeat(): Seat = Seat(
    coordinate = SeatCoordinate.from(number),
    grade = SeatGrade.valueOf(grade),
    isReserved = SeatState.AVAILABLE,
)
