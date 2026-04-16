package spring.repository

import domain.Id
import domain.cinema.Movie
import domain.cinema.MovieTime
import domain.cinema.Screen
import domain.cinema.Showing
import domain.reservation.ReservationInfo
import domain.seat.Seat
import domain.seat.SeatCoordinate
import domain.seat.SeatGrade
import domain.seat.SeatState
import domain.seat.Seats
import java.sql.Statement
import java.time.LocalDateTime
import javax.sql.DataSource
import kotlin.use
import kotlinx.datetime.toKotlinLocalDateTime
import org.springframework.stereotype.Repository
import view.message.SeatMessages

@Repository
class ReservationRepository(val dataSource: DataSource) {
    fun save(
        reservationInfo: ReservationInfo,
        showingId: Long,
    ): List<Long> {
        val reserved = findReservedSeatNumbers(showingId)
        val requested = reservationInfo.seats.seats.map {
            it.coordinate
        }
        require(
            reserved.intersect(requested.toSet())
                .isEmpty(),
        ) { SeatMessages.ERROR_SEAT_ALREADY_RESERVED }

        val savedIds = reservationInfo.seats.seats.map {
            val reservationId = insertReservation(showingId)
            val seatId = getSeatId(it)

            insertReservationSeat(reservationId, seatId)
        }

        return savedIds
    }

    fun findReservedSeatNumbers(showingId: Long): List<SeatCoordinate> {
        val sql = """
            SELECT se.seat_number
            FROM reservation r
            JOIN showing s ON r.showing_id = s.id
            JOIN reservation_seat rs ON r.id = rs.reservation_id
            JOIN seat se ON rs.seat_id = se.id
            WHERE s.id = ?
        """.trimIndent()
        return dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { ps ->
                ps.setLong(1, showingId)
                ps.executeQuery().use { rs ->
                    val seatNumbers = mutableListOf<SeatCoordinate>()
                    while (rs.next()) {
                        val seatNumber = rs.getString("seat_number")
                        seatNumbers.add(
                            SeatCoordinate(
                                seatNumber[0],
                                seatNumber.substring(1).toInt(),
                            ),
                        )
                    }
                    seatNumbers
                }
            }
        }
    }

    fun getSeatId(seat: Seat): Long {
        val sql = """
            SELECT id
            FROM seat
            WHERE seat_number = ?
        """.trimMargin()
        val seatNumber: String = "${seat.coordinate.row}${seat.coordinate.column}"

        return dataSource.connection.use { connection ->
            connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { ps ->
                ps.setString(1, seatNumber)
                ps.executeQuery().use { rs ->
                    if (rs.next()) rs.getLong("id")
                    else error("Seat not found: $seatNumber")
                }
            }
        }
    }

    fun insertReservation(showingId: Long): Long {
        val sql = """
            INSERT INTO reservation (showing_id)
            VALUES (?)
        """.trimMargin()

        return dataSource.connection.use { connection ->
            connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { ps ->
                ps.setLong(1, showingId)
                ps.executeUpdate()

                ps.generatedKeys.use { keys ->
                    if (keys.next()) keys.getLong(1) else error("No generated id")
                }
            }
        }
    }

    fun insertReservationSeat(
        reservationId: Long,
        seatId: Long,
    ): Long {
        val sql = """
            INSERT INTO reservation_seat (reservation_id, seat_id)
            VALUES (?, ?)
        """.trimMargin()

        return dataSource.connection.use { connection ->
            connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { ps ->
                ps.setLong(1, reservationId)
                ps.setLong(2, seatId)
                ps.executeUpdate()

                ps.generatedKeys.use { keys ->
                    if (keys.next()) keys.getLong(1) else error("No generated id")
                }
            }
        }
    }

    fun getReservationInfo(reservationSeatId: Long): ReservationInfo? {
        val sql =
            """                                                                                                                                                                
          SELECT s.id AS showing_id,
                 s.start_time,                                                                                                                                                       
                 s.screen_id,
                 m.id AS movie_id,                                                                                                                                                   
                 m.title,                                                                                                                                                          
                 m.running_minutes,
                 se.seat_number,                                                                                                                                                     
                 se.grade
          FROM reservation r                                                                                                                                                         
          JOIN showing s ON r.showing_id = s.id                                                                                                                                    
          JOIN movie m ON s.movie_id = m.id
          JOIN reservation_seat rs ON rs.reservation_id = r.id                                                                                                                       
          JOIN seat se ON rs.seat_id = se.id
          WHERE r.id = ?                                                                                                                                                             
            """.trimIndent()

        return dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { ps ->
                ps.setLong(1, reservationSeatId)
                ps.executeQuery().use { rs ->
                    var showing: Showing? = null
                    val seats = mutableListOf<Seat>()
                    while (rs.next()) {
                        if (showing == null) {
                            showing = Showing(
                                startTime = MovieTime(
                                    rs.getObject("start_time", LocalDateTime::class.java)
                                        .toKotlinLocalDateTime(),
                                ),
                                screen = Screen(Seats(emptyList()), Id(rs.getInt("screen_id"))),
                                movie = Movie(
                                    title = rs.getString("title"),
                                    id = Id(rs.getInt("movie_id")),
                                    runningTime = rs.getInt("running_minutes"),
                                ),
                                id = Id(rs.getInt("showing_id")),
                            )
                        }
                        val seatNumber = rs.getString("seat_number")
                        seats.add(
                            Seat(
                                coordinate = SeatCoordinate(seatNumber[0], seatNumber.substring(1).toInt()),
                                grade = SeatGrade.valueOf(rs.getString("grade")),
                                isReserved = SeatState.RESERVED,
                            ),
                        )
                    }
                    showing?.let { ReservationInfo(it, Seats(seats)) }
                }
            }
        }
    }
}
