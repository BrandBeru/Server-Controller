package org.beru.ServerController.Model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import javafx.application.Platform;
import org.apache.commons.net.ftp.*;
import org.beru.ServerController.Controller.ApplicationController;
import org.beru.ServerController.View.Loading;

/**
 *
 * @author brand
 */
public class Ftp {

    public static boolean isConnected;
    public static FTPClient ftp = new FTPClient();
    public static FTPFile project = new FTPFile();

    public static boolean connect(CacheDatas data, String pass) {
        try {

            ftp.connect(data.getHost(), data.getPorts().get(0));

            isConnected = ftp.login(data.getUser(), pass);

            return isConnected;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isConnected;
    }

    public static boolean connect(String pass) {
        try {
            Properties properties = new Properties();
            properties.load(Ftp.class.getClassLoader().getResourceAsStream("Properties/DataConnection.properties"));

            String host = properties.getProperty("host");
            int port = Integer.parseInt(properties.getProperty("ftp_port"));

            String user = properties.getProperty("ftp_user");

            ftp.connect(host, port);

            isConnected = ftp.login(user, pass);

            System.out.println("Connected");

            return isConnected;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isConnected;
    }

    public static boolean createProject(String name, String remoteDirPath, String localParentDir, String remoteParentDir) {
        boolean done = false;
        try {
            File localDir = new File(localParentDir);

            if (ftp.makeDirectory(remoteDirPath)) {
                System.out.println("Created Template directory");
            }
            if (ftp.makeDirectory(remoteDirPath + "/" + name)) {
                System.out.println("Created Project directory");
            }

            File[] subFiles = localDir.listFiles();
            if (subFiles != null && subFiles.length > 0) {
                for (File item : subFiles) {
                    String remoteFilePath = remoteDirPath + "/" + name + "/" + remoteParentDir + "/" + item.getName();
                    if (remoteParentDir.equals("")) {
                        remoteFilePath = remoteDirPath + "/" + name + "/" + item.getName();
                    }
                    if (item.isFile()) {
                        String localFilePath = item.getAbsolutePath();
                        done = uploadSingleFile(localFilePath, remoteFilePath);
                    } else {
                        done = ftp.makeDirectory(remoteFilePath);

                        String parent = remoteParentDir + "/" + item.getName();
                        if (remoteParentDir.equals("")) {
                            parent = item.getName();
                        }
                        localParentDir = item.getAbsolutePath();
                        createProject(name, remoteDirPath, localParentDir, parent);
                    }
                }
            }

            return done;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean uploadSingleFile(String localFilePath, String remoteFilePath) {
        try {

            File file = new File(localFilePath);
            Platform.runLater(() -> {
                ApplicationController.instance.consoleOutputTXT.appendText(" |-> Sending File: " + file.getAbsolutePath() + "\n");
            });
            InputStream is = new FileInputStream(file);

            try {
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
                return ftp.storeFile(remoteFilePath, is);
            } finally {
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteProject(String path) {
        try {
            return ftp.removeDirectory(path);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean downloadProject(String remotePath) {
        boolean done = false;
        File baseDir = new File("B:\\");
        try {
            FTPFile[] ftpFiles = ftp.listFiles();
            for (FTPFile file : ftpFiles) {
                if (!file.getName().equals(".") && !file.getName().equals("..")) {
                    if (file.isDirectory()) {
                        ftp.changeWorkingDirectory(file.getName());
                        File newDir = new File(baseDir.getPath() + "\\" + ftp.printWorkingDirectory());
                        newDir.mkdirs();
                        downloadProject(ftp.printWorkingDirectory());
                        ftp.changeToParentDirectory();
                    } else {
                        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                        String remoteFile = ftp.printWorkingDirectory() + "/" + file.getName();
                        File downloadFile = new File(baseDir.getPath() + "\\" + ftp.printWorkingDirectory() + "\\" + file.getName());
                        
                        Platform.runLater(() -> {
                            ApplicationController.instance.errorLogLBL.setText("Downloading: " + downloadFile.getName());
                        });
                        new File(downloadFile.getParent()).mkdirs();
                        OutputStream os = new BufferedOutputStream(new FileOutputStream(downloadFile));
                        done = ftp.retrieveFile(remoteFile, os);
                        os.close();
                    }
                }
            }
            return done;
        } catch (IOException e) {
            e.printStackTrace();
            return done;
        }
    }

    public static FTPFile[] getFiles(String path) {
        try {
            return ftp.listFiles(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
