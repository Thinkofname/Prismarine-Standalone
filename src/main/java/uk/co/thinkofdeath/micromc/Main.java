package uk.co.thinkofdeath.micromc;

public class Main {

    public static void main(String[] args) {
        Configuration config = new Configuration();
        config.setBindAddress("0.0.0.0");
        config.setPort(25565);
        MicroMC microMC = new MicroMC(config);
    }
}
