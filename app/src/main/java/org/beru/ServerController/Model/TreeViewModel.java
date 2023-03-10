/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.beru.ServerController.Model;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.Vector;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.beru.ServerController.Controller.ApplicationController;
import org.beru.ServerController.Controller.ApplicationController.GetFiles;

/**
 *
 * @author brand
 */
public class TreeViewModel {

    private final Node folderIcon = new ImageView(
            new Image(getClass().getResourceAsStream("/Icons/folder.png"), 16, 16, false, false)
    );
    private final Node fileIcon = new ImageView(
            new Image(getClass().getResourceAsStream("/Icons/file.png"), 16, 16, false, false)
    );

    public TreeItem<GetFiles> addNodes(TreeItem<GetFiles> rootItem, File dir) {
        String curPath = dir.getPath();

        String type;
        if (dir.isDirectory()) {
            type = "Directory";
        } else {
            type = "File";
        }

        ApplicationController ap = new ApplicationController();
        GetFiles getFiles = ap.new GetFiles(dir.getName(), dir.getPath(), type);
        TreeItem<GetFiles> curDir = new TreeItem<GetFiles>(getFiles);

        if (rootItem != null) {
            rootItem.getChildren().add(curDir);
        }
        Vector ol = new Vector();
        String[] tmp = dir.list();
        if (tmp != null) {
            for (int i = 0; i < tmp.length; i++) {
                ol.addElement(tmp[i]);
            }
            Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
            File f;
            Vector files = new Vector();

            for (int i = 0; i < ol.size(); i++) {
                String thisObject = (String) ol.elementAt(i);
                String newPath;
                if (curPath.equals(".")) {
                    newPath = thisObject;
                } else {
                    newPath = curPath + File.separator + thisObject;
                }
                if ((f = new File(newPath)).isDirectory()) {
                    addNodes(curDir, f);
                } else {
                    files.addElement(thisObject);
                }

            }
            return curDir;
        } else {
            return null;
        }
    }

    public File[] getFilsInPath(String path) {
        File file = new File(path);
        return file.listFiles();
    }

    public static TreeItem<GetFiles> getFiles(String globalPath) {
        Path pp = Paths.get(globalPath);

        ApplicationController ap = new ApplicationController();

        TreeItem<GetFiles> root = new TreeItem<>(ap.new GetFiles("Projects (B:)", "B:\\", "Disk"));
        try {
            Set<FileVisitOption> fileVisitOptions = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
            Files.walkFileTree(pp, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {                    
//                    String type = "Directory";
//                    GetFiles getFiles = ap.new GetFiles(dir.toFile().getPath(), dir.toFile().getAbsolutePath(), type);
//                    TreeItem<GetFiles> curDir = new TreeItem<GetFiles>(getFiles);
//                    root.getChildren().add(curDir);
                    
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    
                    return super.visitFile(pp, attrs);
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return super.visitFileFailed(file, exc);
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return super.postVisitDirectory(pp, exc);
                }
            });
            return root;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
