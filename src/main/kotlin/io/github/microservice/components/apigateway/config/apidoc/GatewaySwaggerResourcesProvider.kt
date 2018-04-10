package io.github.microservice.components.apigateway.config.apidoc

import org.slf4j.LoggerFactory
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.cloud.netflix.zuul.filters.RouteLocator
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import springfox.documentation.swagger.web.SwaggerResource
import springfox.documentation.swagger.web.SwaggerResourcesProvider
import io.github.microservice.components.apigateway.config.Constants

/**
 * Retrieves all registered microservices Swagger resources.
 *
 * @author hookszhang on 01/11/2017.
 */
@Component
@Primary
@Profile(Constants.SPRING_PROFILE_SWAGGER)
class GatewaySwaggerResourcesProvider(private val routeLocator: RouteLocator, private val discoveryClient: DiscoveryClient) : SwaggerResourcesProvider {

    private val log = LoggerFactory.getLogger(GatewaySwaggerResourcesProvider::class.java)

    override fun get(): List<SwaggerResource> {
        val resources = ArrayList<SwaggerResource>()

        //Add the default swagger resource that correspond to the gateway's own swagger doc
        resources.add(swaggerResource("default", "/v2/api-docs"))

        //Add the registered microservices swagger docs as additional swagger resources
        val routes = routeLocator.routes
        routes.forEach { route -> resources.add(swaggerResource(route.id, route.fullPath.replace("**", "v2/api-docs"))) }

        return resources
    }

    private fun swaggerResource(name: String, location: String): SwaggerResource {
        val swaggerResource = SwaggerResource()
        swaggerResource.name = name
        swaggerResource.location = location
        swaggerResource.swaggerVersion = "2.0"
        return swaggerResource
    }
}