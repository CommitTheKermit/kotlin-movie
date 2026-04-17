package spring.repository

import domain.seat.Seat
import domain.seat.SeatCoordinate
import domain.seat.SeatGrade
import domain.seat.SeatState
import domain.seat.Seats
import javax.sql.DataSource
import org.springframework.stereotype.Repository
import view.message.SeatMessages

@Repository
class SeatRepository(val dataSource: DataSource) {
    fun findBySeatNumber(coordinate: String): Seat {
        val sql = "SELECT seat_number, grade FROM seat WHERE seat_number = ?"
        return dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { ps ->
                ps.setString(1, coordinate)
                ps.executeQuery().use { rs ->
                    if (!rs.next()) error(SeatMessages.ERROR_SEAT_NOT_FOUND)
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

    fun findAllSeats(): Seats {
        val sql = "SELECT seat_number, grade FROM seat"
        return dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { ps ->
                val seats = mutableListOf<Seat>()
                ps.executeQuery().use { rs ->
                    while (rs.next()) {
                        val seatNumber = rs.getString("seat_number")

                        seats.add(
                            Seat(
                                coordinate = SeatCoordinate(seatNumber[0], seatNumber.substring(1).toInt()),
                                grade = SeatGrade.valueOf(rs.getString("grade")),
                                isReserved = SeatState.AVAILABLE,
                            ),
                        )
                    }
                }
                Seats(seats)
            }
        }
    }
}
