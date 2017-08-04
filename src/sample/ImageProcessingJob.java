package sample;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.File;


public class ImageProcessingJob {
    public File file;
    public SimpleStringProperty status = new SimpleStringProperty();
    public DoubleProperty progress = new SimpleDoubleProperty();

    public ImageProcessingJob(File file, String status, double progress) {
        setFile(file);
        setProgress(progress);
        setStatus(status);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public double getProgress() {
        return progress.get();
    }

    public DoubleProperty progressProperty() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress.set(progress);
    }

    public String getFileName() {
        return getFile().getName();
    }

}
