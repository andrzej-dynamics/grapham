package com.dynamics.andrzej.grapham.controllers;

import com.dynamics.andrzej.grapham.Grapham;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;

public class FileLoaderControllerTest {
    @BeforeTest
    public void setup() {
        SpringApplication.run(Grapham.class);
    }

    @Test
    public void testLoadFile() throws URISyntaxException, IOException {
        final URI graphUri = getClass().getClassLoader().getResource("graph.txt").toURI();

        final RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:12345/file";
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        final FileSystemResource fileSystemResource = new FileSystemResource(new File(graphUri));
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileSystemResource);
        final HttpEntity<MultiValueMap<String, Object>> multiValueMapHttpEntity = new HttpEntity<>(body, httpHeaders);
        final ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, multiValueMapHttpEntity, String.class);
    }

    @Test
    public void testLoadInvalidFile() throws URISyntaxException {
        final URI graphUri = getClass().getClassLoader().getResource("graph-invalid-cyclic.txt").toURI();

        final RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:12345/file";
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        final FileSystemResource fileSystemResource = new FileSystemResource(new File(graphUri));
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileSystemResource);
        final HttpEntity<MultiValueMap<String, Object>> multiValueMapHttpEntity = new HttpEntity<>(body, httpHeaders);
        try {
            restTemplate.postForEntity(url, multiValueMapHttpEntity, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(e.getStatusCode(), HttpStatus.BAD_REQUEST);
        }
    }
}