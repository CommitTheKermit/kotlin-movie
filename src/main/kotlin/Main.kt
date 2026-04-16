import client.ApiClientFactory
import client.MovieApi
import client.ReservationApi
import client.ShowingApi
import client.toShowing
import controller.BookingController
import controller.CartController
import controller.PaymentController
import controller.ReservationController
import domain.Id
import domain.cart.Cart
import domain.cinema.Showings
import domain.reservation.ReservationInfos
import domain.user.User

fun main() {
    val user = User(Id(1))
    val cart = Cart(ReservationInfos(emptyList()))

    val apiClientFactory = ApiClientFactory("http://localhost:8080")
    val movieApi = apiClientFactory.create<MovieApi>()
    val reservationApi = apiClientFactory.create<ReservationApi>()
    val showingApi = apiClientFactory.create<ShowingApi>()
    val movies = movieApi.fetchMovies()
    val showingResponses = showingApi.getAllShowings()
    val showings = Showings(
        showingResponses.map {
            val targetIndex = movies.indexOfFirst { movie -> movie.id == it.movieId }
            it.toShowing(movie = movies[targetIndex])
        },
    )
//    MovieTheater(
//        movies =,
//        showings =,
//        reservationInfos = ReservationInfos(emptyList()),
//    )

    BookingController(
        reservationController = ReservationController(MockData.movieTheater),
        cartController = CartController(),
        paymentController = PaymentController(),
        user = user,
    ).run(cart)
}
