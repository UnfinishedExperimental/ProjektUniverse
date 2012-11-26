/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.shaders;

import universetournament.client.rendering.ressourcen.wrapper.ShaderProgramm;

/**
 * Enth�t ein Shaderprogramm das f�r das benutzten als Filter konfiguriert wurde
 * @author Daniel Heinrich
 */
public class FilterShader extends SimpleShader
{
    private int texstep;
    private boolean active = true;
    
    private int tsize;

    public FilterShader(int texsize) {
        texstep = -1;
        setTSize(texsize);
    }

    @Override
    protected void iniShaderProgramm(ShaderProgramm sprog) {
        super.iniShaderProgramm(sprog);
        texstep = sprog.getUniformLocation("texstep");
        setTSize(tsize);
    }

    /**
     * Setzt die Uniform Variable "texstep" die die breite eines Pixels
     * in Textur Coordinaten definiert.
     * @param size
     * Anzahl der Pixel.
     */
    public void setTSize(int size){
        tsize = size;
        ShaderProgramm sp = getShaderprogramm();
        if(texstep>=0 && sp != null){
            sp.use();
            sp.getGl().glUniform1f(texstep,1f /  tsize);
        }
    }

    public int getTexStep()
    {
        return texstep;
    }

    public void use(){
        getShaderprogramm().use();
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

}
