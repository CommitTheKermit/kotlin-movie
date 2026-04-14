package domain.discount

import domain.cinema.MovieTime
import domain.purchase.Price

class MovieDayDiscount : DiscountPolicy {
    override fun isApplicable(at: MovieTime): Boolean {
        return movieDay.contains(at.day)
    }

    override fun discountApply(base: Price): Price {
        return base.subtractPrice((base.price * MOVIE_DAY_DISCOUNT_PERCENT).toInt())
    }

    companion object {
        private val movieDay = listOf(10, 20, 30)
        private const val MOVIE_DAY_DISCOUNT_PERCENT = 0.1
    }
}
