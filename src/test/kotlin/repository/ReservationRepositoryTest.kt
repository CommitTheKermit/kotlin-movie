package repository

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import javax.sql.DataSource
import kotlin.use
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.jdbc.datasource.DriverManagerDataSource
import repository.SchemaInitializer
import spring.repository.ReservationRepository
import spring.repository.ShowingRepository
import view.message.SeatMessages

class ReservationRepositoryTest {
    private lateinit var dataSource: DataSource
    private lateinit var repository: ReservationRepository

    private lateinit var showingRepository: ShowingRepository

    @BeforeEach
    fun setUp() {

        dataSource = DriverManagerDataSource(
            "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "",
        )
        dataSource.connection.use { SchemaInitializer.initialize(it) }
        repository = ReservationRepository(dataSource)
        showingRepository = ShowingRepository(dataSource)
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

    @Test
    fun `같은 좌석을 같은 상영에 중복 예매하면 실패한다`() {
        // given : 상영 정보, 예매가 주어지고 상영 정보들의 id를 가져와 예매를 저장한다
        val reservationInfo = TestFixtureData.reservationInfos.infos.first()
        val showing = reservationInfo.showing
        val savedShowingId = showingRepository.save(showing)
        repository.save(reservationInfo, savedShowingId)

        // when & then : 같은 좌석을 다시 예매하면 예외가 발생한다
        val exception = shouldThrow<IllegalArgumentException> {
            repository.save(reservationInfo, savedShowingId)
        }
        exception.message shouldBe SeatMessages.ERROR_SEAT_ALREADY_RESERVED
    }
}
