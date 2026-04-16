package domain.seat

import view.message.SeatMessages

data class SeatCoordinate(val row: Char, val column: Int) {
    init {
        require(row.isUpperCase()) { SeatMessages.ERROR_INVALID_ROW }
        require(column > 0) { SeatMessages.ERROR_INVALID_COLUMN }
    }

    override fun toString(): String {
        return "$row$column"
    }

    companion object {
        private val FORMAT = Regex("^[A-Z][0-9]+$")

        fun from(input: String): SeatCoordinate {
            require(FORMAT.matches(input)) { SeatMessages.ERROR_INVALID_FORMAT }
            return SeatCoordinate(input[0], input.substring(1).toInt())
        }
    }
}
