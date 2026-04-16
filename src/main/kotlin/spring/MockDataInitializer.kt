package spring

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import repository.ShowingRepository

@Component
class MockDataInitializer(private val showingRepository: ShowingRepository) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        MockData.showings.showings.forEach { showingRepository.save(it) }
    }
}
