package template;

import br.com.davidbuzatto.jsge.collision.CollisionUtils;
import br.com.davidbuzatto.jsge.core.Camera2D;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import static br.com.davidbuzatto.jsge.core.engine.EngineFrame.GRAY;
import br.com.davidbuzatto.jsge.imgui.GuiButtonGroup;
import br.com.davidbuzatto.jsge.imgui.GuiComponent;
import br.com.davidbuzatto.jsge.imgui.GuiLabelButton;
import br.com.davidbuzatto.jsge.imgui.GuiToggleButton;
import br.com.davidbuzatto.jsge.math.Vector2;
import java.awt.Desktop;
import java.awt.Paint;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import simuladorgrafos.grafo.Grafo;

/**
 * Nome do Projeto.
 * @author Eddie Pricefield
 * 
 * Engine JSGE
 * @author Prof. Dr. David Buzatto
 */
public class Main extends EngineFrame {
    
    //Esquema de Cores
    private Paint background = BEIGE;
    private Paint corComponentes = ORANGE;
    
    //Componentes
    private List<GuiComponent> componentes;
    private GuiButtonGroup buttonGroup;
    private GuiToggleButton btnSelecionarGrafo;
    private GuiToggleButton btnAdicionarGrafo;
    private GuiToggleButton btnAdicionarAresta;
    
    private GuiLabelButton btnLink;
    
    //Câmera
    private Camera2D camera;
    private Vector2 cameraPos;
    private final double cameraVel = 300;
    
    //Grafo
    private Grafo grafo;
    private int verticeAtual;
    
    public Main() {
        
        super(
            1200,                 // largura                      / width
            600,                 // algura                       / height
            "Window Title",      // título                       / title
            60,                  // quadros por segundo desejado / target FPS
            true,                // suavização                   / antialiasing
            false,               // redimensionável              / resizable
            false,               // tela cheia                   / full screen
            false,               // sem decoração                / undecorated
            false,               // sempre no topo               / always on top
            false                // fundo invisível              / invisible background
        );
        
    }
    
    //----------< Criar >----------//
    
    @Override
    public void create() {
        
        useAsDependencyForIMGUI();
        camera = new Camera2D();
        componentes = new ArrayList<>();
        buttonGroup = new GuiButtonGroup();
        
        //Criação da Câmera
        cameraPos = new Vector2(0, 0);
        camera.target = cameraPos;
        camera.offset = new Vector2(0, 0);
        camera.rotation = 0;
        camera.zoom = 1;
        
        //Criação dos Componentes
        btnSelecionarGrafo = new GuiToggleButton(25, 25, 120, 30, "Selecionar");
        btnAdicionarGrafo = new GuiToggleButton(175, 25, 120, 30, "Adicionar Grafo");
        btnAdicionarAresta = new GuiToggleButton(325, 25, 120, 30, "Adicionar Aresta");
        btnSelecionarGrafo.setButtonGroup(buttonGroup);
        btnAdicionarGrafo.setButtonGroup(buttonGroup);
        btnAdicionarAresta.setButtonGroup(buttonGroup);
        
        grafo = new Grafo();
        
        grafo.addVertice(100, 100);
        grafo.addVertice(150, 200);
        grafo.addVertice(250, 200);
        grafo.addVertice(200, 300);
        grafo.addVertice(300, 300);
        grafo.addVertice(100, 400);
        grafo.addVertice(350, 200);
        grafo.addVertice(450, 150);
        grafo.addVertice(550, 150);
        grafo.addVertice(450, 250);
        grafo.addVertice(550, 250);
        grafo.addVertice(450, 350);
        grafo.addVertice(550, 350);

        grafo.addAresta(0, 5);
        grafo.addAresta(4, 3);
        grafo.addAresta(0, 1);
        grafo.addAresta(9, 12);
        grafo.addAresta(6, 4);
        grafo.addAresta(5, 4);
        grafo.addAresta(0, 2);
        grafo.addAresta(11, 12);
        grafo.addAresta(9, 10);
        grafo.addAresta(0, 6);
        grafo.addAresta(7, 8);
        grafo.addAresta(9, 11);
        grafo.addAresta(5, 3);
        
        System.out.println(grafo);
        setDefaultFontSize(20);
        setDefaultStrokeLineWidth(2);
        setDefaultStrokeEndCap(STROKE_CAP_ROUND);
        
        btnLink = new GuiLabelButton(getWidth() - 140, getHeight() - 65, 120, 20, "@EddiePricefield");
        
        //Adicionar à Lista de Componentes
        componentes.add(btnLink);
        componentes.add(btnSelecionarGrafo);
        componentes.add(btnAdicionarGrafo);
        componentes.add(btnAdicionarAresta);
        
    }
    
    //----------< Atualizar >----------//

    @Override
    public void update( double delta ) {
        
        atualizarComponentes(delta);
        
        Vector2 mousePos = camera.getScreenToWorld(getMousePositionPoint());
        
        if (isMouseButtonPressed(MOUSE_BUTTON_LEFT)) {
            
            if (btnSelecionarGrafo.isSelected() && !btnSelecionarGrafo.isMousePressed()){
                
                for (var vertice : grafo.vertices.values()) {
                    Vector2 centro = new Vector2(vertice.pos.x, vertice.pos.y);

                    if (CollisionUtils.checkCollisionPointCircle(mousePos, centro, 30)) {
                        vertice.corVertice = RED;
                        verticeAtual = vertice.id;
                        System.out.println(verticeAtual);
                    } else{
                        vertice.corVertice = WHITE;
                        verticeAtual = -1;
                    }

                }
                
            }
            
            if (btnAdicionarGrafo.isSelected() && !btnAdicionarGrafo.isMousePressed()){ //Tive que fazer assim pra ele nao desenhar Vertice embaixo do botão, ja reproduzi pros outros botões também
                grafo.addVertice(mousePos.x, mousePos.y);
            }
            
            
        }
        
        //Github
        if (btnLink.isMousePressed()) {

            try {
                URI link = new URI("https://github.com/EddiePricefield");
                Desktop.getDesktop().browse(link);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        
    }
    
    //----------< Desenhar >----------//
    
    @Override
    public void draw() {
        
        clearBackground( background );
        
        //Desenhar o Grafo
        beginMode2D(camera);
        grafo.draw(this);
        endMode2D();
        
        //Design
        
        desenharComponentes();
        
    
    }
    
    //----------< Complementares >----------//    
    
    public void atualizarComponentes(double delta) {

        for (GuiComponent c : componentes) {
            if (!componentes.isEmpty()) {
                c.update(delta);
            }
        }

    }

    public void desenharComponentes() {

        for (GuiComponent c : componentes) {
            if (!componentes.isEmpty()) {
                c.draw();
            }
        }

    }

    public void drawOutlinedText(String text, int posX, int posY, int fontSize, Paint color, int outlineSize, Paint outlineColor) {
        drawText(text, posX - 2, posY + 2, fontSize, GRAY);
        drawText(text, posX - outlineSize, posY - outlineSize, fontSize, outlineColor);
        drawText(text, posX + outlineSize, posY - outlineSize, fontSize, outlineColor);
        drawText(text, posX - outlineSize, posY + outlineSize, fontSize, outlineColor);
        drawText(text, posX + outlineSize, posY + outlineSize, fontSize, outlineColor);
        drawText(text, posX, posY, fontSize, color);
    }
    
    //----------< Instanciar Engine e Iniciá-la >----------//

    public static void main( String[] args ) {
        new Main();
    }
    
}
