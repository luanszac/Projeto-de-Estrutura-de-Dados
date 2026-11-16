/*
 * Matrícula: 10750029 - Nome: Luan Souza Costa
 * Matrícula: 10445065 - Nome: Estephany Nicole da Silva
 */

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArvoreBST arvore = new ArvoreBST();
        int opcao = 0;

        do {
            exibirMenuPrincipal();
            String entrada = scanner.nextLine().trim();
            try {
                opcao = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    lerDadosDeArquivo(scanner, arvore);
                    break;
                case 2:
                    executarMenuAnalises(scanner, arvore);
                    break;
                case 3:
                    inserirNovoPrograma(scanner, arvore);
                    break;
                case 4:
                    buscarPrograma(scanner, arvore);
                    break;
                case 5:
                    removerPrograma(scanner, arvore);
                    break;
                case 6:
                    exibirAlturaArvore(arvore);
                    break;
                case 7:
                    salvarDadosEmArquivo(scanner, arvore);
                    break;
                case 8:
                    arvore.limpar();
                    System.out.println("Memória da árvore BST liberada. Aplicação encerrada.");
                    break;
                default:
                    System.out.println("Opção inválida. Digite um número de 1 a 8.");
            }
        } while (opcao != 8);

        scanner.close();
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n================ MENU NETFLIX BST ================");
        System.out.println("1. Ler dados de arquivo");
        System.out.println("2. Análise de dados (5 opções)");
        System.out.println("3. Inserir Programa");
        System.out.println("4. Buscar Programa");
        System.out.println("5. Remover Programa");
        System.out.println("6. Exibir a Altura da Árvore");
        System.out.println("7. Salvar dados em arquivo");
        System.out.println("8. Encerrar a Aplicação");
        System.out.print("Escolha uma opção: ");
    }

    private static void lerDadosDeArquivo(Scanner scanner, ArvoreBST arvore) {
        System.out.print("Informe o nome ou caminho do arquivo CSV (ex: titles.csv): ");
        String nomeArquivo = scanner.nextLine().trim();

        try {
            arvore.limpar();
            int[] resultado = LeitorCSV.carregarDataset(nomeArquivo, arvore);
            System.out.println("Leitura concluída com sucesso!");
            System.out.println("Registros válidos inseridos na BST: " + resultado[0]);
            System.out.println("Registros descartados (atributos incompletos): " + resultado[1]);
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    private static void executarMenuAnalises(Scanner scanner, ArvoreBST arvore) {
        if (arvore.estaVazia()) {
            System.out.println("A árvore BST está vazia. Carregue os dados primeiro (Opção 1).");
            return;
        }

        int subOpcao = 0;
        do {
            System.out.println("\n---------------- ANÁLISE DE DADOS ----------------");
            System.out.println("1. Top 10 títulos TV-14 com gênero 'crime' ordenados por IMDB Score (Percurso Em Ordem)");
            System.out.println("2. N títulos (N > 5) com menores valores de TMDB Score (Percurso Pré-Ordem)");
            System.out.println("3. Top 10 SHOWs com maior número de temporadas (Percurso Pós-Ordem)");
            System.out.println("4. Média de IMDB Score por década de lançamento (Percurso em Largura)");
            System.out.println("5. Top 10 MOVIEs com maior popularidade no TMDB (Percurso Em Ordem)");
            System.out.println("6. Voltar ao menu principal");
            System.out.print("Escolha uma análise: ");

            String entrada = scanner.nextLine().trim();
            try {
                subOpcao = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                subOpcao = -1;
            }

            switch (subOpcao) {
                case 1:
                    List<ProgramaNetFlix> topCrime = arvore.analise1Top10Tv14CrimeEmOrdem();
                    System.out.println("\n--- TOP 10 TÍTULOS TV-14 DO GÊNERO CRIME ---");
                    if (topCrime.isEmpty()) {
                        System.out.println("Nenhum título encontrado.");
                    } else {
                        for (int i = 0; i < topCrime.size(); i++) {
                            ProgramaNetFlix p = topCrime.get(i);
                            System.out.printf("%2d. [%s] %s (%d) | IMDB: %.1f | Gêneros: %s%n",
                                    i + 1, p.getId(), p.getTitle(), p.getReleaseYear(), p.getImdbScore(), p.getGenres());
                        }
                    }
                    break;

                case 2:
                    System.out.print("Informe o valor de N (deve ser maior que 5): ");
                    try {
                        int n = Integer.parseInt(scanner.nextLine().trim());
                        if (n <= 5) {
                            System.out.println("Valor inválido. N deve ser estritamente maior que 5.");
                        } else {
                            List<ProgramaNetFlix> menoresTmdb = arvore.analise2MenoresTmdbScorePreOrdem(n);
                            System.out.printf("\n--- %d TÍTULOS COM MENORES TMDB SCORE ---\n", menoresTmdb.size());
                            for (int i = 0; i < menoresTmdb.size(); i++) {
                                ProgramaNetFlix p = menoresTmdb.get(i);
                                System.out.printf("%2d. [%s] %s (%s) | TMDB Score: %.1f | IMDB Score: %.1f%n",
                                        i + 1, p.getId(), p.getTitle(), p.getType(), p.getTmdbScore(), p.getImdbScore());
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada numérica inválida.");
                    }
                    break;

                case 3:
                    List<ProgramaNetFlix> maisTemporadas = arvore.analise3ShowsMaisTemporadasPosOrdem(10);
                    System.out.println("\n--- TOP 10 SHOWS COM MAIS TEMPORADAS ---");
                    for (int i = 0; i < maisTemporadas.size(); i++) {
                        ProgramaNetFlix p = maisTemporadas.get(i);
                        System.out.printf("%2d. [%s] %s | Temporadas: %.0f | Ano: %d | IMDB: %.1f%n",
                                i + 1, p.getId(), p.getTitle(), p.getSeasons(), p.getReleaseYear(), p.getImdbScore());
                    }
                    break;

                case 4:
                    Map<String, double[]> mapaDecadas = new TreeMap<>(arvore.analise4MediaImdbPorDecadaEmLargura());
                    System.out.println("\n--- MÉDIA DE IMDB SCORE POR DÉCADA ---");
                    System.out.printf("%-10s | %-12s | %-15s%n", "Década", "Qtd Títulos", "Média IMDB");
                    System.out.println("------------------------------------------");
                    for (Map.Entry<String, double[]> entry : mapaDecadas.entrySet()) {
                        double soma = entry.getValue()[0];
                        double qtd = entry.getValue()[1];
                        double media = soma / qtd;
                        System.out.printf("%-10s | %-12.0f | %-15.2f%n", entry.getKey(), qtd, media);
                    }
                    break;

                case 5:
                    List<ProgramaNetFlix> moviesPopulares = arvore.analise5MoviesMaisPopularesEmOrdem(10);
                    System.out.println("\n--- TOP 10 FILMES MAIS POPULARES NO TMDB ---");
                    for (int i = 0; i < moviesPopulares.size(); i++) {
                        ProgramaNetFlix p = moviesPopulares.get(i);
                        System.out.printf("%2d. [%s] %s (%d) | Popularidade TMDB: %.2f | IMDB: %.1f%n",
                                i + 1, p.getId(), p.getTitle(), p.getReleaseYear(), p.getTmdbPopularity(), p.getImdbScore());
                    }
                    break;

                case 6:
                    break;

                default:
                    System.out.println("Opção inválida.");
            }
        } while (subOpcao != 6);
    }

    private static void inserirNovoPrograma(Scanner scanner, ArvoreBST arvore) {
        try {
            System.out.print("Tipo (SHOW ou MOVIE): ");
            String type = scanner.nextLine().trim().toUpperCase();
            if (!type.equals("SHOW") && !type.equals("MOVIE")) {
                System.out.println("Tipo inválido. Informe SHOW ou MOVIE.");
                return;
            }

            String prefixo = type.equals("SHOW") ? "ts" : "tm";
            long numeroSugerido = arvore.proximoNumeroId(prefixo);
            System.out.print("Número único para o ID [ENTER para usar " + numeroSugerido + " (" + prefixo + numeroSugerido + ")]: ");
            String entradaNumero = scanner.nextLine().trim();
            long numeroId = entradaNumero.isEmpty() ? numeroSugerido : Long.parseLong(entradaNumero);

            if (numeroId <= 0) {
                System.out.println("O número do ID deve ser positivo.");
                return;
            }

            String id = prefixo + numeroId;

            if (arvore.buscar(id) != null) {
                System.out.println("Já existe um programa cadastrado com o ID: " + id);
                return;
            }

            System.out.print("Título: ");
            String title = scanner.nextLine().trim();

            System.out.print("Descrição: ");
            String description = scanner.nextLine().trim();

            System.out.print("Ano de lançamento: ");
            int releaseYear = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Classificação etária (ex: TV-14, PG-13, R): ");
            String ageCertification = scanner.nextLine().trim();

            System.out.print("Duração em minutos: ");
            int runtime = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Gêneros (ex: ['drama', 'crime']): ");
            String genres = scanner.nextLine().trim();

            System.out.print("Países de produção (ex: ['US']): ");
            String productionCountries = scanner.nextLine().trim();

            double seasons = 0.0;
            if (type.equals("SHOW")) {
                System.out.print("Número de temporadas: ");
                seasons = Double.parseDouble(scanner.nextLine().trim());
            }

            System.out.print("IMDB ID (ex: tt1234567): ");
            String imdbId = scanner.nextLine().trim();

            System.out.print("IMDB Score: ");
            double imdbScore = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("IMDB Votes: ");
            double imdbVotes = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("TMDB Popularity: ");
            double tmdbPopularity = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("TMDB Score: ");
            double tmdbScore = Double.parseDouble(scanner.nextLine().trim());

            if (title.isEmpty() || description.isEmpty() || ageCertification.isEmpty() ||
                genres.isEmpty() || productionCountries.isEmpty() || imdbId.isEmpty()) {
                System.out.println("Todos os atributos obrigatórios devem ser preenchidos. Inserção cancelada.");
                return;
            }

            ProgramaNetFlix novoPrograma = new ProgramaNetFlix(
                id, title, type, description, releaseYear, ageCertification, runtime,
                genres, productionCountries, seasons, imdbId, imdbScore, imdbVotes,
                tmdbPopularity, tmdbScore
            );

            arvore.inserir(novoPrograma);
            System.out.println("Programa inserido com sucesso na árvore BST com o ID: " + id);

        } catch (NumberFormatException e) {
            System.out.println("Erro de formatação numérica. Inserção cancelada.");
        }
    }

    private static void buscarPrograma(Scanner scanner, ArvoreBST arvore) {
        if (arvore.estaVazia()) {
            System.out.println("A árvore BST está vazia.");
            return;
        }

        System.out.print("Informe o ID do programa a ser buscado (ex: ts30039): ");
        String id = scanner.nextLine().trim();

        long inicio = System.nanoTime();
        ProgramaNetFlix encontrado = arvore.buscar(id);
        long fim = System.nanoTime();

        long tempoNanos = fim - inicio;
        double tempoMillis = tempoNanos / 1_000_000.0;
        int comparacoes = arvore.getComparacoesUltimaBusca();

        if (encontrado != null) {
            System.out.println("\n--- PROGRAMA ENCONTRADO ---");
            System.out.println(encontrado);
        } else {
            System.out.println("\nPrograma com ID '" + id + "' não encontrado na árvore BST.");
        }

        System.out.println("Comparações realizadas na BST: " + comparacoes);
        System.out.printf("Tempo de execução da busca: %d ns (%.4f ms)%n", tempoNanos, tempoMillis);
    }

    private static void removerPrograma(Scanner scanner, ArvoreBST arvore) {
        if (arvore.estaVazia()) {
            System.out.println("A árvore BST está vazia.");
            return;
        }

        System.out.print("Informe o ID do programa a ser removido: ");
        String id = scanner.nextLine().trim();

        ProgramaNetFlix programa = arvore.buscar(id);
        if (programa == null) {
            System.out.println("Programa com ID '" + id + "' não encontrado na árvore.");
            return;
        }

        System.out.println("Programa encontrado: [" + programa.getId() + "] " + programa.getTitle() + " (" + programa.getReleaseYear() + ")");
        System.out.print("Confirmar remoção? (s/n): ");
        String confirmacao = scanner.nextLine().trim();

        if (!confirmacao.equalsIgnoreCase("s") && !confirmacao.equalsIgnoreCase("sim")) {
            System.out.println("Remoção cancelada.");
            return;
        }

        arvore.remover(id);
        System.out.println("Programa removido com sucesso. Total restante na árvore: " + arvore.contarNos());
    }

    private static void exibirAlturaArvore(ArvoreBST arvore) {
        if (arvore.estaVazia()) {
            System.out.println("A árvore BST está vazia (altura = -1).");
        } else {
            System.out.println("Altura atual da árvore BST: " + arvore.altura());
            System.out.println("Altura mínima teórica (árvore balanceada): " + arvore.alturaMinimaTeorica());
            System.out.println("Total de nós na árvore BST: " + arvore.contarNos());
        }
    }

    private static void salvarDadosEmArquivo(Scanner scanner, ArvoreBST arvore) {
        if (arvore.estaVazia()) {
            System.out.println("A árvore BST está vazia. Não há dados para salvar.");
            return;
        }

        System.out.print("Informe o nome do arquivo CSV para salvar os dados (ex: titles.csv): ");
        String nomeArquivo = scanner.nextLine().trim();
        if (nomeArquivo.isEmpty()) {
            System.out.println("Nome de arquivo inválido.");
            return;
        }

        File arquivo = new File(nomeArquivo);
        if (arquivo.exists()) {
            System.out.print("O arquivo '" + nomeArquivo + "' já existe e será sobrescrito. Continuar? (s/n): ");
            String confirmacao = scanner.nextLine().trim();
            if (!confirmacao.equalsIgnoreCase("s") && !confirmacao.equalsIgnoreCase("sim")) {
                System.out.println("Gravação cancelada.");
                return;
            }
        }

        try {
            arvore.salvarEmArquivo(nomeArquivo);
            System.out.println("Dados salvos com sucesso no arquivo: " + nomeArquivo);
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }
}