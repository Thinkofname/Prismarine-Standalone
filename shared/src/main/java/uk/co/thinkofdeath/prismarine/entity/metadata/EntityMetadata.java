/*
 * Copyright 2014 Matthew Collins
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.thinkofdeath.prismarine.entity.metadata;

import io.netty.handler.codec.DecoderException;
import uk.co.thinkofdeath.prismarine.item.ItemStack;
import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.util.IntMap;

import java.util.HashMap;
import java.util.Map;

public class EntityMetadata {

    private static final Map<Class<?>, Integer> classIdMap = new HashMap<>();
    private IntMap<MetaEntry> map = new IntMap<>(32);

    public <T> void register(MetaKey<T> key) {
        register(key, key.getDefault());
    }

    public <T> void register(MetaKey<T> key, T value) {
        if (map.contains(key.getId())) {
            throw new IllegalArgumentException(key.getId() + " is already in use");
        }
        map.put(key.getId(), new MetaEntry(key.getId(), value));
    }

    public <T> void update(MetaKey<T> key, T value) {
        MetaEntry entry = map.get(key.getId());
        if (entry.value == null || !entry.equals(value)) {
            entry.value = value;
            entry.dirty = true;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(MetaKey<T> key) {
        return (T) map.get(key.getId()).value;
    }

    public byte getByte(MetaKey<Byte> key) {
        return (byte) map.get(key.getId()).value;
    }

    public short getShort(MetaKey<Short> key) {
        return (short) map.get(key.getId()).value;
    }

    public int getInt(MetaKey<Integer> key) {
        return (int) map.get(key.getId()).value;
    }

    public float getFloat(MetaKey<Float> key) {
        return (float) map.get(key.getId()).value;
    }

    public void write(MCByteBuf buf) {
        map.forEach((v, k) -> {
            int type = classIdMap.get(v.getClass());
            buf.writeByte(k | (type << 5));
            switch (type) {
                case 0:
                    buf.writeByte((byte) v.value);
                    break;
                case 1:
                    buf.writeShort((short) v.value);
                    break;
                case 2:
                    buf.writeInt((int) v.value);
                    break;
                case 3:
                    buf.writeFloat((float) v.value);
                    break;
                case 4:
                    buf.writeString((String) v.value);
                    break;
                case 5:
                    buf.writeItemStack((ItemStack) v.value);
                    break;
            }
        });
        buf.writeByte(0x7F);
    }

    public void read(MCByteBuf buf) {
        while (true) {
            int item = buf.readUnsignedByte();
            if (item == 0x7F) {
                break;
            }
            int id = item & 0x1F;
            int type = item >> 5;

            Object val;
            switch (type) {
                case 0:
                    val = buf.readByte();
                    break;
                case 1:
                    val = buf.readShort();
                    break;
                case 2:
                    val = buf.readInt();
                    break;
                case 3:
                    val = buf.readFloat();
                    break;
                case 4:
                    val = buf.readString(Short.MAX_VALUE);
                    break;
                case 5:
                    val = buf.readItemStack();
                    break;
                default:
                    throw new DecoderException("Unknown metadata type " + type);
            }
            MetaEntry entry;
            if (map.contains(id)) {
                entry = map.get(id);
            } else {
                entry = new MetaEntry(id, val);
                map.put(id, entry);
            }
            entry.value = val;
            entry.dirty = true;
        }
    }

    static {
        classIdMap.put(Byte.class, 0);
        classIdMap.put(Short.class, 1);
        classIdMap.put(Integer.class, 2);
        classIdMap.put(Float.class, 3);
        classIdMap.put(String.class, 4);
        classIdMap.put(ItemStack.class, 5);
    }
}
