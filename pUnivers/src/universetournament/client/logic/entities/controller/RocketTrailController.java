/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.logic.entities.controller;

import universetournament.client.rendering.Scene;
import universetournament.shared.logic.PJUTTimedRefreshable;
import universetournament.shared.logic.entities.ingame.RocketEntity;

/**
 *
 * @author dheinrich
 */
public class RocketTrailController implements PJUTTimedRefreshable
{
    private RocketEntity rocket;
    float timepast = 0, dest;

    public RocketTrailController(RocketEntity rocket, float timedest) {
        this.rocket = rocket;
        dest = timedest;
    }

    public void refresh(float timeDiff) {
        timepast += timeDiff/1000000;
        if(timepast>dest){
            timepast-=dest;
//            Scene.getInstance().emmitSmokeBall(rocket.getTransformation().getPosition(), 0.6f, 1.2f, 3);
        }
    }
}
