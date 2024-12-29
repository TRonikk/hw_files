import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {

        // Установка
        // StringBuilder для сохранения логов о создании каталогов и файлов
        StringBuilder logs = new StringBuilder();

        // Создадим директории в папке Games
        File srcDir = new File("D://Java programming//Netology//Games", "src");
        createDirectory(srcDir, logs);

        File resDir = new File("D://Java programming//Netology//Games", "res");
        createDirectory(resDir, logs);

        File saveGamesDir = new File("D://Java programming//Netology//Games", "savegames");
        createDirectory(saveGamesDir, logs);

        File tempDir = new File("D://Java programming//Netology//Games", "temp");
        createDirectory(tempDir, logs);

        // Создадим в каталоге src две директории
        File mainDir = new File(srcDir, "main");
        createDirectory(mainDir, logs);

        File testDir = new File(srcDir, "test");
        createDirectory(testDir, logs);

        // Создадим в подкаталоге main два файла
        File mainFile = new File(mainDir, "Main.java");
        createFile(mainFile, logs);

        File utilsFile = new File(mainDir, "Utils.java");
        createFile(utilsFile, logs);

        // Создадим в каталоге res три директории
        File drawablesDir = new File(srcDir, "drawables");
        createDirectory(drawablesDir, logs);

        File vectorsDir = new File(srcDir, "vectors");
        createDirectory(vectorsDir, logs);

        File iconsDir = new File(srcDir, "icons");
        createDirectory(iconsDir, logs);

        // В директории temp создадим файл temp.txt
        File tempFile = new File(tempDir, "temp.txt");
        createFile(tempFile, logs);

        // Запишем информацию о создании файлов и каталогов в файл temp.txt
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
            bw.write(logs.toString());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        // Сохранение
        // Создадим три экземпляра класса GameProgress
        GameProgress gameProgress1 = new GameProgress(100, 3, 1, 0.0);
        GameProgress gameProgress2 = new GameProgress(80, 3, 2, 50.0);
        GameProgress gameProgress3 = new GameProgress(60, 2, 3, 100.0);

        // Соранием сериализованные объекты GameProgress в папку savegames
        File gameProgressFile1 = new File(saveGamesDir, "game_progress_1.dat");
        createFile(gameProgressFile1, logs);
        File gameProgressFile2 = new File(saveGamesDir, "game_progress_2.dat");
        createFile(gameProgressFile2, logs);
        File gameProgressFile3 = new File(saveGamesDir, "game_progress_3.dat");
        createFile(gameProgressFile3, logs);

        saveGame(gameProgressFile1.getAbsolutePath(), gameProgress1);
        saveGame(gameProgressFile2.getAbsolutePath(), gameProgress2);
        saveGame(gameProgressFile3.getAbsolutePath(), gameProgress3);

        // Созданные файлы сохранений из папки savegames запакуем в один архив zip
        List<String> listOfFilesPaths = new ArrayList<>();
        listOfFilesPaths.add(gameProgressFile1.getAbsolutePath());
        listOfFilesPaths.add(gameProgressFile2.getAbsolutePath());
        listOfFilesPaths.add(gameProgressFile3.getAbsolutePath());

        // Архивируем файлы
        zipFiles(new File(saveGamesDir, "zip.zip").getAbsolutePath(), listOfFilesPaths);

        for (String path : listOfFilesPaths) {
            File file = new File(path);
            if (file.delete()) {
                logs.append("Deleted file: ").append(file.getName()).append("\n");
            } else {
                logs.append("Failed to delete file: ").append(file.getName()).append("\n");
            }
        }
        System.out.println(logs);
    }

    public static void createDirectory(File directory, StringBuilder logs) {
        if (directory.mkdir()) {
            logs.append("Directory \"").append(directory.getName()).append("\" created!\n");
        } else {
            logs.append("Directory \"").append(directory.getName()).append("\" is not created.\n");
        }
    }

    public static void createFile(File file, StringBuilder logs) {
        try {
            if (file.createNewFile()) {
                logs.append("File \"").append(file.getName()).append("\" created!\n");
            } else {
                logs.append("File \"").append(file.getName()).append("\" is not created.\n");
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void saveGame(String filePath, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipFiles(String zipPath, List<String> listOfFilesPaths) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipPath))) {
            for (String filePath : listOfFilesPaths) {
                File file = new File(filePath);
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry entry = new ZipEntry(file.getName());
                    zout.putNextEntry(entry);

                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);

                    zout.write(buffer);

                    zout.closeEntry();
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
