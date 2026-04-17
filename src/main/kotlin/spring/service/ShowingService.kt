package spring.service

import domain.cinema.Screen
import domain.cinema.Showing
import org.springframework.stereotype.Service
import spring.repository.SeatRepository
import spring.repository.ShowingRepository

@Service
class ShowingService(private val showingRepository: ShowingRepository, private val seatRepository: SeatRepository) {
    fun findAll(): List<Showing> {
        val seats = seatRepository.findAllSeats()
        return showingRepository.findAll().map {
            Showing(
                startTime = it.startTime,
                screen = Screen(
                    seats = seats,
                    id = it.screen.id,
                ),
                movie = it.movie,
                id = it.id,
            )
        }
    }
}
