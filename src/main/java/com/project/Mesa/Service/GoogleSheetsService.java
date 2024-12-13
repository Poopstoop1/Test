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
                "  \"private_key_id\": \"249526d95b0aed8975ba537eea1aace3d11e34ae\",\n" +
                "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\n" + 
                "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCvOtlPFcs+c+ik\\n" + 
                "7c8pnlfNQ8BUkuN0CCU20JNE5sGKIJdW8lCb6F0c1lvXqUUelt3Zdr+tUGqr/0mm\\n" + 
                "Vni7JVmYRfxpcoSOWs8viBbifelyMoeXWTIqouKxvy6Wk2riw4OFKveEyZdOEiDs\\n" + 
                "OiXLAb8FOcyHq3IcCNuFkoyFEkMIj9+Kw1EM+CYMOUkDerPBv/F9+P2rtVT+2wyW\\n" + 
                "dtgtdcHGHNtN7+e2atsfo6mD+J26rWoznjWWjpLAkTzebMH2+c7eJhmSVNfAiQOr\\n" + 
                "nSPhsxKoX1v8tk5rhLqXH3F7NWb/eCKk5quNLRS1S6MJXzDAJTxoJo0BP30m8DUM\\n" + 
                "8YwChfVTAgMBAAECggEACOViHUI8KHEGVBfgpw/Atw05U/HbIKETO+ut4QHIEe+i\\n" + 
                "pJ7hBHFGXOLtvw6lvC5Tl/6P2kgqtOMH2wJ+gZ3H+n4/GMlgEmwTeFRz46QZzcWO\\n" + 
                "na/rvJS6u+RlWe6lBAXXWlOP77VeUZqDrQqCBa2ShDzmyyfN6hTHCZ/NiNSgWOxY\\n" + 
                "wAkp9R7YTw8sQrI5BUqRHYj6MxZAd7iZPRviBIRy9cynpBM9tfKGYTho1f/IAnp3\\n" + 
                "39gqeh6P3V9sGgL3jfx19LdAfmRCjqbMgFTwdAfB0mSolE5ICCeaBDnrNoVqS+1b\\n" + 
                "ZQJvR8xE9B5mLbiudGIfMk5GDHSYcG1mGNlTVDMgJQKBgQDgwTDeyZUE9ZAPbaSU\\n" + 
                "SiFFNQjjgHp8dzzvZU2wL8lsB43AHE4Cx73mXkcV+y5Hy4yjYJTmxaXJC6BQouen\\n" + 
                "TtFBUakk+V349FJw76A5NIW/OMTc4/koBlUSzsdRzddEzx3SNr4GZpDt8MVA+fZQ\\n" + 
                "63eXJvFV2j1/eFdOXwNIfqu7HQKBgQDHlx4IeKWCohT71hjuP60OOYRDJgb08v+X\\n" + 
                "GqIJJQtpaN/24pIFq2w1pm9o93M5M6ji283/wvD4fiYeGKJWTbltTyYpRC4UkwsT\\n" + 
                "gAdfK2XJjnjCZfYS/CzcBuCwMT/15SE8luZp9tnABRCz1zODnELHf+fFdxtDBKTj\\n" + 
                "r2yaKbsXLwKBgDTGiJA364sfknVc/KFpishyfKTXPWvOn/ti4Uw23GhGmc17mAHl\\n" + 
                "Hz1h8/VAZwyqylpuymgqDxTXawykRBswUcixwTMSbzOPQ3CLhCcKo0FUtj1cNtxk\\n" + 
                "KEPjVEBsQllA/TKochSUrwGAYQEx8NntgMZ4eYD8XyZ4fp/DhcTXIcItAoGAeqAc\\n" + 
                "4qWFQspkTAnRTWev4mJyIXNWqN43LFY/oZCx/zRhDeVMQ9F7bUbxgjMuBsH3jFHx\\n" + 
                "sabKuFGBB9Kowo+EUmVjk80A5sEk9/kcDluycPBovz83vZhT02ciWv4XRvNcxUYN\\n" + 
                "EKOKS1CRX3zjeIPj/4BVHNw90qIi0ISyG9Fx0h8CgYEAnOIXagmz0tWB6br69cKM\\n" + 
                "H+ZmJJsXL8U0rdZeNGY70UziBF83c1bmdChmU0ZFFb2lLG+l9IBoe3YxIZcB2j8k\\n" + 
                "IyfcECS3JNDH7Jj6qRVT1340y4v01mdCosKlwRSpSwm7sjrQyo+zdfopNX/KdOPx\\n" + 
                "r81iqR06XCzTa+96lg/evmQ==\\n" + 
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
