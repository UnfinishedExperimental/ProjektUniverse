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
public class TDMHud extends DMHud
{
    private static final Logger logger = Logger.getLogger(DMHud.class.getName());
    private DynamicHudElement tscore, escore;
    private HudNumber score, scorediff;

    public TDMHud(Hud hud) {
        super(hud);

        tscore = (DynamicHudElement) hud.getElementByName("team_score_bar");
        escore = (DynamicHudElement) hud.getElementByName("enemy_score_bar");

        score = (HudNumber) hud.getElementByName("team_score");
        scorediff = (HudNumber) hud.getElementByName("team_diff");
        if (tscore == null || escore == null || score == null || scorediff == null) {
            logger.log(Level.SEVERE,
                       "Das Hud ist nicht als Teamdeathmatch HUD geeignet");
            throw new RuntimeException();
        }
    }

    public void setTeamScoreBar(float p) {
        tscore.setPerc(p);
    }

    public void setEnemyScoreBar(float p) {
        escore.setPerc(p);
    }

    public void setTeamScore(int score) {
        this.score.setNumber(score);
    }

    public void setTeamScoreDiff(int score) {
        scorediff.setNumber(score);
    }
}
