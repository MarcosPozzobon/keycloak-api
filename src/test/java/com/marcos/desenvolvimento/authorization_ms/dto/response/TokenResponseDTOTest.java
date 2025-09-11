package com.marcos.desenvolvimento.authorization_ms.dto.response;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TokenResponseDTOTest {

    private TokenResponseDTO tokenResponseDTO;

    @BeforeEach
    void setup(){
        tokenResponseDTO = new TokenResponseDTO(
          "token",
          "refresh-token",
          300
        );
    }


    @Test
    void testeTokenResponseDTODTODeveSerCriadoCorretamenteQuandoInstanciaNaoEhInvalida() {
        Assertions.assertThat(tokenResponseDTO).isNotNull();
        Assertions.assertThat(tokenResponseDTO.token()).isEqualTo("token");
        Assertions.assertThat(tokenResponseDTO.refreshToken()).isEqualTo("refresh-token");
        Assertions.assertThat(tokenResponseDTO.expiresIn()).isEqualTo(300);
    }

    @Test
    void testeDeveGerarToStringComCampos() {
        String toString = tokenResponseDTO.toString();
        Assertions.assertThat(toString).contains("token");
        Assertions.assertThat(toString).contains("refresh-token");
        Assertions.assertThat(toString).contains("300");
    }

    @Test
    void testeEqualsEHashCode() {
        TokenResponseDTO tokenResponse1 = new TokenResponseDTO("token", "refresh-token", 300);
        TokenResponseDTO tokenResponse2 = new TokenResponseDTO("token", "refresh-token", 300);

        Assertions.assertThat(tokenResponse1).isEqualTo(tokenResponse2);
        Assertions.assertThat(tokenResponse1.hashCode()).isEqualTo(tokenResponse2.hashCode());
    }
}