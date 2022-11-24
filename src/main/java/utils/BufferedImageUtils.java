package utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;

public final class BufferedImageUtils
{
    public static Image convertToFxImage(final BufferedImage bufferedImage)
    {
        return new ImageView(convertToWritableImage(bufferedImage)).getImage();
    }

    public static WritableImage convertToWritableImage(final BufferedImage bufferedImage)
    {
        WritableImage writableImage = null;
        if (bufferedImage != null)
        {
            writableImage = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
            PixelWriter pixelWriter = writableImage.getPixelWriter();
            for (int x = 0; x < bufferedImage.getWidth(); x++)
            {
                for (int y = 0; y < bufferedImage.getHeight(); y++)
                {
                    pixelWriter.setArgb(x, y, bufferedImage.getRGB(x, y));
                }
            }
        }
        return writableImage;
    }
}
