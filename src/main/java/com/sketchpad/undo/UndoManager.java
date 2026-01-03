package com.sketchpad.undo;
import javafx.scene.image.WritableImage;
import java.util.Stack;


public class UndoManager{
  private final Stack<WritableImage> undoStack =new Stack<>();
  private final Stack<WritableImage> redoStack =new Stack<>();

  public void save(WritableImage image){
    undoStack.push(image);
  }
  public WritableImage undo(WritableImage current){
    if(!undoStack.isEmpty()){
      redoStack.push(current);
      return undoStack.pop();
    }
  return null;
  }

  public WritableImage redo(WritableImage current){
    if(!redoStack.isEmpty()){
      undoStack.push(current);
      return redoStack.pop();
    }
    return null;
  }
}
