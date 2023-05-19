package GUI;

import Benchmark.CpuInfo;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class GuiMethods {
    static void saveScoreToFile(String scoreRecord) throws Exception {
        Files.write(Paths.get("src/data/scores.txt"), (scoreRecord + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    //Searches if there is not already a PC with the same PCID
    public static boolean isPCIDUnique(String pcID) throws Exception {
        try (Scanner scanner = new Scanner(new File("src/data/scores.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().startsWith(pcID.trim() + ",")) {
                    return false;
                }
            }
        }
        return true;
    }

}
