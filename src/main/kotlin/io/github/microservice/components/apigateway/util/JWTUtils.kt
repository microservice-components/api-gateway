package io.github.microservice.components.apigateway.util

import io.github.microservice.components.apigateway.config.JwtConfig
import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Component
class JWTUtils{

    private val log = LoggerFactory.getLogger(javaClass)
    
    @Autowired
    private val jwtConfig: JwtConfig? = null
    
    fun createToken(userId: Int, phone: String): String {
        val signatureAlgorithm = SignatureAlgorithm.HS256
        val key = jwtConfig!!.secretKey
        val expirationDay = jwtConfig!!.expiresSecond
        val secretKeySpec = SecretKeySpec(key!!.toByteArray(), signatureAlgorithm.jcaName)
        return Jwts.builder()
            .claim("x-user-id", userId)
            .claim("x-phone", phone)
            .signWith(signatureAlgorithm, secretKeySpec)
            .setExpiration(afterFewDays(expirationDay!!))
            .compact()
    }
    
    private fun afterFewDays(days: Int): Date {
        return Date(System.currentTimeMillis() + days * 24 * 60 * 60 * 1000)
    }

    fun getClaims(token: String): Claims? {
        return if (!validateToken(token)) {
            null
        } else {
            Jwts.parser().setSigningKey(jwtConfig!!.secretKey!!.toByteArray()).parseClaimsJws(token).body
        }
    }

    fun validateToken(authToken: String): Boolean {
        try {
            Jwts.parser().setSigningKey(jwtConfig!!.secretKey!!.toByteArray()).parseClaimsJws(authToken)
            return true
        } catch (e: SignatureException) {
            log.info("Invalid JWT signature.")
            log.trace("Invalid JWT signature trace: {}", e)
        } catch (e: MalformedJwtException) {
            log.info("Invalid JWT token.")
            log.trace("Invalid JWT token trace: {}", e)
        } catch (e: ExpiredJwtException) {
            log.info("Expired JWT token.")
            log.trace("Expired JWT token trace: {}", e)
        } catch (e: UnsupportedJwtException) {
            log.info("Unsupported JWT token.")
            log.trace("Unsupported JWT token trace: {}", e)
        } catch (e: IllegalArgumentException) {
            log.info("JWT token compact of handler are invalid.")
            log.trace("JWT token compact of handler are invalid trace: {}", e)
        }
        return false
    }
}