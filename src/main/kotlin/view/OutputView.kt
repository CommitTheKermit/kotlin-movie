package view

import domain.cart.Cart
import domain.cinema.MovieTime
import domain.cinema.Screen
import domain.cinema.Showings
import domain.purchase.Price
import domain.reservation.ReservationInfo
import domain.user.Point
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.char
import view.message.OutputMessage

object OutputView {
    fun printShowing(showings: Showings) {
        println(OutputMessage.SHOWING_LIST_HEADER)

        showings.showings.forEachIndexed { index, showing ->
            println("[${index + 1}] ${showing.startTime.time}")
        }
    }

    fun printSeats(screen: Screen) {

        println(OutputMessage.SEAT_MAP_HEADER)
        val header = " ".repeat(3) + (1..Screen.MAX_COLUMN).joinToString("    ")
        println(header)
        printSeatRows(screen)
        println()
    }

    fun printSeatRows(screen: Screen) {
        ('A' until 'A' + Screen.MAX_ROW).forEach { row ->
            val line = "$row" + (1..Screen.MAX_COLUMN).joinToString("") { col ->
                val seat = screen.seats.seats.find { it.coordinate.row == row && it.coordinate.column == col }
                " [ ${seat?.grade?.name ?: " "}]"
            }
            println(line)
        }
    }

    fun printCart(items: List<ReservationInfo>) {
        println(OutputMessage.CART_HEADER)
        items.forEach { println(formatReservationInfo(it)) }
        println()
    }

    private fun formatReservationInfo(info: ReservationInfo): String {
        val time = formatMovieTime(info.showing.startTime)
        val seats = info.seats.seats.joinToString(", ") { it.coordinate.toString() }
        return "- [${info.showing.movie.title}] $time 좌석: $seats"
    }

    private fun formatMovieTime(movieTime: MovieTime): String {
        val formatter = LocalDateTime.Format {
            year()
            char('-')
            monthNumber()
            char('-')
            day()
            char(' ')
            hour()
            char(':')
            minute()
        }
        return formatter.format(movieTime.value)
    }

    fun printTotalPrice(price: Int) {
        println(OutputMessage.PRICE_CALCULATION_HEADER)
        println(OutputMessage.FINAL_PRICE_FORMAT.format(printByDecimalFormat(price)))
        println()
    }

    fun printByDecimalFormat(price: Int): String {
        return String.format("%,d", price)
    }

    fun printError(message: String) {
        println("${OutputMessage.ERROR_PREFIX}$message")
    }

    fun printTotal(
        cart: Cart,
        totalPrice: Price,
        usedPoint: Point,
    ) {
        println(OutputMessage.BOOKING_COMPLETE)
        println(OutputMessage.DETAILS_HEADER)
        printCart(cart.showItems())

        println(OutputMessage.PAYMENT_SUMMARY_FORMAT.format(printByDecimalFormat(totalPrice.price), printByDecimalFormat(usedPoint.point)))

        println()
        println(OutputMessage.THANK_YOU)
    }
}
