package br.edu.pedro.greetingservice.controllers;

import br.edu.pedro.greetingservice.configs.GreetingConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/greeting")
public class GreetingController {

//    @Value("${greeting-service.greeting}")
//    private String greeting;
//
//    @Value("${greeting-service.default-name}")
//    private String defaultName;

    private final GreetingConfig config;

    // Injeção de Dependência
    public GreetingController(GreetingConfig config) {
        this.config = config;
    }

    @GetMapping({"", "/"})
    public String getGreeting(@RequestParam(required = false) String name) {
        if (name == null || name.isEmpty()){
            name = config.getDefaultName();
        }
        String greetingReturn = String.format("%s %s!!!", config.getGreeting(), name);
        return greetingReturn;
    }
    // Entregavel
    @GetMapping("/{name}")
    public String getGreetingPath(@PathVariable String name){
        String greetingPathReturn = String.format("%s, %s!!!", config.getGreeting(), name);
        return greetingPathReturn;

    }

    @PostMapping
    public String postGreeting(@RequestBody Pessoa pessoa) {
        String postGreetingReturn = String.format("%s, %s!!!", config.getGreeting(), pessoa.getName());
        return postGreetingReturn;
    }

}
