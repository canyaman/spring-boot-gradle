package me.yaman.can.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
class SwaggerController {

    @Value("#{servletContext.contextPath}")
    private val servletContextPath: String? = null

    @GetMapping("/")
    fun swagger(response: HttpServletResponse) = response.sendRedirect("$servletContextPath/swagger-ui.html")
}
