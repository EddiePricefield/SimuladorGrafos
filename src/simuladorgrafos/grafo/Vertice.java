package simuladorgrafos.grafo;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.math.Vector2;
import java.awt.Paint;

/**
 *
 * @author Eddie
 */

public class Vertice implements Comparable<Vertice> {
    
    public int id;
    public Vector2 pos;
    public Paint corVertice = EngineFrame.BEIGE;

    public Vertice( int id, double x, double y ) {
        this.id = id;
        this.pos = new Vector2( x, y );
    }
    
    public void draw( EngineFrame e ) {
        
        e.fillCircle( pos, 30, corVertice );
        e.drawCircle( pos, 30, EngineFrame.BLACK );
        
        String label = String.valueOf( id );
        int w = e.measureText( label );
        e.drawText( label, pos.x - w/2, pos.y - 5, EngineFrame.BLACK );
        
    }
    
    @Override
    public int compareTo( Vertice o ) {
        return id - o.id;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Vertice other = (Vertice) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return String.valueOf( id );
    }
    
}

