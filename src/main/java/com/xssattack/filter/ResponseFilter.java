package com.xssattack.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xssattack.exception.ExceptionController;
import com.xssattack.exception.XSSServletException;
import com.xssattack.model.ErrorResponse;
import com.xssattack.utils.JsonUtil;
import com.xssattack.utils.XSSValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

@Component
public class ResponseFilter implements Filter {

    ObjectMapper objectMapper = new ObjectMapper();


//    @Value("#{'${skip_words}'.split(',')}")
//    private List<String> skipWords;

    @Autowired
    private ExceptionController exceptionController;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        try {
            RequestWrapper requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);

            String uri = requestWrapper.getRequestURI();
            System.out.println("getRequestURI : " + uri);
            String decodedURI = URLDecoder.decode(uri, "UTF-8");
            System.out.println("decodedURI : " + decodedURI);

            // XSS:  Path Variable Validation
            if (!XSSValidationUtils.isValidURL(decodedURI)) {
                ErrorResponse errorResponse = new ErrorResponse();

                errorResponse.setStatus(HttpStatus.FORBIDDEN.value());
                errorResponse.setMessage("XSS attack error");
                System.out.println("JsonUtil.convertObjectToJson(errorResponse) : " + JsonUtil.convertObjectToJson(errorResponse));
                servletResponse.getWriter().write(JsonUtil.convertObjectToJson(errorResponse));
                httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
                return;
            }

            System.out.println("Response output: " + requestWrapper.getBody());
            if (StringUtils.hasLength(requestWrapper.getBody())) {
                // XSS:  Post Body data validation
                if (XSSValidationUtils.isValidURLPattern(requestWrapper.getBody())) {
                    filterChain.doFilter(requestWrapper, servletResponse);
                } else {
                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.setStatus(HttpStatus.FORBIDDEN.value());
                    errorResponse.setMessage("XSS attack error");
                    servletResponse.getWriter().write(JsonUtil.convertObjectToJson(errorResponse));
                    httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
                }
            } else {
                filterChain.doFilter(requestWrapper, servletResponse);
            }
        } catch (XSSServletException ex) {
            servletResponse.getWriter().write(ex.getMessage());
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        }  catch (Exception ex) {
            servletResponse.getWriter().write(ex.getMessage());
            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } finally {
            System.out.println("clean up");
        }
    }
}


