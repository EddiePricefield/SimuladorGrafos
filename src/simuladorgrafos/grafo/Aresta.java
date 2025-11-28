package simuladorgrafos.grafo;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;

/**
 *
 * @author Eddie
 */
public class Aresta {

    public Vertice origem;
    public Vertice destino;

    public Aresta(Vertice origem, Vertice destino) {
        this.origem = origem;
        this.destino = destino;
    }

    public void draw(EngineFrame e) {
        e.drawLine(origem.pos, destino.pos, EngineFrame.BLACK);
    }

}
