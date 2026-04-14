package controller

import domain.cart.Cart
import domain.purchase.Payment
import domain.purchase.PaymentMethod
import domain.reservation.ReservationInfos
import domain.user.Point
import java.io.ByteArrayInputStream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PaymentControllerTest {

    val controller = PaymentController()

    @Test
    fun `좌석 별로 무비데이 할인(10퍼센트)과 시간 할인(2,000원)이 적용된다`() {
        // given & when : 무비데이 할인과 시간 할인을 적용한다.
        val result = TestFixtureData.cart.getDiscountedItems()

        // then : 할인된 금액이 반환된다.
        assertEquals(27700, result)
    }

    @Test
    fun `사용할 포인트가 보유 포인트보다 크면 예외가 발생한다`() {
        // given : 사용자의 포인트보다 더 큰 포인트가 입력된다.
        val user = TestFixtureData.users.first()
        val cart = TestFixtureData.cart

        // when : 포인트를 처리하면
        val exception = assertThrows<IllegalArgumentException> {
            Payment(
                Cart(ReservationInfos(emptyList())),
                TestFixtureData.users.first(),
            ).calculate(Point(3000), PaymentMethod.CARD)
        }

        // then : 예외가 발생한다.
        assertEquals("차감액은 전체 포인트보다 작아야 합니다.", exception.message)
    }

    @Test
    fun `결제 수단 입력이 유효하지 않으면 예외가 발생한다`() {
        // given : 유효하지 않은 결제 수단 번호가 입력된다.
        val invalidMethod = 3

        // when & then : 예외가 발생한다.
        val exception = assertThrows<IllegalArgumentException> {
            PaymentMethod.from(invalidMethod)
        }
        assertEquals("유효하지 않은 결제 수단입니다.", exception.message)
    }

    @Test
    fun `결제 수단 할인(신용카드 5퍼센트, 현금 2퍼센트)이 적용된다`() {
        // given : 결제 수단으로 신용카드가 제시된다.
        val input = "0\n1"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        val payment = Payment(
            Cart(TestFixtureData.reservationInfos),
            TestFixtureData.users.first(),
        )

        // when : 결제 수단을 적용하면
        val result = controller.run(payment)

        // then : 할인된 금액이 반환된다.
        assertEquals(26_315, result.totalPrice.price)
    }

    @Test
    fun `모든 할인이 순서대로 적용된 최종 금액을 계산한다`() {
        // given : 각각의 입력값을 받는다.
        val input = "2000\n1\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when : 결제를 적용하면
        val result = controller.run(
            Payment(
                Cart(TestFixtureData.reservationInfos),
                TestFixtureData.users.first(),
            ),
        )

        // then : 할인된 총 금액이 반환된다.
        assertEquals(24_415, result.totalPrice.price)
        assertEquals(2000, result.usedPoint.point)
    }
}
