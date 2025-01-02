package com.daydreamer.apistream.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class ModuleDetail {
    private String id;
    private String name;
    private String description;
    private String path;
    private boolean isDisabled;
    private String projectName;
    private List<ApiEndpoint> endpoints;
}
