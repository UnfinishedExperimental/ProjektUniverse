/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.shaders;

import javax.media.opengl.GL;
import universetournament.client.rendering.ressourcen.wrapper.ShaderProgramm;
import universetournament.client.rendering.shaders.ShaderMaterials.PhongShaderMaterial;
import universetournament.client.rendering.shaders.ShaderMaterials.ShaderMaterial;
import universetournament.client.rendering.geometrie.unpacked.Material;
import universetournament.shared.util.math.Vec3;



/**
 * Enth�t ein Shaderprogramm das f�r das benutzten als Phong-Lighting Shader konfiguriert wurde
 * @author Daniel Heinrich
 */
public class PhongShader extends LigthingShader
{
    private int light_pos, light_diffuse, light_ambient, light_specular,
            mat_diffuse, mat_ambient, mat_specular, mat_spec_exp, uni_half;

    private float[] lpos, ldif, lamb, lspec, mdif, mamb, mspec, half;
    private float mspee;
    private Sampler env;

    public PhongShader()
    {
        light_pos = -1;
        light_diffuse = -1;
        light_ambient = -1;
        light_specular = -1;

        mat_diffuse = -1;
        mat_ambient = -1;
        mat_specular = -1;
        mat_spec_exp = -1;

        env = new Sampler(true, "env", GL.GL_TEXTURE5);
    }

    @Override
    protected void iniShaderProgramm(ShaderProgramm sprog) {
        super.iniShaderProgramm(sprog);

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

        env.setShader(sprog);
    }

    public Sampler getEnv(){
        return env;
    }
    
    @Override
    public ShaderMaterial getShaderMaterial(Material mat) {
        return new PhongShaderMaterial(mat, this);
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
