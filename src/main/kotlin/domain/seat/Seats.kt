package domain.seat

import domain.purchase.Price
import view.message.SeatMessages

class Seats(val seats: List<Seat>) {
    fun getAllPrice(): Price {
        return Price(seats.sumOf { it.getSeatPrice() })
    }

    fun checkSeat(input: String): Seat {
        val coordinate = SeatCoordinate.from(input)

        val seat = seats.filter { it.coordinate.row == coordinate.row && it.coordinate.column == coordinate.column }
        require(seat.isNotEmpty()) { SeatMessages.ERROR_SEAT_NOT_FOUND }
        require(seat.first().isReserved != SeatState.RESERVED) { SeatMessages.ERROR_SEAT_ALREADY_RESERVED }

        return seat.first()
    }
}
