/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.universe.shared.events;

/**
 *
 * @author Daniel Heinrich
 */
public enum MainType
{    
    /**
     * Basis-Bits, die anzeigen, dass ein Spiel-Event vorliegt.
     * Diese sind spezifisch für die aktuelle Spiellogik, also Modus-abhängig.
     */
    TYPE_GAME,
    /**
     * Basis-Bits, die anzeigen, dass ein Entitäten-Event vorliegt.
     * Sie betreffen direkt besondere Instanzen, die direkt mit Events versorgt
     * werden können.
     */
    TYPE_GAMEMODE,
    /**
     * Basis-Bits, die anzeigen, dass ein Entitäten-Event vorliegt.
     * Sie betreffen direkt besondere Instanzen, die direkt mit Events versorgt
     * werden können.
     */
    TYPE_ENTITY;


}
