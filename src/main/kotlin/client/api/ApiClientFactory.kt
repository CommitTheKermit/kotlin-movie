package client.api

import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

class ApiClientFactory(baseUrl: String) {
    private val factory: HttpServiceProxyFactory = HttpServiceProxyFactory
        .builderFor(RestClientAdapter.create(RestClient.builder().baseUrl(baseUrl).build()))
        .build()

    fun <T : Any> create(apiClass: Class<T>): T = factory.createClient(apiClass)

    inline fun <reified T : Any> create(): T = create(T::class.java)
}
