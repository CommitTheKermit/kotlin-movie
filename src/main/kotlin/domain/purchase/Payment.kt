package domain.purchase

import domain.cart.Cart
import domain.user.Point
import domain.user.User

class Payment(private val cart: Cart, private val user: User) {
    fun calculate(
        usedPoint: Point,
        method: PaymentMethod,
    ): PaymentResult {
        val afterItem = Price(cart.getDiscountedItems())
        val afterPoint = afterItem.subtractPrice(usedPoint.point)

        user.point.discount(usedPoint.point)

        val final = method.discountApply(afterPoint)

        return PaymentResult(final, usedPoint)
    }
}
