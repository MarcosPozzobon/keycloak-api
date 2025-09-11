package com.marcos.desenvolvimento.authorization_ms.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.cors.CorsConfigurationSource;

class WebSecurityConfigTest {

    private WebSecurityConfig webSecurityConfig = new WebSecurityConfig();

    @Test
    void testeCorsConfigurationSourceNaoDeveRetornarUmaInstanciaNula() {
        CorsConfigurationSource corsConfig = webSecurityConfig.corsConfigurationSource();
        Assertions.assertThat(corsConfig).isNotNull();
    }
}