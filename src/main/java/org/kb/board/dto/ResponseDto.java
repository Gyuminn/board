package org.kb.board.dto;

import lombok.Getter;
import lombok.Setter;
import org.kb.board.domain.StatusEnum;

@Getter
@Setter
public class ResponseDto<T> {
    private StatusEnum statusCode;
    private String message;
    private T data;

}
