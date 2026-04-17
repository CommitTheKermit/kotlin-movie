import client.MovieApi
import client.MovieTheaterLoader
import client.ReservationApi
import client.ReservationRegistrator
import client.SeatApi
import client.ShowingApi
import client.api.ApiClientFactory
import controller.BookingController
import controller.CartController
import controller.PaymentController
import controller.ReservationController
import domain.Id
import domain.cart.Cart
import domain.reservation.ReservationInfos
import domain.user.User

fun main() {
    val user = User(Id(1))
    val cart = Cart(ReservationInfos(emptyList()))

    val apiClientFactory = ApiClientFactory("http://localhost:8080")
    val movieApi = apiClientFactory.create<MovieApi>()
    val reservationApi = apiClientFactory.create<ReservationApi>()
    val showingApi = apiClientFactory.create<ShowingApi>()
    val seatApi = apiClientFactory.create<SeatApi>()

    val movieTheater = MovieTheaterLoader(
        movieApi = movieApi,
        showingApi = showingApi,
        seatApi = seatApi,
    ).load()

    BookingController(
        reservationController = ReservationController(movieTheater),
        cartController = CartController(),
        paymentController = PaymentController(),
        reservationRegistrar = ReservationRegistrator(reservationApi),
        user = user,
    ).run(cart)
}
