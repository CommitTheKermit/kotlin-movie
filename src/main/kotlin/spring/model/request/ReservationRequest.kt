package spring.model.request

import domain.purchase.PaymentMethod

data class ReservationRequest(val reservations: List<ReservationItem>, val usedPoints: Int, val paymentMethod: PaymentMethod)

data class ReservationItem(val showingId: Long, val seats: List<String>)
