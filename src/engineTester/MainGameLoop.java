package engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Light;
import entities.Player;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
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

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
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
        
        MasterRenderer renderer = new MasterRenderer(loader);
        RawModel wormModel = OBJLoader.loadObjModel("ArmyPilot", loader);        
        TexturedModel wormTexturedModel = new TexturedModel(wormModel, new ModelTexture(
                loader.loadTexture("Wormpng")));
        
        List<Player> players = new ArrayList<>();
        //Player[] players = new Player[2];            //(dlugosc, wysokosc, szerokosc)
        
        Player tmpPlayer = new Player(0, wormTexturedModel, new Vector3f(10, 5, -75), 0, 90, 0, 0.6f, 100);
        players.add(tmpPlayer);
        tmpPlayer = new Player(1, wormTexturedModel, new Vector3f(140, 5, -75), 0, 270, 0, 0.6f, 100);
        players.add(tmpPlayer);
        //entities.add(players[0]);
        //entities.add(players[1]);

        Camera[] cameras = new Camera[2];
        cameras[0] = new Camera(players.get(0));
        cameras[1] = new Camera(players.get(1));        
        
        List<GuiTexture> guiTextures = new ArrayList<>();
        GuiTexture crosshair = new GuiTexture(loader.loadTexture("crosshair"), new Vector2f(0.02f, 0.2f), new Vector2f(0.05f, 0.1f));
        guiTextures.add(crosshair);
        GuiRenderer guiRenderer = new GuiRenderer(loader);

        MousePicker[] pickers = new MousePicker[2];
        pickers[0] = new MousePicker(cameras[0], renderer.getProjectionMatrix(), terrain);
        pickers[1] = new MousePicker(cameras[1], renderer.getProjectionMatrix(), terrain);

        int turn = 0;
        tmpPlayer = players.get(turn);
        Camera camera = cameras[turn];
        MousePicker picker = pickers[turn];

        KeyboardHandler keyboard = new KeyboardHandler();

        GUIText worm1Text = new GUIText(players.get(0).getHP() + " HP", 3f, font, new Vector2f(0f, 0.9f), 1f, false);
        worm1Text.setColour(1, 1, 0);
        GUIText worm2Text = new GUIText(players.get(1).getHP() + " HP", 3f, font, new Vector2f(0.85f, 0.9f), 1f, false);
        worm2Text.setColour(1, 1, 0);

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
            renderer.renderScene(players, terrains, lights, camera);
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
