package domain.purchase

import domain.user.Point

data class PaymentResult(val totalPrice: Price, val usedPoint: Point)
