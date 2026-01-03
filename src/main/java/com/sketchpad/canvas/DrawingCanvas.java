package com.sketchpad.canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene,paint.Color;
import javafx.scene.image.WriteableImage;
import com.sketchpad.undo.UndoManager;
public class DrawingCanvas{
  private final Canvas canvas;
  private final GraphicsContext gc;
  private final UndoManager undoManager;

  public DrawingContext(double width, double height){
    canvas =new Canvas(width,height);
    gc=canvas.getGraphicsContext(2D);
    undoManager =new UndoManager();
    clear();

  
  
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
  }
  public void setLineWidth(double width){
    gc.setLineWidth(width);
  }
  public void clear(){
    gc,setFill(Color.WHITE);
    gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
    gc.setStroke(Color.BLACK);
    gc.setLineWidth(2);

  }
  public void undo(){
    WriteableImage img = undoManager.undo();
    if(img !=null){
      gc.drawImage(img,0,0);
    }
  }


  public WriteableImage snapshot(){
    writableImage  image -new WriteableImage(
        (int) canvas.getWidth(),
        (int) canvas.getHeight()
        );
    canvas.snapshot(null,image);
    return image;

  public Canvas getCanvas(){

    return canvas;
  }

}
