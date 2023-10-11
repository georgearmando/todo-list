package ao.com.georgearmando.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import ao.com.georgearmando.todolist.user.IUserRepository;
import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {
  @Autowired
  IUserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    // Pegar a autenticacao (usuario e senha)
    var authorization = request.getHeader("Authorization");
    var authEncoded = authorization.substring("Basic".length()).trim();
    byte[] authDecoded = Base64.getDecoder().decode(authEncoded);
    var authString = new String(authDecoded);

    // Separar o usuario e a senha
    String[] credentials = authString.split(":");
    String username = credentials[0];
    String password = credentials[1];
    
    // Verificar se o usuario existe
    var user = userRepository.findByUsername(username);
    if (user == null) {
      response.sendError(401);
    } else {
      // Verificar se a senha esta correta
      var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
      if (passwordVerify.verified) {
        filterChain.doFilter(request, response);
      } else {
        response.sendError(401);
      }
    }
  }
}
