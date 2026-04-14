package domain.purchase

import view.message.PurchaseMessages

enum class PaymentMethod {
    CARD,
    CASH,
    ;

    fun discountApply(base: Price): Price {
        return when (this) {
            CARD -> base.subtractPrice((base.price * CARD_DISCOUNT_PERCENT).toInt())
            CASH -> base.subtractPrice((base.price * CASH_DISCOUNT_PERCENT).toInt())
        }
    }

    companion object {
        const val CARD_DISCOUNT_PERCENT = 0.05
        const val CASH_DISCOUNT_PERCENT = 0.02

        fun from(index: Int): PaymentMethod {
            require(index in 1..entries.size) { PurchaseMessages.ERROR_INVALID_METHOD }
            return entries[index - 1]
        }
    }
}
