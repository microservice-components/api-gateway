package io.github.microservice.components.apigateway.config

/**
 * Copyright (C), 2017-2018
 *
 * FileName: io.github.microservice.components.apigateway.config.Constants.java
 * Author: yangchengwei
 * Email: yangchengwei2516@gmail.com
 * Date: 2018-04-10 13:58
 * Description:
 * History:
 *   <Author>      <Time>    <version>    <desc>
 *   yangchengwei  13:58    1.0          Create
 */
class Constants {
	
	companion object {
		const val SPRING_PROFILE_DEVELOPMENT = "dev"
		const val SPRING_PROFILE_TEST = "test"
		const val SPRING_PROFILE_PRODUCTION = "prod"
		// Spring profile used to disable swagger
		const val SPRING_PROFILE_SWAGGER = "swagger"
		// Spring profile used to disable running liquibase
		const val SPRING_PROFILE_NO_LIQUIBASE = "no-liquibase"
	}
	
}