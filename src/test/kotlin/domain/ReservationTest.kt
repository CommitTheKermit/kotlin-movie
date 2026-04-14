package domain

import domain.cinema.MovieTime
import domain.cinema.Showing
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ReservationTest {
    @Test
    fun `영화의 id가 존재하지 않으면 예외가 발생한다`() {
        // given : 확인하려는 영화의 id는 10이다.(존재하지 않는다.)
        val movieId = Id(10)

        // when : 전체 영화 리스트에서 영화를 확인하면
        val exception = assertThrows<IllegalArgumentException> {
            TestFixtureData.movieTheater.movies.findMovieById(movieId)
        }
        // then : 예외가 발생한다.
        assertEquals("해당 영화는 존재하지 않습니다.", exception.message)
    }

    @Test
    fun `영화의 id가 존재할 경우, 해당 영화를 반환한다`() {
        // given : 확인하려는 영화의 id는 1이다.
        val movieId = Id(1)

        // when : 영화를 찾으면
        val result = TestFixtureData.movieTheater.movies.findMovieById(movieId)

        // then : 해당 영화가 반환된다.
        assertEquals(TestFixtureData.movies.movies.first(), result)
    }

    @Test
    fun `예매 영화와 예매 날짜를 입력하였을 때 해당하는 상영 일정을 반환한다`() {
        // given : 예매 영화의 id는 1이고, 예매 날짜는 2026-4-10일이다.
        val movieId = Id(1)
        val movieTime = MovieTime.from(LocalDate.parse("2026-04-10"))

        // when : 전체 영화 리스트에서 영화를 확인하고, 전체 상영 일정에서 해당 영화와 예매 날짜를 검색하면
        val movie = TestFixtureData.movieTheater.movies.findMovieById(movieId)
        val result = TestFixtureData.movieTheater.showings.findByMovieAndDate(movie, movieTime)

        // then : 해당하는 상영 일정을 반환한다.
        assertEquals(listOf<Showing>(TestFixtureData.showings.first()), result.showings)
    }

    @Test
    fun `예매 영화와 예매 날짜를 입력하였을 때 해당하는 상영 일정이 없을 경우, 예외가 발생한다`() {
        // given : 예매 영화의 id는 1이고, 예매 날짜는 2026-4-11일이다.
        val movieId = Id(1)
        val movieTime = MovieTime.from(LocalDate.parse("2026-04-11"))

        // when : 전체 영화 리스트에서 영화를 확인하고, 전체 상영 일정에서 해당 영화와 예매 날짜를 검색하면
        val movie = TestFixtureData.movieTheater.movies.findMovieById(movieId)
        val exception = assertThrows<IllegalArgumentException> {
            TestFixtureData.movieTheater.showings.findByMovieAndDate(movie, movieTime)
        }
        // then : 예외가 발생한다.
        assertEquals("해당 영화는 해당 날짜에 상영되지 않습니다.", exception.message)
    }

    @Test
    fun `해당 날짜에 상영이 있으면 선택한 번호의 상영을 반환한다`() {
        // given : 해당 날짜에 상영이 있는 영화와 유효한 상영 번호를 준비한다.
        val movie = TestFixtureData.movieTheater.movies.movies[2]
        val movieTime = MovieTime.from(LocalDate.parse("2026-04-10"))

        // when : 상영 번호로 상영을 조회하면
        val result = TestFixtureData.movieTheater.showings.findAvailableShowing(
            movie,
            movieTime,
            "1",
            TestFixtureData.movieTheater.reservationInfos,
        )

        // then : 해당하는 상영이 반환된다.
        assertEquals(TestFixtureData.movieTheater.showings[2], result)
    }

    @Test
    fun `상영 번호가 유효 범위 밖이면 예외가 발생한다`() {
        // given : 존재하는 영화와 날짜, 그리고 유효 범위 밖의 상영 번호를 준비한다.
        val movie = TestFixtureData.movieTheater.movies.movies.first()
        val movieTime = MovieTime.from(LocalDate.parse("2026-04-10"))

        // when : 유효 범위 밖의 번호로 상영을 조회하면
        val exception = assertThrows<IllegalArgumentException> {
            TestFixtureData.movieTheater.showings.findAvailableShowing(
                movie,
                movieTime,
                "2",
                TestFixtureData.movieTheater.reservationInfos,
            )
        }

        // then : 예외가 발생한다.
        assertEquals("선택하신 상영 번호는 없는 상영 번호입니다.", exception.message)
    }

    @Test
    fun `동일한 시간대에 이미 예매한 내역이 있으면 예외가 발생한다`() {
        // given : 사용자의 id 는 1이다. 사용자는 해당 시간에 예매한 내역이 있다.
        val userId = 1
        val showing = TestFixtureData.showings[0]

        // when : 사용자가 해당 시간에 예약하려고 할 때
        val exception = assertThrows<IllegalArgumentException> {
            TestFixtureData.reservationInfos.checkReservationHistory(showing)
        }
        // then : 예외가 발생한다.
        assertEquals("선택하신 상영 시간이 겹칩니다. 다른 시간을 선택해 주세요.", exception.message)
    }

    @Test
    fun `좌석 정보가 (알파벳)(숫자) 형식이 아니면 예외가 발생한다`() {
        // given : 입력값이 11이다.
        val seat = "11"

        // when : 사용자가 해당 좌석을 예약하려고 할 때
        val exception = assertThrows<IllegalArgumentException> {
            TestFixtureData.screens.first().seats.checkSeat(seat)
        }
        // then : 예외가 발생한다.
        assertEquals("입력된 값이 유효하지 않습니다.", exception.message)
    }

    @Test
    fun `해당 상영관에 존재하지 않는 좌석이면 예외가 발생한다`() {
        // given : 입력값이 D1이다.
        val seat = "D1"

        // when : 사용자가 해당 좌석을 예약하려고 할 때
        val exception = assertThrows<IllegalArgumentException> {
            TestFixtureData.screens.first().seats.checkSeat(seat)
        }
        // then : 예외가 발생한다.
        assertEquals("해당 상영관에는 해당 좌석이 존재하지 않습니다.", exception.message)
    }

    @Test
    fun `이미 예약된 좌석이면 예외가 발생한다`() {
        // given : 입력값이 A1이다.
        val seat = "A1"

        // when : 사용자가 해당 좌석을 예약하려고 할 때
        val exception = assertThrows<IllegalArgumentException> {
            TestFixtureData.screens.first().seats.checkSeat(seat)
        }
        // then : 예외가 발생한다.
        assertEquals("해당 좌석은 이미 예약되었습니다.", exception.message)
    }
}
