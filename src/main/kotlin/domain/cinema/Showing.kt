package domain.cinema

class Showing(val startTime: MovieTime, val screen: Screen, val movie: Movie) {
    val endTime: MovieTime = startTime.plusMinutes(movie.runningTime)
}
