package com.example.timeline_trivia;

public class Question {
    //class only used to link QuestionMaster class and the object calling it
    //primarily used to keep track of data so we don't have to rely on string lookups

    private int index;
    private String text;

    public Question(){
        //if no values supplied, return the joke question
        this.index = -1;
        this.text = "What is a question if one has never been asked? :(";

    }

    public Question(int inID, String inText){
        this.index = inID;
        this.text = inText;
    }

    protected int getIndex(){
        return index;

    }

    protected String getQuestionText(){
        return text;
    }


}
