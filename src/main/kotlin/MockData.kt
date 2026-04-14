import domain.Id
import domain.cinema.Movie
import domain.cinema.MovieTheater
import domain.cinema.MovieTime
import domain.cinema.Movies
import domain.cinema.Screen
import domain.cinema.Showing
import domain.cinema.Showings
import domain.reservation.ReservationInfos
import domain.seat.Seat
import domain.seat.SeatCoordinate
import domain.seat.SeatGrade
import domain.seat.SeatState
import domain.seat.Seats

object MockData {
    val movies = Movies(
        listOf(
            Movie("F1 더 무비", Id(1), 130),
            Movie("토이 스토리", Id(2), 100),
            Movie("아이언맨", Id(3), 126),
        ),
    )

    val seats = Seats(
        listOf(
            Seat(SeatCoordinate('A', 1), SeatGrade.B, SeatState.AVAILABLE),
            Seat(SeatCoordinate('A', 2), SeatGrade.B, SeatState.AVAILABLE),
            Seat(SeatCoordinate('A', 3), SeatGrade.B, SeatState.AVAILABLE),
            Seat(SeatCoordinate('A', 4), SeatGrade.B, SeatState.AVAILABLE),
            Seat(SeatCoordinate('B', 1), SeatGrade.B, SeatState.AVAILABLE),
            Seat(SeatCoordinate('B', 2), SeatGrade.B, SeatState.AVAILABLE),
            Seat(SeatCoordinate('B', 3), SeatGrade.B, SeatState.AVAILABLE),
            Seat(SeatCoordinate('B', 4), SeatGrade.B, SeatState.AVAILABLE),
            Seat(SeatCoordinate('C', 1), SeatGrade.S, SeatState.AVAILABLE),
            Seat(SeatCoordinate('C', 2), SeatGrade.S, SeatState.AVAILABLE),
            Seat(SeatCoordinate('C', 3), SeatGrade.S, SeatState.AVAILABLE),
            Seat(SeatCoordinate('C', 4), SeatGrade.S, SeatState.AVAILABLE),
            Seat(SeatCoordinate('D', 1), SeatGrade.S, SeatState.AVAILABLE),
            Seat(SeatCoordinate('D', 2), SeatGrade.S, SeatState.AVAILABLE),
            Seat(SeatCoordinate('D', 3), SeatGrade.S, SeatState.AVAILABLE),
            Seat(SeatCoordinate('D', 4), SeatGrade.S, SeatState.AVAILABLE),
            Seat(SeatCoordinate('E', 1), SeatGrade.A, SeatState.AVAILABLE),
            Seat(SeatCoordinate('E', 2), SeatGrade.A, SeatState.AVAILABLE),
            Seat(SeatCoordinate('E', 3), SeatGrade.A, SeatState.AVAILABLE),
            Seat(SeatCoordinate('E', 4), SeatGrade.A, SeatState.AVAILABLE),
        ),
    )

    val screens = listOf(
        Screen(seats, Id(1)),
        Screen(seats, Id(2)),
        Screen(seats, Id(3)),
    )

    val showings = Showings(
        listOf(
            Showing(MovieTime(2025, 9, 20, 10, 20), screens[0], movies.movies[0]),
            Showing(MovieTime(2025, 9, 20, 13, 0), screens[0], movies.movies[0]),
            Showing(MovieTime(2025, 9, 20, 15, 40), screens[0], movies.movies[0]),
            Showing(MovieTime(2025, 9, 20, 20, 10), screens[0], movies.movies[0]),
            Showing(MovieTime(2025, 9, 20, 13, 30), screens[1], movies.movies[1]),
            Showing(MovieTime(2025, 9, 20, 16, 0), screens[1], movies.movies[1]),
            Showing(MovieTime(2025, 9, 20, 9, 50), screens[2], movies.movies[2]),
        ),
    )

    val movieTheater = MovieTheater(
        movies,
        showings,
        ReservationInfos(emptyList()),
    )
}
