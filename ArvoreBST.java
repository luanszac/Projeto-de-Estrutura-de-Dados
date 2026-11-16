/*
 * Matrícula: 10750029 - Nome: Luan Souza Costa
 * Matrícula: 10445065 - Nome: Estephany Nicole da Silva
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;

public class ArvoreBST {
    private NoBST raiz;
    private int comparacoesUltimaBusca;

    public ArvoreBST() {
        this.raiz = null;
        this.comparacoesUltimaBusca = 0;
    }

    public NoBST getRaiz() {
        return raiz;
    }

    public int getComparacoesUltimaBusca() {
        return comparacoesUltimaBusca;
    }

    public void limpar() {
        this.raiz = null;
    }

    public boolean estaVazia() {
        return this.raiz == null;
    }

    public void inserir(ProgramaNetFlix programa) {
        this.raiz = inserirRecursivo(this.raiz, programa);
    }

    private NoBST inserirRecursivo(NoBST atual, ProgramaNetFlix programa) {
        if (atual == null) {
            return new NoBST(programa);
        }

        int cmp = programa.getId().compareToIgnoreCase(atual.getId());
        if (cmp < 0) {
            atual.setEsquerda(inserirRecursivo(atual.getEsquerda(), programa));
        } else if (cmp > 0) {
            atual.setDireita(inserirRecursivo(atual.getDireita(), programa));
        } else {
            atual.setDado(programa);
        }

        return atual;
    }

    public ProgramaNetFlix buscar(String id) {
        this.comparacoesUltimaBusca = 0;
        NoBST encontrado = buscarRecursivo(this.raiz, id);
        return encontrado != null ? encontrado.getDado() : null;
    }

    private NoBST buscarRecursivo(NoBST atual, String id) {
        if (atual == null) {
            return null;
        }

        this.comparacoesUltimaBusca++;
        int cmp = id.compareToIgnoreCase(atual.getId());

        if (cmp == 0) {
            return atual;
        } else if (cmp < 0) {
            return buscarRecursivo(atual.getEsquerda(), id);
        } else {
            return buscarRecursivo(atual.getDireita(), id);
        }
    }

    public boolean remover(String id) {
        if (buscar(id) == null) {
            return false;
        }
        this.raiz = removerRecursivo(this.raiz, id);
        return true;
    }

    private NoBST removerRecursivo(NoBST atual, String id) {
        if (atual == null) {
            return null;
        }

        int cmp = id.compareToIgnoreCase(atual.getId());
        if (cmp < 0) {
            atual.setEsquerda(removerRecursivo(atual.getEsquerda(), id));
        } else if (cmp > 0) {
            atual.setDireita(removerRecursivo(atual.getDireita(), id));
        } else {
            if (atual.getEsquerda() == null && atual.getDireita() == null) {
                return null;
            }
            if (atual.getEsquerda() == null) {
                return atual.getDireita();
            }
            if (atual.getDireita() == null) {
                return atual.getEsquerda();
            }

            NoBST sucessor = encontrarMenor(atual.getDireita());
            atual.setDado(sucessor.getDado());
            atual.setDireita(removerRecursivo(atual.getDireita(), sucessor.getId()));
        }

        return atual;
    }

    private NoBST encontrarMenor(NoBST no) {
        while (no.getEsquerda() != null) {
            no = no.getEsquerda();
        }
        return no;
    }

    public int altura() {
        return calcularAltura(this.raiz);
    }

    private int calcularAltura(NoBST no) {
        if (no == null) {
            return -1;
        }
        int altEsq = calcularAltura(no.getEsquerda());
        int altDir = calcularAltura(no.getDireita());
        return Math.max(altEsq, altDir) + 1;
    }

    public int contarNos() {
        return contarNosRecursivo(this.raiz);
    }

    private int contarNosRecursivo(NoBST no) {
        if (no == null) {
            return 0;
        }
        return 1 + contarNosRecursivo(no.getEsquerda()) + contarNosRecursivo(no.getDireita());
    }

    public boolean existeNumeroId(int numero) {
        String idTs = "ts" + numero;
        String idTm = "tm" + numero;
        return buscar(idTs) != null || buscar(idTm) != null;
    }

    public List<ProgramaNetFlix> analise1Top10Tv14CrimeEmOrdem() {
        List<ProgramaNetFlix> filtrados = new ArrayList<>();
        coletarEmOrdemTv14Crime(this.raiz, filtrados);
        filtrados.sort(Comparator.comparingDouble(ProgramaNetFlix::getImdbScore).reversed());
        return filtrados.subList(0, Math.min(10, filtrados.size()));
    }

    private void coletarEmOrdemTv14Crime(NoBST no, List<ProgramaNetFlix> lista) {
        if (no != null) {
            coletarEmOrdemTv14Crime(no.getEsquerda(), lista);
            ProgramaNetFlix p = no.getDado();
            if ("TV-14".equalsIgnoreCase(p.getAgeCertification()) &&
                p.getGenres().toLowerCase().contains("crime")) {
                lista.add(p);
            }
            coletarEmOrdemTv14Crime(no.getDireita(), lista);
        }
    }

    public List<ProgramaNetFlix> analise2MenoresTmdbScorePreOrdem(int n) {
        List<ProgramaNetFlix> todos = new ArrayList<>();
        coletarPreOrdem(this.raiz, todos);
        todos.sort(Comparator.comparingDouble(ProgramaNetFlix::getTmdbScore));
        return todos.subList(0, Math.min(n, todos.size()));
    }

    private void coletarPreOrdem(NoBST no, List<ProgramaNetFlix> lista) {
        if (no != null) {
            lista.add(no.getDado());
            coletarPreOrdem(no.getEsquerda(), lista);
            coletarPreOrdem(no.getDireita(), lista);
        }
    }

    public List<ProgramaNetFlix> analise3ShowsMaisTemporadasPosOrdem(int limite) {
        List<ProgramaNetFlix> shows = new ArrayList<>();
        coletarPosOrdemShows(this.raiz, shows);
        shows.sort(Comparator.comparingDouble(ProgramaNetFlix::getSeasons).reversed()
                .thenComparing(Comparator.comparingDouble(ProgramaNetFlix::getImdbScore).reversed()));
        return shows.subList(0, Math.min(limite, shows.size()));
    }

    private void coletarPosOrdemShows(NoBST no, List<ProgramaNetFlix> lista) {
        if (no != null) {
            coletarPosOrdemShows(no.getEsquerda(), lista);
            coletarPosOrdemShows(no.getDireita(), lista);
            ProgramaNetFlix p = no.getDado();
            if ("SHOW".equalsIgnoreCase(p.getType())) {
                lista.add(p);
            }
        }
    }

    public Map<String, double[]> analise4MediaImdbPorDecadaEmLargura() {
        Map<String, double[]> estatisticasDecada = new HashMap<>();
        if (this.raiz == null) {
            return estatisticasDecada;
        }

        Queue<NoBST> fila = new LinkedList<>();
        fila.add(this.raiz);

        while (!fila.isEmpty()) {
            NoBST atual = fila.poll();
            ProgramaNetFlix p = atual.getDado();
            int decadaInicio = (p.getReleaseYear() / 10) * 10;
            String chaveDecada = decadaInicio + "s";

            estatisticasDecada.putIfAbsent(chaveDecada, new double[]{0.0, 0.0});
            double[] dados = estatisticasDecada.get(chaveDecada);
            dados[0] += p.getImdbScore();
            dados[1] += 1;

            if (atual.getEsquerda() != null) {
                fila.add(atual.getEsquerda());
            }
            if (atual.getDireita() != null) {
                fila.add(atual.getDireita());
            }
        }

        return estatisticasDecada;
    }

    public List<ProgramaNetFlix> analise5MoviesMaisPopularesEmOrdem(int topN) {
        List<ProgramaNetFlix> movies = new ArrayList<>();
        coletarEmOrdemMovies(this.raiz, movies);
        movies.sort(Comparator.comparingDouble(ProgramaNetFlix::getTmdbPopularity).reversed());
        return movies.subList(0, Math.min(topN, movies.size()));
    }

    private void coletarEmOrdemMovies(NoBST no, List<ProgramaNetFlix> lista) {
        if (no != null) {
            coletarEmOrdemMovies(no.getEsquerda(), lista);
            ProgramaNetFlix p = no.getDado();
            if ("MOVIE".equalsIgnoreCase(p.getType())) {
                lista.add(p);
            }
            coletarEmOrdemMovies(no.getDireita(), lista);
        }
    }

    public void salvarEmArquivo(String nomeArquivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
            writer.write("id,title,type,description,release_year,age_certification,runtime,genres,production_countries,seasons,imdb_id,imdb_score,imdb_votes,tmdb_popularity,tmdb_score");
            writer.newLine();
            salvarPreOrdem(this.raiz, writer);
        }
    }

    private void salvarPreOrdem(NoBST no, BufferedWriter writer) throws IOException {
        if (no != null) {
            ProgramaNetFlix p = no.getDado();
            String linha = String.format(
                Locale.US,
                "%s,%s,%s,%s,%d,%s,%d,%s,%s,%.1f,%s,%.1f,%.1f,%.3f,%.1f",
                escaparCsv(p.getId()),
                escaparCsv(p.getTitle()),
                escaparCsv(p.getType()),
                escaparCsv(p.getDescription()),
                p.getReleaseYear(),
                escaparCsv(p.getAgeCertification()),
                p.getRuntime(),
                escaparCsv(p.getGenres()),
                escaparCsv(p.getProductionCountries()),
                p.getSeasons(),
                escaparCsv(p.getImdbId()),
                p.getImdbScore(),
                p.getImdbVotes(),
                p.getTmdbPopularity(),
                p.getTmdbScore()
            );
            writer.write(linha);
            writer.newLine();
            salvarPreOrdem(no.getEsquerda(), writer);
            salvarPreOrdem(no.getDireita(), writer);
        }
    }

    private String escaparCsv(String valor) {
        if (valor == null) {
            return "";
        }
        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n") || valor.contains("\r")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }
}