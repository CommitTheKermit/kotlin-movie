package domain.seat

class Seat(val coordinate: SeatCoordinate, val grade: SeatGrade, val isReserved: SeatState) {
    fun getSeatPrice(): Int {
        return grade.price
    }
}
