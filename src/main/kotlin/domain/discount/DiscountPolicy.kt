package domain.discount

import domain.cinema.MovieTime
import domain.purchase.Price

interface DiscountPolicy {
    fun isApplicable(at: MovieTime): Boolean
    fun discountApply(base: Price): Price
}
