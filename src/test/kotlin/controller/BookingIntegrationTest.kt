package controller

import TestFixtureData
import client.MovieApi
import client.MovieTheaterLoader
import client.ReservationApi
import client.ReservationRegistrator
import client.SeatApi
import client.ShowingApi
import client.api.ApiClientFactory
import domain.cart.Cart
import domain.purchase.Payment
import domain.reservation.ReservationInfos
import domain.seat.SeatCoordinate
import java.io.ByteArrayInputStream
import javax.sql.DataSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import repository.SchemaInitializer
import spring.repository.ReservationRepository
import spring.repository.ShowingRepository

@SpringBootTest(
    classes = [spring.Application::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
class BookingIntegrationTest(
    @param:LocalServerPort val port: Int,
    @Autowired val dataSource: DataSource,
    @Autowired val showingRepository: ShowingRepository,
    @Autowired val reservationRepository: ReservationRepository,
) {
    private lateinit var apiClientFactory: ApiClientFactory

    companion object {
        private const val FIRST_SHOWING_ID = 1L
    }

    @BeforeEach
    fun setUp() {
        dataSource.connection.use { SchemaInitializer.initialize(it) }
        TestFixtureData.showings.showings.forEach { showingRepository.save(it) }
        apiClientFactory = ApiClientFactory("http://localhost:$port")
    }

    @Test
    fun `영화 예매부터 결제까지 전체 흐름이 동작한다`() {
        val scenario = listOf(
            "해리 포터",
            "2025-09-20",
            "1",
            "B1",
            "1000",
            "1",
            "Y",
        ).joinToString("\n")
        System.setIn(ByteArrayInputStream(scenario.toByteArray()))

        val movieApi = apiClientFactory.create<MovieApi>()
        val showingApi = apiClientFactory.create<ShowingApi>()
        val seatApi = apiClientFactory.create<SeatApi>()

        val movieTheater = MovieTheaterLoader(
            movieApi = movieApi,
            showingApi = showingApi,
            seatApi = seatApi,
        ).load()

        val reservationController = ReservationController(movieTheater)
        val cartController = CartController()
        var cart = Cart(ReservationInfos(emptyList()))

        val info = reservationController.run()
        cart = cartController.run(cart, info)

        val paymentController = PaymentController()
        val result = paymentController.run(Payment(cart, TestFixtureData.users.first()))

        assertEquals(1, cart.reservationInfos.infos.size)
        assertEquals(TestFixtureData.showings[0].startTime.value, cart.reservationInfos.infos.first().showing.startTime.value)
        assertEquals(7_410, result.totalPrice.price)
        assertEquals(1_000, result.usedPoint.point)
    }

    @Test
    fun `구매 확정 이후 서버에 예매가 등록된다`() {
        // given : 예매 → 장바구니 → 결제 → 구매 확정(Y) 까지 입력 시나리오
        val scenario = listOf(
            "Y",
            "해리 포터",
            "2025-09-20",
            "1",
            "B1",
            "N",
            "1000",
            "1",
            "Y",
        ).joinToString("\n")
        System.setIn(ByteArrayInputStream(scenario.toByteArray()))

        val movieApi = apiClientFactory.create<MovieApi>()
        val reservationApi = apiClientFactory.create<ReservationApi>()
        val showingApi = apiClientFactory.create<ShowingApi>()
        val seatApi = apiClientFactory.create<SeatApi>()

        val movieTheater = MovieTheaterLoader(
            movieApi = movieApi,
            showingApi = showingApi,
            seatApi = seatApi,
        ).load()

        // when : BookingController 전체 흐름을 실행하면
        BookingController(
            reservationController = ReservationController(movieTheater),
            cartController = CartController(),
            paymentController = PaymentController(),
            reservationRegistrar = ReservationRegistrator(reservationApi),
            user = TestFixtureData.users.first(),
        ).run(Cart(ReservationInfos(emptyList())))

        // then : 서버의 해당 상영에 선택 좌석이 예약돼 있어야 한다
        val reservedSeats = reservationRepository.findReservedSeatNumbers(FIRST_SHOWING_ID)
        assertThat(reservedSeats).contains(SeatCoordinate('B', 1))
    }

    @Test
    fun `구매 확정을 거절하면 서버에 예매가 등록되지 않는다`() {
        // given : 마지막에 구매 확정을 'N'으로 응답하는 시나리오
        val scenario = listOf(
            "Y",
            "해리 포터",
            "2025-09-20",
            "1",
            "B1",
            "N",
            "1000",
            "1",
            "N",
        ).joinToString("\n")
        System.setIn(ByteArrayInputStream(scenario.toByteArray()))

        val movieApi = apiClientFactory.create<MovieApi>()
        val reservationApi = apiClientFactory.create<ReservationApi>()
        val showingApi = apiClientFactory.create<ShowingApi>()
        val seatApi = apiClientFactory.create<SeatApi>()

        val movieTheater = MovieTheaterLoader(
            movieApi = movieApi,
            showingApi = showingApi,
            seatApi = seatApi,
        ).load()

        // when : BookingController 전체 흐름을 실행하면
        BookingController(
            reservationController = ReservationController(movieTheater),
            cartController = CartController(),
            paymentController = PaymentController(),
            reservationRegistrar = ReservationRegistrator(reservationApi),
            user = TestFixtureData.users.first(),
        ).run(Cart(ReservationInfos(emptyList())))

        // then : 서버에는 예약이 남아있지 않아야 한다
        val reservedSeats = reservationRepository.findReservedSeatNumbers(FIRST_SHOWING_ID)
        assertThat(reservedSeats).isEmpty()
    }
}
