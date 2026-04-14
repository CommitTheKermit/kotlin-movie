package domain.reservation

import domain.cinema.Showing
import domain.discount.MovieDayDiscount
import domain.discount.TimeDiscount
import domain.seat.Seats

class ReservationInfo(val showing: Showing, val seats: Seats) {
    fun applyDiscount(): Int {
        var initPrice = seats.getAllPrice()

        val movieDayDiscount = MovieDayDiscount()
        if (movieDayDiscount.isApplicable(showing.startTime)) {
            initPrice = movieDayDiscount.discountApply(initPrice)
        }

        val timeDiscount = TimeDiscount()
        if (timeDiscount.isApplicable(showing.startTime)) {
            initPrice = timeDiscount.discountApply(initPrice)
        }

        return initPrice.price
    }
}
