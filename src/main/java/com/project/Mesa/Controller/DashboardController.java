package com.project.Mesa.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.Mesa.Model.Users;
import com.project.Mesa.Model.campanhas;  // Certifique-se de que a classe Campanha está sendo importada
import com.project.Mesa.Repository.CampanhasRepository;  // Verifique a existência do repositório
import com.project.Mesa.Repository.UserRepository;

import java.util.List;  // Para a lista de campanhas

@Controller
public class DashboardController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CampanhasRepository campanhasRepository;  // Repositório para acessar as campanhas

    @RequestMapping("/")
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Users usua = userRepository.findByUsername(username);
    
        // Passar o nome formatado do usuário
        if (username != null) {
            String nomeFormatado = formatarNome(username);
            model.addAttribute("message", nomeFormatado);
        }
    
        // Verifica se o usuário está autenticado
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
    
        return "dashboard";
    }
    
    private String formatarNome(String username) {
        // Formata o nome para exibição (exemplo: "ErysonMoreira" -> "Eryson Moreira")
        return username.replaceAll("([a-z])([A-Z])", "$1 $2");
    }
}
