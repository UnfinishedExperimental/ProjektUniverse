/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.shared.logic.entities;

import java.io.Serializable;
import universetournament.shared.events.SubType;

/**
 * Basis Klasse aller Entitys, enthält ID-Attribute und basis
 * SubEvent Typen von Entities.
 * @author dheinrich
 */
public abstract class Entity implements Serializable{

    /**
     * Basis Entity SubEvent typen.
     *
     * -CREATE: zum erstellen eines Entites übergibt als Daten das Entity
     * -DELETE: weist an das Entity zu löschen
     */
    public enum SubEvents implements SubType{
        CREATE, DELETE;
    }

    private final short id;

    protected Entity(short id) {
        this.id = id;
    }

    public short getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Entity other = (Entity) obj;
        if (this.id != other.id)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + this.id;
        return hash;
    }
}
