package com.monitoring.application.configuration.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitoring.application.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class JWTFilter extends GenericFilterBean {
    private static final String AUTHORIZATION = "Authorization";
    public static final String TOKEN_BEGIN_STR = "Bearer ";
    @Autowired
    private final UserService userService;

    public JWTFilter(UserService userService) {
        this.userService = userService;
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith(TOKEN_BEGIN_STR)) {
            return bearer.substring(TOKEN_BEGIN_STR.length());
        }
        return null;
    }

    private void raiseException(ServletResponse response, int status, String message) throws IOException {
        if (response instanceof final HttpServletResponse httpResponse) {
            httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpResponse.setStatus(status);
            final byte[] body = new ObjectMapper().writeValueAsBytes(message);
            response.getOutputStream().write(body);
        }
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        if (request instanceof final HttpServletRequest httpRequest) {
            final String token = getTokenFromRequest(httpRequest);
            if (StringUtils.hasText(token)) {
                try {
                    final UserDetails account = userService.loadUserByToken(token);
                    final UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(account, null, account.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } catch (JWTException e) {
                    raiseException(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    raiseException(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            String.format("Internal error: %s", e.getMessage()));
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }
}
