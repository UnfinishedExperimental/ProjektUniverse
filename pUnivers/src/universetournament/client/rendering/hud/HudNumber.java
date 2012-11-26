/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.hud;

/**
 *
 * @author dheinrich
 */
public class HudNumber extends HudElement
{
    transient private int number;

    public HudNumber(String name, float[] position, float[] size,
                     TextureAtlasElement font) {
        super(name, position, size, font);
        setNumber(0);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        if (number < 0)
            return;
        this.number = number;
    }

    @Override
    public float[] convertTexCoord(float s, float t) {
        s *= 0.2;
        t *= 0.5;
        return super.convertTexCoord(s, t);
    }

    private void configDigitVertice(HudVertex[] numbverts, int digit, int offset) {
        HudVertex[] verts = super.getVertice();
        float[] texoffset = getTaElement().getRelativeTexCoord((digit % 5) * 0.2f, (digit / 5) * 0.5f);
        for (int i = 0; i < 6; i++) {
            HudVertex newvert = verts[i].clone();
            newvert.getPosition()[0] -= offset * getSize()[0];
            newvert.getTexcoord()[0] += texoffset[0];
            newvert.getTexcoord()[1] +=  texoffset[1];
            numbverts[offset * 6 + i] = newvert;
        }
    }

    @Override
    public HudVertex[] getVertice() {
        int len = Integer.toString(number).length();
        HudVertex[] numbverts = new HudVertex[len * 6];
        int count = number;
        for (int i = 0; i < len; ++i) {
            configDigitVertice(numbverts, count % 10, i);
            count /= 10;
        }

        return numbverts;
    }

    @Override
    public int getVertCount() {
        return Integer.toString(number).length()*6;
    }


}
