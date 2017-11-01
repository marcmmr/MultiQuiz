package edu.upc.eseiaat.pma.multiquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private int ids_answers[]={
            R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4
    };
    private int correct_answer;
    private int current_question;
    private String[] all_questions; //CREATE FIELD es una variable que hem creat perq sigui accesible des de TOTS els metodes, ja no es local
    private boolean [] answer_is_correct;
    private int[] answer;
    private TextView text_question; //eliminem EL TIPUS (var. local) per creac un FIELD
    private RadioGroup group;// creem camp per accedir des del ShowQuestion
    private Button btn_next,btn_prev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        text_question = (TextView) findViewById(R.id.text_question); //Obtenim referencies a objectes de la pantalla
        group = (RadioGroup) findViewById(R.id.answer_group);//
        btn_next = (Button) findViewById(R.id.btn_check);//Ídem
        btn_prev=(Button) findViewById(R.id.btn_prev);//Ídem

        all_questions = getResources().getStringArray(R.array.all_questions); //obtenim un array de strings
        answer_is_correct = new boolean[all_questions.length]; //creem array de booleans per contar quans correctes tenim i quantes incorrectes
        answer= new int [all_questions.length]; //creem array de enters quan ja sabem el tamany
        for(int i=0; i<answer.length;i++){
            answer[i]=-1;
        }
        current_question=0;
        showQuestion();

        //final int correct_answer = getResources().getInteger(R.integer.correct_answer);


        btn_next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                checkAnswer();
                if(current_question<all_questions.length-1) {//-1 perq currentqustio nomes pot ser 0 o 1 i length arriba a 2
                    current_question++; //canviem de pregunta
                    showQuestion();//la mostrem
                }else {
                    int correctas=0, incorrectas=0;
                    for (boolean b:answer_is_correct){ //passa per tots els elements i els posa (anomena) com a b
                        if (b) correctas++;
                        else incorrectas++;
                    }
                    String resultado =
                            String.format("Correctas: %d -- Incorrectas: %d",correctas,incorrectas); //%b es per dir que agafaras un boolean
                    Toast.makeText(QuizActivity.this, resultado, Toast.LENGTH_LONG).show(); //msotrem resultado en un toast, LONG perq duri+
                    finish();//tanca la aplicació
                }
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {//escoltem el boto de prev perq tiri enrera 1 pregunta si es >0
                                        @Override
                                        public void onClick(View v) {
                                            checkAnswer(); //tornem a comprovat resposta quan tirem enrere
                                            if (current_question>0){
                                                current_question--;
                                                showQuestion();
                                            }                                        }
                                    });


    }

    private void checkAnswer() { //hem creat un metode en el QuizActivity
        int id = group.getCheckedRadioButtonId();
        int ans= -1;
        for (int i=0;i<ids_answers.length;i++){
            if(ids_answers[i]==id){
                ans=i;
            }
        }

        answer_is_correct [current_question]= (ans == correct_answer); //introduiem un true o false en un array de booleans
        answer[current_question]=ans;//la resposta de la pregunta actual es ans
    }

    private void showQuestion() {//REFACOR+EXTRACT+METHOD per creat una funcio

        String q = all_questions[current_question];
        String [] parts = q.split(";"); //partim la pregunta amb ;

        group.clearCheck();//resetejem les respostes al canviar de pregunta quan donem NEXT
        text_question.setText(parts[0]);

        //String[] answers = getResources().getStringArray(R.array.);

        for (int i=0; i<ids_answers.length;i++){
            RadioButton rb = (RadioButton) findViewById(ids_answers[i]);
            String ans =parts [i+1]; //+1 perque a la 0 hi ha la pregunta
            if(ans.charAt(0)=='*'){//metode per buscar un catacter a la posicio 0
                correct_answer=i; //fem create field
                ans=ans.substring(1);// crea un nou string a partir del 1r caracter
            }
            rb.setText(ans); //mostra el text
            if (answer[current_question]==i){ //es marca la que haviem marcat previament al tirar enrere o endavant
                rb.setChecked(true);
            }
        }

        if (current_question==0){
            btn_prev.setVisibility(View.GONE); //si es la 1era pregunta no apareix el boto per tirar enrera
        }else{
            btn_prev.setVisibility(View.VISIBLE);//el tornem a fer visible
        }

        if (current_question==all_questions.length-1){
            btn_next.setText(R.string.finish);
        }else {
            btn_next.setText(R.string.next); //mostrem next si no estem a la ultima pregunta quan hem arribat el final i tornem enrere
        }

    }
}
