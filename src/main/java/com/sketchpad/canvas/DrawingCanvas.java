package com.sketchpad.canvas;

import com.sketchpad.undo.UndoManager;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class DrawingCanvas {

    private enum ToolMode { DRAW, TEXT }

    private ToolMode currentTool = ToolMode.DRAW;

    private final Canvas canvas;
    private final GraphicsContext gc;
    private final UndoManager undoManager;

    // Text state
    private double textX, textY;
    private StringBuilder currentText = new StringBuilder();
    private WritableImage beforeTextImage;

    public DrawingCanvas(double width, double height) {
        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
        undoManager = new UndoManager();

        clear();
        gc.setLineWidth(2);
        gc.setFont(Font.font(20));

        canvas.setOnMousePressed(e -> {
            if (currentTool == ToolMode.DRAW) {
                undoManager.save(snapshot());
                gc.beginPath();
                gc.moveTo(e.getX(), e.getY());
            }
        });

        canvas.setOnMouseDragged(e -> {
            if (currentTool == ToolMode.DRAW) {
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
            }
        });

        canvas.setOnMouseClicked(e -> {
            if (currentTool == ToolMode.TEXT) {
                textX = e.getX();
                textY = e.getY();
                currentText.setLength(0);
                beforeTextImage = snapshot();
            }
        });
    }

    /* ================= TEXT ================= */

    public void enableTextMode() {
        currentTool = ToolMode.TEXT;
    }

    public void handleKeyTyped(String ch) {
        if (currentTool != ToolMode.TEXT) return;
        currentText.append(ch);
        redrawTextPreview();
    }

    public void handleBackspace() {
        if (currentTool != ToolMode.TEXT || currentText.length() == 0) return;
        currentText.deleteCharAt(currentText.length() - 1);
        redrawTextPreview();
    }

    public void commitText() {
        if (currentTool != ToolMode.TEXT) return;
        undoManager.save(beforeTextImage);
        gc.drawImage(beforeTextImage, 0, 0);
        gc.fillText(currentText.toString(), textX, textY);
        currentTool = ToolMode.DRAW;
    }

    public void cancelText() {
        if (currentTool != ToolMode.TEXT) return;
        gc.drawImage(beforeTextImage, 0, 0);
        currentTool = ToolMode.DRAW;
    }

    private void redrawTextPreview() {
        gc.drawImage(beforeTextImage, 0, 0);
        gc.fillText(currentText.toString(), textX, textY);
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

