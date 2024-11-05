package com.project.Mesa;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

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
import com.project.Mesa.Model.filial;
import com.project.Mesa.Repository.FilialRepository;
import com.project.Mesa.Repository.UserRepository;
import com.project.Mesa.Service.GoogleSheetsService;
import com.project.Mesa.Service.SequenceService;



@SpringBootApplication
@EntityScan(basePackages = "com.project.Mesa.Model")
@ComponentScan(basePackages = {"com.*"})
@EnableJpaRepositories({"com.project.Mesa.Repository"})
@EnableTransactionManagement 
@EnableWebMvc
@RestController
@EnableAutoConfiguration
public class MesaApplication {
	
	@Autowired
	 private FilialRepository filialRepository;
	 
	 @Autowired
	 private UserRepository usuario;
	 
	 @Autowired
	 private SequenceService sequenceService;
	 
	@Autowired
    private GoogleSheetsService googleSheetsService;
	
	public static void main(String[] args) {
		SpringApplication.run(MesaApplication.class, args);
	}
	
	 @Bean
	    public CommandLineRunner commandLineRunner(UserRepository userRepository) {
	        return args -> {
	        	sequenciaInicial();
	        	criarFilialSeNaoExistir(filialRepository, "999999999.0001-99", "Saboris Gourmet", "Saboris Gourmeet LTDA");
	        	criarFilialSeNaoExistir(filialRepository, "888888888.0001-88", "Delícia Viva", "Delícia Viva LTDA");
	        	criarFilialSeNaoExistir(filialRepository, "777777777.0001-77", "Bela Mesa Alimentos", "Bela Mesa Alimentos LTDA");
	        	criarFilialSeNaoExistir(filialRepository, "555555555.0001-55", "Essência do Sabor", "Essência do Sabor LTDA");
	        	criarFilialSeNaoExistir(filialRepository, "666666666.0001-66", "Porto Digital", "Porto Digital LTDA");
	        	
	        	criarUsuarioSeNaoExistir(userRepository, "MarianaSoares", "Gerente", "999999999.0001-99", "1234");
				criarUsuarioSeNaoExistir(userRepository, "CarlaFerreira", "Gerente", "888888888.0001-88", "1234");
				criarUsuarioSeNaoExistir(userRepository, "GabrielLima", "Gerente", "777777777.0001-77", "1234");
				criarUsuarioSeNaoExistir(userRepository, "ErysonMoreira", "Administrador", "666666666.0001-66", "1234");
			
				  try {
		                googleSheetsService.loadDataFromGoogleSheets();
		                System.out.println("Dados carregados do Google Sheets com sucesso.");
		            } catch (IOException | GeneralSecurityException e) {
		                System.err.println("Erro ao carregar dados do Google Sheets: " + e.getMessage());
		            }
	        };
	    }
	 
	 private void criarUsuarioSeNaoExistir(UserRepository userRepository, String login, String cargo, String cnpjEmpresa, String senha) {
		 Optional<filial> empresa = filialRepository.findById(cnpjEmpresa);  // Busca a filial pelo nome
		    if (empresa.isPresent()) {  // Verifica se a filial existe
		        Users user = userRepository.findByUsername(login);
		        if (user == null) {
		            Users novoUsuario = new Users();
		            novoUsuario.setLogin(login);
		            novoUsuario.setCargo(cargo);
		            novoUsuario.setEmpresa(empresa.get());
		            novoUsuario.setPassword(senha); 
		            userRepository.save(novoUsuario);
		            System.out.println("Usuário " + login + " criado com sucesso.");
		        }
		    } else {
		    	filial filiall = empresa.get(); 
		        System.out.println("A empresa " + filiall.getNome() + " não foi encontrada.");
		    }
		}
	 
	 
	 private void criarFilialSeNaoExistir(FilialRepository filialrepository, String cnpj, String nome, String razaosocial) {
			Optional<filial> filial = filialrepository.findById(cnpj);
			if (filial.isEmpty()) {
				filial novofilial = new filial();
				novofilial.setCnpj(cnpj);
				novofilial.setNome(nome);
				novofilial.setRazaosocial(razaosocial); 
				filialrepository.save(novofilial);
				System.out.println("Filial " + nome + " criado com sucesso.");
			}
		}
	 
	 
	 private void sequenciaInicial() {
		 
		 Iterable<Users> users = usuario.findAll();  // Obtém todos os usuários
		    if (!users.iterator().hasNext()) {  // Verifica se a lista está vazia
		        sequenceService.restartSequence(1L);  // Reinicia a sequência se não houver usuários
		        System.out.println("Restart da Sequência Realizada");
		    }
		 }
		 
	 }
	    


