package br.edu.usf;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        var grafo = new Grafo(5);
        grafo.setAdjacencia(1, 0);
        grafo.setAdjacencia(0, 2);
        grafo.setAdjacencia(2, 1);
        grafo.setAdjacencia(0, 3);
        grafo.setAdjacencia(3, 4);

        printNosFortementeConectados(grafo);
    }

    //Método que encontra e printa todos as arvores fortemente conectadas
    static void printNosFortementeConectados(Grafo grafo) {
        var pilhaEncontrados = new Stack<Integer>();

        // Marca todos os vertices como não visitados (Para a primeira busca em profundidade)
        var visitados = new HashMap<Integer, Boolean>();
        preencheVisitadosComFalse(grafo, visitados);

        //Faz uma busca por profundidade e preenche a pilha com os vértices encontrados, baseado na ordem de finalização da busca
        System.out.println("Busca profunda grafo padrão:");
        for (int i = 0; i < grafo.vertices; i++) {
            if (!visitados.get(i)) { //Se o vértice não foi visitado, é feito a busca
                buscaProfunda(grafo.adjacencias, i, visitados, pilhaEncontrados);
            }
        }

        //Transpõe o grafo (Inverte as setas)
        Grafo transposto = grafo.getGrafoTransposto();

        // Marca todos os vertices como não visitados (Para a segunda busca em profundidade)
        preencheVisitadosComFalse(grafo, visitados);

        // Faz uma busca em profundidade no grafo transposto, baseada na ordem da pilha de encontrados anteriormente
        System.out.println("\nComponentes fortemente conectados:");
        while (!pilhaEncontrados.empty()) {
            //Remove topo da pilha
            int v = pilhaEncontrados.pop();

            //Printa arvore fortemente conectada com o vértice removido da pilha
            if (!visitados.get(v)) {
                buscaProfunda(transposto.getAdjacencias(), v, visitados, new Stack<>());
                System.out.println();
            }
        }
    }

    static void buscaProfunda(Map<Integer, List<Integer>> adjacencias, Integer vertice, Map<Integer, Boolean> visitados, Stack<Integer> pilhaVisitados) {
        visitados.put(vertice, true);
        System.out.print(vertice + " ");

        var adjacentes = adjacencias.get(vertice);
        if (isNull(adjacentes)) {
            adjacentes = new ArrayList<>();
        }

        //Verifica nas adjacencias, cada vertice conectado
        adjacentes.forEach(verticeAdj -> {
            if (!visitados.get(verticeAdj)) { //Se o vértice não foi visitado, faz busca a partir dele
                buscaProfunda(adjacencias, verticeAdj, visitados, pilhaVisitados);
            }
        });

        //Adiciona o vertice visitado na pilha
        pilhaVisitados.push(vertice);
    }

    private static void preencheVisitadosComFalse(Grafo grafo, HashMap<Integer, Boolean> visitados) {
        for (Integer i = 0; i < grafo.vertices; i++) {
            visitados.put(i, false);
        }
    }
}

class Grafo {
    Integer vertices;
    Map<Integer, List<Integer>> adjacencias;

    public Grafo(Integer vertices) {
        this.vertices = vertices;
        this.adjacencias = new HashMap<>();
    }

    Grafo getGrafoTransposto() {
        Grafo transposto = new Grafo(this.vertices);
        adjacencias.forEach((key, value) -> value.forEach(v -> {
            transposto.setAdjacencia(v, key);
        }));


        return transposto;
    }

    public Integer getVertices() {
        return vertices;
    }

    public void setVertices(Integer vertices) {
        this.vertices = vertices;
    }

    public Map<Integer, List<Integer>> getAdjacencias() {
        return adjacencias;
    }

    public void setAdjacencias(Map<Integer, List<Integer>> adjacencias) {
        this.adjacencias = adjacencias;
    }

    public void setAdjacencia(Integer a, Integer b) {
        var nodes = this.adjacencias.get(a);
        if (isNull(nodes)) {
            nodes = new ArrayList<>();
        }

        nodes.add(b);
        this.adjacencias.put(a, nodes);
    }

    @Override
    public String toString() {
        return "Grafo{" +
               "vertices=" + vertices +
               ", adjacencias=" + adjacencias +
               '}';
    }
}