package com.example.timeline_trivia;

import android.os.Bundle;

//import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.navigation.ui.AppBarConfiguration;
//import androidx.navigation.ui.NavigationUI;

//import com.example.timeline_trivia.databinding.ActivityMainBinding;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//import java.util.Optional;

public class MainActivity extends AppCompatActivity {

    //button labels
    String EnterText = "Submit";
    String QuestionButtonText = "Prompt";
    String ExitText = "Exit";
    String HintInputText = "Type Answer Here";
    String CorrectResponse = "That's Correct!";
    String IncorrectResponse = "Sorry, try another one!";

    //kb added this for integer-key conversion
    Question nowQuestion = new Question();

    Boolean QuestionMasterResponse;

    EditText UserInput;
    TextView Prompt;
    TextView Solution;

    Button EnterButton;
    Button QuestionButton;
    Button ExitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initiate Display Items
        EnterButton = (Button)findViewById(R.id.EnterButton);
        QuestionButton = (Button)findViewById(R.id.QuestionButton);
        ExitButton = (Button)findViewById(R.id.ExitButton);

        UserInput = (EditText)findViewById(R.id.UserInput);
        Prompt = (TextView)findViewById(R.id.Prompt);
        Solution = (TextView)findViewById(R.id.Solution);

        //Set variable text names
        EnterButton.setText(EnterText);
        QuestionButton.setText(QuestionButtonText);
        ExitButton.setText(ExitText);
        UserInput.setHint(HintInputText);

        //Create Helper classes

        //this will likely be database lookups and I put too much thought into this.
        QuestionMaster Q = ExampleQuestions();

        //Ask Next Question
        QuestionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Solution.setText("");
                nowQuestion = Q.NextQuestion();
                String qText = nowQuestion.getQuestionText();
                Prompt.setText(qText);
            }
        });

        //Submit Guess
        EnterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                QuestionMasterResponse = Q.CheckAnswer(nowQuestion.getIndex(), (UserInput.getText().toString()));

                if (QuestionMasterResponse) {
                    Solution.setText(CorrectResponse);
                }else{
                    Solution.setText(IncorrectResponse);
                }
            }
        });

        //Allow User to leave
        ExitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finishAffinity();
            }
        });
    }

    private QuestionMaster ExampleQuestions(){

        QuestionMaster Q = new QuestionMaster();

        //Mostly taken from Today.com
        Q.AddReplaceQuestion("What is the capital of Portugal", "Lison");
        Q.AddReplaceQuestion("What is a Corvus brachyrhynchos", "American crow,crow".split(","));
        Q.AddReplaceQuestion("Which blood type is a universal donor", "O Negative,O-".split(","));
        Q.AddReplaceQuestion("What is August's birthstone", "Peridot");
        Q.AddReplaceQuestion("What was the first message sent by morse code", "What hath God wrought?,What hath God wrought".split(","));
        Q.AddReplaceQuestion("What movie is \"You had me at hello\" from", "Jerry Maguire");
        Q.AddReplaceQuestion("What singer was called the \"Empress of the Blues\"", "Bessie Smith");
        Q.AddReplaceQuestion("How many properties are on a Monopoly board", "28");
        Q.AddReplaceQuestion("Are you still playing", "Yes,Maybe".split(","));
        Q.AddReplaceQuestion("You know you can add your own questions right","Really");
        Q.AddReplaceQuestion("What is this, 20 questions", "Yes");

        return Q;
    }

}