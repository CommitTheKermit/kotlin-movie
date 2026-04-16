package repository

import domain.seat.Seat
import domain.seat.SeatCoordinate
import domain.seat.SeatGrade
import domain.seat.SeatState
import javax.sql.DataSource
import org.springframework.stereotype.Repository

@Repository
class SeatRepository(val dataSource: DataSource) {
    fun findById(id: Long): Seat {
        val sql = "SELECT seat_number, grade FROM seat WHERE id = ?"
        return dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { ps ->
                ps.setLong(1, id)
                ps.executeQuery().use { rs ->
                    if (!rs.next()) error("Seat not found: id=$id")
                    val seatNumber = rs.getString("seat_number")
                    Seat(
                        coordinate = SeatCoordinate(seatNumber[0], seatNumber.substring(1).toInt()),
                        grade = SeatGrade.valueOf(rs.getString("grade")),
                        isReserved = SeatState.AVAILABLE,
                    )
                }
            }
        }
    }
}
