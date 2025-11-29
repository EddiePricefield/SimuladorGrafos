package template;

import br.com.davidbuzatto.jsge.collision.CollisionUtils;
import br.com.davidbuzatto.jsge.core.Camera2D;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import static br.com.davidbuzatto.jsge.core.engine.EngineFrame.GRAY;
import br.com.davidbuzatto.jsge.imgui.GuiButton;
import br.com.davidbuzatto.jsge.imgui.GuiButtonGroup;
import br.com.davidbuzatto.jsge.imgui.GuiComponent;
import br.com.davidbuzatto.jsge.imgui.GuiLabelButton;
import br.com.davidbuzatto.jsge.imgui.GuiSpinner;
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
    private GuiToggleButton btnSelecionarVertice;
    private GuiToggleButton btnAdicionarVertice;
    private GuiButton btnAdicionarAresta;
    private GuiButton btnRemoverVertice;
    private GuiSpinner spinnerVerticeDestino;
    
    private boolean mouseForaDaIMGUI;
            
    private GuiLabelButton btnLink;
    
    //Câmera
    private Camera2D camera;
    private Vector2 cameraPos;
    private final double cameraVel = 300;
    
    //Grafo
    private Grafo grafo;
    private int verticeAtual;
    private boolean verticeClicado;
    
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
        grafo = new Grafo();
        
        //Criação da Câmera
        cameraPos = new Vector2(0, 0);
        camera.target = cameraPos;
        camera.offset = new Vector2(0, 0);
        camera.rotation = 0;
        camera.zoom = 1;
        
        //Criação dos Componentes
        btnSelecionarVertice = new GuiToggleButton(25, 25, 120, 30, "Selecionar");
        btnAdicionarVertice = new GuiToggleButton(175, 25, 120, 30, "Adicionar Vértice");
        btnSelecionarVertice.setButtonGroup(buttonGroup);
        btnAdicionarVertice.setButtonGroup(buttonGroup);
        btnAdicionarAresta = new GuiButton(325, 25, 120, 30, "Adicionar Aresta");
        btnRemoverVertice = new GuiButton(475, 25, 120, 30, "Remover Vertice");
        spinnerVerticeDestino = new GuiSpinner(615, 25, 100, 30, 0, 0, 0);
        
        setDefaultFontSize(20);
        setDefaultStrokeLineWidth(2);
        setDefaultStrokeEndCap(STROKE_CAP_ROUND);
        
        btnLink = new GuiLabelButton(getWidth() - 140, getHeight() - 65, 120, 20, "@EddiePricefield");
        
        //Adicionar à Lista de Componentes
        componentes.add(btnLink);
        componentes.add(btnSelecionarVertice);
        componentes.add(btnAdicionarVertice);
        componentes.add(btnAdicionarAresta);
        componentes.add(btnRemoverVertice);
        componentes.add(spinnerVerticeDestino);
        
    }
    
    //----------< Atualizar >----------//

    @Override
    public void update( double delta ) {
        
        atualizarComponentes(delta);
        
        //Opções Principais
        Vector2 mousePos = camera.getScreenToWorld(getMousePositionPoint());
        
        if (isMouseButtonPressed(MOUSE_BUTTON_LEFT) && mouseForaDaIMGUI) {
            
            if (btnSelecionarVertice.isSelected()){
                
                verticeClicado = false;
                
                for (var vertice : grafo.vertices.values()) {
                    Vector2 centro = new Vector2(vertice.pos.x, vertice.pos.y);

                    if (CollisionUtils.checkCollisionPointCircle(mousePos, centro, 30)) {
                        vertice.corVertice = RED;
                        verticeAtual = vertice.id;
                        verticeClicado = true;
                        System.out.println(verticeAtual);
                    } else{
                        vertice.corVertice = WHITE;
                    }

                }
                
                if(!verticeClicado){
                    verticeAtual = -1;
                }
                
            }
            
            if (btnAdicionarVertice.isSelected() && !btnAdicionarVertice.isMousePressed()){
                grafo.addVertice(mousePos.x, mousePos.y);
            }

        }
        
        //Opções Extras
        btnAdicionarAresta.setEnabled(verticeClicado);
        btnRemoverVertice.setEnabled(verticeClicado);
        spinnerVerticeDestino.setEnabled(verticeClicado);
        spinnerVerticeDestino.setMax(grafo.getQuantidadeVertices() - 1);
        
        if (btnAdicionarAresta.isMousePressed()){
            grafo.addAresta(verticeAtual, spinnerVerticeDestino.getValue());
        }
        
        if (btnRemoverVertice.isMousePressed()){
            grafo.rmvVertice(verticeAtual);
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
        
        mouseForaDaIMGUI = true;

        for (GuiComponent c : componentes) {
            if (!componentes.isEmpty()) {
                c.update(delta);
            }
            
            if (c.isMousePressed()){
                mouseForaDaIMGUI = false;
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
