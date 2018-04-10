package io.github.microservice.components.apigateway.filiter

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import io.github.microservice.components.apigateway.security.jwt.TokenProvider
import io.github.microservice.components.apigateway.util.HttpUtils.Companion.HEADER_AUTHORIZATION
import io.github.microservice.components.apigateway.util.HttpUtils.Companion.HEADER_NICKNAME
import io.github.microservice.components.apigateway.util.HttpUtils.Companion.HEADER_PHONE
import io.github.microservice.components.apigateway.util.HttpUtils.Companion.HEADER_PHOTO
import io.github.microservice.components.apigateway.util.HttpUtils.Companion.HEADER_USER_ID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.PatternMatchUtils
import javax.servlet.http.HttpServletResponse

/**
 * Copyright (C), 2017-2018
 *
 * FileName: io.github.microservice.components.apigateway.filiter.ClientRequestFilter.java
 * Author: yangchengwei
 * Email: yangchengwei2516@gmail.com
 * Date: 2018-04-10 14:27
 * Description:
 * History:
 *   <Author>      <Time>    <version>    <desc>
 *   yangchengwei  14:27    1.0          Create
 */
@Component
class ClientRequestFilter: ZuulFilter() {
	
	@Autowired
	private val tokenProvider: TokenProvider? = null
	
	override fun run(): Any? {
		var userId = "-1"
		val ctx = RequestContext.getCurrentContext()
		val request = ctx.request
		try {
			val authorization = request.getHeader(HEADER_AUTHORIZATION)
			
			// Authorization 头为空，请登录
			if (authorization.isEmpty()) {
				ctx.responseStatusCode = HttpServletResponse.SC_UNAUTHORIZED
				ctx.setSendZuulResponse(false)
				return null
			}
			
			val userInfo = tokenProvider!!.getBaseInfo(authorization)
			// token 到期或无法解析，请登录
			if (userInfo == null || userInfo!!.id == null) {
				ctx.responseStatusCode = HttpServletResponse.SC_UNAUTHORIZED
				ctx.setSendZuulResponse(false)
				return null
			}
			
			// forward user info
			userId = userInfo!!.id.toString()
			ctx.addZuulRequestHeader(HEADER_NICKNAME, userInfo!!.nickname)
			ctx.addZuulRequestHeader(HEADER_PHOTO, userInfo!!.photo)
			ctx.addZuulRequestHeader(HEADER_PHONE, userInfo!!.phone)
		} finally {
			// 务必覆盖 x-user-id，防止攻击
			ctx.addZuulRequestHeader(HEADER_USER_ID, userId)
		}
		return null
	}
	
	override fun shouldFilter(): Boolean {
		val ctx = RequestContext.getCurrentContext()
		val request = ctx.request
		val requestURI = request.requestURI
		return PatternMatchUtils.simpleMatch("**/api/**", requestURI)
	}
	
	override fun filterType(): String {
		return "pre"
	}
	
	override fun filterOrder(): Int {
		return 0
	}
	
}