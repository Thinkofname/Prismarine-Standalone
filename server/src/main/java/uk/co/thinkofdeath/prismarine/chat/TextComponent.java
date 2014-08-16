package uk.co.thinkofdeath.prismarine.chat;

public class TextComponent extends Component {

    private String text;

    public TextComponent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
