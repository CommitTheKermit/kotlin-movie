package repository

import domain.cinema.MovieTime
import domain.cinema.Showing
import javax.sql.DataSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.jdbc.datasource.DriverManagerDataSource
import repository.SchemaInitializer
import spring.repository.ShowingRepository

class ShowingRepositoryTest {
    private lateinit var dataSource: DataSource
    private lateinit var repository: ShowingRepository

    @BeforeEach
    fun setUp() {
        dataSource = DriverManagerDataSource(
            "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "",
        )
        dataSource.connection.use { SchemaInitializer.initialize(it) }
        repository = ShowingRepository(dataSource)
    }

    @Test
    fun `저장한 상영 정보를 조회하면 영화, 시작 시각이 복원된다`() {
        // given : 상영 정보가 주어지고 상영 정보를 저장한다.
        val movieTime = MovieTime(2025, 9, 20, 10, 20)

        val showing = Showing(
            movieTime,
            TestFixtureData.screens.first(),
            TestFixtureData.movies.movies.first(),
        )
        val savedId = repository.save(showing)

        // when : 상영 정보를 조회하면
        val found = repository.findById(savedId)

        // then : 주어졌던 상영정보의 영화와 시작 시간이 반환된다.
        assertThat(found?.movie?.title).isEqualTo(TestFixtureData.movies.movies.first().title)
        assertThat(found?.startTime).isEqualTo(movieTime)
    }

    @Test
    fun `특정 영화의 모든 상영 정보를 조회할 수 있다`() {
        // given : 상영 정보들이 주어지고 상영 정보들을 저장한다.
        val showings = TestFixtureData.showings
        showings.showings.forEach {
            repository.save(it)
        }

        // when : 특정 영화의 모든 상영 정보를 조회하면
        val foundShowings = repository.findByMovieId(
            TestFixtureData.movies.movies.first().id,
        )

        // then : 주어진 상영 정보들이 복원된다
        assertThat(foundShowings.showings)
            .hasSize(4)
            .allSatisfy {
                assertThat(it.movie.title).isEqualTo(
                    TestFixtureData.movies.movies.first().title,
                )
            }
    }
}
