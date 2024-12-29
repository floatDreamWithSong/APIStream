package com.daydreamer.apistream.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Slf4j
@RestController
public class StaticController {
    
    private static final String LOG_PATH = "logs/";
    
    @GetMapping("/APIStreamStaticResources/download")
    public Map<String, List<String>> getLogFiles() {
        Map<String, List<String>> result = new HashMap<>();
        File logDir = new File(LOG_PATH);
        
        if (logDir.exists() && logDir.isDirectory()) {
            scanLogDirectory(logDir, "", result);
        }
        
        return result;
    }
    
    private void scanLogDirectory(File dir, String path, Map<String, List<String>> result) {
        File[] files = dir.listFiles();
        if (files != null) {
            List<String> logFiles = new ArrayList<>();
            for (File file : files) {
                if (file.isDirectory()) {
                    scanLogDirectory(file, path + "/" + file.getName(), result);
                } else if (file.getName().endsWith(".log")) {
                    logFiles.add("/APIStreamStaticResources/logs" + path + "/" + file.getName());
                }
            }
            if (!logFiles.isEmpty()) {
                result.put(path.isEmpty() ? "root" : path.substring(1), logFiles);
            }
        }
    }
    
    @GetMapping("/APIStreamStaticResources/logs/**")
    public ResponseEntity<Resource> downloadFile(HttpServletRequest request) {
        String url = request.getRequestURI();
        String requestPath = "/APIStreamStaticResources/logs";
        String path = url.substring(requestPath.length());
        String filename = path.substring(path.lastIndexOf("/") + 1);
        log.info(path);
        log.info(filename);
        try {
            Path filePath = Paths.get("logs", path);
            Resource resource = new FileSystemResource(filePath.toFile());

            if (resource.exists()) {
                return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(resource);
            }
        } catch (Exception e) {
            log.error("下载文件失败: {}", e.getMessage());
        }
        return ResponseEntity.notFound().build();
    }
}
