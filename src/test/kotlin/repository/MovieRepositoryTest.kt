package repository

import domain.Id
import domain.cinema.Movie
import domain.cinema.Movies
import java.sql.Connection
import java.sql.DriverManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MovieRepository(val connection: Connection) {
    fun getAllMovies(): Movies {
        val sql = "SELECT * FROM movie"
        return connection.prepareStatement(sql).use { ps ->
            ps.executeQuery().use { rs ->
                val movies = mutableListOf<Movie>()
                while (rs.next()) {
                    movies.add(
                        Movie(
                            title = rs.getString("title"),
                            id = Id(rs.getInt("id")),
                            runningTime = rs.getInt("running_minutes"),
                        ),
                    )
                }
                Movies(movies)
            }
        }
    }
}

class MovieRepositoryTest {
    private lateinit var connection: Connection
    private lateinit var repository: MovieRepository

    @BeforeEach
    fun setUp() {
        connection = DriverManager.getConnection(
            "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "",
        )
        repository = MovieRepository(connection)

        SchemaInitializer.initialize(connection)
    }

    @Test
    fun `전체 영화 목록을 반환할 수 있다`() {
        // given & when : DB에 영화 정보가 입력되어 있고 전체 영화 정보를 조회하면
        val found = repository.getAllMovies()

        // then : 전체 영화 정보가 반환된다.
        assertThat(found.movies)
            .hasSize(3)
    }
}
