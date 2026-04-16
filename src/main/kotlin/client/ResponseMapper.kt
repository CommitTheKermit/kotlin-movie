package client

import domain.Id
import domain.cinema.Movie
import domain.cinema.MovieTime
import domain.cinema.Screen
import domain.cinema.Showing
import domain.seat.Seats
import kotlinx.datetime.LocalDateTime
import spring.model.response.MovieResponse
import spring.model.response.ShowingResponse

fun ShowingResponse.toShowing(movie: MovieResponse): Showing = Showing(
    id = Id(id),
    startTime = MovieTime(LocalDateTime.parse(startTime)),
    movie = Movie(title = movie.title, id = Id(movie.id), runningTime = movie.runningTimeMinutes),
    screen = Screen(seats = Seats(emptyList()), id = Id(screenId)),
)
