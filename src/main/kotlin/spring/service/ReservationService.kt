package spring.service

import domain.purchase.Price
import domain.reservation.ReservationInfo
import domain.seat.Seats
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.model.request.ReservationRequest
import spring.model.response.ReservationResponse
import spring.repository.ReservationRepository
import spring.repository.SeatRepository
import spring.repository.ShowingRepository
import view.message.CinemaMessages

@Service
class ReservationService(
    private val showingRepository: ShowingRepository,
    private val seatRepository: SeatRepository,
    private val reservationRepository: ReservationRepository,
) {
    @Transactional
    fun reserve(request: ReservationRequest): ReservationResponse {
        val reservationIds = mutableListOf<Long>()
        val subtotal = request.reservations.sumOf { item ->
            val showing = showingRepository.findById(item.showingId)
            require(showing != null) { CinemaMessages.ERROR_INVALID_SHOWING_NUMBER }

            val seats = Seats(item.seats.map { seatRepository.findBySeatNumber(it) })
            val info = ReservationInfo(showing, seats)
            val savedIds = reservationRepository.save(info, item.showingId)

            reservationIds.addAll(savedIds)
            info.applyDiscount()
        }
        val afterPoints = Price(subtotal - request.usedPoints)
        val totalPrice = request.paymentMethod.discountApply(afterPoints).price
        return ReservationResponse(
            reservationIds = reservationIds,
            reservations = request.reservations,
            usedPoints = request.usedPoints,
            paymentMethod = request.paymentMethod,
            totalPrice = totalPrice,
        )
    }
}
