package domain.discount

import domain.cinema.MovieTime
import domain.purchase.Price
import kotlinx.datetime.LocalTime

class TimeDiscount : DiscountPolicy {
    override fun isApplicable(at: MovieTime): Boolean {
        return at.time <= noneDiscountTimeBoundary.first || at.time >= noneDiscountTimeBoundary.second
    }

    override fun discountApply(base: Price): Price {
        return base.subtractPrice(TIME_DISCOUNT_PRICE)
    }

    companion object {
        private val noneDiscountTimeBoundary =
            LocalTime(11, 0) to LocalTime(20, 0)
        const val TIME_DISCOUNT_PRICE = 2000
    }
}
