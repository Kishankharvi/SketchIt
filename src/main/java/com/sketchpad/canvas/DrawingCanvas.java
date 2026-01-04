package com.sketchpad.canvas;

import com.sketchpad.undo.UndoManager;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class DrawingCanvas {

    public enum Tool { DRAW, TEXT, RECTANGLE, CIRCLE }

    private Tool currentTool = Tool.DRAW;

    private final Canvas canvas;
    private final GraphicsContext gc;
    private final UndoManager undoManager;

  //shared snapshot
  private WritableImage beforeAction;

    //Drawing
    private double lastX, lastY;

    //Text
    private double textX, textY;
    private StringBuilder textBuffer = new StringBuilder();
    //shape
    private double startX, startY;

    public DrawingCanvas(double width, double height) {
        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
        undoManager = new UndoManager();

        clear();
        gc.setLineWidth(2);
        gc.setFont(Font.font(20));

        canvas.setOnMousePressed(e -> {
              
            beforeAction=snapshot();
            startX=e.getX();
            startY=e.getY();
            lastX=startX;
            lastY=startY;






            if (currentTool == Tool.TEXT){
              textX=startX;
              textY=startY;
              textBuffer.setLength(0);
            }
            undoManager.pushState(beforeAction);
        });

        canvas.setOnMouseDragged(e -> {
            gc.drawImage(beforeAction,0,0);
            switch (currentTool){

              case DRAW ->{

              gc.beginPath();
              gc.moveTo(lastX,lastY);
              gc.lineTo(e.getX(),e.getY());
              gc.stroke();
              lastX=e.getX();
              lastY=e.getY();
            }
            case RECTANGLE ->{
              gc.strokeRect(
                  Math.min(startX,e.getX()),
                  Math.min(startY,e.getY()),
                  Math.abs(e.getX()-startX),
                  Math.abs(e.getY()-startY)
                  );
            }
            case CIRCLE ->{
              double r=Math.hypot(e.getX()-startX,e.getY()-startY);
              gc.strokeOval(startX-r,startY-r,r*2,r*2);
            }

            }
        });

        canvas.setOnMouseReleased(e -> {
            if (currentTool == Tool.DRAW) return;
            gc.drawImage(beforeAction,0,0);
            if(currentTool==Tool.RECTANGLE){
              gc.strokeRect(
                
                    Math.min(startX, e.getX()),
                        Math.min(startY, e.getY()),
                        Math.abs(e.getX() - startX),
                        Math.abs(e.getY() - startY)
                  );
            }

              if (currentTool == Tool.CIRCLE) {
                double r = Math.hypot(e.getX() - startX, e.getY() - startY);
                gc.strokeOval(startX - r, startY - r, r * 2, r * 2);
            
            }
        });
    }

    /* ================= TEXT ================= */

    public void enableTextMode() {
        currentTool = Tool.TEXT;
    }

    public void handleKeyTyped(String ch) {
        if (currentTool != Tool.TEXT) return;
        textBuffer.append(ch);
        redrawTextPreview();
    }

    public void handleBackspace() {
        if (currentTool != Tool.TEXT || textBuffer.length() == 0) return;
        textBuffer.deleteCharAt(textBuffer.length() - 1);
        redrawTextPreview();
    }

    public void commitText() {
        if (currentTool != Tool.TEXT) return;
      
        gc.drawImage(beforeAction, 0, 0);
        gc.fillText(textBuffer.toString(), textX, textY);
        currentTool = Tool.DRAW;
    }

    public void cancelText() {
        if (currentTool != Tool.TEXT) return;
        gc.drawImage(beforeAction, 0, 0);
        currentTool = Tool.DRAW;
    }

    private void redrawTextPreview() {
        gc.drawImage(beforeAction, 0, 0);
        gc.fillText(textBuffer.toString(), textX, textY);
    }

    /* ================= UNDO / REDO ================= */

    public void undo() {
        WritableImage img = undoManager.undo(snapshot());
        if (img != null) gc.drawImage(img, 0, 0);
    }

    public void redo() {
        WritableImage img = undoManager.redo(snapshot());
        if (img != null) gc.drawImage(img, 0, 0);
    }

    /* ================= UTIL ================= */

    public void setColor(Color color) {
        gc.setStroke(color);
        gc.setFill(color);
    }
public  void setTool(Tool tool){
  currentTool=tool;
}

    public void clear() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.BLACK);
    }

    private WritableImage snapshot() {
        WritableImage image = new WritableImage(
                (int) canvas.getWidth(),
                (int) canvas.getHeight()
        );
        canvas.snapshot(null, image);
        return image;
    }

    public Canvas getCanvas() {
        return canvas;
    }
}

