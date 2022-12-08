package com.paxier.moviesreviewservice

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.event.ContextClosedEvent

class WireMockContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    void initialize(ConfigurableApplicationContext applicationContext) {
        def wireMockServer = new WireMockServer(new WireMockConfiguration().dynamicPort())
        wireMockServer.start()

        applicationContext.beanFactory.registerSingleton('wireMockServer', wireMockServer)
        applicationContext.addApplicationListener { applicationEvent ->
            if (applicationEvent instanceof ContextClosedEvent) {
                wireMockServer.stop()
            }
        }

//        TestPropertyValues
//                .of("app.downstream.port=${wireMockServer.port()}")
//                .applyTo(applicationContext)
    }
}