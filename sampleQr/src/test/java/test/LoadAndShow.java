package test;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.ImageReader;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.detector.Detector;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class LoadAndShow extends JPanel {
  BufferedImage image;
  Dimension size = new Dimension();

  public LoadAndShow(BufferedImage image) {
    this.image = image;
    size.setSize(image.getWidth(), image.getHeight());
  }

  /**
   * Drawing an image can allow for more
   * flexibility in processing/editing.
   */
  protected void paintComponent(Graphics g) {
    // Center image in this component.
    int x = (getWidth() - size.width) / 2;
    int y = (getHeight() - size.height) / 2;
    g.drawImage(image, x, y, this);
  }

  public Dimension getPreferredSize() {
    return size;
  }

  private static void showImage(BufferedImage bf) {
    LoadAndShow test = new LoadAndShow(bf);
    JFrame f = new JFrame();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.add(new JScrollPane(test));
    f.setSize(600, 600);
    f.setLocation(200, 200);
    f.setVisible(true);
  }

  public static void main(String[] args) throws IOException {
    File f = new File("/Users/kavin/test2.png");
    Path file = f.toPath();
    BufferedImage image;
    try {
      image = ImageReader.readImage(file.toUri());
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
    LuminanceSource source = new BufferedImageLuminanceSource(image);
    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
    BufferedImage bufferedImage = null;
    try {
      bufferedImage = MatrixToImageWriter.toBufferedImage(bitmap.getBlackMatrix());
    } catch (NotFoundException e) {
      e.printStackTrace();
    }
    showImage(bufferedImage);
    Result result = null;
    QRCodeReader qrCodeReader = new QRCodeReader();
    try {
      result = qrCodeReader.decode(bitmap);
    } catch (NotFoundException | ChecksumException | FormatException e) {
      e.printStackTrace();
    }
    DetectorResult detectorResult = null;
    try {
      detectorResult = new Detector(bitmap.getBlackMatrix()).detect();
    } catch (NotFoundException | FormatException e) {
      e.printStackTrace();
    }
//    String res = result.getText();
    //showIcon(image);
  }

  /**
   * Easy way to show an image: load it into a JLabel
   * and add the label to a container in your gui.
   */
  private static void showIcon(BufferedImage image) {
    ImageIcon icon = new ImageIcon(image);
    JLabel label = new JLabel(icon, JLabel.CENTER);
    JOptionPane.showMessageDialog(null, label, "icon", -1);
  }
}
