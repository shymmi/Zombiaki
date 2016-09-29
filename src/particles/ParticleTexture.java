/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particles;

/**
 *
 * @author Szymon
 */


public class ParticleTexture {
    private int textureID;
    private int numberOfRows;
    
    public ParticleTexture(int textureId, int numberOfRows) {
        this.textureID = textureId;
        this.numberOfRows = numberOfRows;
    }

    public int getTextureID() {
        return textureID;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }
    
    
}
