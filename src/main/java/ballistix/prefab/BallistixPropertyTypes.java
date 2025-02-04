package ballistix.prefab;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;

import electrodynamics.prefab.properties.PropertyType;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;

public class BallistixPropertyTypes {

    public static final PropertyType<List<Integer>, ByteBuf> INTEGER_LIST = new PropertyType<>(
            //
            (thisSet, otherSet) -> {
                if (thisSet.size() != otherSet.size()) {
                    return false;
                }
                int a, b;
                for (int i = 0; i < thisSet.size(); i++) {
                    a = thisSet.get(i);
                    b = otherSet.get(i);
                    if (a != b) {
                        return false;
                    }
                }
                return true;
            },
            //
            ByteBufCodecs.fromCodec(Codec.INT.listOf()),
            //
            writer -> {
                List<Integer> list = writer.prop().get();
                CompoundTag data = new CompoundTag();
                data.putInt("size", list.size());
                for (int i = 0; i < list.size(); i++) {
                    data.putInt("" + i, list.get(i));
                }
                writer.tag().put(writer.prop().getName(), data);
            },
            //
            reader -> {
                List<Integer> list = new ArrayList<>();
                CompoundTag data = reader.tag().getCompound(reader.prop().getName());
                int size = data.getInt("size");
                for (int i = 0; i < size; i++) {
                    list.add(data.getInt("" + i));
                }
                return list;
            }
            //
    );

    public static final PropertyType<List<String>, ByteBuf> STRING_LIST = new PropertyType<>(
            //
            (thisSet, otherSet) -> {
                if (thisSet.size() != otherSet.size()) {
                    return false;
                }
                String a, b;
                for (int i = 0; i < thisSet.size(); i++) {
                    a = thisSet.get(i);
                    b = otherSet.get(i);
                    if (a != b) {
                        return false;
                    }
                }
                return true;
            },
            //
            ByteBufCodecs.fromCodec(Codec.STRING.listOf()),
            //
            writer -> {
                List<String> list = writer.prop().get();
                CompoundTag data = new CompoundTag();
                data.putInt("size", list.size());
                for (int i = 0; i < list.size(); i++) {
                    data.putString("" + i, list.get(i));
                }
                writer.tag().put(writer.prop().getName(), data);
            },
            //
            reader -> {
                List<String> list = new ArrayList<>();
                CompoundTag data = reader.tag().getCompound(reader.prop().getName());
                int size = data.getInt("size");
                for (int i = 0; i < size; i++) {
                    list.add(data.getString("" + i));
                }
                return list;
            }
            //
    );

}
