package com.marcos.desenvolvimento.authorization_ms.config;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FullTracingFilterTest {

    private Tracer tracer;
    private Span span;
    private FullTracingFilter filter;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        tracer = mock(Tracer.class);
        span = mock(Span.class);
        filter = new FullTracingFilter(tracer);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);

        when(tracer.currentSpan()).thenReturn(span);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/test");
        when(request.getQueryString()).thenReturn("param=1");
        when(request.getContentType()).thenReturn("application/json");
        when(request.getProtocol()).thenReturn("HTTP/1.1");
        when(request.getCharacterEncoding()).thenReturn("UTF-8");
        when(request.getLocalAddr()).thenReturn("127.0.0.1");
        when(request.getHeaderNames()).thenReturn(Collections.enumeration(Collections.emptyList()));
        when(response.getStatus()).thenReturn(200);
    }

    @Test
    void testeDoFilterInternalChamaChainsEColocaTagsNosSpans() throws ServletException, IOException {
        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(any(), any());

        verify(span).tag("http.method", "GET");
        verify(span).tag("http.path", "/test");
        verify(span).tag("http.query", "param=1");
        verify(span).tag("http.content_type", "application/json");
        verify(span).tag("http.status_code", "200");
        verify(span).tag("http.request.protocol", "HTTP/1.1");
        verify(span).tag("http.request.encoding", "UTF-8");
        verify(span).tag("http.request.internet.protocol", "127.0.0.1");
    }

    @Test
    void testDoFilterInternalQuandoNaoHaSpanNaoDeveLancarException() throws ServletException, IOException {
        when(tracer.currentSpan()).thenReturn(null);
        filter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(any(), any());
    }

    @Test
    void testeTruncate() throws Exception {
        FullTracingFilter filter = new FullTracingFilter(null);

        Method truncateMethod = FullTracingFilter.class.getDeclaredMethod("truncate", String.class);

        truncateMethod.setAccessible(true);

        String input = "a".repeat(5000);
        String result = (String) truncateMethod.invoke(filter, input);

        assertEquals("a".repeat(4096) + "...(truncated)", result);

        String smallInput = "hello";
        String smallResult = (String) truncateMethod.invoke(filter, smallInput);
        assertEquals("hello", smallResult);
    }
}
