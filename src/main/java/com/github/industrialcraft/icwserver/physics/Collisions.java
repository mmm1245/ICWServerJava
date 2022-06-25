package com.github.industrialcraft.icwserver.physics;

import com.github.industrialcraft.icwserver.world.World;
import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.github.industrialcraft.icwserver.world.entity.IPhysicalEntity;
import com.github.industrialcraft.icwserver.world.entity.ItemStackEntity;
import mikera.vectorz.Vector2;

import java.util.function.Predicate;

public class Collisions {
    public static boolean bulletCanHit(Entity entity){
        if(entity instanceof IPhysicalEntity pe){
            if(pe.getPhysicalObject().layer==EPhysicsLayer.PROJECTILE)
                return false;
            if(entity instanceof ItemStackEntity)
                return false;
            return pe.getPhysicalObject().layer != EPhysicsLayer.TRANSPARENT;
        }
        return false;
    }
    public static Entity lineIntersection(Vector2 start, Vector2 end, World world, Predicate<Entity> entityPredicate){
        Vector2 vec = null;
        Entity entityRet = null;
        for(Entity entity : world.getEntities()){
            if(entity instanceof IPhysicalEntity physicalEntity){
                if(!entityPredicate.test(entity))
                    continue;
                PhysicsObject po = physicalEntity.getPhysicalObject();
                Vector2 intersect = lineIntersectAABB(start, end, new Vector2(entity.getLocation().x(), entity.getLocation().y()), new Vector2(entity.getLocation().x()+po.hitboxW, entity.getLocation().y()+po.hitboxH));
                if(vec == null && intersect != null){
                    vec = intersect;
                    entityRet = entity;
                } else if(intersect != null){
                    if(intersect.distance(start) < vec.distance(start)) {
                        vec = intersect;
                        entityRet = entity;
                    }
                }
            }
        }
        return entityRet;
    }

    public static boolean AABB(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2){
        return (x1 < x2 + w2 &&
                x1 + w1 > x2 &&
                y1 < y2 + h2 &&
                y1 + h1 > y2);
    }
    public static Vector2 lineIntersectAABB(Vector2 line1, Vector2 line2, Vector2 a, Vector2 b){
        Vector2 in1 = lineIntersectLine(line1, line2, a, new Vector2(b.x, a.y));
        Vector2 in2 = lineIntersectLine(line1, line2, a, new Vector2(a.x, b.y));
        Vector2 in3 = lineIntersectLine(line1, line2, b, new Vector2(b.x, a.y));
        Vector2 in4 = lineIntersectLine(line1, line2, b, new Vector2(b.x, a.y));

        Vector2 leastDistance = in1;
        float leastDistanceF = in1==null?Float.MAX_VALUE:(float) leastDistance.distance(line1);

        if(in2!=null){
            float lineDist = (float) in2.distance(line1);
            if(lineDist < leastDistanceF){
                leastDistanceF = lineDist;
                leastDistance = in2;
            }
        }

        if(in3!=null){
            float lineDist = (float) in3.distance(line1);
            if(lineDist < leastDistanceF){
                leastDistanceF = lineDist;
                leastDistance = in3;
            }
        }

        if(in4!=null){
            float lineDist = (float) in4.distance(line1);
            if(lineDist < leastDistanceF){
                leastDistanceF = lineDist;
                leastDistance = in4;
            }
        }
        return leastDistance;
    }
    public static Vector2 lineIntersectLine(Vector2 a1, Vector2 a2, Vector2 b1, Vector2 b2)
    {
        Vector2 intersection = Vector2.of(0, 0);

        Vector2 b = (Vector2) a2.subCopy(a1);
        Vector2 d = (Vector2) b2.subCopy(b1);
        float bDotDPerp = (float) (b.x * d.y - b.y * d.x);

        // if b dot d == 0, it means the lines are parallel so have infinite intersection points
        if (bDotDPerp == 0)
            return null;

        Vector2 c = (Vector2) b1.subCopy(a1);
        float t = (float) ((c.x * d.y - c.y * d.x) / bDotDPerp);
        if (t < 0 || t > 1)
            return null;

        float u = (float) ((c.x * b.y - c.y * b.x) / bDotDPerp);
        if (u < 0 || u > 1)
            return null;

        return (Vector2) a1.addCopy(b.multiplyCopy(t));
    }
}
