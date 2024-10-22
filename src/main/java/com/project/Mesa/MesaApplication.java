package com.project.Mesa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.project.Mesa.Model.Users;
import com.project.Mesa.Repository.SequenceService;
import com.project.Mesa.Repository.UserRepository;



@SpringBootApplication
@EntityScan(basePackages = "com.project.Mesa.Model")
@ComponentScan(basePackages = {"com.*"})
@EnableJpaRepositories({"com.project.Mesa.Repository"})
@EnableTransactionManagement 
@EnableWebMvc
@RestController
@EnableAutoConfiguration
public class MesaApplication {
	
	 
	public static void main(String[] args) {
		SpringApplication.run(MesaApplication.class, args);
	}
	
	 @Bean
	    public CommandLineRunner commandLineRunner(UserRepository userRepository) {
	        return args -> {
	        	sequenciaInicial();
	        	criarUsuarioSeNaoExistir(userRepository, "MarianaSoares", "Gerente", "Saboris Gourmeet LTDA", "1234");
				criarUsuarioSeNaoExistir(userRepository, "CarlaFerreira", "Gerente", "Delicia viva LTDA", "1234");
				criarUsuarioSeNaoExistir(userRepository, "GabrielLima", "Gerente", "Bela Mesa Alimentos", "1234");
				criarUsuarioSeNaoExistir(userRepository, "ErysonMoreira", "Administrador", "PortoDigital", "1234");
	        };
	    }
	 
	 private void criarUsuarioSeNaoExistir(UserRepository userRepository, String login, String cargo, String empresa, String senha) {
			Users user = userRepository.findByUsername(login);
			if (user == null) {
				Users novoUsuario = new Users();
				novoUsuario.setLogin(login);
				novoUsuario.setCargo(cargo);
				novoUsuario.setEmpresa(empresa);
				novoUsuario.setPassword(senha); 
				userRepository.save(novoUsuario);
				System.out.println("Usuário " + login + " criado com sucesso.");
			}
		}
	 
	 @Autowired
	 private UserRepository usuario;
	 
	 @Autowired
	 private SequenceService sequenceService;
	 
	 private void sequenciaInicial() {
		 
		 Iterable<Users> users = usuario.findAll();  // Obtém todos os usuários
		    if (!users.iterator().hasNext()) {  // Verifica se a lista está vazia
		        sequenceService.restartSequence(1L);  // Reinicia a sequência se não houver usuários
		        System.out.println("Restart da Sequência Realizada");
		    }
		 }
		 
	 }
	    


