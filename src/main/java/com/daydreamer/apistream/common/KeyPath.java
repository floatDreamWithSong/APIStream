package com.daydreamer.apistream.common;

import java.util.HashSet;

public class KeyPath {
    public static HashSet<String> keyUrls = new HashSet<>();
    static {
        keyUrls.add("/APIStreamModuleServiceSDK");
        keyUrls.add("/APIStreamProjectServiceSDK");
        keyUrls.add("/APIStreamModuleQueryService");
        keyUrls.add("/APIStreamModuleDetailQueryService");
        keyUrls.add("/APIStreamModuleDisableService");
        keyUrls.add("/APIStreamModuleEnableService");
        keyUrls.add("/APIStreamStaticResources/download");
        keyUrls.add("/APIStreamStaticResources/logs");
    }
}
