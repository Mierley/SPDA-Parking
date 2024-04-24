package ru.innopolis.spdaparking.responces;

import lombok.Data;

@Data
public class Response {

    public String message;
    public int code;

    public Response(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
