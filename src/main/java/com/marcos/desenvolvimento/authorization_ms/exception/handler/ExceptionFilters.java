package com.marcos.desenvolvimento.authorization_ms.exception.handler;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionFilters {

    private String titulo;
    private Integer status;
    private String detalhes;
    private LocalDateTime timestamp;

}
