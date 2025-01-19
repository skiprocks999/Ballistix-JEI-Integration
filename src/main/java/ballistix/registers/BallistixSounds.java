package ballistix.registers;

import ballistix.References;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BallistixSounds {

	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, References.ID);

	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_ANTIMATTEREXPLOSION = sound("antimatterexplosion", 100);
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_LARGE_ANTIMATTEREXPLOSION = sound("largeantimatterexplosion", "antimatterexplosion", 160);
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_DARKMATTER = sound("darkmatter", 100);
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_NUCLEAREXPLOSION = sound("nuclearexplosion", 90);
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_EMPEXPLOSION = sound("empexplosion", 100);
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_MISSILE_ROCKETLAUNCHER = sound("missile_launch_rocketlauncher", 32);
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_MISSILE_SILO = sound("missile_launch_silo", 64);
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_RADAR = sound("radar", 16);
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_FIRECONTROLRADAR = sound("firecontrolradar", 32);



	private static DeferredHolder<SoundEvent, SoundEvent> sound(String name, float range) {
		return sound(name, name, range);
	}
	private static DeferredHolder<SoundEvent, SoundEvent> sound(String name, String soundName, float range) {
		return SOUNDS.register(name, () -> SoundEvent.createFixedRangeEvent(ResourceLocation.parse(References.ID + ":" + soundName), range));
	}
}
