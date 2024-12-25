package com.daydreamer.faastest.entity.dto.response;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ModuleDetail {
    public List<ListItem> functions;
    public String log;
}
