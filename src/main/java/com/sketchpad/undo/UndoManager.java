package com.sketchpad.undo;
import javafx.scene.image.WritableImage;
import java.util.Stack;


public class UndoManager{
  private final Stack<WritableImage> undoStack =new Stack<>();
  private final Stack<WritableImage> redoStack =new Stack<>();

  public void pushState(WritableImage image){
    undoStack.push(image);
    redoStack.clear();
  }
  public WritableImage undo(WritableImage current){
    if(undoStack.isEmpty()) return null;
      redoStack.push(current);
      return undoStack.pop();
    

  }

  public WritableImage redo(WritableImage current){
    if(redoStack.isEmpty()) return null;
      undoStack.push(current);
      return redoStack.pop();
    
    
  }
}
