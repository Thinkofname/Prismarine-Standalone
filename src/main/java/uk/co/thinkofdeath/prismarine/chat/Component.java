package uk.co.thinkofdeath.prismarine.chat;

import uk.co.thinkofdeath.prismarine.util.Stringable;

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

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public Boolean getItalic() {
        return italic == null ? parent.getItalic() : italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public Boolean getUnderlined() {
        return underlined == null ? parent.getUnderlined() : underlined;
    }

    public void setUnderlined(boolean underlined) {
        this.underlined = underlined;
    }

    public Boolean getStrikethrough() {
        return strikethrough == null ? parent.getStrikethrough() : strikethrough;
    }

    public void setStrikethrough(boolean strikethrough) {
        this.strikethrough = strikethrough;
    }

    public Boolean getObfuscated() {
        return obfuscated == null ? parent.getObfuscated() : strikethrough;
    }

    public void setObfuscated(boolean obfuscated) {
        this.obfuscated = obfuscated;
    }

    public Color getColor() {
        return color == null ? parent.getColor() : color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean hasFormatting() {
        return color != null
                || bold != null
                || italic != null
                || underlined != null
                || strikethrough != null
                || obfuscated != null;
    }
}
