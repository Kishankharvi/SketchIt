package com.sketchpad.canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.WritableImage;
import com.sketchpad.undo.UndoManager;


public class DrawingCanvas{
  private final Canvas canvas;
  private final GraphicsContext gc;
  private final UndoManager undoManager;

  public DrawingCanvas(double width, double height){
    canvas =new Canvas(width,height);
    gc=canvas.getGraphicsContext2D();
    undoManager =new UndoManager();
    clear();
gc.setLineWidth(2);
  
  
    canvas.setOnMousePressed(e->{
    undoManager.save(snapshot());
      gc.beginPath();

      gc.moveTo(e.getX(),e.getY());
      gc.stroke();

    });

    canvas.setOnMouseDragged(e->{
      gc.lineTo(e.getX(),e.getY());
      gc.stroke();
    });

  }

  public void setColor(Color color){
    gc.setStroke(color);
    gc.setFill(color);
  }
  public void setLineWidth(double width){
    gc.setLineWidth(width);
  }
  public void clear(){
    gc.setFill(Color.WHITE);
    gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
    gc.setStroke(Color.BLACK);
    gc.setLineWidth(2);

  }
  public void undo(){
    WritableImage img = undoManager.undo(snapshot());
    if(img !=null){
      gc.drawImage(img,0,0);
    }
  }
public void redo(){
  WritableImage img=undoManager.redo(snapshot());
 if(img!=null) gc.drawImage(img,0,0);
}

public void drawText(String text,double x, double y){
  undoManager.save(snapshot());
  gc.fillText(text,x,y);
}

  public WritableImage snapshot(){
    WritableImage  image =new WritableImage(
        (int) canvas.getWidth(),
        (int) canvas.getHeight()
        );
    canvas.snapshot(null,image);
    return image;
  }
  public Canvas getCanvas(){

    return canvas;
  }

}
