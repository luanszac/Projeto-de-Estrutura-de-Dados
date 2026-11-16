/*
 * Matrícula: 10750029 - Nome: Luan Souza Costa
 * Matrícula: 10445065 - Nome: Estephany Nicole da Silva
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeitorCSV {

    public static int[] carregarDataset(String caminhoArquivo, ArvoreBST arvore) throws IOException {
        int inseridos = 0;
        int descartados = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            StringBuilder registroAtual = new StringBuilder();
            boolean dentroDeAspas = false;
            boolean primeiraLinha = true;
            String linha;

            while ((linha = br.readLine()) != null) {
                if (registroAtual.length() > 0) {
                    registroAtual.append("\n");
                }
                registroAtual.append(linha);

                for (int i = 0; i < linha.length(); i++) {
                    if (linha.charAt(i) == '"') {
                        dentroDeAspas = !dentroDeAspas;
                    }
                }

                if (!dentroDeAspas) {
                    String registroCompleto = registroAtual.toString();
                    registroAtual.setLength(0);

                    if (primeiraLinha) {
                        primeiraLinha = false;
                        continue;
                    }

                    List<String> colunas = parseLinhaCSV(registroCompleto);
                    if (validarRegistro(colunas)) {
                        try {
                            ProgramaNetFlix programa = criarPrograma(colunas);
                            arvore.inserir(programa);
                            inseridos++;
                        } catch (NumberFormatException e) {
                            descartados++;
                        }
                    } else {
                        descartados++;
                    }
                }
            }
        }

        return new int[]{inseridos, descartados};
    }

    private static List<String> parseLinhaCSV(String linha) {
        List<String> campos = new ArrayList<>();
        StringBuilder campoAtual = new StringBuilder();
        boolean dentroDeAspas = false;

        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);
            if (c == '"') {
                if (dentroDeAspas && i + 1 < linha.length() && linha.charAt(i + 1) == '"') {
                    campoAtual.append('"');
                    i++;
                } else {
                    dentroDeAspas = !dentroDeAspas;
                }
            } else if (c == ',' && !dentroDeAspas) {
                campos.add(campoAtual.toString().trim());
                campoAtual.setLength(0);
            } else {
                campoAtual.append(c);
            }
        }
        campos.add(campoAtual.toString().trim());
        return campos;
    }

    private static boolean validarRegistro(List<String> colunas) {
        if (colunas.size() != 15) {
            return false;
        }
        boolean isMovie = "MOVIE".equalsIgnoreCase(colunas.get(2));
        for (int i = 0; i < colunas.size(); i++) {
            if (i == 9 && isMovie) {
                continue;
            }
            String col = colunas.get(i);
            if (col == null || col.isEmpty() || col.equalsIgnoreCase("null") || col.equalsIgnoreCase("nan")) {
                return false;
            }
        }
        return true;
    }

    private static ProgramaNetFlix criarPrograma(List<String> c) {
        String id = c.get(0);
        String title = c.get(1);
        String type = c.get(2);
        String description = c.get(3);
        int releaseYear = Integer.parseInt(c.get(4));
        String ageCertification = c.get(5);
        int runtime = Integer.parseInt(c.get(6));
        String genres = c.get(7);
        String productionCountries = c.get(8);
        String seasonsStr = c.get(9);
        double seasons = (seasonsStr == null || seasonsStr.isEmpty() ||
                seasonsStr.equalsIgnoreCase("null") || seasonsStr.equalsIgnoreCase("nan"))
                ? 0.0
                : Double.parseDouble(seasonsStr);
        String imdbId = c.get(10);
        double imdbScore = Double.parseDouble(c.get(11));
        double imdbVotes = Double.parseDouble(c.get(12));
        double tmdbPopularity = Double.parseDouble(c.get(13));
        double tmdbScore = Double.parseDouble(c.get(14));

        return new ProgramaNetFlix(
            id, title, type, description, releaseYear, ageCertification, runtime,
            genres, productionCountries, seasons, imdbId, imdbScore, imdbVotes,
            tmdbPopularity, tmdbScore
        );
    }
}