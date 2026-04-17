package client

import domain.cart.Cart
import domain.purchase.PaymentResult
import spring.model.request.ReservationItem
import spring.model.request.ReservationRequest

class ReservationRegistrator(private val reservationApi: ReservationApi) {
    fun register(
        cart: Cart,
        paymentResult: PaymentResult,
    ) {
        val items = cart.reservationInfos.infos.map { info ->
            ReservationItem(
                showingId = info.showing.id.value.toLong(),
                seats = info.seats.seats.map { it.coordinate.toString() },
            )
        }
        reservationApi.reserve(
            ReservationRequest(
                reservations = items,
                usedPoints = paymentResult.usedPoint.point,
                paymentMethod = paymentResult.paymentMethod,
            ),
        )
    }
}
