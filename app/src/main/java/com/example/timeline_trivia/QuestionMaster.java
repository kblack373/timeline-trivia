package com.example.timeline_trivia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public class QuestionMaster {
    //private HashMap<String, HashSet<String>> QuestionAndAnswerList; //Question followed by answers
    private ArrayList<String> QuestionIterator; //Index-able questions
    //private HashMap<String, Integer> QuestionKey; //Question to index


    //testing integer stuff
    private HashMap<Integer, HashSet<String>> AnswerKey;
    private HashMap<Integer, String> QuestionKey; //Question to index
    //private ArrayList askedQuestions = new ArrayList();
    private int askedQuestions[] = {};
    private int askedID = 0;

    protected QuestionMaster(){
        AnswerKey = new HashMap<>();
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

    protected Boolean AddReplaceQuestion(String nowQuestion, String Answer){
        if(nowQuestion == null || Answer == null){
            return false;
        }

        //Integer i = QuestionKey.get(nowQuestion);
        HashSet<String> Answers;
        //kb moved the below line there from the following conditional statement
        Integer i = QuestionIterator.size() + 1; //guarantees index/key value hasn't been used before
        if (!QuestionKey.containsKey(nowQuestion)){
            //if the question doesn't exist yet:

            Answers = new HashSet<>();
            Answers.add(Answer);
            //put the question in there, with a guaranteed new index value
            AnswerKey.put(i, Answers);
            QuestionKey.put(i, nowQuestion);
            QuestionIterator.add(nowQuestion);
        }else{

            //the question exists, but we're going to change the answer
            Answers = AnswerKey.get(nowQuestion);
            Answers.add(Answer);
            //kb: not sure what's going on below here
            //TODO: josh will comment this.
            //kb - oh, is this just a compoatibility issue with android or the java sdk?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                AnswerKey.replace(i, Answers);
            }else{
                AnswerKey.remove(nowQuestion);
                AnswerKey.put(i, Answers);
            }
            QuestionAndAnswerList.replace(Question, Answers);
        }

        return true;
    }

    protected void AddReplaceQuestion(String nowQuestion, String[] Answer){
        HashSet<String> Answers;
        int i =0;
        boolean exists = QuestionKey.values().contains(nowQuestion);
        if (!(exists)){ //if it does not exist:
            i = QuestionIterator.size() + 1;
            Answers = new HashSet<>();
            //For each answer in the answer parameter
            Collections.addAll(Answers, Answer);
            AnswerKey.put(i, Answers);
            QuestionKey.put(i, nowQuestion);
            QuestionIterator.add(nowQuestion);
        }else if (AnswerKey.containsKey(i)){
            Answers = AnswerKey.get(i);
            //For each answer in the answer parameter
            Collections.addAll(Answers, Answer);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                AnswerKey.replace(i, Answers);
            }else{
                AnswerKey.remove(nowQuestion);
                AnswerKey.put(i, Answers);
            }

            QuestionAndAnswerList.replace(Question, Answers);

        }
        else{
            throw new RuntimeException("Key exists in QuestionKey but not AnswerKey");
        }
    }


    protected String RemoveQuestion (int index, String removedQ){

    //TODO:Consider alternatives since timing is unknown
    //Option - When a database is implemented, questions can be removed directly and the overall structure wiped and reloaded in bulk
    protected void RemoveQuestion(String Question){
        Integer index = QuestionKey.get(Question);


        if (removedQ != QuestionKey.get(index)) {
            return ""; //does not delete anything if it can't verify
        }
        if(index != 0) {
            AnswerKey.remove(index);
            QuestionIterator.remove(index);
            QuestionKey.remove(index);
        }
        return removedQ;
    }

    protected int[] reverseLookup(String lookup, HashMap haystack){
        //very ineffeciently looks up a string on demand, returns an array of keys
        //probably should have used aan array list but i don't feel like rewriting this!

        int[] matches ={};
        int matchesCount = 0;
        //put keys and values in their own arrays
        Object[] values = haystack.values().toArray();
        Object[] allKeys = haystack.keySet().toArray();
        //iterate through the keys
        for (int i=0; i<allKeys.length;i++){
            //pluck each value, and record if the key matches
            Object nowValue = haystack.get((allKeys[i]));
            String plucked = nowValue.toString();
            if (plucked.contains(lookup)) {
                matches[matchesCount] = (int)allKeys[i];
            }
        }

        return matches;
    }

    protected void ClearQuestions(){
        AnswerKey = new HashMap<>();
        QuestionIterator = new ArrayList<>();
        QuestionKey = new HashMap<>();
    }

    protected Question NextQuestion(){
        int min = 0;
        int max = QuestionIterator.size();
        int randIndex = ThreadLocalRandom.current().nextInt(min, max);

        String nowQuestion = QuestionIterator.get(randIndex);

        //todo: add new index logic
        int sizeNewAsk = askedQuestions.length+1;
        int[] newAsk = new int[sizeNewAsk];
        newAsk[sizeNewAsk-1] = randIndex;

        //make sure there is something to return
        if (nowQuestion != null){
            String strQuestion = nowQuestion.toString();
            //build the Question object so we can return two values
            Question craftQuestion = new Question(randIndex, strQuestion);
        }
        //if no matchez, return the default
        return new Question();

    }

    protected String[] GetAllQuestions(){
        String[] a = new String[QuestionIterator.size()];
        return QuestionIterator.toArray(a);
    }

    protected boolean CheckAnswer(int index, String inAnswer){
        HashSet<String>realAnswers = AnswerKey.get(index);
        return (realAnswers != null && realAnswers.contains(inAnswer));
    }

    protected String[] GetAllAnswers(int questionIndex){
        HashSet<String> Answers = AnswerKey.get(questionIndex);

        if(Answers != null){
            String[] a = new String[Answers.size()];
            return Answers.toArray(a);
        }
        return null;
    }

    protected boolean validateIndexes(){
        //returns true if AK, QK, and QI sizes match and returns false if any do not.
        if (QuestionKey.size() == AnswerKey.size()){if (QuestionKey.size() == QuestionIterator.size()){
                return true;
            } }
        return false;
    }
}
