package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class Controller {

    private List<ImageProcessingJob> imageProcessingJobs = new ArrayList<>();

    @FXML
    private TableView itemsTable;

    @FXML private Label labelSelectedDirectory;
    @FXML private File selectedDirectory;
    @FXML private ComboBox comboBox;


    @FXML
    public void chooseFile(){
        FileChooser fileChooser = new FileChooser();
        //fileChooser.setInitialDirectory(new File("tutaj wpisac initial directory"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG images", "*.jpg"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

        for (File file : selectedFiles) {
            imageProcessingJobs.add(new ImageProcessingJob(file, "waiting...", 0));
        }

        ObservableList<ImageProcessingJob> tableRows = FXCollections.observableArrayList(imageProcessingJobs);
        itemsTable.setItems(tableRows);
    }

    @FXML
    private TableColumn<ImageProcessingJob, Double> progressCol;

    @FXML
    public void chooseDirectory(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        selectedDirectory = directoryChooser.showDialog(null);
        labelSelectedDirectory.setText(selectedDirectory.getAbsolutePath());
    }

    @FXML
    public void initialize() {
        progressCol.setCellFactory(ProgressBarTableCell.<ImageProcessingJob>forTableColumn());
    }

    @FXML
    public void processFiles() {
        String value = comboBox.getValue().toString();
        if("1 thread".equals(value)) {
            new Thread(this::backgroundJob).start();
        }
        else if("2 thread".equals(value)){
            ForkJoinPool pool = new ForkJoinPool(2);
            pool.submit(this::backgroundJob);
        }
        else if("4 threads".equals(value)){
            ForkJoinPool pool = new ForkJoinPool(4);
            pool.submit(this::backgroundJob);
        }
        else if("8 threads".equals(value)){
            ForkJoinPool pool = new ForkJoinPool(8);
            pool.submit(this::backgroundJob);
        }
        else if("common".equals(value)){
            ForkJoinPool pool = ForkJoinPool.commonPool();
            pool.submit(this::backgroundJob);
        }
    }

    private void backgroundJob(){

        String value = comboBox.getValue().toString();
        long start = System.currentTimeMillis();
        GreyscaleConverter converter = new GreyscaleConverter();

        if(value.equals("1 thread")) {
            imageProcessingJobs.stream().forEach(job -> converter.convertToGrey(selectedDirectory, job));
            long end = System.currentTimeMillis();
            long duration = end - start;
            System.out.println(duration);
        }
        else{
            imageProcessingJobs.parallelStream().forEach(job -> converter.convertToGrey(selectedDirectory, job));
            long end = System.currentTimeMillis();
            long duration = end - start;
            System.out.println(duration);
        }
    }
}
