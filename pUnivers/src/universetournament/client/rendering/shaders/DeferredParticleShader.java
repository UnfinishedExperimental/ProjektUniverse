/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.shaders;

import universetournament.client.rendering.ressourcen.wrapper.ShaderProgramm;

/**
 *
 * @author dheinrich
 */
public class DeferredParticleShader extends ParticleShader
{
    private float[] lpos, scolor, acolor;
    private float samt, ssmo;
    private int lp, sc, ac, sa, ss;

    public DeferredParticleShader() {
        lp = -1;
        sc = -1;
        ac = -1;
        sa = -1;
        ss = -1;
        lpos = new float[]{0.0f, 1000.0f, 1000f, 0};
        scolor = new float[]{0.4f, 0.4f, 0.35f, 1};
        acolor = new float[]{0.05f, 0.05f, 0.05f, 1};
        samt = 0.1f;
        ssmo = 0.3f;
    }

    @Override
    protected void iniShaderProgramm(ShaderProgramm sprog) {
        super.iniShaderProgramm(sprog);
        lp = sprog.getUniformLocation("light_pos");
        sc = sprog.getUniformLocation("ScatterColor");
        ac = sprog.getUniformLocation("AmbientColor");
        sa = sprog.getUniformLocation("ScatterAmt");
        ss = sprog.getUniformLocation("ScatterSmooth");

        setAmbientColor(acolor);
        setLightPos(lpos);
        setScatterAmount(samt);
        setScatterColor(scolor);
        setScatterSmooth(ssmo);
    }

    public void setAmbientColor(float[] acolor) {
        this.acolor = acolor;
        sendFloats(ac, acolor);
    }

    public void setLightPos(float[] lpos) {
        this.lpos = lpos;
        sendFloats(lp, lpos);
    }

    public void setScatterAmount(float samt) {
        this.samt = samt;
        sendFloat(sa, samt);
    }

    public void setScatterColor(float[] scolor) {
        this.scolor = scolor;
        sendFloats(sc, scolor);
    }

    public void setScatterSmooth(float ssmo) {
        this.ssmo = ssmo;
        sendFloat(ss, ssmo);
    }

    private void sendFloats(int uid, float[] data) {
        ShaderProgramm sp = getShaderprogramm();
        if (sp != null && data != null) {
            sp.use();
            sp.getGl().glUniform4fv(uid, 1, data, 0);
        }
    }

    private void sendFloat(int uid, float data) {
        ShaderProgramm sp = getShaderprogramm();
        if (sp != null) {
            sp.use();
            sp.getGl().glUniform1f(uid, data);
        }
    }
}
