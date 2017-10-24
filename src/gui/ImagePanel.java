package gui;

import boofcv.alg.filter.binary.GThresholdImageOps;
import boofcv.core.image.ConvertImage;
import boofcv.gui.binary.VisualizeBinaryData;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
    private BufferedImage img;
    private String path;
    private int threshold;
    
    public static final int SUCCESS = 1;
    public static final int FAILURE = 0;
    
    public int setImage(BufferedImage img) {
        if(img == null) {
            JOptionPane.showConfirmDialog(
                    null, 
                    "Please insert a path", 
                    "Image selection error", 
                    JOptionPane.ERROR_MESSAGE);
            return FAILURE;
        }
        
        this.img = img;
        return SUCCESS;
    }
    
    public int setImage() {
        if(path == null) {
            JOptionPane.showConfirmDialog(
                    null, 
                    "Please insert a path", 
                    "Image selection error", 
                    JOptionPane.ERROR_MESSAGE);
            return FAILURE;
        }
        else {
            try {
                img = ImageIO.read(new File(path));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                    null, 
                    "Please select an image", 
                    "Image selection error", 
                    JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(
                        ImagePanel.class.getName()).log(Level.SEVERE, null, ex);
                return FAILURE;
            }
        }
        return SUCCESS;
    }
    
    public BufferedImage getImage() {
        return img;
    }
    
    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    public BufferedImage umbralizar(BufferedImage image, int threshold) {
        // Convierte la imagen en color Buffered Image en formato de la librería BoofCV
        Planar<GrayU8> colorImage = ConvertBufferedImage.convertFromPlanar(
                image, 
                null, 
                true, 
                GrayU8.class);

        // Crea dos imágenes en niveles de grises
        GrayU8 grayImage = new GrayU8(image.getWidth(), image.getHeight());
        GrayU8 thresholdImage = new GrayU8(image.getWidth(), image.getHeight());
        
        // Convierte a niveles de gris la imagen de entrada
        ConvertImage.average(colorImage, grayImage);
        
        // Umbraliza la imagen:
        //  - Píxeles con nivel de gris > Umbral se pone a 0
        //  - Píxeles con nivel de gris <= Umbral se ponen a 1
        GThresholdImageOps.threshold(grayImage, thresholdImage, threshold, false);

        // Se devuelve la imagen umbralizada en formato BufferedImage
        return VisualizeBinaryData.renderBinary(thresholdImage, false, null);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
    }
    
    public void resetComponent(Graphics g) {
        g.finalize();
        super.paintComponent(g);
        img = null;
        path = null;
        threshold = 0;
    }
}
