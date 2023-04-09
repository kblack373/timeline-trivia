package com.example.timeline_trivia;

import android.os.Build;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public class QuestionMaster {
    private HashMap<String, HashSet<String>> QuestionAndAnswerList; //Question followed by answers
    private ArrayList<String> QuestionIterator; //Index-able questions
    private HashMap<String, Integer> QuestionKey; //Question to index
    private final String JokeQuestion = "What is a question if one has never been asked";

    protected QuestionMaster(){
        QuestionAndAnswerList = new HashMap<>();
        QuestionIterator = new ArrayList<>();
        QuestionKey = new HashMap<>();
    }

    protected Boolean AddReplaceQuestion(String Question, String Answer){
        if(Question == null || Answer == null){
            return false;
        }

        Integer i = QuestionKey.get(Question);
        HashSet<String> Answers;

        if (i == null){
            i = QuestionIterator.size() + 1;
            Answers = new HashSet<>();
            Answers.add(Answer);
            QuestionAndAnswerList.put(Question, Answers);
            QuestionKey.put(Question, i);
            QuestionIterator.add(Question);
        }else{
            Answers = QuestionAndAnswerList.get(Question);
            Answers.add(Answer);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                QuestionAndAnswerList.replace(Question, Answers);
            }else{
                QuestionAndAnswerList.remove(Question);
                QuestionAndAnswerList.put(Question, Answers);
            }
        }

        return true;
    }

    protected void AddReplaceQuestion(String Question, String[] Answer){
        Integer i = QuestionKey.get(Question);
        HashSet<String> Answers;

        if (i == null){
            i = QuestionIterator.size() + 1;
            Answers = new HashSet<>();
            //For each answer in the answer parameter
            Collections.addAll(Answers, Answer);
            QuestionAndAnswerList.put(Question, Answers);
            QuestionKey.put(Question, i);
            QuestionIterator.add(Question);
        }else{
            Answers = QuestionAndAnswerList.get(Question);
            //For each answer in the answer parameter
            Collections.addAll(Answers, Answer);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                QuestionAndAnswerList.replace(Question, Answers);
            }else{
                QuestionAndAnswerList.remove(Question);
                QuestionAndAnswerList.put(Question, Answers);
            }
        }
    }

    protected void RemoveQuestion(String Question){
        Integer index = QuestionKey.get(Question);

        if(index != null) {
            QuestionAndAnswerList.remove(Question);
            QuestionIterator.remove((int) index);
            QuestionKey.remove(Question);
        }
    }

    protected void ClearQuestions(){
        QuestionAndAnswerList = new HashMap<>();
        QuestionIterator = new ArrayList<>();
        QuestionKey = new HashMap<>();
    }

    protected String NextQuestion(){
        int min = 0;
        int max = QuestionIterator.size();
        int randIndex = ThreadLocalRandom.current().nextInt(min, max);

        String Question = QuestionIterator.get(randIndex);

        //make sure there is something to return
        if (Question != null){
            return Question;
        }else{
            return JokeQuestion;
        }
    }

    protected String[] GetAllQuestions(){
        String[] a = new String[QuestionIterator.size()];
        return QuestionIterator.toArray(a);
    }

    protected boolean CheckAnswer(String Question, String Answer){
        HashSet<String> Answers = QuestionAndAnswerList.get(Question);

        return (Answers != null && Answers.contains(Answer));
    }

    protected String[] GetAllAnswers(String Question){
        HashSet<String> Answers = QuestionAndAnswerList.get(Question);

        if(Answers != null){
            String[] a = new String[Answers.size()];
            return Answers.toArray(a);
        }
        return null;
    }
}
