/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.client.rendering.hud;


/**
 *
 * @author dheinrich
 */
public class HudVertex {
    private float[] position, texcoord;

    public HudVertex(float[] position, float[] texcoord) {
        this.position = position;
        this.texcoord = texcoord;
    }

    public float[] getPosition() {
        return position;
    }

    public float[] getTexcoord() {
        return texcoord;
    }

    @Override
    protected HudVertex clone(){
        return new HudVertex(position.clone(), texcoord.clone());
    }
}
