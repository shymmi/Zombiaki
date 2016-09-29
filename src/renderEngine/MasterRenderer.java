package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import entities.*;
import models.TexturedModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import skybox.SkyboxRenderer;
import terrains.Terrain;

public class MasterRenderer {

	private static final float FOV = 60;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;

	public static final float RED = 0.1f;
	public static final float GREEN = 0.1f;
	public static final float BLUE = 0.1f;

	private Matrix4f projectionMatrix;

	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;

	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	
	private SkyboxRenderer skyboxRenderer;

        private Map<TexturedModel, List<Enemy>> enemyEntities = new HashMap<>();
	private Map<TexturedModel, List<Player>> entities = new HashMap<>();
        private Map<TexturedModel, List<Tree>> treesEntities = new HashMap<>();
	private Map<TexturedModel, List<Player>> normalMapEntities = new HashMap<>();
	private List<Terrain> terrains = new ArrayList<>();

	public MasterRenderer(Loader loader) {
		enableCulling();
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
	}

	public Matrix4f getProjectionMatrix() {
		return this.projectionMatrix;
	}

	public void renderScene(Player player, Terrain terrain, List<Light> lights,
			Camera camera, List<Tree> trees, List<Enemy> enemies) {
                
                processTerrain(terrain);
                processEntity(player);
                
                for (Tree t : trees) {
			processEntity(t);
		}
                for (Enemy e : enemies) {
			processEntity(e);
		}
                
		render(lights, camera);
	}

	public void render(List<Light> lights, Camera camera) {
		prepare();
		shader.start();                
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
                renderer.renderTrees(treesEntities);
                renderer.renderEnemy(enemyEntities);
		shader.stop();
		terrainShader.start();
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		skyboxRenderer.render(camera, RED, GREEN, BLUE);
		terrains.clear();
		entities.clear();
		normalMapEntities.clear();
	}

	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	public void processEntity(Player entity) {
		TexturedModel entityModel = entity.getModel();
		List<Player> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Player> newBatch = new ArrayList<>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
        
        public void processEntity(Enemy entity) {
            TexturedModel entityModel = entity.getModel();
            List<Enemy> batch = enemyEntities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Enemy> newBatch = new ArrayList<>();
			newBatch.add(entity);
			enemyEntities.put(entityModel, newBatch);
		}  
	}
        
        public void processEntity(Tree entity) {
            TexturedModel entityModel = entity.getModel();
            List<Tree> batch = treesEntities.get(entityModel);
            if (batch != null) {
                    batch.add(entity);
            } else {
                    List<Tree> newBatch = new ArrayList<>();
                    newBatch.add(entity);
                    treesEntities.put(entityModel, newBatch);
            }
	}
	
	public void processNormalMapEntity(Player entity) {
		TexturedModel entityModel = entity.getModel();
		List<Player> batch = normalMapEntities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Player> newBatch = new ArrayList<>();
			newBatch.add(entity);
			normalMapEntities.put(entityModel, newBatch);
		}
	}

	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
	}

	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
	}

	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}

}
