package uk.co.thinkofdeath.prismarine.chat;

import com.google.gson.*;

import java.lang.reflect.Type;

class ComponentSerializer implements JsonSerializer<Component>, JsonDeserializer<Component> {
    @Override
    public Component deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (jsonElement.isJsonArray()) {
            TextComponent component = new TextComponent("");
            for (Component c : jsonDeserializationContext.<Component[]>deserialize(jsonElement.getAsJsonArray(), Component[].class)) {
                component.addComponent(c);
            }
            return component;
        } else if (jsonElement.isJsonObject()) {
            JsonObject object = jsonElement.getAsJsonObject();
            Component component;
            if (object.has("text")) {
                component = new TextComponent(object.get("text").getAsString());
            } else {
                throw new RuntimeException("Unhandled component");
            }

            if (object.has("bold")) {
                component.bold = object.get("bold").getAsBoolean();
            }
            if (object.has("italic")) {
                component.italic = object.get("italic").getAsBoolean();
            }
            if (object.has("underlined")) {
                component.underlined = object.get("underlined").getAsBoolean();
            }
            if (object.has("strikethrough")) {
                component.strikethrough = object.get("strikethrough").getAsBoolean();
            }
            if (object.has("obfuscated")) {
                component.obfuscated = object.get("obfuscated").getAsBoolean();
            }
            if (object.has("color")) {
                component.color = Color.valueOf(object.get("color").getAsString().toUpperCase());
            }

            if (object.has("extra")) {
                for (Component c : jsonDeserializationContext.<Component[]>deserialize(object.getAsJsonArray("extra"), Component[].class)) {
                    component.addComponent(c);
                }
            }
        }
        throw new RuntimeException("Unhandled component");
    }

    @Override
    public JsonElement serialize(Component component, Type type, JsonSerializationContext jsonSerializationContext) {
        if (component instanceof TextComponent && !component.hasFormatting()) {
            return new JsonPrimitive(((TextComponent) component).getText());
        }
        JsonObject jsonObject = new JsonObject();
        if (component instanceof TextComponent) {
            jsonObject.addProperty("text", ((TextComponent) component).getText());
        }

        if (component.bold != null) {
            jsonObject.addProperty("bold", component.bold);
        }
        if (component.italic != null) {
            jsonObject.addProperty("italic", component.italic);
        }
        if (component.underlined != null) {
            jsonObject.addProperty("underlined", component.underlined);
        }
        if (component.strikethrough != null) {
            jsonObject.addProperty("strikethrough", component.strikethrough);
        }
        if (component.obfuscated != null) {
            jsonObject.addProperty("obfuscated", component.obfuscated);
        }
        if (component.color != null) {
            jsonObject.addProperty("color", component.color.toString().toLowerCase());
        }
        if (component.subComponents != null) {
            Component[] com = new Component[component.subComponents.size()];
            com = component.subComponents.toArray(com);
            jsonObject.add("extra", jsonSerializationContext.serialize(com, Component[].class));
        }
        return jsonObject;
    }
}
