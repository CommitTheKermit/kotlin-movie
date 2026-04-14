import domain.Id
import domain.cart.Cart
import domain.cinema.Movie
import domain.cinema.MovieTheater
import domain.cinema.MovieTime
import domain.cinema.Movies
import domain.cinema.Screen
import domain.cinema.Showing
import domain.cinema.Showings
import domain.reservation.ReservationInfo
import domain.reservation.ReservationInfos
import domain.seat.Seat
import domain.seat.SeatCoordinate
import domain.seat.SeatGrade
import domain.seat.SeatState
import domain.seat.Seats
import domain.user.User

object TestFixtureData {
    val users = listOf(
        User(Id(1)),
        User(Id(2)),
        User(Id(3)),
    )

    val movies = Movies(
        listOf(
            Movie("해리 포터", Id(1), 152),
            Movie("인터스텔라", Id(2), 169),
            Movie("기생충", Id(3), 132),
        ),
    )

    val seats = Seats(
        listOf(
            Seat(SeatCoordinate('A', 1), SeatGrade.S, SeatState.RESERVED),
            Seat(SeatCoordinate('A', 2), SeatGrade.S, SeatState.AVAILABLE),
            Seat(SeatCoordinate('B', 1), SeatGrade.A, SeatState.AVAILABLE),
            Seat(SeatCoordinate('B', 2), SeatGrade.A, SeatState.AVAILABLE),
            Seat(SeatCoordinate('C', 1), SeatGrade.B, SeatState.AVAILABLE),
            Seat(SeatCoordinate('C', 2), SeatGrade.B, SeatState.AVAILABLE),
        ),
    )
    val screens = listOf(
        Screen(seats, Id(1)),
        Screen(seats, Id(2)),
        Screen(seats, Id(3)),
    )

    val showings = Showings(
        listOf(
            Showing(MovieTime(2026, 4, 10, 10, 0), screens[0], movies.movies[0]),
            Showing(MovieTime(2026, 4, 10, 14, 0), screens[1], movies.movies[1]),
            Showing(MovieTime(2026, 4, 10, 21, 0), screens[2], movies.movies[2]),
        ),
    )

    val reservationInfos = ReservationInfos(
        listOf(
            ReservationInfo(showings[0], Seats(listOf(seats.seats[0]))),
            ReservationInfo(showings[1], Seats(listOf(seats.seats[2]))),
        ),
    )

    val movieTheater = MovieTheater(
//        screens,
        movies,
        showings,
        reservationInfos,
    )

    val cart: Cart = Cart(
        reservationInfos = reservationInfos,
    )

    val firstShowing = showings.first()
    val firstScreen = screens.first()
    val seatB1 = seats.seats[2]
    val seatB2 = seats.seats[3]

    val emptyMovieTheater = MovieTheater(
        movies,
        showings,
        ReservationInfos(emptyList()),
    )

    const val MOVIE_HARRY_POTTER = "해리 포터"
    const val SEAT_A1 = "A1"
    const val SEAT_B1 = "B1"
    const val SEAT_B1_B2 = "B1,B2"
    const val INVALID_SEAT_FORMAT = "F//10"
    const val NON_EXISTENT_SEAT = "F10"
    const val NON_EXISTENT_MOVIE = "X"
}
