package controller

import java.io.ByteArrayInputStream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ReservationControllerTest {

    @Test
    fun `영화 선택부터 좌석 선택까지 전체 예매 플로우가 동작한다`() {
        // given : 영화 제목, 날짜, 상영 번호, 좌석을 순서대로 입력한다.
        val controller = ReservationController(TestFixtureData.emptyMovieTheater)
        setInput("해리 포터\n2026-04-10\n1\nB1,B2\n")

        // when : 예매를 진행하면
        val result = controller.run()

        // then : 선택한 영화와 좌석이 담긴 예매 정보가 반환된다.
        assertEquals(TestFixtureData.MOVIE_HARRY_POTTER, result.showing.movie.title)
        assertEquals(listOf(TestFixtureData.seatB1, TestFixtureData.seatB2), result.seats.seats)
    }

    @Test
    fun `단일 좌석으로 예매할 수 있다`() {
        // given : 영화 제목, 날짜, 상영 번호, 좌석을 순서대로 입력한다.
        val controller = ReservationController(TestFixtureData.emptyMovieTheater)
        setInput("기생충\n2026-04-10\n1\nB1\n")

        // when : 예매를 진행하면
        val result = controller.run()

        // then : 선택한 영화와 좌석이 담긴 예매 정보가 반환된다.
        assertEquals("기생충", result.showing.movie.title)
        assertEquals(listOf(TestFixtureData.seatB1), result.seats.seats)
    }

    @Test
    fun `존재하지 않는 영화 제목이면 예외가 발생한다`() {
        // given & when : 존재하지 않는 영화 제목으로 검색하면
        val exception = assertThrows<IllegalArgumentException> {
            TestFixtureData.movieTheater.movies.findMovieByTitle(TestFixtureData.NON_EXISTENT_MOVIE)
        }

        // then : 예외가 발생한다.
        assertEquals("존재하지 않는 영화입니다.", exception.message)
    }

    @Test
    fun `존재하는 영화 제목이면 해당 영화를 반환한다`() {
        // given & when : 존재하는 영화 제목으로 검색하면
        val result = TestFixtureData.movieTheater.movies.findMovieByTitle(TestFixtureData.MOVIE_HARRY_POTTER)

        // then : 영화가 반환된다.
        assertEquals(TestFixtureData.movieTheater.movies.movies.first(), result)
    }

    @Test
    fun `좌석 입력 형식이 올바르지 않으면 예외가 발생한다`() {
        // given & when : 올바르지 않은 형식의 좌석을 입력하면
        val exception = assertThrows<IllegalArgumentException> {
            TestFixtureData.firstScreen.seats.checkSeat(TestFixtureData.INVALID_SEAT_FORMAT)
        }

        // then : 예외가 발생한다.
        assertEquals("입력된 값이 유효하지 않습니다.", exception.message)
    }

    @Test
    fun `상영관에 존재하지 않는 좌석이면 예외가 발생한다`() {
        // given & when : 존재하지 않는 좌석을 입력하면
        val exception = assertThrows<IllegalArgumentException> {
            TestFixtureData.firstScreen.seats.checkSeat(TestFixtureData.NON_EXISTENT_SEAT)
        }

        // then : 예외가 발생한다.
        assertEquals("해당 상영관에는 해당 좌석이 존재하지 않습니다.", exception.message)
    }

    @Test
    fun `쉼표로 구분된 여러 좌석을 파싱하면 좌석 리스트를 반환한다`() {
        // given & when : ,로 구분된 좌석 번호들을 입력하면
        val result = TestFixtureData.firstScreen.selectSeats(TestFixtureData.SEAT_B1_B2.split(","))

        // then : 좌석 리스트가 반환된다.
        assertEquals(listOf(TestFixtureData.seatB1, TestFixtureData.seatB2), result.seats)
    }

    @Test
    fun `이미 예약된 좌석이면 예외가 발생한다`() {
        // given & when : 이미 예약된 좌석을 입력하면
        val exception = assertThrows<IllegalArgumentException> {
            TestFixtureData.firstScreen.seats.checkSeat(TestFixtureData.SEAT_A1)
        }

        // then : 예외가 발생한다.
        assertEquals("해당 좌석은 이미 예약되었습니다.", exception.message)
    }

    @Test
    fun `예약되지 않고 존재하는 좌석이면 해당 좌석을 반환한다`() {
        // given & when : 존재하는 좌석을 입력하면
        val result = TestFixtureData.firstScreen.selectSeats(listOf(TestFixtureData.SEAT_B1))

        // then : 좌석 리스트가 반환된다.
        assertEquals(listOf(TestFixtureData.seatB1), result.seats)
    }

    private fun setInput(input: String) {
        System.setIn(ByteArrayInputStream(input.toByteArray()))
    }
}
