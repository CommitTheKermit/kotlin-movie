package domain

import domain.cinema.MovieTime
import domain.discount.DiscountPolicy
import domain.discount.MovieDayDiscount
import domain.discount.TimeDiscount
import domain.purchase.PaymentMethod
import domain.purchase.Price
import domain.user.Point
import domain.user.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CalculatorTest {
    @Test
    fun `무비데이에 상영되는 영화는 10퍼센트 할인이 적용된다`() {
        // given : 날짜가 10일이고, 영화가 16_000원이다.
        val price = Price(16_000)
        val date = MovieTime(2026, 4, 10, 14, 0)
        val policy = MovieDayDiscount()

        // when : 할인율을 적용하면
        val result = policy.discountApply(price)

        // then : 14_400원이 반환된다.
        assertEquals(true, policy.isApplicable(date))
        assertEquals(14_400, result.price)
    }

    @Test
    fun `할인 시간에 상영되는 영화는 2000원 할인이 적용된다`() {
        // given : 시간이 10시이고, 영화가 16_000원이다.
        val price = Price(16_000)
        val date = MovieTime(2026, 4, 10, 10, 0)
        val policy = TimeDiscount()

        // when : 할인액을 적용하면
        val result = policy.discountApply(price)

        // then : 14_000원이 반환된다.
        assertEquals(true, policy.isApplicable(date))
        assertEquals(14_000, result.price)
    }

    @Test
    fun `무비데이도 아니고 할인 시간도 아닌 영화는 할인이 적용되지 않는다`() {
        // given : 날짜가 11일이고 시간이 14시이고, 영화가 16_000원이다.
        val price = Price(16_000)
        val date = MovieTime(2026, 4, 11, 14, 0)
        val policies: List<DiscountPolicy> = listOf(MovieDayDiscount(), TimeDiscount())

        // when : 할인을 적용하면
        val result = policies
            .filter { it.isApplicable(date) }
            .fold(price) { acc, policy -> policy.discountApply(acc) }

        // then : 16_000원이 반환된다.
        assertEquals(16_000, result.price)
    }

    @Test
    fun `무비데이 할인과 시간 할인이 순서대로 적용된다`() {
        // given : 날짜가 10일이고 시간이 10시이고, 영화가 16_000원이다.
        val price = Price(16_000)
        val date = MovieTime(2026, 4, 10, 10, 0)
        val policies: List<DiscountPolicy> = listOf(MovieDayDiscount(), TimeDiscount())

        // when : 할인을 적용하면
        val result = policies
            .filter { it.isApplicable(date) }
            .fold(price) { acc, policy -> policy.discountApply(acc) }

        // then : 12_400원이 반환된다.
        assertEquals(12_400, result.price)
    }

    @Test
    fun `포인트가 예매 금액에서 직접 차감된다`() {
        // given : user는 2000 포인트를 가지고 있다.
        val user = User(Id(2))
        val price = Price(16_000)

        // when : 사용 포인트가 2000이면
        val result = price.subtractPrice(2000)
        val resultUser = user.discountPoint(Point(2000))

        // then : user의 포인트는 0이 되고 14_000원이 반환된다.
        assertEquals(14_000, result.price)
        assertEquals(0, resultUser.point.point)
    }

    @Test
    fun `결제 수단에 따라 할인이 적용된다`() {
        // given : 결제 수단은 카드이다.
        val price = Price(16_000)
        val method = PaymentMethod.CARD

        // when : 할인을 적용하면
        val result = method.discountApply(price)

        // then : 15_200원이 반환된다.
        assertEquals(15_200, result.price)
    }
}
