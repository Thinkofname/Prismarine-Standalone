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
