package template;

import br.com.davidbuzatto.jsge.collision.CollisionUtils;
import br.com.davidbuzatto.jsge.core.Camera2D;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import static br.com.davidbuzatto.jsge.core.engine.EngineFrame.GRAY;
import br.com.davidbuzatto.jsge.imgui.GuiButton;
import br.com.davidbuzatto.jsge.imgui.GuiButtonGroup;
import br.com.davidbuzatto.jsge.imgui.GuiComponent;
import br.com.davidbuzatto.jsge.imgui.GuiLabelButton;
import br.com.davidbuzatto.jsge.imgui.GuiToggleButton;
import br.com.davidbuzatto.jsge.math.Vector2;
import java.awt.Desktop;
import java.awt.Paint;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
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
    private GuiToggleButton btnAdicionarAresta;
    private GuiButton btnRemoverVertice;
    private GuiButton btnProfundidade;
    private GuiButton btnLargura;
    
    private boolean mouseForaDaIMGUI; //Mais facil fazer isso daqui pra conferir se o mouse ta em cima de algum botão
            
    private GuiLabelButton btnLink;
    
    //Câmera
    private Camera2D camera;
    private Vector2 cameraPos;
    private final double cameraVel = 300;
    
    //Grafo
    private Grafo grafo;
    private int verticeAtual = -1;
    private int verticeAdicionar = -1;
    private boolean verticeClicado;
    private boolean desenharBusca;
    private List<int[]> ordemBusca;
    
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
        ordemBusca = new ArrayList<>();
        
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
        btnAdicionarAresta = new GuiToggleButton(325, 25, 120, 30, "Adicionar Aresta");
        btnRemoverVertice = new GuiButton(475, 25, 120, 30, "Remover Vertice");
        btnLargura = new GuiButton(725, 25, 120, 30, "Largura");
        btnProfundidade = new GuiButton(850, 25, 120, 30, "Profundidade");
        
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
        componentes.add(btnLargura);
        componentes.add(btnProfundidade);
        
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
                        if (!btnAdicionarAresta.isSelected()) {
                            verticeAtual = vertice.id;
                            verticeClicado = true;
                        }else if (btnAdicionarAresta.isSelected()){
                            vertice.corVertice = BLUE;
                            verticeAdicionar = vertice.id;
                            grafo.addAresta(verticeAtual, verticeAdicionar);
                            verticeClicado = true;
                        }
                        
                    } else{
                        vertice.corVertice = WHITE;
                    }

                }
                
                if(verticeClicado){
                    grafo.vertices.get(verticeAtual).corVertice = RED;
                }else{
                    verticeAtual = -1;
                    verticeAdicionar = -1;
                }
                
            }
            
            if (btnAdicionarVertice.isSelected() && !btnAdicionarVertice.isMousePressed()){
                grafo.addVertice(mousePos.x, mousePos.y);
            }

        }
        
        if (btnAdicionarVertice.isSelected() && verticeAtual != -1 && verticeAdicionar != -1){
            grafo.vertices.get(verticeAtual).corVertice = WHITE;
            grafo.vertices.get(verticeAdicionar).corVertice = WHITE;
        }
        
        //Opções Extras
        if (!verticeClicado){
            btnAdicionarAresta.setSelected(false);
        }
        btnAdicionarAresta.setEnabled(verticeClicado);
        btnRemoverVertice.setEnabled(verticeClicado);
        
        if (btnRemoverVertice.isMousePressed()){
            grafo.rmvVertice(verticeAtual);
        }
        
        //Buscas
        if (btnLargura.isMousePressed()){
            if (verticeAtual != -1){
               ordemBusca = grafo.bfs(verticeAtual);
            }else{
               ordemBusca = grafo.bfs(Collections.min(grafo.vertices.keySet())); 
            }
            
        }
        
        if (btnProfundidade.isMousePressed()){
            if (verticeAtual != -1){
               ordemBusca = grafo.dfs(verticeAtual);
            }else{
               ordemBusca = grafo.bfs(Collections.min(grafo.vertices.keySet())); 
            }
        }
        
        if (btnProfundidade.isMouseDown() || btnLargura.isMouseDown()){
            desenharBusca = true;
        }else{
            desenharBusca = false;
        }
        
        //Controle da Câmera
        if (isKeyDown(KEY_UP)) {
            cameraPos.y += cameraVel * delta;
        }

        if (isKeyDown(KEY_DOWN)) {
            cameraPos.y -= cameraVel * delta;
        }

        if (isKeyDown(KEY_LEFT)) {
            cameraPos.x += cameraVel * delta;
        }

        if (isKeyDown(KEY_RIGHT)) {
            cameraPos.x -= cameraVel * delta;
        }

        //Zoom da Câmera
        if (getMouseWheelMove() < 0) {
            camera.zoom -= 1 * delta;
        }

        if (getMouseWheelMove() > 0) {
            camera.zoom += 1 * delta;
        }

        //Resetar Câmera
        if (isKeyDown(KEY_R)) {
            camera.rotation = 0;
            camera.zoom = 1;
            camera.target.x = 0;
            camera.target.y = 0;
        }

        //Atualizar Câmera
        camera.target.x = cameraPos.x;
        camera.target.y = cameraPos.y;
        
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
        
        setDefaultStrokeLineWidth(2);
        grafo.draw(this);
        
        if (desenharBusca) {
            for (int[] p : ordemBusca) {
                
                int de = p[0];
                int para = p[1];

                desenharSeta(grafo.vertices.get(de).pos, grafo.vertices.get(para).pos, 30, 15, 20,GREEN);
                
            }
        }

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
    
    public Vector2[] pontaCirculo(Vector2 circuloInicial, Vector2 circuloFinal, int raio){
        
        Vector2[] pontasDosCirculos = new Vector2[2];
        
        double distX = circuloFinal.x - circuloInicial.x;
        double distY = circuloFinal.y - circuloInicial.y;
        
        double hipotenusa = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
        
        pontasDosCirculos[0] = new Vector2(circuloInicial.x + (distX / hipotenusa) * raio, circuloInicial.y + (distY / hipotenusa) * raio);
        pontasDosCirculos[1] = new Vector2(circuloFinal.x - (distX / hipotenusa) * raio, circuloFinal.y - (distY / hipotenusa) * raio);
        
        return pontasDosCirculos;
        
    }
    
    public Vector2[] posParaSetas(Vector2 posInicial, Vector2 posFinal, int tamanho, int abertura){
        
        Vector2[] posDasSetas = new Vector2[2];
        
        double distX = posFinal.x - posInicial.x;
        double distY = posFinal.y - posInicial.y;

        double hipotenusa = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
        double normX = distX / hipotenusa;
        double normY = distY / hipotenusa;

        posDasSetas[0] = new Vector2(posFinal.x - normX * tamanho + normY * abertura, posFinal.y - normY * tamanho - normX * abertura);
        posDasSetas[1] = new Vector2(posFinal.x - normX * tamanho - normY * abertura, posFinal.y - normY * tamanho + normX * abertura);
        
        return posDasSetas;
    }
    
    public void desenharSeta(Vector2 vetorIni, Vector2 vetorFim, int raio, int tamanho, int abertura, Paint cor) {
        
        setStrokeLineWidth(4);
        Vector2[] linha = pontaCirculo(vetorIni, vetorFim, raio);

        drawLine(linha[0], linha[1], cor);

        Vector2[] seta = posParaSetas(linha[0], linha[1], tamanho, abertura);

        drawLine(linha[1], seta[0], cor);
        drawLine(linha[1], seta[1], cor);
    }

    
    //----------< Instanciar Engine e Iniciá-la >----------//

    public static void main( String[] args ) {
        new Main();
    }
    
}
