package view

import domain.cinema.MovieTime
import kotlinx.datetime.LocalDate
import view.message.InputMessage

object InputView {
    fun readLine(): String {
        val input = readln()
        println()
        return input
    }

    fun startTicketing(): YesNo {
        println(InputMessage.START_TICKETING)
        return YesNo.from(readLine())
    }

    fun continueTicketing(): YesNo {
        println(InputMessage.CONTINUE_TICKETING)
        return YesNo.from(readLine())
    }

    fun readMovieTitle(): String {
        println(InputMessage.INPUT_MOVIE_TITLE)
        val input = readLine()
        return input
    }

    fun readDate(): MovieTime {
        println(InputMessage.INPUT_DATE)
        val input = readLine()
        val date = runCatching { LocalDate.parse(input) }.getOrNull()
            ?: throw IllegalArgumentException(InputMessage.ERROR_INVALID_DATE_FORMAT)
        return MovieTime.from(date)
    }

    fun readShowingNumber(): String {
        println(InputMessage.INPUT_SHOWING_NUMBER)
        val input = readLine()
        return input
    }

    fun readSeat(): List<String> {
        println(InputMessage.INPUT_SEAT)
        val input = readLine()
        val inputs = input.split(',').map { it.trim() }
        return inputs
    }

    fun readPurchaseConfirm(): YesNo {
        println(InputMessage.CONFIRM_PURCHASE)
        return YesNo.from(readLine())
    }

    fun readPoint(): String {
        println(InputMessage.INPUT_POINT)
        val input = readLine()
        return input
    }

    fun readPaymentMethod(): String {
        println(InputMessage.SELECT_PAYMENT_METHOD)
        val input = readLine()
        return input
    }
}
