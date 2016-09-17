package engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.*;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import java.util.Random;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.KeyboardHandler;
import toolbox.MousePicker;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();
        Loader loader = new Loader();
        TextMaster.init(loader);

        FontType font = new FontType(loader.loadTexture("harrington"), new File("res/harrington.fnt"));

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "wellington");
        List<Terrain> terrains = new ArrayList<>();
        terrains.add(terrain);

        TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader),
                new ModelTexture(loader.loadTexture("lamp")));
        lamp.getTexture().setUseFakeLighting(true);
                
        List<Light> lights = new ArrayList<>();
        Light sun = new Light(new Vector3f(10000, 10000, -10000), new Vector3f(1.3f, 1.3f, 1.3f));
        //Light sun = new Light(new Vector3f(10000, -10000, -10000), new Vector3f(1.3f, 1.3f, 1.3f));
        lights.add(sun);
        
        //trees
        RawModel treeModel = OBJLoader.loadObjModel("DeadTree", loader);        
        TexturedModel treeTextureModel = new TexturedModel(treeModel, new ModelTexture(
                loader.loadTexture("mud")));
        
        List<Tree> trees = new ArrayList<>();
        //Player[] players = new Player[2];            //(dlugosc, wysokosc, szerokosc)
        
        int treesCount = 100;
        
        for (int i=0; i<treesCount; i++)
        {   
            Random rand = new Random();
            float randx = rand.nextInt((int)terrain.SIZE) + 1;
            float randz = (rand.nextInt((int)terrain.SIZE) * -1) - 1;
            float scale = rand.nextInt(3) + 1.5f;
            float rot = rand.nextInt(180);
            
            Tree tree = new Tree(0, treeTextureModel, new Vector3f(randx, terrain.getHeightOfTerrain(randx, randz), randz), rot, scale);
            trees.add(tree);
        }
        
        RawModel enemyModel = OBJLoader.loadObjModel("Alien", loader);        
        TexturedModel enemyTextureModel = new TexturedModel(enemyModel, new ModelTexture(
                loader.loadTexture("grassy2")));
        
        List<Enemy> enemies = new ArrayList<>();
        int enemyCount = 20;
        for (int i=0; i<enemyCount; i++)
        {   
            Random rand = new Random();
            float randx = rand.nextInt((int)terrain.SIZE) + 1;
            float randz = (rand.nextInt((int)terrain.SIZE) * -1) - 1;
            float rot = rand.nextInt(180);
            Enemy enemy = new Enemy(i, enemyTextureModel, new Vector3f(randx, terrain.getHeightOfTerrain(randx, randz), randz), rot, 5, 100);
            enemies.add(enemy);
        }

        MasterRenderer renderer = new MasterRenderer(loader);
        RawModel soldierModel = OBJLoader.loadObjModel("ArmyPilot", loader);        
        TexturedModel wormTexturedModel = new TexturedModel(soldierModel, new ModelTexture(
                loader.loadTexture("Wormpng")));
        
        List<Player> players = new ArrayList<>();
        //Player[] players = new Player[2];            //(dlugosc, wysokosc, szerokosc)
        
        Player tmpPlayer = new Player(0, wormTexturedModel, new Vector3f(10, 5, -75), 0, 90, 0, 0.6f, 100);
        players.add(tmpPlayer);

        //entities.add(players[0]);
        //entities.add(players[1]);

        Camera[] cameras = new Camera[1];
        cameras[0] = new Camera(players.get(0));     
        
        List<GuiTexture> guiTextures = new ArrayList<>();
        GuiTexture crosshair = new GuiTexture(loader.loadTexture("crosshair"), new Vector2f(0.02f, 0.2f), new Vector2f(0.05f, 0.1f));
        guiTextures.add(crosshair);
        GuiRenderer guiRenderer = new GuiRenderer(loader);

        MousePicker[] pickers = new MousePicker[1];
        pickers[0] = new MousePicker(cameras[0], renderer.getProjectionMatrix(), terrain);

        int turn = 0;
        tmpPlayer = players.get(turn);
        Camera camera = cameras[turn];
        MousePicker picker = pickers[turn];

        KeyboardHandler keyboard = new KeyboardHandler();

        GUIText hpText = new GUIText(players.get(0).getHP() + " HP", 3f, font, new Vector2f(0f, 0.9f), 1f, false);
        hpText.setColour(1, 1, 0);

        while (!Display.isCloseRequested()) {
            turn = keyboard.getTurn();
            tmpPlayer = players.get(turn);
            camera = cameras[turn];
            picker = pickers[turn];

            for(Player player : players){
                player.move(terrain, turn);
            }

            camera.move();
            picker.update();
            renderer.renderScene(players, terrains, lights, camera, trees, enemies);
            guiRenderer.render(guiTextures);
            TextMaster.render();

            DisplayManager.updateDisplay();
        }

        //*********Clean Up Below**************
        TextMaster.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}
