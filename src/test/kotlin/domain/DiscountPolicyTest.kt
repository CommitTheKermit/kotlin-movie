package domain

import domain.cinema.MovieTime
import domain.discount.MovieDayDiscount
import domain.discount.TimeDiscount
import domain.purchase.PaymentMethod
import domain.purchase.Price
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DiscountPolicyTest {
    @Test
    fun `매월 10일에 상영되는 영화는 10퍼센트 할인된다`() {
        // given : 날짜가 10일이다.
        val date = MovieTime(2026, 4, 10, 14, 0)
        val policy = MovieDayDiscount()

        // when : 할인을 적용하면
        val result = policy.discountApply(Price(16_000))

        // then : 적용 대상이고 14_400원이 반환된다.
        assertEquals(true, policy.isApplicable(date))
        assertEquals(14_400, result.price)
    }

    @Test
    fun `매월 20일에 상영되는 영화는 10퍼센트 할인된다`() {
        // given : 날짜가 20일이다.
        val date = MovieTime(2026, 4, 20, 14, 0)
        val policy = MovieDayDiscount()

        // when : 할인을 적용하면
        val result = policy.discountApply(Price(16_000))

        // then : 적용 대상이고 14_400원이 반환된다.
        assertEquals(true, policy.isApplicable(date))
        assertEquals(14_400, result.price)
    }

    @Test
    fun `매월 30일에 상영되는 영화는 10퍼센트 할인된다`() {
        // given : 날짜가 30일이다.
        val date = MovieTime(2026, 4, 30, 14, 0)
        val policy = MovieDayDiscount()

        // when : 할인을 적용하면
        val result = policy.discountApply(Price(16_000))

        // then : 적용 대상이고 14_400원이 반환된다.
        assertEquals(true, policy.isApplicable(date))
        assertEquals(14_400, result.price)
    }

    @Test
    fun `무비데이가 아닌 날에는 무비데이 할인이 적용되지 않는다`() {
        // given : 날짜가 1일이다.
        val date = MovieTime(2026, 4, 1, 14, 0)
        val policy = MovieDayDiscount()

        // when : 적용 여부를 확인하면
        val applicable = policy.isApplicable(date)

        // then : false가 반환된다.
        assertEquals(false, applicable)
    }

    @Test
    fun `오전 11시 이전에 시작하는 상영은 2,000원이 할인된다`() {
        // given : 오전 11시 이전 시간이 주어진다
        val time: MovieTime = MovieTime(2026, 4, 8, 10, 0)
        val policy = TimeDiscount()

        // when : 할인을 적용하면
        val result = policy.discountApply(Price(16_000))

        // then : 적용 대상이고 14_000원이 반환된다.
        assertEquals(true, policy.isApplicable(time))
        assertEquals(14_000, result.price)
    }

    @Test
    fun `오후 8시 이후에 시작하는 상영은 2,000원이 할인된다`() {
        // given : 오후 8시 이후 시간이 주어진다
        val time: MovieTime = MovieTime(2026, 4, 8, 21, 0)
        val policy = TimeDiscount()

        // when : 할인을 적용하면
        val result = policy.discountApply(Price(16_000))

        // then : 적용 대상이고 14_000원이 반환된다.
        assertEquals(true, policy.isApplicable(time))
        assertEquals(14_000, result.price)
    }

    @Test
    fun `오전 11시 ~ 오후 8시 사이에 시작하는 상영은 시간 할인이 적용되지 않는다`() {
        // given : 오후 4시 시간이 주어진다
        val time: MovieTime = MovieTime(2026, 4, 8, 16, 0)
        val policy = TimeDiscount()

        // when : 적용 여부를 확인하면
        val applicable = policy.isApplicable(time)

        // then : false가 반환된다.
        assertEquals(false, applicable)
    }

    @Test
    fun `신용카드 결제 시 5퍼센트 할인된다`() {
        // given : 결제수단으로 신용카드가 제시된다
        val method = PaymentMethod.CARD

        // when : 할인을 적용하면
        val result = method.discountApply(Price(16_000))

        // then : 15_200원이 반환된다.
        assertEquals(15_200, result.price)
    }

    @Test
    fun `현금 결제 시 2퍼센트 할인된다`() {
        // given : 결제수단으로 현금이 제시된다
        val method = PaymentMethod.CASH

        // when : 할인을 적용하면
        val result = method.discountApply(Price(16_000))

        // then : 15_680원이 반환된다.
        assertEquals(15_680, result.price)
    }
}
