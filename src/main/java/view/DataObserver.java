package view;

import java.awt.image.BufferedImage;

/**
 * DataObserver Interface
 */
public interface DataObserver
{
    /**
     * updates an image
     * @param image to be updated
     */
    void update(BufferedImage image);

    void updateScaling(int scaleFactor);
}
