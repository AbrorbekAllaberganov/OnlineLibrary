package com.example.LibraryService.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Each API responses based on this model")
public class Result {
    private String message;
    private boolean status;
    private Object data;
    private String exception;

    public Result(String message, boolean success, Exception error) {
        this.message = message;
        this.status = success;
        this.exception = error.getMessage();
    }

    public Result(String message, boolean success, Object data) {
        this.message = message;
        this.status = success;
        this.data = data;
    }

    public Result(String message, boolean success) {
        this.message = message;
        this.status = success;
    }

    public static Result success(Object data) {
        return new Result("success", true, data);
    }

    public static Result exception(Exception e) {
        return new Result("fail", false, e);
    }


    public static Result message(String message, boolean status) {
        return new Result(message, status);
    }
}
