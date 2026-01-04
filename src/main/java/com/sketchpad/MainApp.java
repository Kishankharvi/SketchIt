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
        Button rectBtn = new Button("Rectangle");
Button circleBtn = new Button("Circle");

        ColorPicker colorPicker = new ColorPicker(Color.BLACK);

        undoBtn.setOnAction(e -> drawingCanvas.undo());
        redoBtn.setOnAction(e->drawingCanvas.redo());

        clearBtn.setOnAction(e -> drawingCanvas.clear());
        textBtn.setOnAction(e->{
          drawingCanvas.enableTextMode();
          drawingCanvas.getCanvas().requestFocus();
        });

        colorPicker.setOnAction(e -> drawingCanvas.setColor(colorPicker.getValue()));

rectBtn.setOnAction(e -> drawingCanvas.setTool(DrawingCanvas.Tool.RECTANGLE));
circleBtn.setOnAction(e -> drawingCanvas.setTool(DrawingCanvas.Tool.CIRCLE));
textBtn.setOnAction(e -> {
    drawingCanvas.setTool(DrawingCanvas.Tool.TEXT);
    drawingCanvas.getCanvas().requestFocus();
});

//new codes for texts
/*
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
*/











        ToolBar toolBar = new ToolBar(undoBtn,redoBtn, clearBtn,textBtn, colorPicker);

        MenuItem saveItem = new MenuItem("Save");
        Menu fileMenu = new Menu("File");
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

        
        Scene scene = new Scene(root);   

        // Keyboard input for text typing
        scene.setOnKeyTyped(e -> {
            String ch = e.getCharacter();
            if (ch.length() > 0 && ch.charAt(0) >= 32) {
                drawingCanvas.handleKeyTyped(ch);
            }
        });

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case BACK_SPACE -> drawingCanvas.handleBackspace();
                case ENTER -> drawingCanvas.commitText();
                case ESCAPE -> drawingCanvas.cancelText();
            }
        });


        stage.setTitle("SketchPad");
        stage.setScene(scene);
        stage.show();
        drawingCanvas.getCanvas().setFocusTraversable(true);
drawingCanvas.getCanvas().requestFocus();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

