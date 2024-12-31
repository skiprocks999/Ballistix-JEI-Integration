package ballistix.datagen.client;

import ballistix.References;
import ballistix.registers.BallistixSounds;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

public class BallistixSoundProvider extends SoundDefinitionsProvider {

	public BallistixSoundProvider(PackOutput output, ExistingFileHelper helper) {
		super(output, References.ID, helper);
	}

	@Override
	public void registerSounds() {
		add(BallistixSounds.SOUND_ANTIMATTEREXPLOSION);
		add(BallistixSounds.SOUND_DARKMATTER);
		add(BallistixSounds.SOUND_NUCLEAREXPLOSION);
	}

	private void add(DeferredHolder<SoundEvent, SoundEvent> sound) {
		add(sound.get(), SoundDefinition.definition().subtitle("subtitles." + References.ID + "." + sound.getId().getPath()).with(SoundDefinition.Sound.sound(sound.getId(), SoundDefinition.SoundType.SOUND)));
	}

}
