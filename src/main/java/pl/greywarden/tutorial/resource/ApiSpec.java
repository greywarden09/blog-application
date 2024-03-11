package pl.greywarden.tutorial.resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ApiSpec {
    @Value("classpath:api.yml")
    private Resource apiJson;

    @GetMapping("/api.yml")
    Resource apiDoc() {
        return apiJson;
    }
}
