package spring.repository

import domain.Id
import domain.cinema.Movie
import domain.cinema.MovieTime
import domain.cinema.Screen
import domain.cinema.Showing
import domain.cinema.Showings
import domain.seat.Seats
import java.sql.ResultSet
import java.sql.Statement
import java.time.LocalDateTime
import javax.sql.DataSource
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.springframework.stereotype.Repository

@Repository
class ShowingRepository(val dataSource: DataSource) {
    fun save(showing: Showing): Long {
        val sql = "INSERT INTO showing (start_time, end_time, screen_id, movie_id)" +
            " VALUES (?, ?, ?, ?)"

        return dataSource.connection.use { connection ->
            connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { ps ->
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
    }

    fun findById(id: Long): Showing? {
        val sql = """
            SELECT s.id AS showing_id,
                   s.start_time,
                   s.screen_id,
                   m.id AS movie_id,
                   m.title,
                   m.running_minutes
            FROM showing s
            JOIN movie m ON s.movie_id = m.id
            WHERE s.id = ?
        """.trimIndent()
        return dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { ps ->
                ps.setLong(1, id)
                ps.executeQuery().use { rs ->
                    if (!rs.next()) return@use null
                    rs.toShowing()
                }
            }
        }
    }

    fun findByMovieId(movieId: Id): Showings {
        val sql = """
            SELECT s.id AS showing_id,
                   s.start_time,
                   s.screen_id,
                   m.id AS movie_id,
                   m.title,
                   m.running_minutes
            FROM showing s
            JOIN movie m ON s.movie_id = m.id
            WHERE m.id = ?
        """.trimIndent()

        return dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { ps ->
                ps.setLong(1, movieId.value.toLong())
                ps.executeQuery().use { rs ->
                    val showings = mutableListOf<Showing>()
                    while (rs.next()) {
                        showings.add(rs.toShowing())
                    }

                    Showings(showings)
                }
            }
        }
    }

    private fun ResultSet.toShowing(): Showing {
        val startTime = MovieTime(
            getObject("start_time", LocalDateTime::class.java)
                .toKotlinLocalDateTime(),
        )
        val movie = Movie(
            title = getString("title"),
            id = Id(getInt("movie_id")),
            runningTime = getInt("running_minutes"),
        )
        val screen = Screen(
            seats = Seats(emptyList()),
            id = Id(getInt("screen_id")),
        )
        return Showing(startTime, screen, movie, Id(getInt("showing_id")))
    }
}
