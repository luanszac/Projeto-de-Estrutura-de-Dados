/*
 * Matrícula: 10750029 - Nome: Luan Souza Costa
 * Matrícula: 10445065 - Nome: Estephany Nicole da Silva
 */

public class NoBST {
    private String id;
    private ProgramaNetFlix dado;
    private NoBST esquerda;
    private NoBST direita;

    public NoBST(ProgramaNetFlix dado) {
        this.id = dado.getId();
        this.dado = dado;
        this.esquerda = null;
        this.direita = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProgramaNetFlix getDado() {
        return dado;
    }

    public void setDado(ProgramaNetFlix dado) {
        this.dado = dado;
        this.id = dado.getId();
    }

    public NoBST getEsquerda() {
        return esquerda;
    }

    public void setEsquerda(NoBST esquerda) {
        this.esquerda = esquerda;
    }

    public NoBST getDireita() {
        return direita;
    }

    public void setDireita(NoBST direita) {
        this.direita = direita;
    }
}