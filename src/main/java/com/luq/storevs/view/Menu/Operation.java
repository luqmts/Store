package com.luq.storevs.view.Menu;

public class Operation {
    private String text;
    private Runnable action;

    public Operation(String text, Runnable action){
        this.text = text;
        this.action = action;
    }

    public String getText(){
        return text;
    }

    public void run(){
        action.run();
    }
}
