package domain

import domain.seat.SeatGrade
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SeatGradeTest {
    @Test
    fun `S석의 가격은 18,000원이다`() {
        // given & when : SeatGrade에서 S석을 생성할 때
        val result = SeatGrade.S
        // then : 해당 자리의 가격은 18_000이다.
        assertEquals(18_000, result.price)
    }

    @Test
    fun `A석의 가격은 15,000원이다`() {
        // given & when : SeatGrade에서 A석을 생성할 때
        val result = SeatGrade.A
        // then : 해당 자리의 가격은 15_000이다.
        assertEquals(15_000, result.price)
    }

    @Test
    fun `B석의 가격은 12,000원이다`() {
        // given & when : SeatGrade에서 B석을 생성할 때
        val result = SeatGrade.B
        // then : 해당 자리의 가격은 12_000이다.
        assertEquals(12_000, result.price)
    }
}
