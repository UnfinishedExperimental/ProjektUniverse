/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.universe.client.rendering.particles;

import darwin.geometrie.data.Vertex;
import darwin.geometrie.data.VertexBuffer;
import darwin.renderer.opengl.ShaderProgramm;
import darwin.util.math.base.matrix.Matrix4;
import darwin.util.math.base.vector.Vector3;
import de.fhhof.universe.client.rendering.shaders.ParticleShader;
import de.fhhof.universe.client.rendering.shaders.StandartShader;
import java.util.*;
import javax.media.opengl.*;

/**
 *
 * @author dheinrich
 */
public class ParticleBuffer implements TransparentRO{

    private ParticleShader pshader;
    private final List<Particle> particles = new ArrayList<Particle>(1000);
    private List<Particle> notinview = new ArrayList<Particle>(200);
    private int buffer = -1;
    private float fadetime;

    public ParticleBuffer(float fadetime, ParticleShader pshader) {
        this.fadetime = fadetime;
        this.pshader = pshader;
    }

    private void createBuffer() {
        int[] tmp = new int[1];
        GL gl = GLContext.getCurrentGL().getGL2GL3();
        gl.glGenBuffers(1, tmp, 0);
        buffer = tmp[0];
    }

    public void addParticle(Particle p) {
        particles.add(p);
    }

    public int particleCount() {
        return particles.size()+notinview.size();
    }

    public List<Particle> getParticle(){
        List<Particle> p = new LinkedList<Particle>();
        p.addAll(particles);
        p.addAll(notinview);
        return p;
    }

    public void advance(float delta, Matrix4 mv) {
        advance(delta, particles);
        advance(delta, notinview);
        sortParticles(mv);
    }

    private void advance(float delta, List<Particle> particles) {
        List<Particle> remove = new LinkedList<Particle>();
        for (Particle p : particles) {
            float life = p.getLife() - delta;
            if (life < 0) {
                remove.add(p);
                continue;
            }
            p.setLife(life);

            Vector3 pos = p.getPosition();
            pos.add(p.getVelocity().mul(delta));
        }
        for (Particle p : remove) {
            particles.remove(p);
        }
    }

    public void render() {
        ShaderProgramm sp = pshader.getShaderprogramm();
        if (sp == null) {
            return;
        }
        sp.use();

        if (buffer < 0) {
            createBuffer();
        }
        GL2GL3 gl = GLContext.getCurrentGL().getGL2GL3();
        AttributeCollection pattr = pshader.getAttributs();
        pattr.enableAttributArrays(gl);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffer);
        synchronized (particles) {
            buildBuffer(gl);

            pattr.bindAttributPointer(AttType.FLOAT, gl);

            gl.glDrawArrays(gl.GL_POINTS, 0, particles.size());
        }

        pattr.disableAttributArrays(gl);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    }

    public StandartShader getShader() {
        return pshader;
    }

    private void buildBuffer(GL2GL3 gl) {
        VertexBuffer vb = new VertexBuffer(pshader.getAttributs(), particles.size());
        for (Particle p : particles) {
            Vertex v = vb.getVertex(vb.addVertex());
            try {
                v.setAttribute(pshader.getPositionAttr(),
                        double2Float(p.getPosition().getCoords()));
                v.setAttribute(pshader.getSizeAttr(), p.getScale());
                v.setAttribute(pshader.getAlphaAttr(), calcAlpha(p.getLife()));
            } catch (AttributeExeption ex) {
                ex.printStackTrace();
            }
        }
        gl.glBufferData(GL.GL_ARRAY_BUFFER, particles.size() * pshader.getAttributs().getTypeSize(AttType.FLOAT) * AttType.FLOAT.getBSize(),
                vb.getBuffer(AttType.FLOAT), GL.GL_STATIC_DRAW);


    }

    private void sortParticles(Matrix4 matrix) {
        List<Particle> top = new ArrayList<>();
        List<Particle> ton = new ArrayList<>();

//        matrix.inverse(matrix);
        synchronized (particles) {
            for (Particle p : particles) {
                double dist = matrix.mult(p.getPosition()).getCoords()[2];
                if (dist > 0) {
                    ton.add(p);
                } else {
                    p.setCameradist(dist);
                }
            }
            for (Particle p : notinview) {
                double dist = matrix.mult(p.getPosition()).getCoords()[2];
                if (dist < 0) {
                    top.add(p);
                    p.setCameradist(dist);
                }
            }

            for (Particle p : top) {
                notinview.remove(p);
                particles.add(p);
            }

            for (Particle p : ton) {
                particles.remove(p);
                notinview.add(p);
            }

            quickSort();
        }
    }

    private void quickSort() {
        int right = particles.size() - 1;
        if (right > 0) {
            quickSortRec(0, right);
        }
//        for (Particle p : particles) {
//            System.out.println(p.getCameradist());
//        }
    }

    private void quickSortRec(int left, int right) {
        int i = left, j = right;
        double pivot = particles.get((i + j) / 2).getCameradist();

        while (i <= j) {
            Particle ii = particles.get(i);
            Particle jj = particles.get(j);
            while (ii.getCameradist() < pivot) {
                ii = particles.get(++i);
            }
            while (jj.getCameradist() > pivot) {
                jj = particles.get(--j);
            }

            if (i <= j) {
                particles.set(i, jj);
                particles.set(j, ii);
                i++;
                j--;
            }
        }

        if (left < j) {
            quickSortRec(left, j);
        }
        if (right > i) {
            quickSortRec(i, right);
        }
    }

    private float calcAlpha(float lifetime) {
        return Math.min(lifetime / fadetime, 1f);
    }

    private Float[] double2Float(double[] fs) {
        Float[] i = new Float[fs.length];
        for (int j = 0; j < i.length; j++) {
            i[j] = (float)fs[j];
        }
        return i;
    }

    public static void main(String[] args) {
//        ParticleBuffer pb = new ParticleBuffer(1, null);
//        for (int i = 0; i < 1000; ++i) {
//            pb.addParticle(new Particle(new Vector3f(0, 0, (float) (Math.random() * 1000)),
//                    new Vector3f(), 2, 2));
//        }
        Matrix4 matrix = new Matrix4();
        matrix.loadIdentity();
        matrix.translate(0, 2, 2);
        matrix.inverse();
        System.out.println(matrix+"\n");

        Particle p = new Particle(new Vector3(), new Vector3(), 1, 1);

        double dist = matrix.mult(p.getPosition()).getCoords()[2];
        System.out.println(dist);
//        pb.sortParticles(matrix);
    }
}
