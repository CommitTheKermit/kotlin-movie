package domain.cinema

import domain.Id

class Showing(val startTime: MovieTime, val screen: Screen, val movie: Movie, val id: Id = Id(0)) {
    val endTime: MovieTime = startTime.plusMinutes(movie.runningTime)
}
