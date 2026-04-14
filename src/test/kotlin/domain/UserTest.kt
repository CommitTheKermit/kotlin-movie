package domain

import domain.user.Point
import domain.user.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows

class UserTest {
    @Test
    fun `ID는 양수일 때 정상 생성 되어야 한다`() {
        // given & when : User의 ID에 양수값을 넣는다.
        val result = User(Id(1))

        // then : 정상 생성 된다.
        assertNotNull(result)
    }

    @Test
    fun `ID가 음수일 때 예외가 발생한다`() {
        // given & when : User의 ID에 음수값을 넣는다.
        val exception = assertThrows<IllegalArgumentException> {
            User(Id(-1))
        }

        // then : 예외가 발생한다.
        assertEquals("ID는 양수이어야 합니다.", exception.message)
    }

    @Test
    fun `포인트 사용 시 포인트가 차감된다`() {
        // given : User 객체가 주어진다
        val user = User(Id(1), Point(2000))

        // when : discountPoint로 500을 입력했을 때
        val result = user.discountPoint(Point(1000))

        // then : 전체 포인트 값이 1500이 된다.
        assertEquals(1000, result.point.point)
    }

    @Test
    fun `보유 포인트보다 많은 금액을 차감하면 예외가 발생한다`() {
        // given : User 객체가 주어진다
        val result = User(Id(1))

        // when : discountPoint로 1500을 입력했을 때
        val exception = assertThrows<IllegalArgumentException> {
            result.discountPoint(Point(2500))
        }

        // then : 예외가 발생 한다.
        assertEquals("차감액은 전체 포인트보다 작아야 합니다.", exception.message)
    }
}
