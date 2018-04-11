package io.github.microservice.components.apigateway.security.jwt

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import io.github.microservice.components.apigateway.util.HttpUtils.Companion.HEADER_PHONE
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
	
	@Autowired
	private val jwtUtils: JWTUtils? = null
	
	/**
	 * 根据 JWT token获取存在里边的 用户基本信息
	 *
	 * @param jsonWebToken
	 * @return
	 */
	fun getBaseInfo(jsonWebToken: String): BaseUserInfo? {
		val claims = jwtUtils!!.getClaims(jsonWebToken) ?: return null
		val userInfo = BaseUserInfo()
		userInfo.id = claims.get(HEADER_USER_ID,Any::class.java) as Int
		userInfo.phone = claims.get(HEADER_PHONE, String::class.java)
		return userInfo
	}
	
}