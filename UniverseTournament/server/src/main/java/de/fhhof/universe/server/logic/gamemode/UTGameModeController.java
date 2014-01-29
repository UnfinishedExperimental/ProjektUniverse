package de.fhhof.universe.server.logic.gamemode;

import darwin.util.math.base.vector.Vector3;
import java.util.Map.Entry;

import de.fhhof.universe.server.core.ServerMainController;
import de.fhhof.universe.server.events.buffers.PJUTEventBuffManager;
import de.fhhof.universe.server.logic.entities.PhysicalDecFactory;
import de.fhhof.universe.shared.core.GameSubType;
import de.fhhof.universe.shared.data.proto.ShipConfig;
import de.fhhof.universe.shared.events.GameEvent;
import de.fhhof.universe.shared.events.MainType;
import de.fhhof.universe.shared.logic.entities.Entity;
import de.fhhof.universe.shared.logic.entities.EntityEvent;
import de.fhhof.universe.shared.logic.entities.PJUTPlayer;
import de.fhhof.universe.shared.logic.entities.controller.PlayerAttribute;
import de.fhhof.universe.shared.logic.entities.controller.ShipResetController;
import de.fhhof.universe.shared.logic.entities.ingame.ShipEntity;
import de.fhhof.universe.shared.logic.entities.ingame.WorldEntity;
import de.fhhof.universe.shared.logic.entities.ingame.controller.MoveData;
import de.fhhof.universe.shared.logic.entities.requests.PDecRequest;
import de.fhhof.universe.shared.logic.entities.util.EntityController;
import de.fhhof.universe.shared.logic.gamemode.UTGameModeData;
import de.fhhof.universe.shared.logic.gamemode.events.GameAttribute;
import de.fhhof.universe.shared.logic.gamemode.events.GameModeSubType;

/**
 * Abstrakte Oberklasse für alle GameModeController, die bereits die Verwaltung
 * der Zeit- und Punktebegrenzung, sowie die allgemeine Spielerverwaltung und
 * Score-Verwaltung für Kills und Deaths bietet. Übeschreibt handleEvent und
 * refresh Methoden des Oberklasse für besonderes Verhalten des Controllers.
 *
 * @author Bernd Marbach
 *
 */
public abstract class UTGameModeController
        extends EntityController<UTGameModeData> {

    /**
     * Punktzahl, die ein Spieler für einen Kill bekommt.
     */
    protected short scoreForKill;

    /**
     * Punktzahl, die ein Spieler für einen Tod bekommt. Wahrscheinlich ist hier
     * ein negativer Wert gewünscht.
     */
    protected short scoreForDeath;

    /**
     * Allgemeine Daten für jeden Spielmodus
     */
    protected final UTGameModeData gameData;

    /**
     * Radius der Kreis-Karte.
     */
    protected float mapSize;

    /**
     * Initialisierungsvariable für zufälligen Weltraumschrott
     */
    protected boolean start = true;

    /**
     * Sonstige Dektorationen
     */
    protected short[] decorations = {16, 17, 18};

    private int decorationNum;

    /**
     * Erstellt einen GameModeController mit der angegebenen Restzeit und dem
     * angegebenen Punktelimit. Standardmäßig erhält jeder Spieler für einen
     * Kill einen Punkt und verliert einen für jeden Tod. Wirft eine
     * NullpointerException, wenn die übergebenen Spieldaten null sind.
     *
     * @param gameData zu nutzende Spieldaten
     */
    public UTGameModeController(UTGameModeData gameData) {
        super(gameData);

        if (gameData != null) {
            this.gameData = gameData;
        } else {
            throw new NullPointerException("Spieldaten sind null");
        }

        scoreForKill = 1;
        scoreForDeath = -1;
        mapSize = 180.0f;

        decorationNum = 5;
    }

    /**
     * Verarbeitet intern ein SubEvent - andere Modi können diese Methode für
     * eigene Events überschreiben.
     *
     * @param subType Subtyp des Events
     * @param data Daten des Events
     */
    protected void processSubEvent(GameModeSubType subType, Object data) {
        switch (subType) {
            case SET_TIME:
                setTimeLeft((Double) data);
                break;

            case PLAYER_JOINED:
                playerJoined((PJUTPlayer) data);
                break;

            case PLAYER_LEFT:
                playerLeft((Byte) data);
                break;

            case PLAYER_KILLED:
                playerKilled((Byte) data);
                break;

            case PLAYER_DIED:
                playerDied((Byte) data);
                break;
        }
    }

    @Override
    public void refresh(float timeDiff) {
        if (gameData.isRunning()) {
            timeAdvanced(timeDiff);

            if (gameData.getTimeLeft() <= 0.0 || pointsReached()) {
                gameData.setRunning(false);
                gameOver();
            }
        }
    }

    @Override
    public void handleEvent(GameEvent ge) {
        try {
            processSubEvent((GameModeSubType) ge.getSub(), ge.getData());
        } catch (Exception e) {
            System.err.println("Ankommende Daten unbrauchbar");
        }
    }

    /**
     * @return verbleibende Zeit in Sekunden
     */
    public double getTimeLeft() {
        return gameData.getTimeLeft();
    }

    /**
     * Setzt die verbleibende Zeit in Sekunden. Werden negative Werte übergeben
     * wird diese auf 0 gesetzt.
     *
     * @param seconds neue verbleibende Zeit
     */
    public void setTimeLeft(double seconds) {
        gameData.setTimeLeft(seconds);
    }

    /**
     * Fügt den Spieler der internen Liste hinzu.
     *
     * @param player neuer Spieler
     */
    protected void playerJoined(PJUTPlayer player) {
        if (player != null) {
            gameData.getPlayers().put(player.getClientId(), player);
        }
    }

    /**
     * Entfernt einen Spieler aus der internen Liste, falls vorhanden.
     *
     * @param id ID des zu entfernenden Spielers
     */
    protected void playerLeft(byte id) {
        gameData.getPlayers().remove(id);
    }

    /**
     * Gibt dem Spieler die Punkte für einen Kill und erhöht seinen Kill-Zähler
     * um 1. Ignoriert Aufrufe mit unbekannten IDs.
     *
     * @param id ID des Spielers, der einen Kill erzielt hat
     */
    protected void playerKilled(byte id) {
        PJUTPlayer player = gameData.getPlayers().get(id);

        if (player != null) {
            player.killed();
            player.modScore(scoreForKill);

            //mitteilen
            EntityEvent ee = new EntityEvent(player.getId(),
                    PlayerAttribute.KILLS, player.getKills());
            PJUTEventBuffManager.getInstance().sendToAll(ee);
            ee = new EntityEvent(player.getId(), PlayerAttribute.SCORE,
                    player.getScore());
            PJUTEventBuffManager.getInstance().sendToAll(ee);
        }
    }

    /**
     * Zieht einem Spieler die Punkte für einen Tod ab und erhöht seinen
     * Tod-Zähler um 1. Ignoriert Aufrufe mit unbekannten IDs.
     *
     * @param id ID des getöteten Spielers
     */
    protected void playerDied(byte id) {
        PJUTPlayer player = gameData.getPlayers().get(id);

        if (player != null) {
            player.died();
            player.modScore(scoreForDeath);

            //mitteilen
            EntityEvent ee = new EntityEvent(player.getId(),
                    PlayerAttribute.DEATHS, player.getDeaths());
            PJUTEventBuffManager.getInstance().sendToAll(ee);
            ee = new EntityEvent(player.getId(), PlayerAttribute.SCORE,
                    player.getScore());
            PJUTEventBuffManager.getInstance().sendToAll(ee);
        }

        //Schiff zurücksetzen
        ShipEntity se = ServerMainController.getInstance().getEntities().
                getShip(id);
        ShipConfig sc = se.getConfiguration();

        se.setHitPoints(sc.getMaxHitpoints());
        se.setShieldEnergy(sc.getShieldCapacity());
        se.setRocketCount(sc.getMaxRockets());

        //bei Client zurücksetzen lassen
        EntityEvent ee = new EntityEvent(se.getId(),
                ShipResetController.SubEvents.RESET, null);
        PJUTEventBuffManager.getInstance().sendToId(id, ee);

        placeShip(se);
    }

    /**
     * Zieht die vergangene Zeit von der Restzeit ab und gibt zurück, ob die
     * Zeit bereits abgelaufen ist.
     *
     * @param timeDiff Zeitdifferenz in Nanosekunden
     * @return ob Zeit abgelaufen ist
     */
    protected boolean timeAdvanced(float timeDiff) {
        boolean over = false;

        gameData.timePassed(timeDiff / 1000000000.f);

        if (gameData.getTimeLeft() <= 0.0) {
            over = true;
        }

        return over;
    }

    /**
     * @return ob die Einelspieler-Punktzahlgrenze schon erreicht wurde
     */
    protected boolean pointsReached() {
        boolean reached = false;
        short limit = gameData.getScoreLimit();

        for (Entry<Byte, PJUTPlayer> e : gameData.getPlayers().entrySet()) {
            if (e.getValue().getScore() > limit) {
                reached = true;
                break;
            }
        }

        return reached;
    }

    /**
     * Verschickt an die Clients einen Event, welcher mitteilt, dass das Spiel
     * vorbei ist und gegebenenfalls, wie es ausgegangen ist. Sollte von
     * Unterklassen für verschiedenes Verhalten angepasst werden.
     */
    protected void gameOver() {
        String nachricht = "Spiel vorbei!\n";
        PJUTPlayer winner = null;
        short currScore = 0, nScore = 0;

        //Gewinner ermitteln, falls vorhanden
        for (Entry<Byte, PJUTPlayer> pe : gameData.getPlayers().entrySet()) {
            nScore = pe.getValue().getScore();

            if (nScore > currScore) {
                winner = pe.getValue();
                currScore = nScore;
            }
        }

        if (winner != null) {
            nachricht += winner.getName() + " gewinnt!";
        }

        GameEvent ge = new GameEvent(MainType.TYPE_GAMEMODE,
                GameAttribute.GAME_OVER, nachricht);
        PJUTEventBuffManager.getInstance().sendToAll(ge);
    }

    /**
     * Weist den Spielmodus an, das Schiff an eine gültige Ausgangsposition zu
     * setzen. Wird bei der Erstellung des Schiffs oder beim "Respawnen"
     * aufgerufen. Sollte von den Unterklassen enstprechendend überschrieben
     * werden. In der Standard-Implementierung werden die Schiffe zufällig auf
     * dem Spielfeld positioniert. Wird das erste Schiff gesetzt werden
     * ebenfalls Dekorationen gesetzt. Sendet entsprechende Override-Events.
     * Ignoriert null.
     *
     * @param ship zu platzierendes Schiff
     */
    public void placeShip(ShipEntity ship) {
        if (ship != null) {
            //Zufällige Position in einem Kreis
            float angle = (float) (Math.random() * 2 * Math.PI);
            Vector3 pos = new Vector3((float) Math.sin(angle), 0.f,
                    (float) Math.cos(angle));
            pos.normalize().mul((float) (Math.random() * mapSize));
            Vector3 vel = new Vector3();

            ship.getTransformation().setWorldPosition(pos);
            ship.getTransformation().setVelocity(vel);

            //den Clients Änderung übermitteln
            MoveData data = new MoveData(pos, vel, vel, 0);
            data.setOverride(true);
            EntityEvent ee = new EntityEvent(ship.getId(),
                    WorldEntity.SubEvents.MOVE, data);
            PJUTEventBuffManager.getInstance().sendToAll(ee);
        }

        spawnDecorations();
    }

    /**
     * Kickt den Spieler mit dem angegebenen Index in der internen Liste, sofern
     * vorhanden.
     *
     * @param index
     */
    public void kick(int index) {
        int pos = 0;
        byte id = -1;
        for (Entry<Byte, PJUTPlayer> e : gameData.getPlayers().entrySet()) {
            if (pos < index) {
                ++pos;
                continue;
            }
            id = e.getValue().getClientId();
        }

        GameEvent ge = new GameEvent(MainType.TYPE_GAME, GameSubType.KICK, id);
        PJUTEventBuffManager.getInstance().sendToId(id, ge);
    }

    /**
     * Spawnt zufällig Dekorationsobjekte.
     */
    protected void spawnDecorations() {
        if (start) {
            start = false;
            PhysicalDecFactory fact = new PhysicalDecFactory();

            float angle = 0;
            short config = 0;
            Vector3 pos = null;

            while (decorationNum-- > 0) {
                config = decorations[(int) (Math.random()
                        * decorations.length)];

                angle = (float) (Math.random() * 2 * Math.PI);
                pos = new Vector3((float) Math.sin(angle), 0.f,
                        (float) Math.cos(angle));
                pos.normalize().mul((float) (Math.random() * mapSize));

                fact.handleEvent(new GameEvent(MainType.TYPE_ENTITY,
                        Entity.SubEvents.CREATE, new PDecRequest(pos, config)));
            }
        }
    }
}
