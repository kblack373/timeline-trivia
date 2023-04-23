package com.example.timeline_trivia;

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
        /*
             KB - I wonder if it's worth keeping two copies of each question in QuestionAndAnswerList and QuestionKey
             Maybe this should be [QuestionKey -> Answer] rather than [Question -> Answer]
             TODO: talk this structure out

            JDL - Right now the question IS the key for these structures. The QuestionKey structure maps to the index of the array map
         */


        QuestionIterator = new ArrayList<>();
        QuestionKey = new HashMap<>();
    }

    protected Boolean AddReplaceQuestion(String Question, String Answer){
        if(Question == null || Answer == null){
            return false;
        }

        //Integer i = QuestionKey.get(Question);
        HashSet<String> Answers;

        if (!QuestionKey.containsKey(Question)){
            //if the question doesn't exist yet:
            Integer i = QuestionIterator.size() + 1; //guarantees index/key value hasn't been used before
            Answers = new HashSet<>();
            Answers.add(Answer);
            //put the question in there, with a guaranteed new index value
            QuestionAndAnswerList.put(Question, Answers);
            QuestionKey.put(Question, i);
            QuestionIterator.add(Question);
        }else{

            //the question exists, but we're going to add a new answer
            Answers = QuestionAndAnswerList.get(Question);
            Answers.add(Answer);
            QuestionAndAnswerList.replace(Question, Answers);
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
            QuestionAndAnswerList.replace(Question, Answers);
        }
    }

    //TODO:Consider alternatives since timing is unknown
    //Option - When a database is implemented, questions can be removed directly and the overall structure wiped and reloaded in bulk
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
