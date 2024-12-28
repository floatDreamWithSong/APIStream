package com.daydreamer.faastest.common.modules;

import lombok.Builder;

import java.util.UUID;

public class SystemSQLResult {
    public String sql;
    public Object result;
    public String error;
}
