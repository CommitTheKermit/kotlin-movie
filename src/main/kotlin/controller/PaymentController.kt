package controller

import domain.purchase.Payment
import domain.purchase.PaymentMethod
import domain.purchase.PaymentResult
import domain.user.Point
import view.InputView
import view.OutputView
import view.retryUntilValid

class PaymentController {
    fun run(payment: Payment): PaymentResult {
        val point = retryUntilValid { Point(InputView.readPoint().toInt()) }
        val method = retryUntilValid { PaymentMethod.from(getPaymentMethod()) }
        val result = retryUntilValid { payment.calculate(point, method) }
        OutputView.printTotalPrice(result.totalPrice.price)
        return result
    }

    private fun getPaymentMethod(): Int {
        return InputView.readPaymentMethod().toInt()
    }
}
