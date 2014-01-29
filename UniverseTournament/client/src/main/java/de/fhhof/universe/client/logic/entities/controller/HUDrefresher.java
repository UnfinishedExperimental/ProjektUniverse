/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.universe.client.logic.entities.controller;

import de.fhhof.universe.client.core.ClientMainController;
import de.fhhof.universe.client.gui.huds.DMHud;
import de.fhhof.universe.shared.logic.PJUTTimedRefreshable;
import de.fhhof.universe.shared.logic.entities.ingame.ShipEntity;

/**
 *
 * @author dheinrich
 */
public class HUDrefresher implements PJUTTimedRefreshable{
    private ShipEntity ship;
    private DMHud hud;

    public HUDrefresher(ShipEntity ship) {
        this.ship = ship;
        hud = ClientMainController.getInstance().getGameModeController().getHud();
    }

    public void refresh(float timeDiff) {
       hud.setRockets((float)ship.getRocketCount()/ship.getConfiguration().getMaxRockets());
       hud.setLaser((float)ship.getWeaponEnergy()/ship.getConfiguration().getMaxWeaponEnergy());
       hud.setEnergy((float)ship.getHitPoints()/ship.getConfiguration().getMaxHitpoints());
       hud.setShield((float)ship.getShieldEnergy()/ship.getConfiguration().getShieldCapacity());
    }
}
