package com.project.Mesa.Service;
import org.springframework.stereotype.Service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.oauth2.GoogleCredentials;
import com.project.Mesa.Model.campanhas;
import com.project.Mesa.Model.filial;
import com.project.Mesa.Repository.CampanhasRepository;
import com.project.Mesa.Repository.FilialRepository;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;


import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;


@Service 
public class GoogleSheetsService {

	private static final String APPLICATION_NAME = "Google Sheets API Java";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String SPREADSHEET_ID = "1rLLWCe77_QSN8hFbndTOcKVHBy81TtDxs5DT6Asb0nY";

    @Autowired
    private CampanhasRepository campanhasRepository;
    
    @Autowired
    private FilialRepository filialRepository;

    public void loadDataFromGoogleSheets() throws IOException, GeneralSecurityException {
        Sheets sheetsService = getSheetsService();
        List<String> sheetNames = List.of("Comida Premiada", "Melhores Empresas", "Come Melhor", "Destaque", "Destaque Corporativo");

        for (String sheetName : sheetNames) {
            String range = sheetName + "!A:O";
            ValueRange response = sheetsService.spreadsheets().values()
                    .get(SPREADSHEET_ID, range)
                    .execute();
            List<List<Object>> values = response.getValues();

            if (values == null || values.isEmpty()) {
                System.out.println("Nenhum dado encontrado para " + sheetName);
                continue;
            }

            // Iterar sobre as linhas e mapear para a entidade `campanhas`
            for (int i = 0; i < values.size(); i++) {
                List<Object> row = values.get(i);

                // Pula a primeira linha (índice 0)
                if (i == 0) {
                    continue; // Ignora a primeira linha
                }
            	  if (sheetName.equals("Comida Premiada")) {
            		  campanhas campanha = new campanhas();
            	        campanha.setPeriodo(row.get(0).toString());
            	        campanha.setGrupo(row.get(1).toString());
                        String nomeFilial = row.get(2).toString(); 
                        if (nomeFilial != null) {
                            Optional<filial> filialOpt = filialRepository.findByNome(nomeFilial);
                            if (filialOpt.isPresent()) {
                                campanha.setEmpresa(filialOpt.get()); 
                            } else {
                                System.out.println("Filial com nome " + nomeFilial + " não encontrada.");
                            }
                        }
            	        campanha.setCategoria_participante(row.size() > 5 ? row.get(5).toString() : null);
                        campanha.setCargoParticipante(row.size() > 6 ? row.get(6).toString() : null);
                        campanha.setNomeParticipante(row.size() > 7 ? row.get(7).toString() : null);
                        campanha.setMeta(row.size() > 8 ? row.get(8).toString() : null);
                        campanha.setRealizado(row.size() > 9 ? row.get(10).toString() : null);
                        campanha.setMeta_atingida(row.size() > 10 ? row.get(10).toString() : null);
                        campanha.setValor_por_bateria(row.size() > 11 ? row.get(11).toString() : null);
                        campanha.setValor_premiacao(row.size() > 12 ? row.get(12).toString() : null);
                        campanha.setValor_com_taxa(row.size() > 13 ? row.get(13).toString() : null);
                        campanha.setPaginas("Comida Premiada");
                        
                        
                     // Verificar se a campanha já existe no banco de dados
                        boolean exists = campanhasRepository.existsByPeriodoAndGrupoAndCargoParticipanteAndNomeParticipanteAndEmpresa(
                                campanha.getPeriodo(), 
                                campanha.getGrupo(), 
                                campanha.getCargoParticipante(),
                                campanha.getNomeParticipante(), 
                                campanha.getEmpresa()
                            );

                        if (exists) {
                            System.out.println("Campanha já existe: " + campanha.getPeriodo() + ", " + campanha.getGrupo() + ", " + campanha.getNomeParticipante());
                            continue; // Pula para a próxima iteração se a campanha já existir
                        }else {

                        // Se não existir, salva a nova campanha
                        campanhasRepository.save(campanha);
                        System.out.println("Campanha adicionada: " + campanha.getNomeParticipante());
                    }
                        
            	        
            	    } else if (sheetName.equals("Melhores Empresas")) {
            	    	campanhas campanha = new campanhas();
            	    	campanha.setPeriodo(row.get(0).toString());
                        campanha.setGrupo(row.get(1).toString());
                        String nomeFilial = row.size() > 2 ? row.get(2).toString() : null; 
                        if (nomeFilial != null) {
                            Optional<filial> filialOpt = filialRepository.findByNome(nomeFilial);
                            if (filialOpt.isPresent()) {
                                campanha.setEmpresa(filialOpt.get()); // Associar a filial encontrada
                            } else {
                                System.out.println("Filial com nome " + nomeFilial + " não encontrada.");
                                continue; // Não salva se a filial não foi encontrada
                            }
                        } else {
                            System.out.println("Nome da filial é nulo.");
                            continue; // Não salva se o nome da filial é nulo
                        }
            	        campanha.setNomeParticipante(row.get(5).toString());
            	        campanha.setMeta(row.get(6).toString());
            	        campanha.setMeta_atingida(row.get(7).toString());
            	        campanha.setPositivou(row.get(8).toString());
            	        campanha.setFaixa_de_premiacao(row.size() > 9 ? row.get(9).toString() : null);
            	        campanha.setValor_premiacao(row.size() > 10 ? row.get(10).toString() : null);
                        campanha.setValor_com_taxa(row.size() > 11 ? row.get(11).toString() : null);
                        campanha.setPaginas("Melhores Empresas");
                        
                        boolean exists = campanhasRepository.existsByPeriodoAndGrupoAndCargoParticipanteAndNomeParticipanteAndEmpresa(
                                campanha.getPeriodo(), 
                                campanha.getGrupo(), 
                                campanha.getCargoParticipante(),
                                campanha.getNomeParticipante(), 
                                campanha.getEmpresa()
                            );

                            if (exists) {
                                System.out.println("Campanha já existe: " + campanha.getPeriodo() + ", " + campanha.getGrupo() + ", " + campanha.getNomeParticipante());
                                continue;
                            }else {

                            // Se não existir, salva a nova campanha
                            campanhasRepository.save(campanha);
                            System.out.println("Campanha adicionada: " + campanha.getNomeParticipante());
                        }
                        
                        
                         
            	    } else if(sheetName.equals("Come Melhor")) {
            	    	campanhas campanha = new campanhas();
            	    	campanha.setPeriodo(row.get(0).toString());
                        campanha.setGrupo(row.get(1).toString());
                        String nomeFilial = row.size() > 2 ? row.get(2).toString() : null; 
                        if (nomeFilial != null) {
                            Optional<filial> filialOpt = filialRepository.findByNome(nomeFilial);
                            if (filialOpt.isPresent()) {
                                campanha.setEmpresa(filialOpt.get()); // Associar a filial encontrada
                            } else {
                                System.out.println("Filial com nome " + nomeFilial + " não encontrada.");
                            }
                        }
                        campanha.setCargoParticipante(row.size() > 3 ? row.get(3).toString() : null);
                        campanha.setNomeParticipante(row.size() > 4 ? row.get(4).toString() : null);
                        campanha.setMeta(row.get(5).toString());
                        campanha.setReal_volume(row.get(6).toString());
            	        campanha.setMeta_atingida(row.get(7).toString());
            	        campanha.setMeta_positivacao(row.get(8).toString());
            	        campanha.setReal_positivacao(row.get(9).toString());
            	        campanha.setMeta_positivacao_atingida(row.get(10).toString());
            	        campanha.setValor_premiacao(row.size() > 11 ? row.get(11).toString() : null);
                        campanha.setValor_com_taxa(row.size() > 12 ? row.get(12).toString() : null);
                        campanha.setPaginas("Come Melhor");
                        

                        boolean exists = campanhasRepository.existsByPeriodoAndGrupoAndCargoParticipanteAndNomeParticipanteAndEmpresa(
                                campanha.getPeriodo(), 
                                campanha.getGrupo(), 
                                campanha.getCargoParticipante(),
                                campanha.getNomeParticipante(), 
                                campanha.getEmpresa()
                            );

                            if (exists) {
                                System.out.println("Campanha já existe: " + campanha.getPeriodo() + ", " + campanha.getGrupo() + ", " + campanha.getNomeParticipante());
                                continue; 
                            }else {

                            // Se não existir, salva a nova campanha
                            campanhasRepository.save(campanha);
                            System.out.println("Campanha adicionada: " + campanha.getNomeParticipante());
                        }
            	    	
            	    } else if(sheetName.equals("Destaque")) {
            	    	campanhas campanha = new campanhas();
            	    	campanha.setPeriodo(row.get(0).toString());
                        campanha.setGrupo(row.get(1).toString());
                        String nomeFilial = row.size() > 2 ? row.get(2).toString() : null; 
                        if (nomeFilial != null) {
                            Optional<filial> filialOpt = filialRepository.findByNome(nomeFilial);
                            if (filialOpt.isPresent()) {
                                campanha.setEmpresa(filialOpt.get()); // Associar a filial encontrada
                            } else {
                                System.out.println("Filial com nome " + nomeFilial + " não encontrada.");
                            }
                        }
                        campanha.setCargoParticipante(row.get(3).toString());
                        campanha.setNomeParticipante(row.size() > 4 ? row.get(4).toString() : null);
                        campanha.setValor_premiacao(row.size() > 5 ? row.get(5).toString() : null);
                        campanha.setValor_com_taxa(row.size() > 6 ? row.get(6).toString() : null);
                        campanha.setPaginas("Destaque");
                        boolean exists = campanhasRepository.existsByPeriodoAndGrupoAndCargoParticipanteAndNomeParticipanteAndEmpresa(
                                campanha.getPeriodo(), 
                                campanha.getGrupo(), 
                                campanha.getCargoParticipante(),
                                campanha.getNomeParticipante(), 
                                campanha.getEmpresa()
                            );

                            if (exists) {
                                System.out.println("Campanha já existe: " + campanha.getPeriodo() + ", " + campanha.getGrupo() + ", " + campanha.getNomeParticipante());
                                continue; 
                            }else {

                            // Se não existir, salva a nova campanha
                            campanhasRepository.save(campanha);
                            System.out.println("Campanha adicionada: " + campanha.getNomeParticipante());
                        }
            	    } else if(sheetName.equals("Destaque Corporativo")) {
            	    	campanhas campanha = new campanhas();
            	    	campanha.setPeriodo(row.get(0).toString());
                        campanha.setGrupo(row.get(1).toString());
                        String nomeFilial = row.size() > 2 ? row.get(2).toString() : null; 
                        if (nomeFilial != null) {
                            Optional<filial> filialOpt = filialRepository.findByNome(nomeFilial);
                            if (filialOpt.isPresent()) {
                                campanha.setEmpresa(filialOpt.get()); // Associar a filial encontrada
                            } else {
                                System.out.println("Filial com nome " + nomeFilial + " não encontrada.");
                            }
                        }
                        campanha.setCargoParticipante(row.get(3).toString());
                        campanha.setColocacao(row.get(4).toString());
                        campanha.setNomeParticipante(row.size() > 5 ? row.get(5).toString() : null);
                        campanha.setValor_premiacao(row.size() > 6 ? row.get(6).toString() : null);
                        campanha.setValor_com_taxa(row.size() > 7 ? row.get(7).toString() : null);
                        campanha.setPaginas("Destaque Corporativo");
                        
                        boolean exists = campanhasRepository.existsByPeriodoAndGrupoAndCargoParticipanteAndNomeParticipanteAndEmpresa(
                                campanha.getPeriodo(), 
                                campanha.getGrupo(), 
                                campanha.getCargoParticipante(),
                                campanha.getNomeParticipante(), 
                                campanha.getEmpresa()
                            );

                            if (exists) {
                                System.out.println("Campanha já existe: " + campanha.getPeriodo() + ", " + campanha.getGrupo() + ", " + campanha.getNomeParticipante());
                                continue; 
                            }else {

                            // Se não existir, salva a nova campanha
                            campanhasRepository.save(campanha);
                            System.out.println("Campanha adicionada: " + campanha.getNomeParticipante());
                        }
            	    }
                /*
            	 campanha.setPeriodo(row.get(0).toString());
                campanha.setGrupo(row.get(1).toString());
                campanha.setCategoria_participante(row.size() > 2 ? row.get(2).toString() : null);
                campanha.setCargo_participante(row.size() > 3 ? row.get(3).toString() : null);
                campanha.setNome_participante(row.size() > 4 ? row.get(4).toString() : null);
                campanha.setMeta(row.size() > 5 ? row.get(5).toString() : null);
                campanha.setReal_volume(row.size() > 6 ? row.get(6).toString() : null);
                campanha.setRealizado(row.size() > 7 ? row.get(7).toString() : null);
                campanha.setMeta_atingida(row.size() > 8 ? row.get(8).toString() : null);
                campanha.setMeta_positivacao(row.size() > 9 ? row.get(9).toString() : null);
                campanha.setReal_positivacao(row.size() > 10 ? row.get(10).toString() : null);
                campanha.setMeta_positivacao_atingida(row.size() > 11 ? row.get(11).toString() : null);
                campanha.setValor_por_bateria(row.size() > 12 ? row.get(12).toString() : null);
                campanha.setFaixa_de_premiacao(row.size() > 13 ? row.get(13).toString() : null);
                campanha.setValor_premiacao(row.size() > 14 ? row.get(14).toString() : null);
                campanha.setValor_com_taxa(row.size() > 15 ? row.get(15).toString() : null);
                campanha.setColocacao(row.size() > 16 ? row.get(16).toString() : null);
                campanha.setPositivou(row.size() > 17 ? row.get(17).toString() : null);
                campanha.setPaginas(sheetName);
                */
                
                
                
               
            }
        }
    }

  public Sheets getSheetsService() throws IOException, GeneralSecurityException {
    // Carregar as credenciais do Google a partir da variável de ambiente
    GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(System.getenv("GOOGLE_CREDENTIALS").getBytes()))
            .createScoped(List.of(SheetsScopes.SPREADSHEETS));
    
    // Conectar-se à API do Google Sheets
    HttpCredentialsAdapter credentialsAdapter = new HttpCredentialsAdapter(credentials);
    return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credentialsAdapter)
            .setApplicationName(APPLICATION_NAME)
            .build();
}
}
