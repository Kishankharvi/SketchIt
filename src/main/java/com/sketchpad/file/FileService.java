package com.sketchpad.file;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelReader;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

public class FileService{

  public static void save(Canvas canvas, File file) throws IOException{
    WritableImage fxImage =canvas.snapshot(null,null);
    PixelReader reader =fxImage.getPixelReader();
    int width=(int) fxImage.getWidth();
    int height=(int) fxImage.getHeight();
    BufferedImage image =new BufferedImage(
        width,height,BufferedImage.TYPE_INT_ARGB
        );
    for(int y=0;y<height;y++){
      for(int x=0;x<width;x++){
        image.setRGB(x,y,reader.getArgb(x,y));
      }
    }
    ImageIO.write(image,"png",file);
  }
}
