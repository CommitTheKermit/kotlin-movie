package controller

import client.MovieApi
import client.MovieTheaterLoader
import client.ReservationApi
import client.SeatApi
import client.ShowingApi
import client.api.ApiClientFactory
import domain.cart.Cart
import domain.purchase.Payment
import domain.reservation.ReservationInfos
import java.io.ByteArrayInputStream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BookingIntegrationTest {
    @Test
    fun `영화 예매부터 결제까지 전체 흐름이 동작한다`() {
        // given : 예매 시나리오
        //  - 영화: 기생충 (예약 내역 없는 showings[2], 2026-04-10 21:00)
        //  - 좌석: B1 (A등급 15_000원)
        //  - 포인트: 1_000 사용
        //  - 결제 수단: 신용카드(5%)
        //
        // 기대 금액 계산:
        //  15_000 → MovieDay 10% 할인 → 13_500
        //         → 21시 시간 할인 -2_000 → 11_500
        //         → 포인트 1_000 차감 → 10_500
        //         → 카드 5% 할인 → 9_975
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

        val apiClientFactory = ApiClientFactory("http://localhost:8080")
        val movieApi = apiClientFactory.create<MovieApi>()
        val reservationApi = apiClientFactory.create<ReservationApi>()
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

        // when : 예매 → 장바구니 → 결제
        val info = reservationController.run()
        cart = cartController.run(cart, info)

        val paymentController = PaymentController()
        val result = paymentController.run(Payment(cart, TestFixtureData.users.first()))

        // then : 장바구니에 1건이 담기고, 최종 결제 금액과 사용 포인트가 기대값과 일치한다.
        assertEquals(1, cart.reservationInfos.infos.size)
        assertEquals(TestFixtureData.showings[0].startTime.value, cart.reservationInfos.infos.first().showing.startTime.value)
        assertEquals(7_410, result.totalPrice.price)
        assertEquals(1_000, result.usedPoint.point)
    }
}
