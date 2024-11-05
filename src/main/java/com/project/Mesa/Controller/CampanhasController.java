package com.project.Mesa.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.Mesa.Model.Users;
import com.project.Mesa.Repository.CampanhasRepository;
import com.project.Mesa.Repository.UserRepository;

@Controller
public class CampanhasController {

  	@Autowired
  	private UserRepository userRepository;
  	
	
	
   @RequestMapping("/campanhas")
    public String campanha(Model model){

       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       String username = auth.getName();

       Users usua = userRepository.findByUsername(username);
      
       
       if(username != null){
           String nomeFormatado = formatarNome(username);
           model.addAttribute("message", nomeFormatado);
       }
       

       return "/paginas/campanhasglobais";
   }

    private String formatarNome(String username) {
        // Divide o nome em letras maiúsculas e minúsculas
        // Exemplo: "ErysonMoreira" -> "Eryson Moreira"
        return username.replaceAll("([a-z])([A-Z])", "$1 $2");
    }
}
