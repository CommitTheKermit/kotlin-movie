package spring.model.response

import domain.purchase.PaymentMethod
import spring.model.request.ReservationItem

data class ReservationResponse(
    val reservationIds: List<Long>,
    val reservations: List<ReservationItem>,
    val usedPoints: Int,
    val paymentMethod: PaymentMethod,
    val totalPrice: Int,
)
