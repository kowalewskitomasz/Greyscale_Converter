package sample;

import javafx.application.Platform;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GreyscaleConverter {


    public void convertToGrey(File outputDir,ImageProcessingJob job) {
        try {
            job.setStatus("in progress...");
            BufferedImage original = ImageIO.read(job.file);
            BufferedImage grayscale = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
            for (int i = 0; i < original.getWidth(); i++) {

                for (int j = 0; j < original.getHeight(); j++) {
                    int red = new Color(original.getRGB(i, j)).getRed();
                    int green = new Color(original.getRGB(i, j)).getGreen();
                    int blue = new Color(original.getRGB(i, j)).getBlue();
                    int luminosity = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
                    int newPixel = new Color(luminosity, luminosity, luminosity).getRGB();
                    grayscale.setRGB(i, j, newPixel);
                }
                double progress = (1.0 + i) / original.getWidth();
                Platform.runLater(() -> job.progress.set(progress));
            }
            Path outputPath = Paths.get(outputDir.getAbsolutePath(), job.file.getName());
            ImageIO.write(grayscale, "jpg", outputPath.toFile());
            job.setStatus("done!");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
