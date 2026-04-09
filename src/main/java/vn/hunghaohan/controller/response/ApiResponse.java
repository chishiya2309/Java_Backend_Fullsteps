package vn.hunghaohan.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Builder
public class ApiResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int status;
    private String message;
    private Serializable data;
}
