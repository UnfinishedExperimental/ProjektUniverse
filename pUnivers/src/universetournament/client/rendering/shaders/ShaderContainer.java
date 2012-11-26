/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.client.rendering.shaders;

import universetournament.client.rendering.ressourcen.wrapper.ShaderProgramm;

/**
 * H�lt ein ShaderProgramm
 * @author Daniel Heinrich
 */
public abstract class ShaderContainer {
    private ShaderProgramm shaderprogramm;

    public void setShaderProgramm(ShaderProgramm sp){
        shaderprogramm = sp;
        iniShaderProgramm(sp);
    }

    public ShaderProgramm getShaderprogramm() {
        return shaderprogramm;
    }

    protected abstract void iniShaderProgramm(ShaderProgramm sprog);

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ShaderContainer other = (ShaderContainer) obj;
        if (this.shaderprogramm != other.shaderprogramm && (this.shaderprogramm == null || !this.shaderprogramm.equals(other.shaderprogramm))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.shaderprogramm != null ? this.shaderprogramm.hashCode() : 0);
        return hash;
    }
}
