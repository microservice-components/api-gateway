package io.github.microservice.components.apigateway

import io.github.microservice.components.apigateway.security.jwt.TokenProvider
import io.github.microservice.components.apigateway.util.JWTUtils
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

/**
 * Copyright (C), 2017-2018
 *
 * FileName: io.github.microservice.components.apigateway.YwtTest.java
 * Author: yangchengwei
 * Email: yangchengwei@xingyunfenqi.com
 * Date: 2018-04-11 10:29
 * Description:
 * History:
 *   <Author>      <Time>    <version>    <desc>
 *   yangchengwei  10:29    1.0          Create
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class YwtTest{

	
	@Autowired
	lateinit var p: TokenProvider
	
	@Autowired
	lateinit var util:JWTUtils
	
	@Test
	fun tokenTest(){
		var token = util.createToken(1,"18221616115")
		p.getBaseInfo(token)
	}

}
