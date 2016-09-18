/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particles;

import entities.Camera;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.util.vector.Matrix4f;
import renderEngine.Loader;

/**
 *
 * @author Szymon
 */
public class ParticleMaster {
    private static List<Particle> particles = new ArrayList<Particle>();
    private static ParticleRenderer renderer;
    
    public static void init(Loader loader, Matrix4f projectionMatrix) {
        renderer = new ParticleRenderer(loader, projectionMatrix);
        
    }
    
    public static void update() {
        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle p = iterator.next();
            boolean stillAlive = p.update();
            if (!stillAlive) {
                iterator.remove();
            }
        }
    }
    
    public static void renderParticles (Camera camera) {
        renderer.render(particles, camera);
    }
    
    public static void cleanUp() {
        renderer.cleanUp();
    }
    
    public static void addParticle(Particle particle) {
        particles.add(particle);
    }
}
