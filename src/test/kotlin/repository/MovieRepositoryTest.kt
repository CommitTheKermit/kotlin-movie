package repository

import javax.sql.DataSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.jdbc.datasource.DriverManagerDataSource

class MovieRepositoryTest {
    private lateinit var dataSource: DataSource
    private lateinit var repository: MovieRepository

    @BeforeEach
    fun setUp() {
        dataSource = DriverManagerDataSource(
            "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "",
        )
        dataSource.connection.use { SchemaInitializer.initialize(it) }
        repository = MovieRepository(dataSource)
    }

    @Test
    fun `전체 영화 목록을 반환할 수 있다`() {
        // given & when : DB에 영화 정보가 입력되어 있고 전체 영화 정보를 조회하면
        val found = repository.getAllMovies()

        // then : 전체 영화 정보가 반환된다.
        assertThat(found.movies)
            .hasSize(3)
    }

    @Test
    fun `영화의 타이틀로 영화를 검색하여 반환할 수 있다`() {
        // given & when : DB에 영화 정보가 입력되어 있고 영화 타이틀이 제공된다
        val found = repository.getMovieByTitle(
            "해리 포터",
        )

        // then : 전체 영화 정보가 반환된다.
        assertThat(found.title).isEqualTo(
            "해리 포터",
        )
    }
}
