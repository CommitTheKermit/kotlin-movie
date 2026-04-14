package controller

import domain.cart.Cart
import domain.reservation.ReservationInfo
import domain.reservation.ReservationInfos
import domain.seat.Seats
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CartControllerTest {
    val controller: CartController = CartController()

    @Test
    fun `장바구니에 예매 항목을 추가할 수 있다`() {
        // given : 선택한 상영과 좌석 정보가 주어진다.
        val cart = Cart(ReservationInfos(emptyList()))

        // when : 장바구니에 예매 항목을 추가하면
        val result = cart.addInfo(
            ReservationInfo(
                showing = TestFixtureData.firstShowing,
                seats = Seats(listOf(TestFixtureData.seatB1, TestFixtureData.seatB2)),
            ),
        )

        // then :
        assertEquals(2, result.reservationInfos.infos.first().seats.seats.size)
    }

    @Test
    fun `장바구니에 담긴 전체 항목을 조회할 수 있다`() {
        // given : 선택한 상영과 좌석 정보가 주어지고 장바구니에 예매 항목을 추가한다.
        val cart = Cart(ReservationInfos(emptyList()))

        // when : 장바구니에 담긴 전체 항목을 조회하면
        val result = cart.addInfo(
            ReservationInfo(
                showing = TestFixtureData.firstShowing,
                seats = Seats(listOf(TestFixtureData.seatB1, TestFixtureData.seatB2)),
            ),
        )

        val actual = result.reservationInfos.getAllInfos()

        // then : 전체 항목이 반환된다.
        assertEquals(1, actual.size)
        assertEquals("해리 포터", actual.first().showing.movie.title)
        assertEquals(2, actual.first().seats.seats.size)
    }
}
