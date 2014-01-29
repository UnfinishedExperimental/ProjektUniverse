/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.universe.client.rendering.shaders;

import javax.media.opengl.GL;
import de.fhhof.universe.client.rendering.geometrie.unpacked.Material;
import de.fhhof.universe.client.rendering.ressourcen.wrapper.ShaderProgramm;
import de.fhhof.universe.client.rendering.shaders.ShaderMaterials.ShaderMaterial;
import de.fhhof.universe.client.rendering.shaders.ShaderMaterials.TerrainMaterial;
import de.fhhof.universe.shared.util.math.Matrix;
import de.fhhof.universe.shared.util.math.Vec3;

/**
 *
 * @author some
 */
public class TerrainShader extends StandartShader
{
    private int  mat_n;
    private Matrix normal_mat;
    private Sampler heigthmap;
    private int light_pos, light_diffuse, light_ambient, light_specular,
            mat_diffuse, mat_ambient, mat_specular, mat_spec_exp, uni_half;
    private float[] lpos, ldif, lamb, lspec, mdif, mamb, mspec, half;
    private float mspee;

    public TerrainShader() {
        mat_n = -1;

        heigthmap = new Sampler(true, "heightmap", GL.GL_TEXTURE2);

        light_pos = -1;
        light_diffuse = -1;
        light_ambient = -1;
        light_specular = -1;

        mat_diffuse = -1;
        mat_ambient = -1;
        mat_specular = -1;
        mat_spec_exp = -1;
    }

    @Override
    protected void iniShaderProgramm(ShaderProgramm sprog) {
        super.iniShaderProgramm(sprog);

        mat_n = sprog.getUniformLocation("mat_n");

        heigthmap.setShader(sprog);

        setNormalMatrix(normal_mat);

        light_pos = sprog.getUniformLocation("light_pos");
        light_diffuse = sprog.getUniformLocation("light_diffuse");
        light_ambient = sprog.getUniformLocation("light_ambient");
        light_specular = sprog.getUniformLocation("light_specular");

        uni_half = sprog.getUniformLocation("halfvector");

        mat_diffuse = sprog.getUniformLocation("mat_diffuse");
        mat_ambient = sprog.getUniformLocation("mat_ambient");
        mat_specular = sprog.getUniformLocation("mat_specular");
        mat_spec_exp = sprog.getUniformLocation("mat_spec_exp");

        setLight_ambient(lamb);
        setLight_diffuse(ldif);
        setLight_pos(lpos);
        setLight_specular(lspec);

        setHalfVector(half);

        setMat_ambient(mamb);
        setMat_diffuse(mdif);
        setMat_spec_exp(mspee);
        setMat_specular(mspee);
    }

    @Override
    public ShaderMaterial getShaderMaterial(Material mat) {
        return new TerrainMaterial(mat, this);
    }

    public Sampler getHeightMap(){
        return heigthmap;
    }

    public void setNormalMatrix(Matrix mat) {
        if (mat == null)
            return;
        if (mat.getDimension() != 3)
            throw new IllegalArgumentException(
                    "Die Normalen Matrix muss eine 3x3 Matrix sein");
        normal_mat = mat;

        ShaderProgramm sp = getShaderprogramm();
        if (sp != null) {
            sp.use();
            sp.getGl().glUniformMatrix3fv(mat_n, 1, false, mat.getFloatArray(),
                                          0);
        }
    }

     private void setHalfVector(float[] h) {
        this.half = h;
        sendFloats(uni_half, half);
    }

    public void setHalfVector(Vec3 h) {
        this.half = double2float(h.getCoords());
        sendFloats(uni_half, half);
    }

    public void setLight_ambient(float ...ambient)
    {
        lamb= ambient;
        sendFloats(light_ambient, lamb);
    }

    public void setLight_diffuse(float ...diffuse)
    {
        ldif = diffuse;
        sendFloats(light_diffuse, ldif);
    }

    private void setLight_pos(float[] pos)
    {
        lpos = pos;
        sendFloats(light_pos, lpos);
    }

    public void setLight_dir(Vec3 pos)
    {
        lpos = double2float(pos.getCoords());
        sendFloats(light_pos, lpos);
    }

    public void setLight_specular(float ...specular)
    {
        lspec = specular;
        sendFloats(light_specular, lspec);
    }

    public void setMat_ambient(float ...ambient)
    {
        mamb = ambient;
        sendFloats(mat_ambient, mamb);
    }

    public void setMat_diffuse(float ...diffuse)
    {
        mdif = diffuse;
        sendFloats(mat_diffuse, mdif);
    }

    public void setMat_spec_exp(float spec_exp)
    {
        mspee = spec_exp;

        ShaderProgramm sp = getShaderprogramm();
        if(sp != null){
            sp.use();
            sp.getGl().glUniform1f(mat_spec_exp, spec_exp);
        }
    }

    public void setMat_specular(float ...specular)
    {
        mspec = specular;
        sendFloats(mat_specular, mspec);
    }

    private void sendFloats(int uid, float[] data){
        ShaderProgramm sp = getShaderprogramm();
        if(sp != null && data != null){
            sp.use();
            switch(data.length){
                case 3:
                    sp.getGl().glUniform3fv(uid, 1, data, 0);
                    break;
                case 4:
                    sp.getGl().glUniform4fv(uid, 1, data, 0);
            }
        }
    }

    private float[] double2float(double [] data){
        float[] d = new float[data.length];
        for(int i=0; i<d.length; ++i)
            d[i] = (float)data[i];
        return d;
    }
}
