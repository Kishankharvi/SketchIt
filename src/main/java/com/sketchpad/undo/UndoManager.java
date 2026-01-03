package com.sketchpad.undo;
import javafx.scene.image.WriteableImage;
import java.util.Stack;


public class UndoManager{
  private final Stack<WriteableImage> undoStack =new Stack<>();
  

  public void save(WriteableImage image){
    undostack.push(image);
  }
  public WriteableImage undo(){
    if(!undoStack.isEmpty()){
      return undoStack.pop();
    }
  return null;
  }

