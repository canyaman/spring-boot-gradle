package me.yaman.can

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForObject
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnableAutoConfiguration(exclude = [(RepositoryRestMvcAutoConfiguration::class)])
class IntegrationTests {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun `Health check service running`() {
        val health: Map<String, *>? = restTemplate.getForObject("/system/health", Map::class)
        assertThat(health).isNotNull
        assertThat(health?.get("status")).isEqualTo("UP")
    }

    @Test
    fun `Liveness check service running`() {
        val info: Map<String, *>? = restTemplate.getForObject("/system/info", Map::class)
        assertThat(info).isNotNull
    }

    @Test
    fun `Query param validation`() {
        val list10Response = restTemplate
            .getForEntity("/test/list", List::class.java)
        assertThat(list10Response.statusCode).isEqualTo(HttpStatus.OK)
        val page10 = list10Response.body as? List<String>
        assertThat(page10?.size).isEqualTo(10)

        val list20Response = restTemplate
            .getForEntity("/test/list?size={size}", List::class.java, mapOf(Pair("size", 20)))
        assertThat(list20Response.statusCode).isEqualTo(HttpStatus.OK)
        val page20 = list20Response.body as? List<String>
        assertThat(page20?.size).isEqualTo(20)
    }

    @Test
    fun `Query pageable validation`(@Value("\${spring.data.web.pageable.max-page-size}") maxSize: Long) {
        val pageableDefaultResponse = restTemplate
            .getForEntity("/test/pageable", List::class.java)
        assertThat(pageableDefaultResponse.statusCode).isEqualTo(HttpStatus.OK)
        val page10 = pageableDefaultResponse.body as? List<String>
        assertThat(page10?.size).isEqualTo(10)

        val pageableMaxResponse = restTemplate
            .getForEntity("/test/pageable?size={size}", List::class.java, mapOf(Pair("size", 10000)))
        assertThat(pageableMaxResponse.statusCode).isEqualTo(HttpStatus.OK)
        val maxList = pageableMaxResponse.body as? List<String>
        assertThat(maxList?.size).isEqualTo(maxSize)
    }
}
