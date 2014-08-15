package uk.co.thinkofdeath.prismarine;

import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        ConsoleHandler console = new ConsoleHandler();
        console.setFormatter(new NeatFormatter());
        Logger root = Logger.getLogger("");
        for (Handler handler : root.getHandlers()) {
            root.removeHandler(handler);
        }
        root.addHandler(console);

        Configuration config = new Configuration();
        Prismarine prismarine = new Prismarine(config);
        prismarine.start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("exit")) {
                prismarine.close();
                return;
            }
        }
    }
}
