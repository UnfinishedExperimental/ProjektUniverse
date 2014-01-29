/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.universe.shared.logic.entities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import de.fhhof.universe.shared.events.MainType;
import de.fhhof.universe.shared.events.SubType;
import de.fhhof.universe.shared.events.GameEvent;

/**
 * Event speziell für Entities. Entity Events übertragen zusätzlich zu
 * den normalen Event Daten eine Entity ID.
 * Diese Klasse existiert um eine optimierte übertragung solch spezieller
 * Events zu gewährleisten.
 * @author dheinrich
 */
public class EntityEvent extends GameEvent
{
    private short id;

    /**
     * Initialisiert ein Entity Event für angegebenes entity mit Subtyp und
     * dazugehörigen Daten.
     */
    public EntityEvent(Entity entity, SubType sub, Serializable data) {
        this(entity.getId(), sub, data);
    }

    /**
     * Test Konstruktor.
     * Entfernen vor Release!!
     */
    public EntityEvent(short id, SubType sub, Serializable data) {
        super(MainType.TYPE_ENTITY, sub, data);
        this.id = id;
    }

    public short getId() {
        return id;
    }

    @Override
    protected void writeEvent(ObjectOutputStream out) throws IOException {
        out.writeObject(getSub());
        out.writeObject(getData());
        out.writeShort(id);
    }

    @Override
    protected void readEvent(ObjectInputStream in) throws IOException, ClassNotFoundException {
        ini(MainType.TYPE_ENTITY,
            (SubType) in.readObject(),
            (Serializable) in.readObject());
        id = in.readShort();
    }
}
