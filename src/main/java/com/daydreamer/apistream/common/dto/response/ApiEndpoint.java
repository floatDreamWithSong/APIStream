package com.daydreamer.apistream.common.dto.response;
import lombok.Data;

@Data
public class ApiEndpoint {
    private String id;
    private String path;
    private String method;
    private String description;
}
