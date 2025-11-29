package simuladorgrafos.grafo;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

/**
 *
 * @author Eddie
 */
public class Grafo {

    public Map<Vertice, List<Aresta>> st;
    public Map<Integer, Vertice> vertices;
    private int ids = 0;

    public Grafo() {
        st = new TreeMap<>();
        vertices = new TreeMap<>();
    }

    public Vertice addVertice(double x, double y) {
        Vertice v = new Vertice(ids, x, y);
        vertices.put(v.id, v);
        ids++;
        return v;
    }
    
    public void rmvVertice(int id) {

        Vertice alvo = vertices.get(id);

        for (var listaArestas : st.values()) {

            for (int i = listaArestas.size() - 1; i >= 0; i--) {
                Aresta a = listaArestas.get(i);

                if (a.origem.id == id || a.destino.id == id) {
                    listaArestas.remove(i);
                }
            }
        }

        st.remove(alvo);
        vertices.remove(id);
    }


    public void addAresta(int origem, int destino) {
        Vertice vo = vertices.get(origem);
        Vertice vd = vertices.get(destino);
        if (!st.containsKey(vo)) {
            st.put(vo, new ArrayList<>());
        }
        if (!st.containsKey(vd)) {
            st.put(vd, new ArrayList<>());
        }
        st.get(vo).add(0, new Aresta(vo, vd));
        st.get(vd).add(0, new Aresta(vd, vo));
    }

    public List<Aresta> adjacentes(int origem) {
        return st.getOrDefault(vertices.get(origem), new ArrayList<>());
    }

    public int getQuantidadeVertices() {
        return ids;
    }

    public void draw(EngineFrame e) {

        for (Map.Entry<Vertice, List<Aresta>> entry : st.entrySet()) {
            for (Aresta a : entry.getValue()) {
                a.draw(e);
            }
        }

        for (Map.Entry<Integer, Vertice> entry : vertices.entrySet()) {
            entry.getValue().draw(e);
        }

    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<Vertice, List<Aresta>> entry : st.entrySet()) {
            sb.append(entry.getKey()).append(" -> ");
            boolean primeiro = true;
            for (Aresta a : entry.getValue()) {
                if (primeiro) {
                    primeiro = false;
                } else {
                    sb.append(", ");
                }
                sb.append(a.destino.id);
            }
            sb.append("\n");
        }

        return sb.toString().trim();

    }
    
    public void dfs(int idInicial) {
        Vertice start = vertices.get(idInicial);
        if (start == null) {
            return;
        }

        // Marca visitados
        Map<Integer, Boolean> visitado = new TreeMap<>();

        System.out.println("DFS:");
        dfsVisitar(start, visitado);
    }

    private void dfsVisitar(Vertice v, Map<Integer, Boolean> visitado) {

        visitado.put(v.id, true);
        System.out.println("Visitou: " + v.id);

        List<Aresta> lista = st.getOrDefault(v, new ArrayList<>());
        for (Aresta a : lista) {
            Vertice dest = a.destino;
            if (!visitado.containsKey(dest.id)) {
                dfsVisitar(dest, visitado);
            }
        }
    }
    
    public void bfs(int idInicial) {
        Vertice start = vertices.get(idInicial);
        if (start == null) {
            return;
        }

        Map<Integer, Boolean> visitado = new TreeMap<>();
        Queue<Vertice> fila = new LinkedList<>();

        fila.add(start);
        visitado.put(start.id, true);

        System.out.println("BFS:");

        while (!fila.isEmpty()) {

            Vertice atual = fila.poll();
            System.out.println("Visitou: " + atual.id);

            List<Aresta> lista = st.getOrDefault(atual, new ArrayList<>());
            for (Aresta a : lista) {
                Vertice dest = a.destino;

                if (!visitado.containsKey(dest.id)) {
                    visitado.put(dest.id, true);
                    fila.add(dest);
                }
            }
        }
    }

}
