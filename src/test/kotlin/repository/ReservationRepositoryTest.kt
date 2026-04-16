package repository

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
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import java.time.LocalDateTime
import kotlin.use
import kotlinx.datetime.toKotlinLocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ReservationRepository(val connection: Connection) {
    fun save(
        reservationInfo: ReservationInfo,
        showingId: Long,
    ): List<Long> {
        val savedIds = reservationInfo.seats.seats.map {
            val seatId = getSeatId(it)
            val reservationId = insertReservation(showingId)

            insertReservationSeat(reservationId, seatId)
        }

        return savedIds
    }

    fun getSeatId(seat: Seat): Long {
        val sql = """
            SELECT id
            FROM seat
            WHERE seat_number = ?
        """.trimMargin()
        val seatNumber: String = "${seat.coordinate.row}${seat.coordinate.column}"

        return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { ps ->
            ps.setString(1, seatNumber)
            ps.executeQuery().use { rs ->
                if (rs.next()) rs.getLong("id")
                else error("Seat not found: $seatNumber")
            }
        }
    }

    fun insertReservation(showingId: Long): Long {
        val sql = """
            INSERT INTO reservation (showing_id)
            VALUES (?)
        """.trimMargin()

        return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { ps ->
            ps.setLong(1, showingId)
            ps.executeUpdate()

            ps.generatedKeys.use { keys ->
                if (keys.next()) keys.getLong(1) else error("No generated id")
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

        return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { ps ->
            ps.setLong(1, reservationId)
            ps.setLong(2, seatId)
            ps.executeUpdate()

            ps.generatedKeys.use { keys ->
                if (keys.next()) keys.getLong(1) else error("No generated id")
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

        return connection.prepareStatement(sql).use { ps ->
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

class ReservationRepositoryTest {
    private lateinit var connection: Connection
    private lateinit var repository: ReservationRepository

    private lateinit var showingRepository: ShowingRepository

    @BeforeEach
    fun setUp() {
        connection = DriverManager.getConnection(
            "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "",
        )
        repository = ReservationRepository(connection)

        showingRepository = ShowingRepository(connection)
        SchemaInitializer.initialize(connection)
    }

    @Test
    fun `저장한 예매를 조회하면 상영, 좌석, 금액이 복원된다`() {
        // given : 상영 정보, 예매가 주어지고 상영 정보들의 id를 가져와 예매를 저장한다
        val reservationInfo = TestFixtureData.reservationInfos.infos.first()
        val showing = reservationInfo.showing
        val savedShowingId = showingRepository.save(showing)
        val reservationSeatIds = repository.save(reservationInfo, savedShowingId)

        // when : 예메 정보를 조회하면
        val found = repository.getReservationInfo(
            reservationSeatId = reservationSeatIds.first(),
        )

        // then : 주어졌던 상영정보의 영화와 시작 시간이 반환된다.
        assertThat(found?.showing?.movie?.title)
            .isEqualTo(reservationInfo.showing.movie.title)
        assertThat(found?.showing?.startTime)
            .isEqualTo(reservationInfo.showing.startTime)
        assertThat(found?.seats?.seats?.map { "${it.coordinate.row}${it.coordinate.column}" })
            .containsExactlyInAnyOrderElementsOf(
                reservationInfo.seats.seats.map { "${it.coordinate.row}${it.coordinate.column}" },
            )
    }
}
