/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.client.rendering.ressourcen.resmanagment;

import universetournament.client.rendering.ressourcen.wrapper.ShaderProgramm;
import universetournament.client.rendering.shaders.ShaderContainer;

/**
 *
 * @author dheinrich
 */
public class ThreadSafeShaderLoading{
    private ShaderContainer sc;
    private ShaderProgramm sp;

    public ThreadSafeShaderLoading(ShaderContainer sc, ShaderProgramm sp) {
        this.sc = sc;
        this.sp = sp;
    }

    public void load() {
        sc.setShaderProgramm(sp);
    }

}
