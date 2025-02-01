package ballistix.api.turret;

import ballistix.common.entity.EntityMissile;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public interface ITarget<T> {

    Vec3 getTargetLocation();

    float getTargetSpeed();

    Vec3 getTargetMovement();

    T getTarget();

    public static record TargetMissile(EntityMissile missile) implements ITarget<EntityMissile> {

        @Override
        public Vec3 getTargetLocation() {
            return missile.getPosition();
        }

        @Override
        public float getTargetSpeed() {
            return missile.speed;
        }

        @Override
        public Vec3 getTargetMovement() {
            return missile.getDeltaMovement();
        }

        @Override
        public EntityMissile getTarget() {
            return missile;
        }
    }

    public static record TargetLivingEntity(LivingEntity entity) implements ITarget<LivingEntity> {

        @Override
        public Vec3 getTargetLocation() {
            return new Vec3(entity.getX(), entity.getY() + entity.getBbHeight() * 0.75, entity.getZ());
        }

        @Override
        public float getTargetSpeed() {
            return 1.0F;
        }

        @Override
        public Vec3 getTargetMovement() {
            return entity.getDeltaMovement();
        }

        @Override
        public LivingEntity getTarget() {
            return entity;
        }
    }
}
