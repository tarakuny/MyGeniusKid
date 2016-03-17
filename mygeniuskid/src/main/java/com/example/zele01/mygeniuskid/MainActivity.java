package com.example.zele01.mygeniuskid;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Intent i;
    SpeechRecognizer mRecognizer;
    TextView tv;
    Context context;

    private RecognitionListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREAN.toString());
        i.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 50);

        tv = (TextView)findViewById(R.id.tv);

        Button callButton = (Button)findViewById(R.id.button);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ZELE", "이벤트는 나오냐");

                listener = new RecognitionListener() {
                    @Override
                    public void onReadyForSpeech(Bundle params) {
                        Log.d("ZELE","ready for speech");
                    }

                    @Override
                    public void onBeginningOfSpeech() {
Log.d("ZELE","beginning of speech");
                    }

                    @Override
                    public void onRmsChanged(float rmsdB) {
                        Log.d("ZELE", "rms changed");
                    }

                    @Override
                    public void onBufferReceived(byte[] buffer) {
                        Log.d("ZELE", "buffer received");
                    }

                    @Override
                    public void onEndOfSpeech() {
                        Log.d("ZELE","end of speech");
                    }

                    @Override
                    public void onError(int error) {
                        Log.d("ZELE","에러났어 - " + error);
                    }

                    @Override
                    public void onResults(Bundle results) {
                        String key="";
                        key = SpeechRecognizer.RESULTS_RECOGNITION;
                        ArrayList<String> mResult = (ArrayList<String>)results.getStringArrayList(key);
                        String[] rs = new String[mResult.size()];
                        mResult.toArray();
                        tv.setText("" + rs[0]);
                        Log.d("ZELE", "results : " + rs[0]);
                    }

                    @Override
                    public void onPartialResults(Bundle partialResults) {
                        Log.d("ZELE","partial result");
                    }

                    @Override
                    public void onEvent(int eventType, Bundle params) {
                        Log.d("ZELE","onEvent");

                    }
                };

//                mRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
//                mRecognizer.setRecognitionListener(listener);
//                mRecognizer.startListening(i);
                startActivityForResult(i, 11);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<String> resultString=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

        String resultBest = resultString.get(0);
        tv.setText(resultBest);
    }

    @Override
    protected void onResume() {
        super.onResume();
  //      mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
    //    mRecognizer.setRecognitionListener(listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mRecognizer.destroy();
    }
}
