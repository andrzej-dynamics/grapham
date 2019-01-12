package com.dynamics.andrzej.grapham.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileLoaderController {

    @PostMapping("/file")
    public void loadFile(@RequestParam("file") MultipartFile file) {

    }
}
