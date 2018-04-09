package io.github.microservice.components.apigateway.gateway

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.zuul.context.RequestContext
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import org.springframework.cloud.netflix.zuul.filters.post.SendResponseFilter
import springfox.documentation.swagger2.web.Swagger2Controller
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.zip.GZIPInputStream

/**
 * @author hookszhang on 27/10/2017.
 */
class SwaggerBasePathRewritingFilter : SendResponseFilter() {

    private val log = LoggerFactory.getLogger(javaClass)

    private var mapper = ObjectMapper()

    override fun filterType(): String {
        return "post"
    }

    override fun filterOrder(): Int {
        return 100
    }

    /**
     * Filter requests to micro-services Swagger docs.
     */
    override fun shouldFilter(): Boolean {
        return RequestContext.getCurrentContext().request.requestURI.endsWith(Swagger2Controller.DEFAULT_URL)
    }

    override fun run(): Any? {
        val context = RequestContext.getCurrentContext()

        if (!context.responseGZipped) {
            context.response.characterEncoding = "UTF-8"
        }

        val rewrittenResponse = rewriteBasePath(context)
        context.responseBody = rewrittenResponse
        return null
    }

    private fun rewriteBasePath(context: RequestContext): String? {
        var responseDataStream = context.responseDataStream
        val requestUri = RequestContext.getCurrentContext().request.requestURI
        try {
            if (context.responseGZipped) {
                responseDataStream = GZIPInputStream(context.responseDataStream)
            }
            val response = IOUtils.toString(responseDataStream, StandardCharsets.UTF_8)
            if (response != null) {
                val map = this.mapper.readValue(response, LinkedHashMap::class.java) as LinkedHashMap<String, Any>
                val basePath = requestUri.replace(Swagger2Controller.DEFAULT_URL, "")
                map["basePath"] = basePath
                log.debug("Swagger-docs: rewritten Base URL with correct micro-service route: {}", basePath)
                return mapper.writeValueAsString(map)
            }
        } catch (e: IOException) {
            log.error("Swagger-docs filter error", e)
        }
        return null
    }
}