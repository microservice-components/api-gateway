package io.github.microservice.components.apigateway.security.jwt

import io.github.microservice.components.apigateway.config.JwtConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.security.Key
import javax.annotation.PostConstruct
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter
import io.jsonwebtoken.*
import io.github.microservice.components.apigateway.util.HttpUtils.Companion.HEADER_NICKNAME
import io.github.microservice.components.apigateway.util.HttpUtils.Companion.HEADER_PHONE
import io.github.microservice.components.apigateway.util.HttpUtils.Companion.HEADER_PHOTO
import io.github.microservice.components.apigateway.util.HttpUtils.Companion.HEADER_USER_ID
import io.github.microservice.components.apigateway.util.JWTUtils

/**
 * Copyright (C), 2017-2018
 *
 * FileName: io.github.microservice.components.apigateway.security.jwt.TokenProvider.java
 * Author: yangchengwei
 * Email: yangchengwei2516@gmail.com
 * Date: 2018-04-10 14:59
 * Description:
 * History:
 *   <Author>      <Time>    <version>    <desc>
 *   yangchengwei  14:59    1.0          Create
 */
@Component
class TokenProvider {
	
	internal var signatureAlgorithm = SignatureAlgorithm.HS256
	
	@Autowired
	private val jwtConfig: JwtConfig? = null
	@Autowired
	private val jwtUtils: JWTUtils? = null
	
	private var signingKey: Key? = null
	
	@PostConstruct
	fun init() {
		//生成签名密匙
		val apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtConfig!!.base64Secret)
		signingKey = SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName())
		
	}
	
	
	/**
	 * 根据 JWT token获取存在里边的 用户基本信息
	 *
	 * @param jsonWebToken
	 * @return
	 */
	fun getBaseInfo(jsonWebToken: String): BaseUserInfo? {
		val claims = jwtUtils!!.getClaims(jsonWebToken) ?: return null
		val userInfo = BaseUserInfo()
		userInfo.id = claims.get(HEADER_USER_ID, Int::class.java)
		userInfo.nickname = claims.get(HEADER_NICKNAME, String::class.java)
		userInfo.phone = claims.get(HEADER_PHONE, String::class.java)
		userInfo.photo = claims.get(HEADER_PHOTO, String::class.java)
		return userInfo
	}
	
}