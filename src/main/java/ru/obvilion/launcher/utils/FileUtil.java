package ru.obvilion.launcher.utils;

import javafx.application.Platform;
import ru.obvilion.launcher.config.Vars;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtil {
    public static void downloadFromURL(String url, Path target) {
        try {
            URL website = new URL(url);
            try (InputStream in = website.openStream()) {
                System.out.println("Downloading file: " + url);

                Platform.runLater(() -> {
                    Vars.loadingController.now.setText(Lang.get("loading") + Lang.get("downloading").replace("{0}", url));
                });

                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downloadFromURL2(String url, Path target) {
        try {
            URL website = new URL(url);
            try (InputStream in = website.openStream()) {
                System.out.println("Downloading file: " + url);

                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long folderSize(File directory) {
        long length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file);
        }
        return length;
    }

    public static void downloadAndUnpackFromURL(String url, Path target) {
        try {
            URL website = new URL(url);
            try (InputStream in = website.openStream()) {
                System.out.println("Downloading file: " + url);
                Platform.runLater(() -> {
                    Vars.loadingController.now.setText(Lang.get("loading") + Lang.get("downloading").replace("{0}", url));
                });

                Files.copy(in, new File(target.toFile(), "temp.zip").toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Unzip file: " + url);
            Platform.runLater(() -> {
                Vars.loadingController.now.setText(Lang.get("loading") + Lang.get("unzip").replace("{0}", url));
            });
            unzip(new File(target.toFile(), "temp.zip"), target.toFile());
        }
    }

    public static void unzip(File zip, File dest) {
        ZipFile zipFile = null;

        try {

            zipFile = new ZipFile(zip);

            Enumeration<? extends ZipEntry> e = zipFile.entries();

            while (e.hasMoreElements()) {

                ZipEntry entry = e.nextElement();

                File destinationPath = new File(dest, entry.getName());

                destinationPath.getParentFile().mkdirs();

                if (!entry.isDirectory()) {

                    System.out.println("Extracting file: " + destinationPath);
                    Platform.runLater(() -> {
                        Vars.loadingController.now.setText(Lang.get("loading") + Lang.get("unzipFile").replace("{0}", destinationPath.getAbsolutePath()));
                    });

                    BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));

                    int b;
                    byte buffer[] = new byte[8192];

                    FileOutputStream fos = new FileOutputStream(destinationPath);

                    BufferedOutputStream bos = new BufferedOutputStream(fos, 8192);

                    while ((b = bis.read(buffer, 0, 8192)) != -1) {
                        bos.write(buffer, 0, b);
                    }

                    bos.close();
                    bis.close();
                }
            }

        } catch (IOException ioe) {
            System.out.println("Error opening zip file" + ioe);
        } finally {
            try {
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (IOException ioe) {
                System.out.println("Error while closing zip file" + ioe);
            }
        }
    }

    public static void deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                File f = new File(dir, children[i]);
                deleteDirectory(f);
            }
            dir.delete();
        } else dir.delete();
    }
}
