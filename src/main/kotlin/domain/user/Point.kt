package domain.user

import view.message.UserMessages

class Point(val point: Int) {
    fun discount(discount: Int): Point {
        require(point - discount >= 0) { UserMessages.ERROR_INSUFFICIENT_POINT }

        return Point(point - discount)
    }
}
