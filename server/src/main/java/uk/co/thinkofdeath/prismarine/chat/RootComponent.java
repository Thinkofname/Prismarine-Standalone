package uk.co.thinkofdeath.prismarine.chat;

class RootComponent extends Component {

    public final static RootComponent INSTANCE = new RootComponent();

    RootComponent() {
        bold = false;
        italic = false;
        underlined = false;
        strikethrough = false;
        obfuscated = false;
        color = Color.WHITE;
        parent = null;
    }
}
