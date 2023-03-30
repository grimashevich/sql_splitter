import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class Program {
     public static void main(String[] args) {
          Scanner scanner = new Scanner(System.in);
          System.out.println("Enter input SQL file path:");
          String inputFileName = scanner.nextLine().trim();
          System.out.println("Enter output repository folder:");
          String outPutFolder = scanner.nextLine().trim();
          scanner.close();


          File inputFile = new File(inputFileName);
          File outputFolderFile = new File(outPutFolder);
          if (! inputFile.exists()) {
               System.err.printf("File " + inputFileName + " does not exist");
               return;
          }

          if (! outputFolderFile.exists()) {
               System.err.println("Output folder does not exist");
               System.exit(-1);
          }
          int day = getBootcampDay(outPutFolder);
          ExerciseKeeper keeper = new ExerciseKeeper(day);
          try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile))) {
               String line;
               while ((line = bufferedReader.readLine()) != null) {
                    keeper.acceptString(line);
               }
          } catch (IOException e) {
               throw new RuntimeException(e);
          }

          keeper.writeToFiles(outPutFolder);

          Path sourceFilePath = inputFile.toPath();
          Path destFilePath = outputFolderFile.toPath().resolve("src")
                  .resolve(inputFile.toPath().getFileName());
          try {
               Files.copy(sourceFilePath,
                       destFilePath,
                       StandardCopyOption.REPLACE_EXISTING);
          } catch (IOException e) {
               throw new RuntimeException(e);
          }
          System.out.println(destFilePath + " saved");
     }

     private static int getBootcampDay(String folder) {
          Path subjectFilePath = Paths.get(folder).resolve("README.MD");
          if (! subjectFilePath.toFile().exists()) {
               System.err.println("Subject file (README.md) does not exist");
               System.exit(-1);
          }
          int day = -1;
          try (BufferedReader bufferedReader = new BufferedReader(
                  new FileReader(subjectFilePath.toFile()))) {
               String line = bufferedReader.readLine();
               if (line == null) {
                    System.err.println("Empty subject file");
                    System.exit(-1);
               }
               String s = "# Day 05 - Piscine SQL";
               day = Integer.parseInt(line.substring(6, 8));
          } catch (IOException e) {
               throw new RuntimeException(e);
          }
          return day;
     }
}


/*
/Users/eclown/Desktop/SQL_BOOTCAMP/GitHub/Day03/day03.sql
/Users/eclown/Desktop/111/3DAYtest
*/