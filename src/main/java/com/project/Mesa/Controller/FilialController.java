package com.project.Mesa.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.project.Mesa.Model.filial;
import com.project.Mesa.Repository.FilialRepository;


@Controller
public class FilialController {
	
	@Autowired
	private FilialRepository filialrepository;
	
	@RequestMapping("/filial")
	public String filial(Model model) {	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();


		if(username != null){
			String nomeFormatado = formatarNome(username);
			model.addAttribute("message", nomeFormatado);
		}
		model.addAttribute("filialobj", new filial());
		Iterable<filial> filiais = filialrepository.findAll();
        model.addAttribute("filiais", filiais);
		return "/paginas/gestaodefiliais";
	}
	
	private String formatarNome(String username) {
		// Divide o nome em letras maiúsculas e minúsculas
		// Exemplo: "ErysonMoreira" -> "Eryson Moreira"
		return username.replaceAll("([a-z])([A-Z])", "$1 $2");
	}
	
	
	 @RequestMapping(method = RequestMethod.POST, value = "/salvarfiliais")
	    public ModelAndView salvar(@ModelAttribute filial filiais) {
	        ModelAndView modelview = new ModelAndView("redirect:/filial");
	        filialrepository.save(filiais);
	        return modelview;
	    }

	 @RequestMapping(method = RequestMethod.POST, value = "/editarfilial")
	 public ModelAndView editarFilial(@ModelAttribute filial filial) {
	     // Busca a filial pelo CNPJ
	     Optional<filial> filialExistente = filialrepository.findById(filial.getCnpj());
	     
	     if (filialExistente.isPresent()) {
	         // Atualiza os dados da filial existente
	         filial existente = filialExistente.get();
	         existente.setNome(filial.getNome());
	         existente.setRazaosocial(filial.getRazaosocial());
	         
	         // Salva as alterações no banco
	         filialrepository.save(existente);
	     } else {
	         System.out.println("Filial não encontrada com o CNPJ: " + filial.getCnpj());
	     }
	     
	     // Redireciona para a página de gestão de filiais
	     return new ModelAndView("redirect:/filial");
	 }

	    @RequestMapping(method = RequestMethod.GET, value = "/removerfilial/{cnpjfilial}")
	    public ModelAndView excluir(@PathVariable("cnpjfilial") String cnpj) {
	        filialrepository.deleteById(cnpj);
	        return new ModelAndView("redirect:/filial");
	    }
	

}
