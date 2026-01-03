package com.sketchpad;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.sketchpad.canvas.DrawingCanvas;


public class MainApp extends Application
{
  @Override
  public void start(Stage stage){
    DrawingCanvas canvas= new DrawingCanvas();
    BorderPane root=new BorderPane();
    root.setCenter(canvas.getCanvas());
    Scene scene =new Scene(root,1000,700);
    stage.setTitle("SketchPad");
    stage.setScene(scene);
    stage.show();
  }
  public static void main(String[] args){
    launch();
  }
}
