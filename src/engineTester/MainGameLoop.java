package engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import entities.*;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import java.util.Random;
import models.RawModel;
import models.TexturedModel;
import particles.ParticleMaster;
import particles.ParticleSystem;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;

public class MainGameLoop {
    
    public static void main(String[] args) {
        int TREES_COUNT = 50;
        int ENEMIES_COUNT = 10;

        DisplayManager.createDisplay();
        Loader loader = new Loader();
        TextMaster.init(loader);
        MasterRenderer renderer = new MasterRenderer(loader);
        ParticleMaster.init(loader, renderer.getProjectionMatrix());
        
        //font
        FontType font = new FontType(loader.loadTexture("harrington"), new File("res/harrington.fnt"));

        //textures
        TerrainTexture backgroundTerrainTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture redTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture greenTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture blueTexture = new TerrainTexture(loader.loadTexture("path"));
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTerrainTexture, redTexture, greenTexture, blueTexture);
        TerrainTexture texturesComposition = new TerrainTexture(loader.loadTexture("blendMap"));

        //objects declarations
        Terrain terrain = new Terrain(0, -1, loader, texturePack, texturesComposition, "wellington");
        
        Light light1 = new Light(new Vector3f(10000, 10000, -10000), new Vector3f(1.3f, 1.3f, 1.3f));
        Light light2 = new Light(new Vector3f(10000, -10000, -10000), new Vector3f(1.3f, 1.3f, 1.3f));
        
        RawModel treeModel = OBJLoader.loadObjModel("DeadTree", loader);        
        TexturedModel treeTextureModel = new TexturedModel(treeModel, new ModelTexture(loader.loadTexture("mud")));
        
        RawModel enemyModel = OBJLoader.loadObjModel("Alien", loader);        
        TexturedModel enemyTexturedModel = new TexturedModel(enemyModel, new ModelTexture(loader.loadTexture("grassy2")));
                
        RawModel soldierModel = OBJLoader.loadObjModel("ArmyPilot", loader);        
        TexturedModel soldierTexturedModel = new TexturedModel(soldierModel, new ModelTexture(loader.loadTexture("Wormpng")));
        
        Player player = new Player(0, soldierTexturedModel, new Vector3f(10, 5, -75), 0, 90, 0, 0.6f, 100);
        Camera camera = new Camera(player);  
        GuiTexture gunpoint = new GuiTexture(loader.loadTexture("crosshair"), new Vector2f(0.02f, 0.2f), new Vector2f(0.05f, 0.1f));
        MousePicker mousePicker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
        ParticleSystem bleedingSystem = new ParticleSystem(20, 15, 0.1f, 1, 0.5f);
        
        GUIText HealthPoints_Text;
        GuiRenderer guiRenderer = new GuiRenderer(loader);
 
        List<Light> lights = new ArrayList<>();
        List<Tree> trees = new ArrayList<>();
        List<Enemy> enemies = new ArrayList<>();
        List<GuiTexture> guiTextures = new ArrayList<>();

        lights.add(light1);
        lights.add(light2);  
        
        for (int i=0; i<TREES_COUNT; i++)
        {   
            Random rand = new Random();
            float randx = rand.nextInt((int)terrain.SIZE) + 1;
            float randz = (rand.nextInt((int)terrain.SIZE) * -1) - 1;
            float scale = rand.nextInt(3) + 1.5f;
            float rot = rand.nextInt(180);
            
            Tree tree = new Tree(0, treeTextureModel, new Vector3f(randx, terrain.getHeightOfTerrain(randx, randz), randz), rot, scale);
            trees.add(tree);
        }
        
        for (int i=0; i<ENEMIES_COUNT; i++)
        {   
            Random rand = new Random();
            float randx = rand.nextInt((int)terrain.SIZE) + 1;
            float randz = (rand.nextInt((int)terrain.SIZE) * -1) - 1;
            float rot = rand.nextInt(180);
            Enemy enemy = new Enemy(i, enemyTexturedModel, new Vector3f(randx, terrain.getHeightOfTerrain(randx, randz), randz), rot, 5, 100);
            enemies.add(enemy);
        }

        guiTextures.add(gunpoint);

        //game loop
        while (!Display.isCloseRequested()) {
            GUIText enemiesLeft = new GUIText("Enemies left: " + enemies.size(), 3f, font, new Vector2f(0f, 0f), 1f, false);
            enemiesLeft.setColour(1, 0, 0);
            
            if(player.getHP() > 0) {
                HealthPoints_Text = new GUIText(player.getHP() + " HP", 3f, font, new Vector2f(0f, 0.9f), 1f, false);
                HealthPoints_Text.setColour(1, 1, 0);
            } else {
                HealthPoints_Text = new GUIText("You were weak! Try again.", 3f, font, new Vector2f(0.3f, 0.5f), 1f, false);
                HealthPoints_Text.setColour(1, 1, 0);
            }
            
            if(player.getHP() > 0 && enemies.size() == 0) {
                GUIText youWinMessage = new GUIText("You win!", 3f, font, new Vector2f(0.44f, 0.4f), 1f, false);
                youWinMessage.setColour(0, 1, 0);
            }
            
            player.move(terrain);
            player.kill(enemies);
            
            for(Enemy e : enemies) {
                e.moveToPlayerDirection(terrain, player);
                Vector3f enemyPosition = new Vector3f(e.getPosition().x, e.getPosition().y, e.getPosition().z);
                enemyPosition.y += 20;
                if(e.getHP() < 100) {
                    bleedingSystem.generateParticles(enemyPosition);
                }
            }
            
            camera.moveCamera();
            
            mousePicker.updatePicker();
            ParticleMaster.updateParticles();
            
            //render
            renderer.renderScene(player, terrain, lights, camera, trees, enemies);
            ParticleMaster.renderParticles(camera);
            guiRenderer.renderGUI(guiTextures);
            TextMaster.renderText();

            //update
            DisplayManager.updateDisplay();
            HealthPoints_Text.updateGUI();
            enemiesLeft.updateGUI();
        }

        //Clean up
        ParticleMaster.cleanUp();
        TextMaster.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}
