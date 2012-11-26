package universetournament.server.logic.controller;

import java.util.LinkedList;
import java.util.List;

import universetournament.server.core.ServerMainController;
import universetournament.server.events.buffers.PJUTEventBuffManager;
import universetournament.shared.logic.PJUTTimedRefreshable;
import universetournament.shared.logic.entities.EntityEvent;
import universetournament.shared.logic.entities.controller.CollisionType;
import universetournament.shared.logic.entities.ingame.RocketEntity;
import universetournament.shared.logic.entities.ingame.ShipEntity;
import universetournament.shared.logic.entities.ingame.ShotEntity;
import universetournament.shared.logic.entities.ingame.WorldEntity;
import universetournament.shared.logic.entities.ingame.container.PhysicContainer;
import universetournament.shared.logic.entities.ingame.controller.MoveData;
import universetournament.shared.logic.entities.util.EntityCollection;
import universetournament.shared.logic.entities.util.UTEntityManager;
import universetournament.shared.util.math.Vec3;

/**
 * Controller, welcher die Kollisionen zwischen Entities in der Spielwelt
 * berechnet.
 * 
 * @author Daniel Heinrich
 *
 */
public class CollisionController implements PJUTTimedRefreshable
{
    private final EntityCollection entities;

    public CollisionController() {
        entities = ServerMainController.getInstance().getEntities();
    }

    @Override
    public void refresh(float timeDiff) {
        timeDiff /= 1000000000.f;
        List<ShipEntity> ships = new LinkedList<ShipEntity>(entities.getShips());
        List<ShotEntity> shots = new LinkedList<ShotEntity>(entities.getShots());
        List<RocketEntity> rockets = new LinkedList<RocketEntity>(entities.
                getRockets());
        List<WorldEntity<PhysicContainer>> rnd = new LinkedList<WorldEntity<PhysicContainer>>(entities.
                getWorldEntities());

        // nach wahrscheinlichkeit geordned in der Kollisions Paarungen vllcht auftreten
            //Schüsse gegen Hindernisse
            checkCollisions(shots, rnd, timeDiff);

            //Schiffe gegen Schüsse
            checkCollisions(ships, shots, timeDiff);

            //Schiffe gegen Hindernisse
            checkCollisions(ships, rnd, timeDiff);

            //Raketen gegen Hindernisse
            checkCollisions(rockets, rnd, timeDiff);

            //Schiffe gegen Schiffe
            checkCollisions(ships, ships, timeDiff);

            //Schiffe gegen Raketen
            checkCollisions(ships, rockets, timeDiff);
            
            //Hindernisse gegen Hindernisse
            checkCollisions(rnd, rnd, timeDiff);
    }

    private void checkCollisions(List<? extends WorldEntity<PhysicContainer>> a,
                                 List<? extends WorldEntity<PhysicContainer>> b,
                                 float timeDiff) {
        boolean same = a==b;

        for (int pos = 0; pos < a.size() - (same?1:0); ++pos) {
            WorldEntity<PhysicContainer> body1 = a.get(pos);
            for (int i = same?pos + 1:0; i < b.size(); ++i) {
                WorldEntity<PhysicContainer> body2 = b.get(i);
                if (checkCollision(body1, body2, timeDiff)) {
                    a.remove(body1);
                    b.remove(body2);
                    pos -= 1;
                    break;
                }
            }
        }
    }

    private boolean checkCollision(WorldEntity<PhysicContainer> body1,
    		WorldEntity<PhysicContainer> body2, float delta) {
    	
    	PhysicContainer s1 = body1.getTransformation();
    	PhysicContainer s2 = body2.getTransformation();
    	
        Vec3 pos = (Vec3) s2.getPosition().sub(s1.getPosition()); // s2 pos
        // relativ
        // to s1
        Vec3 vel = (Vec3) s2.getVelocity().sub(s1.getVelocity()); // s2 vel
        // relativ
        // to s1
        vel.mult(delta, vel);

        double mindist = s1.getRadius() + s2.getRadius();
        double mindistquad = mindist * mindist;

        double dot = pos.dot(vel);
        if (dot >= 0)
            return false;

        double vellenquad = vel.lenquad();
        double x = (-dot) / vellenquad;
        Vec3 p = new Vec3(); // p: nearest point on the line(s2.p + x * s2.v)
        // to s1
        pos.add(vel.mult(x, p), p);

        double distquad = p.lenquad(); // distance from s1 to line: s2.p + x *
        // s2.v

        if (distquad >= mindistquad)
            return false;

        double fen = Math.sqrt(mindistquad - distquad);
        double vellen = Math.sqrt(vellenquad);
        double fenx = x - fen / vellen; // x value of the collision
        if (fenx > 1)
            return false;

        Vec3 normal = vel.mult(-fen / vellen);
        normal.add(p, normal);
        normal.mult(s1.getRadius() / mindist, normal);//collision normal

        Vec3 colpos = normal.add(s1.getPosition());//point of collision

        double permoved = fenx / x; // percentage of the movment of both spheres
        // until collision
        
        double predelta = permoved * delta;
        double postdelta = delta - predelta;

        processCollision(body1, body2, normal.normalize(), colpos, predelta, postdelta);

        return true;
    }
    
    private void processCollision(WorldEntity<PhysicContainer> body1,
    		WorldEntity<PhysicContainer> body2, Vec3 normal, Vec3 colpos,
    		double predelta, double postdelta)
    {
    	//TODO: Event für Engine
    	
    	PhysicContainer s1 = body1.getTransformation();
    	PhysicContainer s2 = body2.getTransformation();
    	
    	Vec3 vel1 = s1.getVelocity();
    	Vec3 vel2 = s2.getVelocity();
    	
    	//Bewegen bis zur Kollision
    	Vec3 movement1 = vel1.mult(predelta);
    	Vec3 movement2 = vel2.mult(predelta);
    	
    	double mass1 = s1.getMass();
    	double mass2 = s2.getMass();
    	double massTotal = mass1 + mass2;
    	
    	//nur Verhältnis der Geschwindigkeiten für Impuls wichtig
    	double nVel1 = vel1.dot(normal);
    	double nVel2 = vel2.dot(normal);
    	
    	double impulse1 = nVel1 * mass1;
    	double impulse2 = nVel2 * mass2;
    	double impulseTotal = impulse1 + impulse2;
    	
    	//Geschwindigkeiten verändern
    	vel1.add(normal.mult((impulseTotal + impulse2 - nVel1 * mass2)
    			/ massTotal - nVel1), vel1);
    	vel2.add(normal.mult((impulseTotal + impulse1 - nVel2 * mass1)
    			/ massTotal - nVel2), vel2);
    	
    	//Nach Kollision bewegen
    	movement1.add(vel1.mult(postdelta), movement1);
    	movement2.add(vel1.mult(postdelta), movement2);
    	
    	//Änderungen anwenden und versenden
    	modifyEntity(body1, movement1, vel1);
    	modifyEntity(body2, movement2, vel2);
    	
    	short id1 = body1.getId();
    	short id2 = body2.getId();
    	notifyCollision(id1, id2);
    	notifyCollision(id2, id1);
    }
    
    private void modifyEntity(WorldEntity<PhysicContainer> body,
    		Vec3 moved, Vec3 speed)
    {
    	PhysicContainer pc = body.getTransformation();
    	pc.shiftWorldPosition(moved);
    	Vec3 position = pc.getPosition();
    	
    	//der lokalen Entity und den Clients Änderung übermitteln
    	MoveData data = new MoveData(position, new Vec3(), speed);
    	data.setOverride(true);
    	EntityEvent ee = new EntityEvent(body.getId(),
    			WorldEntity.SubEvents.MOVE, data);
    	UTEntityManager.getInstance().handleEvent(ee);
    	PJUTEventBuffManager.getInstance().sendToAll(ee);
    }
    
    private void notifyCollision(short recipient, short object)
    {
    	/*
    	 * intern Event versenden, damit jeweilige Controller Schaden und
    	 * andere Einflüsse auf das getroffene Objekt berechnen können
    	 */
    	EntityEvent ee = new EntityEvent(recipient, CollisionType.NOTIFY,
    			object);
    	UTEntityManager.getInstance().handleEvent(ee);
    }
}
