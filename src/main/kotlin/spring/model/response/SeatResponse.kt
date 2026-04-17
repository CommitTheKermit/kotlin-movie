package spring.model.response

import domain.seat.Seat

data class SeatResponse(val number: String, val grade: String) {
    companion object {
        fun from(seat: Seat) = SeatResponse(
            number = seat.coordinate.toString(),
            grade = seat.grade.name,
        )
    }
}
