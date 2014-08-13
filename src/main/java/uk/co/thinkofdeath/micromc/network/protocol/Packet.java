package uk.co.thinkofdeath.micromc.network.protocol;

import uk.co.thinkofdeath.micromc.network.MCByteBuf;
import uk.co.thinkofdeath.micromc.network.PacketHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;

public interface Packet<H extends PacketHandler> {

    default void read(MCByteBuf buf) {
        throw new UnsupportedOperationException();
    }

    default void write(MCByteBuf buf) {
        throw new UnsupportedOperationException();
    }

    default void handle(H handler) {
        throw new UnsupportedOperationException();
    }

    default String printable() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getSimpleName());
        builder.append("{");
        for (Field field : getClass().getDeclaredFields()) {
            if ((field.getModifiers() & Modifier.STATIC) != 0) {
                continue;
            }
            field.setAccessible(true);
            String print;
            try {
                Object value = field.get(this);
                if (value == null) {
                    print = "null";
                } else if (value.getClass().isArray()) {
                    if (value instanceof byte[]) {
                        print = Arrays.toString((byte[]) value);
                    } else if (value instanceof boolean[]) {
                        print = Arrays.toString((boolean[]) value);
                    } else if (value instanceof int[]) {
                        print = Arrays.toString((int[]) value);
                    } else if (value instanceof short[]) {
                        print = Arrays.toString((short[]) value);
                    } else if (value instanceof char[]) {
                        print = Arrays.toString((char[]) value);
                    } else if (value instanceof long[]) {
                        print = Arrays.toString((long[]) value);
                    } else if (value instanceof float[]) {
                        print = Arrays.toString((float[]) value);
                    } else if (value instanceof double[]) {
                        print = Arrays.toString((double[]) value);
                    } else {
                        print = Arrays.deepToString((Object[]) value);
                    }
                } else if (value instanceof String) {
                    print = "\"" + value + "\"";
                } else {
                    print = Objects.toString(value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            builder.append(field.getName())
                    .append("=")
                    .append(print)
                    .append(",");
        }
        if (builder.charAt(builder.length() - 1) == ',') {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("}");
        return builder.toString();
    }
}
