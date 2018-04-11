package io.github.microservice.components.apigateway.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * Copyright (C), 2017-2018
 *
 * FileName: io.github.microservice.components.apigateway.config.JwtConfig.java
 * Author: yangchengwei
 * Email: yangchengwei2516@gmail.com
 * Date: 2018-04-10 15:04
 * Description:
 * History:
 *   <Author>      <Time>    <version>    <desc>
 *   yangchengwei  15:04    1.0          Create
 */
@Component
class JwtConfig{
	
	@Value("\${jwt.secretKey}")
	var secretKey: String? = null
	@Value("\${jwt.expiresSecond}")
	var expiresSecond: Int? = null
	
}