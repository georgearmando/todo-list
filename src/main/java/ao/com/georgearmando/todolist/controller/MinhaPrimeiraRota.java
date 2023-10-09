package ao.com.georgearmando.todolist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/minha-primeira-rota")
public class MinhaPrimeiraRota {
  
  @GetMapping()
  public String primeiroMetodo() {
    return "Minha primeira rota";
  }
}
