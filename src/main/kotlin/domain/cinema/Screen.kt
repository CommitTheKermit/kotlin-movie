package domain.cinema

import domain.Id
import domain.seat.Seats

class Screen(val seats: Seats, val id: Id) {
    fun selectSeats(inputs: List<String>): Seats {
        return Seats(inputs.map { seats.checkSeat(it) })
    }

    companion object {
        const val MAX_ROW = 5
        const val MAX_COLUMN = 4
    }
}
