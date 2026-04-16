package repository

import domain.Id
import domain.cinema.Movie
import domain.cinema.Movies
import java.sql.Connection
import view.message.CinemaMessages

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

    fun getMovieByTitle(title: String): Movie {
        val sql = "SELECT * FROM movie WHERE title = ?"
        return connection.prepareStatement(sql).use { ps ->
            ps.setString(1, title)
            ps.executeQuery().use { rs ->
                require(rs.next()) { CinemaMessages.ERROR_MOVIE_NOT_FOUND_BY_TITLE }
                Movie(
                    id = Id(rs.getInt("id")),
                    title = rs.getString("title"),
                    runningTime = rs.getInt("running_minutes"),
                )
            }
        }
    }
}
