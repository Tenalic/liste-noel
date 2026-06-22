package sc.liste.noel.liste_noel.back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping(path = {"/health", "/"})
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("API UP");
    }
}
