package domain.reservation

import domain.cinema.Showing
import view.message.ReservationMessages

class ReservationInfos(val infos: List<ReservationInfo>) {
    fun getAllInfos(): List<ReservationInfo> {
        return infos.toList()
    }

    fun checkReservationHistory(showing: Showing) {
        val history = infos.filter {
            showing.startTime >= it.showing.startTime && showing.startTime <= it.showing.endTime
        }

        require(history.isEmpty()) { ReservationMessages.ERROR_TIME_OVERLAP }
    }

    fun applyAllDiscount(): Int {
        return infos.sumOf {
            it.applyDiscount()
        }
    }
}
