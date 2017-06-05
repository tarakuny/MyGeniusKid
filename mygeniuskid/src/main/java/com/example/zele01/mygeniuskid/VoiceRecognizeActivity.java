package com.example.zele01.mygeniuskid;

import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class VoiceRecognizeActivity extends AppCompatActivity {

    private SpeechRecognizer mRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_recognize);

        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);
        mRecognizer.startListening(i);
    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Log.d("ZELE", "ready for speech");
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.d("ZELE", "beginning of speech");
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
            Log.d("ZELE", "end of speech");
        }

        @Override
        public void onError(int error) {
            Log.d("ZELE", "에러났어 - " + error);
        }

        @Override
        public void onResults(Bundle results) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = (ArrayList<String>) results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray();
//            tv.setText("" + rs[0]);
            Log.d("ZELE", "results : " + rs[0]);
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            Log.d("ZELE", "partial result");
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            Log.d("ZELE", "onEvent");

        }
    };
}
