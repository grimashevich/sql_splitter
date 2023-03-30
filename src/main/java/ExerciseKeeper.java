import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class ExerciseKeeper {
    private List<StringBuilder> exercises;
    private int currentEx = 0;
    private final String subfolder = "src";
    private int currentDay;

    public ExerciseKeeper(int day) {
        exercises = new LinkedList<>();
        exercises.add(new StringBuilder());
        currentDay = day;
    }

    public void acceptString(String line) {
        if (line.startsWith("--") && line.substring(2).matches("-?\\d+(\\.\\d+)?")) {
            int newCurEx = Integer.parseInt(line.substring(2));
            if (newCurEx != 0 & newCurEx - currentEx != 1) {
                throw new RuntimeException("Ошибка последовательности упражнений");
            }
            currentEx = newCurEx;
            return;
        }
        else if (line.startsWith("--")) {
            System.out.println("COMMENT FOUND: " + line);
            return;
        }
        if (exercises.size() - 1 != currentEx) {
            exercises.add(new StringBuilder());
        }
        exercises.get(currentEx).append(line).append(System.lineSeparator());
    }

    public void writeToFiles(String folder) {
        for (int i = 0; i < exercises.size(); i++) {
            Path currentFolderPath = Paths.get(folder)
                    .resolve(subfolder)
                    .resolve("ex" + String.format("%02d", i));
            File currentFolderFile = new File(currentFolderPath.toString());
            if (! currentFolderFile.exists()) {
                if (! currentFolderFile.mkdirs()) {
                    System.err.println("Cant create a dir " + currentFolderPath);
                    System.exit(-1);
                }
            }
            File currentFile = currentFolderFile.toPath()
                    .resolve("day" + String.format("%02d", currentDay) +
                            "_ex" + String.format("%02d", i) + ".sql")
                    .toFile();
            String curExercise = exercises.get(i).toString().trim();
            if (curExercise.length() < 5) {
                System.err.println("Warning!!! ex" + String.format("%02d", i) + " contains only "
                                + curExercise.length() + " symbols");
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
                writer.write(curExercise);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("File " + currentFile + " written");
        }
    }
}
