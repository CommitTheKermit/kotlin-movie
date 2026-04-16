package repository

import domain.seat.SeatGrade
import javax.sql.DataSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.jdbc.datasource.DriverManagerDataSource

class SeatRepositoryTest {
    private lateinit var dataSource: DataSource
    private lateinit var repository: SeatRepository

    @BeforeEach
    fun setUp() {
        dataSource = DriverManagerDataSource(
            "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "",
        )
        dataSource.connection.use { SchemaInitializer.initialize(it) }
        repository = SeatRepository(dataSource)
    }

    @Test
    fun `id로 좌석을 조회하면 좌표와 등급이 복원된다`() {
        // given & when : DB에 id가 1인 좌석이 존재하고, id로 조회하면
        val found = repository.findById(1L)

        // then : 좌표 A1과 B 등급을 가진 좌석이 반환된다
        assertThat(found.coordinate.row).isEqualTo('A')
        assertThat(found.coordinate.column).isEqualTo(1)
        assertThat(found.grade).isEqualTo(SeatGrade.B)
    }
}
