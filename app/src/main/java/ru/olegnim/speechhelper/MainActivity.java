package ru.olegnim.speechhelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView dialogWindow;
    EditText inputText;
    Button btnSay;
    Button btnListen;
    TextToSpeech speechText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialogWindow = findViewById(R.id.dialogWindows);
        inputText = findViewById(R.id.inputText);
        btnSay = findViewById(R.id.buttonSay);
        btnListen = findViewById(R.id.buttonListen);

        speechText = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR){
                    speechText.setLanguage(new Locale("ru"));
                }
            }
        });

        btnSay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputTxt = inputText.getText().toString();
                if (inputTxt.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Ошибка!")
                            .setIcon(R.drawable.ic_error)
                            .setMessage("Вы не ввели текст для озвучивания")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog Error = builder.create();
                    Error.show();
                } else {
                    dialogWindow.append(inputTxt + "\n");
                    inputText.setText("");
                    speechText.speak(inputTxt, TextToSpeech.QUEUE_ADD, null, null);
                    Toast.makeText(getApplicationContext(), "Озвучиваю введнный текст: " + inputTxt, Toast.LENGTH_SHORT).show();
                }
            }

        });
        btnListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Я Вас слушаю внимательно, говорите...", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                startActivityForResult(intent, 10);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null)
        {
            switch (requestCode)
            {
                case 10:
                    ArrayList<String> text =  data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    dialogWindow.append(text.get(0) + "\n");
                    break;

            }
        }
    }
}