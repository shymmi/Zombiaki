package renderEngine;

import java.util.List;
import java.util.Map;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;
import entities.*;

public class EntityRenderer {

	private StaticShader shader;

	public EntityRenderer(StaticShader shader,Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(Map<TexturedModel, List<Player>> entities) {
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Player> batch = entities.get(model);
			for (Player entity : batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(),
						GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}
        
        public void renderTrees(Map<TexturedModel, List<Tree>> entities) {
            for (TexturedModel model : entities.keySet()) {
                    prepareTexturedModel(model);
                    List<Tree> batch = entities.get(model);
                    for (Tree entity : batch) {
                            prepareInstance(entity);
                            GL11.glDrawElements(GL11.GL_TRIANGLE_FAN, model.getRawModel().getVertexCount(),
                                            GL11.GL_UNSIGNED_INT, 0);
                    }
                    unbindTexturedModel();
            }
	}

	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = model.getTexture();
		if(texture.isHasTransparency()){
			MasterRenderer.disableCulling();
		}
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}

	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Player entity) {
            Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
                            entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
            shader.loadTransformationMatrix(transformationMatrix);
	}
        
        private void prepareInstance(Tree entity) {
            Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
                            entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
            shader.loadTransformationMatrix(transformationMatrix);
	}

}
