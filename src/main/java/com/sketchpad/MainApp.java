package com.sketchpad;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import com.sketchpad.canvas.DrawingCanvas;
import com.sketchpad.file.FileService;

import java.io.File;
import java.util.Optional;
public class MainApp extends Application {
   private boolean textMode =false;
    @Override
    public void start(Stage stage) {
        DrawingCanvas drawingCanvas = new DrawingCanvas(900, 600);

        Button undoBtn = new Button("Undo");
        Button redoBtn = new Button("Redo");
        Button clearBtn = new Button("Clear");
        Button textBtn =new Button("Text");

        ColorPicker colorPicker = new ColorPicker(Color.BLACK);

        undoBtn.setOnAction(e -> drawingCanvas.undo());
        redoBtn.setOnAction(e->drawingCanvas.redo());

        clearBtn.setOnAction(e -> drawingCanvas.clear());
        textBtn.setOnAction(e->textMode=true);

        colorPicker.setOnAction(e -> drawingCanvas.setColor(colorPicker.getValue()));



//new codes for texts
drawingCanvas.getCanvas().setOnMouseClicked(e->{
  if(textMode){
  TextInputDialog dialog =new TextInputDialog();
  dialog.setTitle("Add Text");
  dialog.setHeaderText(null);
  dialog.setContentText("Enter text");

  Optional<String> result =dialog.showAndWait();
  result.ifPresent(text->
      drawingCanvas.drawText(text,e.getX(),e.getY())
      );
  textMode =false;
}
    });













        ToolBar toolBar = new ToolBar(undoBtn,redoBtn, clearBtn,textBtn, colorPicker);

        MenuItem saveItem = new MenuItem("Save");
        Menu fileMenu = new Menu("File", null, saveItem);
        fileMenu.getItems().add(saveItem);
        MenuBar menuBar = new MenuBar(fileMenu);

        saveItem.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PNG Image", "*.png")
            );
            File file = chooser.showSaveDialog(stage);
            if (file != null) {
                try {
                    FileService.save(drawingCanvas.getCanvas(), file);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        BorderPane root = new BorderPane();
        root.setTop(new VBox(menuBar, toolBar));
        root.setCenter(drawingCanvas.getCanvas());

        stage.setTitle("SketchPad");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

