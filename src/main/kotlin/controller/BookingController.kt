package controller

import domain.cart.Cart
import domain.purchase.Payment
import domain.user.User
import view.InputView
import view.OutputView
import view.retryUntilValid

class BookingController(
    private val reservationController: ReservationController,
    private val cartController: CartController,
    private val paymentController: PaymentController,
    private val user: User,
) {
    fun run(inputCart: Cart) {
        var cart = inputCart
        var answer = retryUntilValid { InputView.startTicketing() }
        while (answer.isYes()) {
            val info = reservationController.run()
            cart = cartController.run(cart, info)
            answer = retryUntilValid { InputView.continueTicketing() }
        }

        val result = paymentController.run(Payment(cart, user))
        if (!retryUntilValid {
                InputView.readPurchaseConfirm()
            }.isYes()
        ) return

        OutputView.printTotal(
            cart = cart,
            totalPrice = result.totalPrice,
            usedPoint = result.usedPoint,
        )
    }
}
