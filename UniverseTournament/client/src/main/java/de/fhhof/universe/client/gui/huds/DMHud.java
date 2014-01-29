/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.universe.client.gui.huds;

import java.util.logging.Level;
import java.util.logging.Logger;
import de.fhhof.universe.client.rendering.hud.DynamicHudElement;
import de.fhhof.universe.client.rendering.hud.Hud;
import de.fhhof.universe.client.rendering.hud.HudNumber;

/**
 *
 * @author Daniel Heinrich
 */
public class DMHud
{
    private static final Logger logger = Logger.getLogger(DMHud.class.getName());
    private DynamicHudElement laser, rockets, shield, energy;
    private HudNumber pscore, scorediff;
    private Hud hud;

    public DMHud(Hud hud) {
        this.hud = hud;
        try {
            laser = (DynamicHudElement) hud.getElementByName("laser");
            rockets = (DynamicHudElement) hud.getElementByName("rockets");
            shield = (DynamicHudElement) hud.getElementByName("shield");
            energy = (DynamicHudElement) hud.getElementByName("energy");

            pscore = (HudNumber) hud.getElementByName("player_score");
            scorediff = (HudNumber) hud.getElementByName("player_diff");
            if (laser == null || rockets == null || shield == null
                    || energy == null || pscore == null || scorediff == null)
                throw new Exception();
        } catch (Exception ex) {
            logger.log(Level.SEVERE,
                       "Das Hud ist nicht als Deathmatch HUD geeignet");
            throw new RuntimeException();
        }
    }

    public Hud getHud() {
        return hud;
    }

    public void setLaser(float p) {
        laser.setPerc(p);
    }

    public void setRockets(float p) {
        rockets.setPerc(p);
    }

    public void setShield(float p) {
        shield.setPerc(p);
    }

    public void setEnergy(float p) {
        energy.setPerc(p);
    }

    public void setPlayerScore(int score) {
        pscore.setNumber(score);
    }

    public void setScoreDiff(int score) {
        scorediff.setNumber(score);
    }
}
