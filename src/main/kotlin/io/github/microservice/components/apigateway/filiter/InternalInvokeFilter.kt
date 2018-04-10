package io.github.microservice.components.apigateway.filiter

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import org.springframework.stereotype.Component
import org.springframework.util.PatternMatchUtils
import javax.servlet.http.HttpServletResponse

/**
 * Copyright (C), 2017-2018
 *
 * FileName: io.github.microservice.components.apigateway.filiter.InternalInvokeFilter.java
 * Author: yangchengwei
 * Email: yangchengwei2516@gmail.com
 * Date: 2018-04-10 18:42
 * Description:
 * History:
 *   <Author>      <Time>    <version>    <desc>
 *   yangchengwei  18:42    1.0          Create
 */
@Component
class InternalInvokeFilter:ZuulFilter(){
	
	override fun filterType(): String {
		return "pre"
	}
	
	override fun filterOrder(): Int {
		// 要放在权限验证之前
		return -2
	}
	
	override fun shouldFilter(): Boolean {
		//以RPC开头的表示只能内部调用
		val ctx = RequestContext.getCurrentContext()
		val request = ctx.request
		val requestURI = request.requestURI
		return PatternMatchUtils.simpleMatch("/**/rpc/**", requestURI)
	}
	
	override fun run(): Any? {
		val ctx = RequestContext.getCurrentContext()
		ctx.responseStatusCode = HttpServletResponse.SC_FORBIDDEN
		ctx.setSendZuulResponse(false)
		return null
	}

}