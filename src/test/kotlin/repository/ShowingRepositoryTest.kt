package repository

import domain.Id
import domain.cinema.Movie
import domain.cinema.MovieTime
import domain.cinema.Screen
import domain.cinema.Showing
import domain.cinema.Showings
import domain.seat.Seats
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ShowingRepository(val connection: Connection) {
    fun save(showing: Showing): Long {
        val sql = "INSERT INTO showing (start_time, end_time, screen_id, movie_id)" +
            " VALUES (?, ?, ?, ?)"

        return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { ps ->
            ps.setObject(1, showing.startTime.value.toJavaLocalDateTime())
            ps.setObject(2, showing.endTime.value.toJavaLocalDateTime())
            ps.setInt(3, showing.screen.id.value)
            ps.setInt(4, showing.movie.id.value)
            ps.executeUpdate()

            ps.generatedKeys.use { keys ->
                if (keys.next()) keys.getLong(1) else error("No generated id")
            }
        }
    }

    fun findById(id: Long): Showing? {
        val sql = """
            SELECT s.start_time,
                   s.screen_id,
                   m.id AS movie_id,
                   m.title,
                   m.running_minutes
            FROM showing s
            JOIN movie m ON s.movie_id = m.id
            WHERE s.id = ?
        """.trimIndent()

        return connection.prepareStatement(sql).use { ps ->
            ps.setLong(1, id)
            ps.executeQuery().use { rs ->
                if (!rs.next()) return@use null
                val startTime = MovieTime(
                    rs.getObject("start_time", java.time.LocalDateTime::class.java)
                        .toKotlinLocalDateTime(),
                )
                val movie = Movie(
                    title = rs.getString("title"),
                    id = Id(rs.getInt("movie_id")),
                    runningTime = rs.getInt("running_minutes"),
                )
                val screen = Screen(
                    seats = Seats(emptyList()),
                    id = Id(rs.getInt("screen_id")),
                )
                Showing(startTime, screen, movie)
            }
        }
    }

    fun findByMovieId(movieId: Id): Showings {
        val sql = """
            SELECT s.start_time,
                   s.screen_id,
                   m.id AS movie_id,
                   m.title,
                   m.running_minutes
            FROM showing s
            JOIN movie m ON s.movie_id = m.id
            WHERE m.id = ?
        """.trimIndent()

        return connection.prepareStatement(sql).use { ps ->
            ps.setLong(1, movieId.value.toLong())
            ps.executeQuery().use { rs ->
                val showings = mutableListOf<Showing>()
                while (rs.next()) {
                    val startTime = MovieTime(
                        rs.getObject("start_time", java.time.LocalDateTime::class.java)
                            .toKotlinLocalDateTime(),
                    )
                    val movie = Movie(
                        title = rs.getString("title"),
                        id = Id(rs.getInt("movie_id")),
                        runningTime = rs.getInt("running_minutes"),
                    )
                    val screen = Screen(
                        seats = Seats(emptyList()),
                        id = Id(rs.getInt("screen_id")),
                    )
                    showings.add(Showing(startTime, screen, movie))
                }

                Showings(showings)
            }
        }
    }
}

object SchemaInitializer {
    fun initialize(connection: Connection) {
        val sql = this::class.java.getResource("/schema.sql")!!.readText()
        connection.createStatement().use { it.execute(sql) }
    }
}

class ShowingRepositoryTest {
    private lateinit var connection: Connection
    private lateinit var repository: ShowingRepository

    @BeforeEach
    fun setUp() {
        connection = DriverManager.getConnection(
            "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "",
        )
        repository = ShowingRepository(
            connection = connection,
        )
        SchemaInitializer.initialize(connection)
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
            .hasSize(3)
            .allSatisfy {
                assertThat(it.movie.title).isEqualTo(
                    TestFixtureData.movies.movies.first().title,
                )
            }
    }
}
