/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.universe.shared.events;

import java.io.IOException;
import java.io.Serializable;

/**
 * Oberklasse für alle Events, die an den entsprechenden Empfänger
 * geleitet werden sollen, meist über das Netzwerk.
 * Besitzt einen Basis- und Subtyp.
 * Events sollten generell schlank gehalten werden, um die
 * Netzwerkkommunikation effizient zu halten. Daher ist es rastam immer nur den
 * minimal nötigen Datentyp (byte statt int, float statt double) zu benutzen,
 * falls dies möglich ist.
 *
 * @author Daniel Heinrich
 *
 */
public class GameEvent implements Serializable
{
    transient private MainType main;
    transient private SubType sub;
    transient private Serializable data;

    /**
     * Initialisiert ein Event Objekt mit dessen Haupt- und Subtypen sowie der
     * Daten des Events.
     * Beide Typen müssen gesetzt werden, die Daten dürfen null sein.
     */
    public GameEvent(MainType main, SubType sub, Serializable data) {
        if (main == null || sub == null)
            throw new NullPointerException(
                    "Event-Typen dürfen nicht null sein!");
        ini(main, sub, data);
    }

    protected void ini(MainType main, SubType sub, Serializable data) {
        this.main = main;
        this.sub = sub;
        this.data = data;
    }

    /**
     * @return
     * gibt den Haupttypen des Events zurück
     */
    public MainType getMain() {
        return main;
    }

    /**
     * @return
     * Gibt den ein Hashwert Wrapper des Subtypes zurück.
     */
    public SubType getSub() {
        return sub;
    }

    public Serializable getData() {
        return data;
    }

    /**
     * Methode zuständig fürs serialsieren des Events,
     * überschreibbar durch Sub Klassen um optimierte Übertragen gewärleisten zu können.
     */
    protected void writeEvent(java.io.ObjectOutputStream out) throws IOException {
        out.writeByte(main.ordinal());
        out.writeObject(sub);
        out.writeObject(data);
    }

    /**
     * Methode zuständig fürs deserialsieren des Events,
     * überschreibbar durch Sub Klassen um optimierte Übertragen gewärleisten zu können.
     */
    protected void readEvent(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        ini(MainType.values()[in.readByte()],
            (SubType) in.readObject(),
            (Serializable) in.readObject());
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        writeEvent(out);
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        readEvent(in);
    }
}
