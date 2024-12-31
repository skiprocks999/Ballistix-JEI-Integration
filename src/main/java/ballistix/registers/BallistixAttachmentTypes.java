package ballistix.registers;

import ballistix.References;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class BallistixAttachmentTypes {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, References.ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<HashMap<Integer, HashSet<BlockPos>>>> SILO_FREQUENCIES = ATTACHMENT_TYPES.register("silofrequencies", () -> AttachmentType.builder(() -> new HashMap<Integer, HashSet<BlockPos>>()).serialize(new IAttachmentSerializer<CompoundTag, HashMap<Integer, HashSet<BlockPos>>>() {
        @Override
        public HashMap<Integer, HashSet<BlockPos>> read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
            HashMap<Integer, HashSet<BlockPos>> data = new HashMap<>();

            int size = tag.getInt("size");
            for (int i = 0; i < size; i++) {

                CompoundTag stored = tag.getCompound("" + i);

                int freq = stored.getInt("freq");

                int setSize = stored.getInt("setsize");

                HashSet<BlockPos> tiles = new HashSet<>();

                for(int j = 0; j < setSize; j++) {
                    tiles.add(BlockPos.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, stored.get("pos" + j))).result().get());
                }

                data.put(freq, tiles);
            }


            return data;
        }

        @Override
        public @Nullable CompoundTag write(HashMap<Integer, HashSet<BlockPos>> attachment, HolderLookup.Provider provider) {
            CompoundTag data = new CompoundTag();
            int size = attachment.size();
            data.putInt("size", size);
            int i = 0;
            for (Map.Entry<Integer, HashSet<BlockPos>> entry : attachment.entrySet()) {

                CompoundTag store = new CompoundTag();

                store.putInt("freq", entry.getKey());

                HashSet<BlockPos> tiles = entry.getValue();

                store.putInt("setsize", tiles.size());

                int j = 0;

                for(BlockPos pos : tiles) {

                    BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, pos).ifSuccess(tag -> store.put("pos" + j, tag));

                }

                data.put(i + "", store);

                i++;
            }
            return data;
        }
    }).build());

}
