package ballistix.registers;

import ballistix.Ballistix;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class BallistixDamageTypes {

	public static final ResourceKey<DamageType> CHEMICAL_GAS = create("chemicalgas");
	public static final ResourceKey<DamageType> SHRAPNEL = create("shrapnel");
	public static final ResourceKey<DamageType> CIWS_BULLET = create("ciwsbullet");
	public static final ResourceKey<DamageType> LASER_TURRET = create("laserturret");

	public static ResourceKey<DamageType> create(String name) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, Ballistix.rl(name));
	}

	public static void registerTypes(BootstrapContext<DamageType> context) {
		context.register(CHEMICAL_GAS, new DamageType("chemicalgas", DamageScaling.NEVER, 0.1F, DamageEffects.HURT));
		context.register(SHRAPNEL, new DamageType("shrapnel", DamageScaling.NEVER, 0, DamageEffects.HURT));
		context.register(CIWS_BULLET, new DamageType("ciwsbullet", DamageScaling.NEVER, 0, DamageEffects.HURT));
		context.register(LASER_TURRET, new DamageType("laserturret", DamageScaling.NEVER, 0, DamageEffects.HURT));
	}

}
