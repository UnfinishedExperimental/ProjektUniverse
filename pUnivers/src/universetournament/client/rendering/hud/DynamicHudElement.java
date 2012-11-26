/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.hud;

/**
 *
 * @author dheinrich
 */
public class DynamicHudElement extends HudElement
{
    public enum Direction
    {
        HORIZONTAL, VERTICAL, BOTH;
    }

    public enum Alligment
    {
        LEFT_BOTTOM, RIGTH_TOP;
    }
    transient private float perc;
    private Direction direction;
    private Alligment alligment;
    private boolean crop;

    public DynamicHudElement(Direction direction,
                             Alligment alligment, boolean crop, String name,
                             float[] position, float[] size,
                             TextureAtlasElement taelement) {
        super(name, position, size, taelement);
        this.perc = 1;
        this.direction = direction;
        this.alligment = alligment;
        this.crop = crop;
    }

    public DynamicHudElement(HudElement he) {
        super(he);
        direction = Direction.HORIZONTAL;
        alligment = Alligment.LEFT_BOTTOM;
        crop = true;
        perc = 1;
    }

    public float getPerc() {
        return perc;
    }

    public void setPerc(float perc) {
        if (perc > 1f)
            perc = 1f;
        else if (perc < 0f)
            perc = 0f;
        this.perc = perc;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Alligment getAlligment() {
        return alligment;
    }

    public void setAlligment(Alligment alligment) {
        this.alligment = alligment;
    }

    public boolean isCrop() {
        return crop;
    }

    public void setCrop(boolean crop) {
        this.crop = crop;
    }

    public float[] getAccSize() {
        float[] tmp = getSize().clone();
        scale(tmp, false);
        return tmp;
    }

    public float[] getAccPos() {
        float[] tp = getPosition().clone();
        if (alligment == Alligment.RIGTH_TOP) {
            float[] ts = getSize().clone();
            scale(ts, true);
            tp[0] += ts[0];
            tp[1] += ts[1];
        }
        return tp;
    }

    @Override
    public float[] convertTexCoord(float s, float t) {
        float ns = s, nt = t;
        if (crop)
            switch (direction) {
                case HORIZONTAL:
                    ns *= perc;
                    if (alligment == Alligment.RIGTH_TOP)
                        ns += (1 - perc);
                    break;
                case VERTICAL:
                    nt *= perc;
                    if (alligment == Alligment.LEFT_BOTTOM)
                        nt += (1 - perc);
                    break;
                case BOTH:
                    ns *= perc;
                    nt *= perc;
                    if (alligment == Alligment.RIGTH_TOP){
                        ns += (1 - perc);
                        nt += (1 - perc);
                    }
                    break;
            }
        return super.convertTexCoord(ns, nt);
    }

    @Override
    public HudVertex[] getVertice() {
        HudVertex[] vertice = new HudVertex[6];
        float[] pos = getAccPos();
        float[] size = getAccSize();

        vertice[0] = new HudVertex(pos, convertTexCoord(0, 1));
        vertice[1] = new HudVertex(new float[]{pos[0] + size[0],
                                               pos[1] + size[1]},
                                   convertTexCoord(1, 0));
        vertice[2] = new HudVertex(new float[]{pos[0],
                                               pos[1] + size[1]},
                                   convertTexCoord(0, 0));
        vertice[3] = vertice[0];
        vertice[4] = new HudVertex(new float[]{pos[0] + size[0],
                                               pos[1]},
                                   convertTexCoord(1, 1));
        vertice[5] = vertice[1];

        return vertice;
    }

    public Direction getDirection() {
        return direction;
    }

    private void scale(float[] val, boolean neg) {
        float p = perc;
        if (neg)
            p = 1 - p;

        switch (direction) {
            case HORIZONTAL:
                val[0] *= p;
                if (neg)
                    val[1] = 0;
                break;
            case VERTICAL:
                val[1] *= p;
                if (neg)
                    val[0] = 0;
                break;
            case BOTH:
                val[0] *= p;
                val[1] *= p;
                break;
        }
    }
}
