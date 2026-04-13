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

    BookingController(
        reservationController = ReservationController(MockData.movieTheater),
        cartController = CartController(),
        paymentController = PaymentController(),
        user = user,
    ).run(cart)
}
