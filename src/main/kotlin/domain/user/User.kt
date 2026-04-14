package domain.user

import domain.Id
import view.message.UserMessages

data class User(val id: Id, val point: Point = Point(2000)) {
    init {
        require(id.value > 0) { UserMessages.ERROR_INVALID_ID }
    }

    fun discountPoint(discount: Point): User {
        return copy(point = point.discount(discount.point))
    }
}
