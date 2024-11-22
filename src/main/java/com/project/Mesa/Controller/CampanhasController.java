package com.project.Mesa.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.project.Mesa.Model.Users;
import com.project.Mesa.Model.campanhas;
import com.project.Mesa.Repository.CampanhasRepository;
import com.project.Mesa.Repository.UserRepository;
import com.project.Mesa.Service.GoogleSheetsService;

@Controller
public class CampanhasController {

	
	@Autowired
	private CampanhasRepository campanhasRepository;
	
  	@Autowired
  	private UserRepository userRepository;
  	
  	@Autowired
  	private GoogleSheetsService googleSheetsService;
  	
	
	
   @RequestMapping("/campanhas")
    public String campanha(Model model){

       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       String username = auth.getName();

       Users usua = userRepository.findByUsername(username);
      
       
       if(username != null){
           String nomeFormatado = formatarNome(username);
           model.addAttribute("message", nomeFormatado);
       }
       
       if (auth.getAuthorities().stream()
               .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_MANAGER"))) {
           List<campanhas> campanhasComidaPremiada = campanhasRepository.findCampanhasByPaginaComidaPremiada();
           model.addAttribute("campanhas", campanhasComidaPremiada);
           
           List<campanhas> campanhasMelhoresEmpresas = campanhasRepository.findCampanhasByPaginaMelhoresEmpresas();
           model.addAttribute("campanhas1", campanhasMelhoresEmpresas);
           
           List<campanhas> campanhasComeMelhor = campanhasRepository.findCampanhasByPaginaComeMelhor();
           model.addAttribute("campanhas2", campanhasComeMelhor);
           
           List<campanhas> campanhasDestaque = campanhasRepository.findCampanhasByPaginaDestaque();
           model.addAttribute("campanhas3", campanhasDestaque);
           
           List<campanhas> campanhasDestaqueCorporativo = campanhasRepository.findCampanhasByPaginaDestaqueCorporativo();
           model.addAttribute("campanhas4", campanhasDestaqueCorporativo);
           
       }else {
    	   if(usua.getLogin().equals("CarlaFerreira")) {
    		   List<campanhas> campanhasComidaPremiada = campanhasRepository.findCampanhasByPaginaComidaPremiadaAndCnpjAndEssencia(usua.getCnpjEmpresa());
               model.addAttribute("campanhas", campanhasComidaPremiada);
        	     
               List<campanhas> campanhasMelhoresEmpresas = campanhasRepository.findCampanhasByPaginaMelhoresEmpresasAndCnpjAndEssencia(usua.getCnpjEmpresa());
               model.addAttribute("campanhas1", campanhasMelhoresEmpresas);
               
               List<campanhas> campanhasComeMelhor = campanhasRepository.findCampanhasByPaginaComeMelhorAndCnpjAndEssencia(usua.getCnpjEmpresa());
               model.addAttribute("campanhas2", campanhasComeMelhor);
               
               List<campanhas> campanhasDestaque = campanhasRepository.findCampanhasByPaginaDestaqueAndCnpjAndEssencia(usua.getCnpjEmpresa());
               model.addAttribute("campanhas3", campanhasDestaque);
               
    		   List<campanhas> campanhasDestaqueCorporativo = campanhasRepository.findCampanhasByPaginaDestaqueCorporativoAndCnpjAndEssencia(usua.getCnpjEmpresa());
               model.addAttribute("campanhas4", campanhasDestaqueCorporativo);
              
    	   }else {
    	   List<campanhas> campanhasComidaPremiada = campanhasRepository.findCampanhasByPaginaComidaPremiadaAndCnpj(usua.getCnpjEmpresa());
           model.addAttribute("campanhas", campanhasComidaPremiada);
    	     
           List<campanhas> campanhasMelhoresEmpresas = campanhasRepository.findCampanhasByPaginaMelhoresEmpresasAndCnpj(usua.getCnpjEmpresa());
           model.addAttribute("campanhas1", campanhasMelhoresEmpresas);
           
           List<campanhas> campanhasComeMelhor = campanhasRepository.findCampanhasByPaginaComeMelhorAndCnpj(usua.getCnpjEmpresa());
           model.addAttribute("campanhas2", campanhasComeMelhor);
           
           List<campanhas> campanhasDestaque = campanhasRepository.findCampanhasByPaginaDestaqueAndCnpj(usua.getCnpjEmpresa());
           model.addAttribute("campanhas3", campanhasDestaque);
           
           List<campanhas> campanhasDestaqueCorporativo = campanhasRepository.findCampanhasByPaginaDestaqueCorporativoAndCnpj(usua.getCnpjEmpresa());
           model.addAttribute("campanhas4", campanhasDestaqueCorporativo);
    	   }
       }


       return "/paginas/campanhasglobais";
   }

    private String formatarNome(String username) {
        // Divide o nome em letras maiúsculas e minúsculas
        // Exemplo: "ErysonMoreira" -> "Eryson Moreira"
        return username.replaceAll("([a-z])([A-Z])", "$1 $2");
    }
    
    @RequestMapping(method = RequestMethod.GET,value = "/update-sheets")
    public ModelAndView updateGoogleSheets(){
    	ModelAndView modelview = new ModelAndView("redirect:/campanhas");
    	try {
            googleSheetsService.loadDataFromGoogleSheets();
            System.out.println("Dados atualizados com sucesso!"); 
        } catch (Exception e) {
            System.out.println("Erro ao atualizar dados: " + e.getMessage()); 
        }
    	return modelview;
    }
     
}
