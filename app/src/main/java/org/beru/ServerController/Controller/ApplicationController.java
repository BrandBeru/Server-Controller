/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.beru.ServerController.Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.beru.ServerController.App;
import org.beru.ServerController.Model.CacheDatas;
import org.beru.ServerController.Model.JsonController;
import org.beru.ServerController.Model.Ftp;
import static org.beru.ServerController.Model.Ftp.ftp;
import org.beru.ServerController.Model.Encypter;
import org.beru.ServerController.Model.ProjectInfo;
import org.beru.ServerController.Model.Sql;
import org.beru.ServerController.Model.TreeViewModel;
import org.beru.ServerController.View.Messages;

/**
 *
 * @author brand
 */
public class ApplicationController implements Initializable {

    @FXML
    private TextField hostTXT;
    @FXML
    private TextField userTXT;
    @FXML
    private TreeView<GetFiles> filesTreeView;
    @FXML
    private VBox pnlRemoteFolders;
    @FXML
    private Label pathLBL;
    @FXML
    private AnchorPane pnlRemoteFiles;

    private TreeViewModel tvModel = new TreeViewModel();
    @FXML
    private TableColumn<GetFiles, String> nameColumnFiles;
    @FXML
    private TableColumn<GetFiles, String> pathColumnFiles;
    @FXML
    private TableColumn<GetFiles, String> typeColumnFiles;
    @FXML
    private TableView<GetFiles> filesTable;
    @FXML
    private TextField consoleTXT;
    @FXML
    private Label consolePathLBL;
    @FXML
    public TextArea consoleOutputTXT;
    @FXML
    public Label errorLogLBL;
    @FXML
    public ProgressIndicator status;
    @FXML
    private Circle ftp_connect;
    @FXML
    private Circle bd_connect;
    @FXML
    private MenuItem recentConnection;

    private String recentCon;

    public static String pSelected;
    @FXML
    private Circle ssh_connect;
    @FXML
    private TextArea queryTXT;
    @FXML
    private VBox numbers;
    @FXML
    private HBox queryContent;
    @FXML
    private HBox queryContent1;
    @FXML
    private TabPane tabPane;
    @FXML
    private TabPane editorTabs;
    @FXML
    private ContextMenu cacheConnectionsContext;

    @FXML
    private AnchorPane remoteStatus;

    public static CacheDatas datas;

    public static ApplicationController instance;
    @FXML
    private SplitPane remoteServerSplit;
    @FXML
    private VBox loginPNL;
    @FXML
    private TextField ftp_portTXT;
    @FXML
    private TextField sql_portTXT;
    @FXML
    private TextField ssh_portTXT;
    @FXML
    private PasswordField passwordTXT;
    @FXML
    private TextField conNameTXT;
    @FXML
    private ContextMenu cacheConnectionsName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        TreeItem root = new TreeItem("Projects (B:)");
//        root.getChildren().add(tvModel.addNodes(root, new File("B:\\")));    
//        filesTreeView.setRoot(root);

        filesTreeView.setRoot(TreeViewModel.getFiles("B:"));

        queryText();

        instance = this;

        contextMenuDatas();
    }

    private void contextMenuDatas() {
        if (JsonController.get() != null) {
            for (CacheDatas data : JsonController.get()) {
                MenuItem host = new MenuItem(data.getHost());
                cacheConnectionsContext.getItems().add(host);

                MenuItem name = new MenuItem(data.getConnection_name());
                cacheConnectionsName.getItems().add(name);

                host.setOnAction((t) -> {
                    datas = data;
                    recentCon(null);
                });
                name.setOnAction((t) -> {
                    datas = data;
                    recentCon(null);
                });
            }
        }
    }

    public int queryText() {
        numbers.getChildren().clear();
        int lines = (queryTXT.getText().split("\n").length);
        for (int i = 0; i < lines; i++) {
            Label l = new Label(String.valueOf((i + 1)));
            l.setId("number");
            numbers.getChildren().add(l);
        }
        return lines;
    }

    //"192.168.0.103", "projects", "Beru_Projects", 21
    @FXML
    private void Connection(ActionEvent event) {
        Task t = new Task() {
            @Override
            protected Object call() throws Exception {
                String pass = passwordTXT.getText();
                if (!pass.equals("")) {
                    try {
                        Ftp.connect(datas, pass);
                        Sql.mysql_connect(datas, pass);
                        Platform.runLater(() -> {
                            try {
                                infoCon();
                            } catch (SQLException ex) {
                                Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });

                        boolean exists = false;
                        for (int i = 0; i < cacheConnectionsContext.getItems().size(); i++) {
                            exists = hostTXT.getText().equals(JsonController.get().get(i).getHost());
                            if (exists) {
                                break;
                            }
                        }
                        if (!exists) {
                            setCacheDatas(conNameTXT.getText(), pass);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    errorLogLBL.setText("All values are required");
                }

                return null;
            }
        };
        t.setOnRunning((success) -> {
            status.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
            errorLogLBL.setText("Connecting to remote server...");
        });
        t.setOnSucceeded((success) -> {
            status.setProgress(1);
            errorLogLBL.setText("Connected");
            contextMenuDatas();
        });
        t.setOnFailed((failed) -> {
            status.setProgress(0);
            errorLogLBL.setText("¡Error occurred while connect with the server!");
        });

        new Thread(t).start();

    }

    private void setCacheDatas(String name, String pass) {
        CacheDatas d = new CacheDatas();
        d.setConnection_name(name);
        d.setHost(hostTXT.getText());
        d.setUser(userTXT.getText());
        List<Integer> ports = new ArrayList<>();
        int ftp = (!ftp_portTXT.getText().equals("")) ? Integer.parseInt(ftp_portTXT.getText()) : 21;
        int sql = (!sql_portTXT.getText().equals("")) ? Integer.parseInt(sql_portTXT.getText()) : 3306;
        int ssh = (!ssh_portTXT.getText().equals("")) ? Integer.parseInt(ssh_portTXT.getText()) : 22;
        ports.add(ftp);
        ports.add(sql);
        ports.add(ssh);
        d.setPorts(ports);

        System.out.println("Host: " + d.getHost());

        datas = d;

        System.out.println("Data: " + datas);
        System.out.println("D: " + d);

        try {
            if (Ftp.isConnected && !Sql.con.isClosed()) {
                JsonController.set(d);
            } else {
                Messages.info("Connection is closed", "Cannot stablished the connection");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void infoCon() throws SQLException {

        int con = 0;
        con = ((Ftp.isConnected ? 2 : 0) + (!Sql.con.isClosed() ? 1 : 0));
        pnlRemoteFolders.getChildren().clear();
        switch (con) {
            case 3:
                bd_connect.setFill(Color.BLUE);
                ftp_connect.setFill(Color.BLUE);
                updateRemoteFiles();
                break;
            case 2:
                bd_connect.setFill(Color.RED);
                ftp_connect.setFill(Color.BLUE);
                break;
            case 1:
                bd_connect.setFill(Color.BLUE);
                ftp_connect.setFill(Color.RED);
                break;
            default:
                bd_connect.setFill(Color.RED);
                ftp_connect.setFill(Color.RED);
                break;
        }
    }

    @FXML
    private void recentCon(ActionEvent event) {
        try {
            conNameTXT.setText(datas.getConnection_name());
            hostTXT.setText(datas.getHost());
            userTXT.setText(datas.getUser());

            ftp_portTXT.setText("" + datas.getPorts().get(0));
            sql_portTXT.setText("" + datas.getPorts().get(1));
            ssh_portTXT.setText("" + datas.getPorts().get(2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateRemoteFiles() {
        pnlRemoteFolders.getChildren().clear();
        try {
            loadRemotePanelsInfo();
            loadProject();
            remoteServerSplit.getItems().remove(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProject() {
        pnlRemoteFolders.getChildren().clear();
        try {
            for (ProjectInfo info : Sql.getProjects()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ProjectInfo.fxml"));

                //Project info
                ProjectInfoController con = new ProjectInfoController(info);
                pSelected = con.p.getName();
                loader.setController(con);
                TitledPane p = loader.load();
                pnlRemoteFolders.getChildren().add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadRemotePanelsInfo() {
        remoteStatus.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/FtpPanelStatus.fxml"));
            VBox ftppane = loader.load();
            remoteStatus.getChildren().add(ftppane);

            loader = new FXMLLoader(getClass().getResource("/Views/SqlPanelStatus.fxml"));
            VBox sqlPane = loader.load();
            remoteStatus.getChildren().add(sqlPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeProjectSelected(FTPFile path) {
        islocal = false;
        TreeViewModel modelView = new TreeViewModel();
        ObservableList<GetFiles> filesList = FXCollections.observableArrayList();
        for (FTPFile file : Ftp.getFiles(path.getName())) {
            if (file.isDirectory()) {
                filesList.add(new GetFiles(file.getName(), path.getName() + file.getName(), "Directory"));
            } else {
                filesList.add(new GetFiles(file.getName(), file.getLink(), "File"));
            }
        }
        nameColumnFiles.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        pathColumnFiles.setCellValueFactory(new PropertyValueFactory<>("filePath"));
        typeColumnFiles.setCellValueFactory(new PropertyValueFactory<>("fileType"));

        filesTable.getColumns().clear();
        filesTable.getColumns().addAll(nameColumnFiles);
        filesTable.getColumns().addAll(pathColumnFiles);
        filesTable.getColumns().addAll(typeColumnFiles);

        filesTable.setItems(filesList);
    }

    private Button remoteFileBTN(FTPFile file) {
        Button btn = new Button(file.getName());

        btn.setPrefWidth(120);
        btn.setPrefHeight(120);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                changeProjectSelected(file);
            }
        });

        return btn;
    }
    private boolean islocal;
    private String globalPath = "C:\\";

    private List<String> path = new ArrayList<>();
    private int id = 0;

    @FXML
    private void setPath(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (event.getClickCount() == 1) {
                islocal = true;

                TreeItem<GetFiles> name = filesTreeView.getSelectionModel().getSelectedItem();
                changePath(name.getValue().getFilePath());

            }
        }
    }

    private void changePath(String name) {
        if ((path.size()) != id) {
            path.set(id, name);
        } else {
            path.add(name);
        }
        actPath = new File(name);
        globalPath = name;
        pathLBL.setText(globalPath);
        consolePathLBL.setText(actPath.getName());
        changeTableViewPath(name);

        id++;
    }

    public void changeTableViewPath(String path) {

        TreeViewModel modelView = new TreeViewModel();
        ObservableList<GetFiles> filesList = FXCollections.observableArrayList();
        for (File file : modelView.getFilsInPath(path)) {
            if (file.isDirectory()) {
                filesList.add(new GetFiles(file.getName(), file.getPath(), "Directory"));
            } else {
                filesList.add(new GetFiles(file.getName(), file.getPath(), "File"));
            }
        }

        pathLBL.setText(globalPath);
        nameColumnFiles.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        pathColumnFiles.setCellValueFactory(new PropertyValueFactory<>("filePath"));
        typeColumnFiles.setCellValueFactory(new PropertyValueFactory<>("fileType"));

        filesTable.getColumns().clear();
        filesTable.getColumns().addAll(nameColumnFiles);
        filesTable.getColumns().addAll(pathColumnFiles);
        filesTable.getColumns().addAll(typeColumnFiles);

        filesTable.setItems(filesList);
        filesTable.getSelectionModel().getSelectedCells();

    }
    private File actPath;

    @FXML
    private void itemSelection(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (event.getClickCount() == 2) {
                File file = new File(filesTable.getSelectionModel().getSelectedItem().filePath);
                if (file.isDirectory()) {
                    actPath = file;
                    changePath(file.getAbsolutePath());
                }
            }
        }
    }

    ProcessBuilder process;

    private void executeComand(String command) {
        try {
            List<String> commands = new ArrayList<String>();
            String[] splitCommands = command.split(" ");
            commands.add(0, "");
            for (int i = 0; i < splitCommands.length; i++) {
                commands.add(i + 1, splitCommands[i]);
                System.out.println("commands: " + splitCommands[i]);
            }
            if (command.equalsIgnoreCase("clear")) {
                consoleOutputTXT.setText("");
            } else if (commands.get(1).equalsIgnoreCase("cd")) {
                globalPath = commands.get(2);

                path.add(globalPath);
                id++;
                changeTableViewPath(globalPath);
            } else if (commands.get(1).equalsIgnoreCase("create")) {
                status.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
                boolean remote = commands.get(2).equalsIgnoreCase("remote");
                LocalDate d = LocalDate.now();
                Task t = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        try {
                            if (Ftp.ftp.isConnected()) {
                                String name = commands.get(5);
                                String pass = commands.get(6);
                                String template = commands.get(3);
                                String type = commands.get(4);
                                String remoteDirPath = template + "/";
                                String localDirPath = "B:\\Templates\\" + template + "\\" + type;
                                boolean query = Sql.createProject(name, Encypter.encrypt(pass), template, remote, d, remoteDirPath + name + "/");
                                if (query) {

                                    query = Ftp.createProject(name, remoteDirPath, localDirPath, "");
                                }
                                String create = (query) ? "Sucessfull" : "Failed";
                                consoleOutputTXT.appendText("Project creation: " + create + "\n");

                                updateRemoteFiles();
                            } else {
                                consoleOutputTXT.appendText("ERROR LOG -> FTP Connection failed\n");
                            }
                        } catch (IndexOutOfBoundsException e) {
                            consoleOutputTXT.appendText("ERROR LOG -> Project creation: *empty arguments*" + "\n");
                        }

                        return null;
                    }
                };
                t.setOnSucceeded((succeded) -> {
                    Platform.runLater(() -> {
                        status.setProgress(1);
                        errorLogLBL.setText("Project created: " + commands.get(5));
                    });
                });
                new Thread(t).start();

            } else if (commands.get(1).equalsIgnoreCase("remove")) {
                if (Ftp.ftp.isConnected()) {
                    String name = commands.get(2);
                    String path = Sql.getProjectInfo(name, "path");

                    boolean sqlDone = Sql.deleteProject(name);
                    boolean ftpDone = Ftp.deleteProject(path);

                    consoleOutputTXT.appendText("Deleting project: " + name + " status -> " + (sqlDone && ftpDone) + "\n");

                    loadProject();
                }
            } else {

                changeIndicator(-1);
                commands.add(0, "powershell");

                process = new ProcessBuilder(commands);

                process.redirectErrorStream(false);
                process.directory(new File(globalPath));
                Process p = process.start();

                BufferedReader input = new BufferedReader(p.inputReader());
                String line = null;

                consoleOutputTXT.appendText(consoleTXT.getText() + "-> ");
                while ((line = input.readLine()) != null) {
                    consoleOutputTXT.appendText(line + "\n");
                }
                BufferedReader error = new BufferedReader(p.errorReader());
                line = null;
                errorLogLBL.setText("No error");
                while ((line = error.readLine()) != null) {
                    errorLogLBL.setText(line);
                    break;
                }
                changeIndicator(1);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String message;

    private void changeIndicator(double val) {
        status.setProgress(val);
    }

    @FXML
    private void setCommand(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            executeComand(consoleTXT.getText());
            consolePathLBL.setText(globalPath);

            consoleTXT.setText("");
        }
    }

    @FXML
    private void back(ActionEvent event) {
        if (id > 0) {
            id--;
        } else {
            return;
        }
        globalPath = path.get(id - 1);
        pathLBL.setText(globalPath);
        changeTableViewPath(globalPath);
        consolePathLBL.setText(globalPath);
    }

    @FXML
    private void forward(ActionEvent event) {
        if (id > path.size() - 1) {
            return;
        } else {
            id++;
        }
        globalPath = path.get(id - 1);
        pathLBL.setText(globalPath);
        changeTableViewPath(globalPath);
        consolePathLBL.setText(globalPath);
    }

    @FXML
    private void NewProject(ActionEvent event) {
        consoleTXT.setText("create remote project java");
        consoleTXT.requestFocus();
    }

    @FXML
    private void Open(ActionEvent event) {
        Task t = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Properties properties = new Properties();
                    properties.load(ApplicationController.class.getClassLoader().getResourceAsStream("Properties/Paths.properties"));

                    FileChooser fc = new FileChooser();
                    fc.setInitialDirectory(new File(properties.getProperty("last")));
                    File f = fc.showOpenDialog(null);

                    if (f != null) {
                        properties.setProperty("last", f.getAbsolutePath());
                        String p = f.getPath();
                        Platform.runLater(() -> {
                            consoleOutputTXT.appendText(" |-> Sending: " + p + "\n");
                        });
                        ftp.makeDirectory("~/.tools");

                        InputStream is = new FileInputStream(p);

                        ftp.setFileType(FTP.BINARY_FILE_TYPE);
                        ftp.storeFile("~/.tools/" + f.getName(), is);

                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        t.setOnRunning((running) -> {
            status.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
            errorLogLBL.setText("Uploading file...");
        });
        t.setOnSucceeded((succeded) -> {
            status.setProgress(1);
            errorLogLBL.setText("Done.");
        });
    }

    @FXML
    private void Delete(ActionEvent event) {
        consoleTXT.setText("remove " + pSelected);
        consoleTXT.requestFocus();
    }

    @FXML
    private void close(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void disconnect(ActionEvent event) {
        try {
            consoleOutputTXT.appendText("FTP -> Status: DISCONNECTED" + "\n");
            consoleOutputTXT.appendText("BD -> Status: DISCONNECTED" + "\n");
            bd_connect.setFill(Color.RED);
            ftp_connect.setFill(Color.RED);
            pnlRemoteFolders.getChildren().clear();

            AnchorPane pnl = (AnchorPane) remoteServerSplit.getItems().get(0);
            pnl.getChildren().clear();
            remoteServerSplit.getItems().add(0, loginPNL);

            Ftp.ftp.disconnect();
            Sql.con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void maximized(ActionEvent event) {
        App.stage.setFullScreen(!App.stage.isFullScreen());
        App.stage.setAlwaysOnTop(!App.stage.isFullScreen());
    }

    @FXML
    private void changeTextQuery(KeyEvent event) {
        int jumps = queryText() * 25;
        if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.BACK_SPACE) {
            codeTextJumps(jumps);
        }
    }

    private void codeTextJumps(int jumps) {
        queryTXT.setPrefHeight(jumps);
        queryContent.setPrefHeight(jumps);
    }

    @FXML
    private void minimized(ActionEvent event) {
        App.stage.toBack();
    }

    @FXML
    private void openLocalFile(ActionEvent event) {
        try {
            File fselected = new File(filesTable.getSelectionModel().getSelectedItem().filePath);

            System.out.println(fselected.getAbsolutePath());
            if (fselected.isFile()) {
                queryTXT.setText("");
                tabPane.getSelectionModel().selectLast();
                editorTabs.getSelectionModel().getSelectedItem().setText(fselected.getName());

                BufferedReader r = new BufferedReader(new FileReader(fselected));
                String curLine;
                while ((curLine = r.readLine()) != null) {
                    queryTXT.appendText(curLine + "\n");
                }
                int jumps = queryText() * 25;
                codeTextJumps(jumps);
                r.close();
            } else {
                actPath = fselected;
                changePath(fselected.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void renameLocalFile(ActionEvent event) {
        File fselected = new File(filesTable.getSelectionModel().getSelectedItem().filePath);
        filesTable.setEditable(true);
        int row = filesTable.getSelectionModel().getSelectedIndex();
    }

    @FXML
    private void DeleteLocalFile(ActionEvent event) {
    }

    @FXML
    private void refreshProjects(ActionEvent event) {
        loadProject();
    }

    public class GetFiles {

        private String fileName;
        private String filePath;
        private String fileType;

        public GetFiles(String fileName, String filePath, String fileType) {
            super();
            this.fileName = fileName;
            this.filePath = filePath;
            this.fileType = fileType;
        }

        public GetFiles() {

        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String toString() {
            return getFileName();
        }
    }

}
