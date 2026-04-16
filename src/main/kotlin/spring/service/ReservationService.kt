package spring.service

import domain.reservation.ReservationInfo
import domain.seat.Seats
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import repository.ReservationRepository
import repository.SeatRepository
import repository.ShowingRepository
import spring.model.request.ReservationRequest
import spring.model.response.ReservationResponse
import view.message.CinemaMessages

@Service
class ReservationService(
    private val showingRepository: ShowingRepository,
    private val seatRepository: SeatRepository,
    private val reservationRepository: ReservationRepository,
) {
    @Transactional
    fun reserve(request: ReservationRequest): ReservationResponse {
        val reservations = mutableListOf<String>()
        val subtotal = request.reservations.sumOf { item ->
            val showing = showingRepository.findById(item.showingId)
            require(showing != null) { CinemaMessages.ERROR_INVALID_SHOWING_NUMBER }

            val seats = Seats(item.seats.map { seatRepository.findBySeatNumber(it) })
            val info = ReservationInfo(showing, seats)
            val list = reservationRepository.save(info, item.showingId)

            reservations.addAll(list.map { it.toString() })
            info.applyDiscount()
        }
        val totalPrice = (subtotal - request.usedPoints)
        return ReservationResponse(reservations, totalPrice)
    }
}
