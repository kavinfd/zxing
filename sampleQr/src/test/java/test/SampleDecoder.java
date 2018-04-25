/*
 * Copyright 2007 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.ImageReader;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import org.junit.Assert;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Sean Owen
 */
public final class SampleDecoder extends Assert {

  @Test
  public void testMask0() {
    File f = new File("/Users/kavin/test.png");
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
    Result result = null;
    QRCodeReader qrCodeReader = new QRCodeReader();
    try {
      result = qrCodeReader.decode(bitmap);
    } catch (NotFoundException | ChecksumException | FormatException e) {
      e.printStackTrace();
    }
    String res = result.getText();
    assertEquals(res, "QR CODE TEST");
  }

}
