package uk.co.thinkofdeath.micromc.chat;

import uk.co.thinkofdeath.micromc.util.Stringable;

import java.util.ArrayList;
import java.util.List;

public abstract class Component implements Stringable {

    Component parent = RootComponent.INSTANCE;
    List<Component> subComponents;
    Boolean bold;
    Boolean italic;
    Boolean underlined;
    Boolean strikethrough;
    Boolean obfuscated;
    Color color;

    Component() {

    }

    public void addComponent(Component component) {
        if (subComponents == null) {
            subComponents = new ArrayList<>();
        }
        subComponents.add(component);
        component.parent = this;
    }

    public boolean getBold() {
        return bold == null ? parent.getBold() : bold;
    }

    public void setBold(Boolean bold) {
        this.bold = bold;
    }

    public Boolean getItalic() {
        return italic == null ? parent.getItalic() : italic;
    }

    public void setItalic(Boolean italic) {
        this.italic = italic;
    }

    public Boolean getUnderlined() {
        return underlined == null ? parent.getUnderlined() : underlined;
    }

    public void setUnderlined(Boolean underlined) {
        this.underlined = underlined;
    }

    public Boolean getStrikethrough() {
        return strikethrough == null ? parent.getStrikethrough() : strikethrough;
    }

    public void setStrikethrough(Boolean strikethrough) {
        this.strikethrough = strikethrough;
    }

    public Boolean getObfuscated() {
        return obfuscated == null ? parent.getObfuscated() : strikethrough;
    }

    public void setObfuscated(Boolean obfuscated) {
        this.obfuscated = obfuscated;
    }

    public Color getColor() {
        return color == null ? parent.getColor() : color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
