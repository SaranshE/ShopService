package snippet;

public class Snippet
{
	spring:
	  application:
	    name: Product
	  datasource:
	    url: jdbc:mysql://localhost:3306/product
	    username: root
	    password: password
	    driver-class-name: com.mysql.cj.jdbc.Driver
	  jpa:
	    hibernate:
	      ddl-auto: update
	    database: mysql
	    show-sql: true
	    database-platform: org.hibernate.dialect.MySQLDialect
	eureka:
	  client:
	    service-url:
	      defaultZone: http://localhost:8761/eureka/
	server:
	  port: 8081
	logging:
	  level:
	    root: warn
}

