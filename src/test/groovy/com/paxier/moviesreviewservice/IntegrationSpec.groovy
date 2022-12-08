package com.paxier.moviesreviewservice

import com.github.tomakehurst.wiremock.WireMockServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.get
import static org.springframework.http.HttpHeaders.CONTENT_TYPE
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@SpringBootTest
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = [WireMockContextInitializer.class])
@ActiveProfiles("test")
abstract class IntegrationSpec extends Specification {
    @Autowired
    WireMockServer wireMockServer

    @Autowired
    WebTestClient webTestClient

    def cleanup() {
        wireMockServer.resetAll()
    }

    def stubGetResponse(String url, String responseBody, int responseStatus = OK.value(), int delayInMs = 0) {
        wireMockServer.stubFor(get(url).willReturn(aResponse()
                .withStatus(responseStatus)
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .withBody(responseBody)
                .withFixedDelay(delayInMs)
        ))
    }
}
