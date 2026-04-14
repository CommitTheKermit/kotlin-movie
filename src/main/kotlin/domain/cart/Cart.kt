package domain.cart

import domain.reservation.ReservationInfo
import domain.reservation.ReservationInfos

data class Cart(val reservationInfos: ReservationInfos) {
    fun addInfo(info: ReservationInfo): Cart {
        return copy(
            reservationInfos = ReservationInfos(
                reservationInfos.infos + info,
            ),
        )
    }
    fun showItems(): List<ReservationInfo> {
        return reservationInfos.getAllInfos()
    }

    fun getDiscountedItems(): Int {
        return reservationInfos.applyAllDiscount()
    }
}
