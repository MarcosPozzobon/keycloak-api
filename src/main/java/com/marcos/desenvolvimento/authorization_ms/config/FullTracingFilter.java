package com.marcos.desenvolvimento.authorization_ms.config;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component
@Order(1000)
public class FullTracingFilter extends OncePerRequestFilter {

    private final Tracer tracer;

    public FullTracingFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            addTracingInfo(requestWrapper, responseWrapper);
            responseWrapper.copyBodyToResponse();
        }
    }

    private void addTracingInfo(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan == null) {
            return;
        }

        currentSpan.tag("http.method", request.getMethod());
        currentSpan.tag("http.path", request.getRequestURI());
        currentSpan.tag("http.query", request.getQueryString() != null ? request.getQueryString() : "");
        currentSpan.tag("http.content_type", request.getContentType() != null ? request.getContentType() : "none");
        currentSpan.tag("http.status_code", String.valueOf(response.getStatus()));
        currentSpan.tag("http.request.protocol", request.getProtocol());
        currentSpan.tag("http.request.encoding", request.getCharacterEncoding());
        currentSpan.tag("http.request.internet.protocol", request.getLocalAddr());

        Collections.list(request.getHeaderNames())
                .forEach(headerName -> currentSpan.tag("header." + headerName, request.getHeader(headerName)));

        String requestBody = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);
        if (!requestBody.isEmpty()) {
            currentSpan.tag("http.request.body", truncate(requestBody));
        }

        String responseBody = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
        if (!responseBody.isEmpty()) {
            currentSpan.tag("http.response.body", truncate(responseBody));
        }

    }

    private String truncate(String str) {
        int max = 4096;
        return str.length() > max ? str.substring(0, max) + "...(truncated)" : str;
    }
}