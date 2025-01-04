package ballistix.registers;

import ballistix.References;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BallistixSounds {

	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, References.ID);

	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_ANTIMATTEREXPLOSION = sound("antimatterexplosion");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_DARKMATTER = sound("darkmatter");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_NUCLEAREXPLOSION = sound("nuclearexplosion");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_EMPEXPLOSION = sound("empexplosion");

	private static DeferredHolder<SoundEvent, SoundEvent> sound(String name) {
		return SOUNDS.register(name, () -> SoundEvent.createFixedRangeEvent(ResourceLocation.parse(References.ID + ":" + name), 16.0F));
	}
}
