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
public class DashboardController {
	

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CampanhasRepository campanhasRepository;

	@RequestMapping("/")
	public String index(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();

		/*
		 * Verifica qual usuário foi autenticado após login
		System.out.println("Username: " + username);
		System.out.println("Authorities: " + auth.getAuthorities());
		*/
		
		/*
		Users usuari = userRepository.findByUsername(username);
		*/
		
		// Adiciona o nome do usuário ao modelo
		if (username != null) {
			// Separar o nome e sobrenome
			String nomeFormatado = formatarNome(username);
			model.addAttribute("message", nomeFormatado);
		}

		/*
			Double metaTotal = campanhasRepository.sumMetaByCnpjAndGrupo("Base");
		model.addAttribute("metaTotal", metaTotal != null ? metaTotal : 0);*/
		
		
		if (auth != null && auth.isAuthenticated()) {
			// Lógica existente
			return "dashboard"; 
		}
		// Se não autenticado, redirecione para login
		
		return "redirect:/login";
	}
	

	private String formatarNome(String username) {
		// Divide o nome em letras maiúsculas e minúsculas
		// Exemplo: "ErysonMoreira" -> "Eryson Moreira"
		return username.replaceAll("([a-z])([A-Z])", "$1 $2");
	}

	


	
	
}
