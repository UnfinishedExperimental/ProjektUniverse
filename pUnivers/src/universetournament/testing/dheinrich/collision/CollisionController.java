/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.testing.dheinrich.collision;

import universetournament.shared.util.math.Vec3;

/**
 *
 * @author dheinrich
 */
public class CollisionController
{

    public boolean checkCollision(Sphere s1, Sphere s2, float delta) {
        Vec3 pos = (Vec3) s2.getPosition().sub(s1.getPosition()); // s2 pos relativ to s1
        Vec3 vel = (Vec3) s2.getVelocity().sub(s1.getVelocity()); // s2 vel relativ to s1
        vel.mult(delta, vel);

        double mindist = s1.getRadius() + s2.getRadius();
        double mindistquad = mindist * mindist;

        double dot = pos.dot(vel);
        if (dot >= 0)
            return false;


        double vellenquad = vel.lenquad();
        double x = (-dot) / vellenquad;
        Vec3 p = new Vec3(); // p: nearest point on the line(s2.p + x * s2.v) to s1
        pos.add(vel.mult(x, p), p);

        double distquad = p.lenquad(); // distance from s1 to line: s2.p + x * s2.v

        if (distquad >= mindistquad)
            return false;

        double fen = Math.sqrt(mindistquad - distquad);
        double vellen = Math.sqrt(vellenquad);
        double fenx = x - fen / vellen; // x value of the collision
        if (fenx > 1)
            return false; // on a way to collision but not this frame


        Vec3 normal = vel.mult(-fen / vellen);
        normal.add(p, normal);
        normal.mult(s1.getRadius() / mindist, normal);

        Vec3 colpos = normal.add(s1.getPosition());

        double permoved = fenx / x; // percentage of the movment of both spheres until collision

//        fireEvent(new CollisionEvent(s1, s2, (Vec3f) normal.normalize(normal),
//                                     colpos, permoved, delta));

        return true;
    }
}
