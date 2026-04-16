package spring

import javax.sql.DataSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.RestTestClient
import repository.SchemaInitializer
import repository.ShowingRepository
import spring.model.response.MovieResponse
import spring.model.response.ReservationResponse

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest(
    @param:LocalServerPort
    val port: Int,
    @Autowired
    val dataSource: DataSource,
    @Autowired
    val showingRepository: ShowingRepository,
) {
    private lateinit var client: RestTestClient

    @BeforeEach
    fun setUp() {
        client = RestTestClient.bindToServer().baseUrl("http://localhost:$port").build()
        dataSource.connection.use { SchemaInitializer.initialize(it) }
    }

    @Test
    fun `전체 영화 목록 요청 시 영화 목록과 상영 정보가 응답된다`() {
        // giving : 상영 정보 목록을 저장하고
        val showings = TestFixtureData.showings
        showings.showings.forEach {
            showingRepository.save(it)
        }

        // when : 전체 영화 목록을 요청하면
        val movies: List<MovieResponse> = client.get().uri("/api/movies").exchange()
            .expectStatus().isOk
            .expectBody(Array<MovieResponse>::class.java)
            .returnResult().responseBody!!
            .toList()

        // then : 영화 목록과 상영 정보가 응답된다.
        val expectedTitles = TestFixtureData.showings.showings
            .map { it.movie.title }
            .distinct()

        assertThat(movies).extracting<String> { it.title }
            .containsExactlyInAnyOrderElementsOf(expectedTitles)
    }

    @Test
    fun `예약 등록시, 요청 시 예매가 생성되고 총 금액이 응답된다`() {
        // given : 상영을 저장하고 예약할 좌석 id를 준비한다
        val showing = TestFixtureData.showings.showings[1]
        showingRepository.save(showing)

        // when : 예약을 등록하면
        val response = client.post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """
                {
                  "reservations": [
                    {
                      "showingId": 1,
                      "seats": ["C2", "C3"]
                    }
                  ],
                  "usedPoints": 2000,
                  "paymentMethod": "CARD"
                }
                """.trimIndent(),
            )
            .exchange()
            .expectStatus().isOk
            .expectBody(ReservationResponse::class.java)
            .returnResult().responseBody!!

        // then : 좌석 개수만큼 예매가 생성되고 총 금액이 양수로 응답된다
        assertThat(response.reservationIds).hasSize(2)
        assertThat(response.totalPrice).isPositive()
    }
}
