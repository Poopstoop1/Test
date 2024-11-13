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
import java.nio.charset.StandardCharsets;
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
                        boolean exists = campanhasRepository.existsByPeriodoAndGrupoAndCargoParticipanteAndNomeParticipanteAndEmpresaAndPaginas(
                                campanha.getPeriodo(), 
                                campanha.getGrupo(), 
                                campanha.getCargoParticipante(),
                                campanha.getNomeParticipante(), 
                                campanha.getEmpresa(),
                                campanha.getPaginas()
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
            	        campanha.setRealizado(row.get(7).toString());
            	        campanha.setMeta_atingida(row.get(8).toString());
            	        campanha.setPositivou(row.get(9).toString());
            	        campanha.setFaixa_de_premiacao(row.size() > 10 ? row.get(10).toString() : null);
            	        campanha.setValor_premiacao(row.size() > 11 ? row.get(11).toString() : null);
                        campanha.setValor_com_taxa(row.size() > 12 ? row.get(12).toString() : null);
                        campanha.setPaginas("Melhores Empresas");
                        
                        boolean exists = campanhasRepository.existsByPeriodoAndGrupoAndCargoParticipanteAndNomeParticipanteAndEmpresaAndPaginas(
                                campanha.getPeriodo(), 
                                campanha.getGrupo(), 
                                campanha.getCargoParticipante(),
                                campanha.getNomeParticipante(), 
                                campanha.getEmpresa(),
                                campanha.getPaginas()
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
                        

                        boolean exists = campanhasRepository.existsByPeriodoAndGrupoAndCargoParticipanteAndNomeParticipanteAndEmpresaAndPaginas(
                                campanha.getPeriodo(), 
                                campanha.getGrupo(), 
                                campanha.getCargoParticipante(),
                                campanha.getNomeParticipante(), 
                                campanha.getEmpresa(),
                                campanha.getPaginas()
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
                        boolean exists = campanhasRepository.existsByPeriodoAndGrupoAndCargoParticipanteAndNomeParticipanteAndEmpresaAndPaginas(
                                campanha.getPeriodo(), 
                                campanha.getGrupo(), 
                                campanha.getCargoParticipante(),
                                campanha.getNomeParticipante(), 
                                campanha.getEmpresa(),
                                campanha.getPaginas()
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
                        
                        boolean exists = campanhasRepository.existsByPeriodoAndGrupoAndCargoParticipanteAndNomeParticipanteAndEmpresaAndPaginas(
                                campanha.getPeriodo(), 
                                campanha.getGrupo(), 
                                campanha.getCargoParticipante(),
                                campanha.getNomeParticipante(), 
                                campanha.getEmpresa(),
                                campanha.getPaginas()
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

    private Sheets getSheetsService() throws IOException, GeneralSecurityException {
        // Coloque o conteúdo do seu arquivo credentials.json aqui como uma string JSON
        String jsonCredentials = "{\n" +
                "  \"type\": \"service_account\",\n" +
                "  \"project_id\": \"eminent-wares-440602-i2\",\n" +
                "  \"private_key_id\": \"2581197b30d49f1f2d606a1520f285ae2eb7385d\",\n" +
                "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\n" + 
                "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDbGsXgh8mMpFGv\\n" + 
                "nuTTDFwGSqcZUIFhnmF/4UvR10jsEdFPp24ewNu8RlweMd9SgFD/nogP4PZPPptZ\\n" + 
                "iIEpe98R40A08OppJodcccfSjyXgKdUZZKiKhjYdX/5n0nvZ41kA0/wTYsOpGfnc\\n" + 
                "UwpqjVS6ajdBb14l58woGsI3LHBtZCfx7qo2CYSaR3591Hx828jsQNNj8Xr6ib9O\\n" + 
                "Xzh7EyAfiYVyPqYYiEX6c1NOnm0+67vsXMmBD0cB7V3ZmAzx9OirPAyMqWojCRTb\\n" + 
                "wGuNb9oAfDwh8SLLSIa0qKbkXTAtCM5NSEg5efVHMLWbFG+s7MJtkHItaxL9wMh/\\n" + 
                "YlSiKf4NAgMBAAECggEAIjFZ6xHVysyD8O/Pjv4RSGwTCrjNECJq3c2Xiv5ZY3ne\\n" + 
                "W9tprIP1ZrrmOlIXI2Vh/ppLaor1dWV0UfMjEQnDRKPdy8VDF7jLDAq3n+z2ALcW\\n" + 
                "ifly8fdC76+g4Kvw5w57P81VO+NAXJ3wp8kPyZQXwmW8DMqn+yti6WlPoNOfEfry\\n" + 
                "29fxAJqXfbx8YtH+PmlotzpTvmxzNESCR3MQHhBVYUDpIGN0oUfJ3s3M656eRxFv\\n" + 
                "X7NX4EozikFcdk/CVObKZMKhWKRmkMnaFzkBkLrGplJQxToezyp8hdm6tL7m71Sq\\n" + 
                "GjWBbkfgkYDJQLm2ozEHwljwSmezLApbGdedevU+QQKBgQD46HFKbKQsCjGYtnyU\\n" + 
                "sjdLld1fT1JFgPh0S9pp8VMA7RIkMg36sMKxRYivt5u42he3vPAHzG/WB7MNKOkb\\n" + 
                "HwqU2lcO9erFU+A3Atrvevh5ae6+BzU2Nol6GnM9UmJ/2wDzZ9OOI3B3yyNfVUgc\\n" + 
                "bPjCNvuqmCfEpNBz5mt4ojXA7QKBgQDhWPEU7oOK/b62jQZ0U/gGoGpQAVfxlijd\\n" + 
                "RuTI5y2hx4QZMVuCRjU2oIZfnxT6wznBJmiMszTnLD3Gx65QtfB9c/sAlfAC6Ceb\\n" + 
                "BrzKT87DEYDv/g1CkPv7PDmg7QmnCnDY70CyiD5olkmqsBqPxq0HJlTV6r4UOG+Y\\n" + 
                "Qz3jVPotoQKBgHZTcb/yo5z3/5ncbp71lcnN5Z3whGjcJcCLarpgaZgQG0avsOuS\\n" + 
                "6gsBxjfabiHTM+E55VWfvy6dHGZOI3qsKAiZPzeyejfyZq1gIxojeEmnrUITBR3P\\n" + 
                "kU1Fk7D8IC0tvGb53Z09hbK9FAS28v/oYd1Z7AuuJ4GY5Cukx0RiwHEFAoGAD1Ci\\n" + 
                "IdAL9JhGK9XtyF4kjx672vAcb/jKki9NQTIk8cfDfIrUM0heOXYza7A+FsTJ2gyo\\n" + 
                "MlfDkqp5EFdly2pyC7SkLGgERc5NUsXkcsN1w+AeqTDU6d88oNh4+izh6Q5WmQe5\\n" + 
                "l+iWhTkhjI4nX/oarE+4mEk53dnwC6MWQ6r0zcECgYA34xNn2iuYEJXYSk+xtW5a\\n" + 
                "9KLGGMgrLJuzbUWaIHexhglavxxvDaBhE9ySKdCTvajZN1ViMmRfb5vNoQ3mpJkY\\n" + 
                "yAmmx7UcG4P4GIJiT3C9eueOPK36UendJa1AE81DxqwgjLaUNIZiMyOvyQbND00N\\n" + 
                "jef6Ka9epLXGvhv93kX3Qw==\\n" + 
                "-----END PRIVATE KEY-----\\n" + 
                "\",\n" +
                "  \"client_email\": \"mesa-project@eminent-wares-440602-i2.iam.gserviceaccount.com\",\n" +
                "  \"client_id\": \"116010670162467489194\",\n" +
                "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/mesa-project%40eminent-wares-440602-i2.iam.gserviceaccount.com\"\n" +
                "}";

        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new ByteArrayInputStream(jsonCredentials.getBytes(StandardCharsets.UTF_8)))
                .createScoped(List.of(SheetsScopes.SPREADSHEETS_READONLY));

        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
