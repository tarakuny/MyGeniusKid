package com.example.zele01.mygeniuskid;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    Intent recogIntent;
    SpeechRecognizer mRecognizer;
    TextView tv;
    Context context;
    ImageButton readAgainBtn;
    int ttsErrorState = -1;
    TimerTask mTask;
    Timer mTimer;

    private static String[] s_words = {"냉장고","우유","물","컵","아빠","엄마","동생","누나","한글","호랑이","사자","내방","유치원","어린이","할머니","할아버지","목연수","하성빈","정찬희","이하린","선생님","뽀로로","옥토넛","바다","고래","상어","돌고래","컴퓨터","전화기","사과","포도","딸기","토끼","바나나","하승훈","이희진","디즈니","마우스","공주님","백설공주","신데렐라","마녀","루피","에디","포비","크롱"};
    String currentWord;

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        context = getApplicationContext();

        tts = new TextToSpeech(this, initListener);
        tts.setLanguage(Locale.KOREA);

        tts.setOnUtteranceProgressListener(untteranceListener);

        recogIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recogIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recogIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);

        setRandomWord();

        readAgainBtn = (ImageButton) findViewById(R.id.imageButton);
        readAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                tts.speak("단어를 다시 읽어보세요", TextToSpeech.QUEUE_ADD, null);
//                while (tts.isSpeaking()) {
//                }
                startVoiceListening();
//                mRecognizer.startListening(recogIntent);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission(){
        if(checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if(shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)){
                Toast.makeText(this, "Record Audio", Toast.LENGTH_SHORT).show();
            }

            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.RECORD_AUDIO},111);
        } else {

        }
    }
    //----------------------- override method ------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRecognizer.destroy();
    }

    //----------------------- Listener -------------------------------------
    TextToSpeech.OnInitListener initListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            Log.d("ZELE", "onInit");
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "First read");
            tts.speak("글자를 읽는 연습을 해보아요. 아래 단어를 읽어보세요.", TextToSpeech.QUEUE_ADD, null);
            while(tts.isSpeaking()){

            }
            startVoiceListening();
//            mRecognizer.startListening(recogIntent);
        }
    };

    UtteranceProgressListener untteranceListener = new UtteranceProgressListener() {
        @Override
        public void onStart(String utteranceId) {
            Log.d("ZELE", "UtteranceListener OnStart" + utteranceId);
        }

        @Override
        public void onDone(String utteranceId) {
            Log.d("ZELE","UtteranceListener OnDone" + utteranceId);
            if(utteranceId.equals("First read")){
//                readAgainBtn.callOnClick();
            }
        }

        @Override
        public void onError(String utteranceId) {
            Log.d("ZELE", "UtteranceListener OnError" + utteranceId);
        }
    };
    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
//            Toast.makeText(getApplicationContext(), "읽어보세요", Toast.LENGTH_SHORT).show();
//            tts.speak("읽어보세요", TextToSpeech.QUEUE_FLUSH, null);
            Log.d("ZELE", "ready for speech");
        }


        @Override
        public void onBeginningOfSpeech() {
            Log.d("ZELE", "beginning of speech");
            ttsErrorState = 0;
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
            ttsErrorState = error;

//            mRecognizer.startListening(recogIntent);
        }

        @Override
        public void onResults(Bundle results) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ttsErrorState = 0;
            ArrayList<String> mResult = (ArrayList<String>) results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            Log.d("ZELE", "results : " + rs[0]);
            boolean resultPlag = false;
            for(int i=0; i<rs.length; i++){
                if(currentWord.equals(rs[i])){
                    tts.speak(rs[i] + ", 정답이에요.", TextToSpeech.QUEUE_ADD, null);
                    while(tts.isSpeaking()){}
                    SystemClock.sleep(100);
                    setRandomWord();
                    tts.speak("이제 다음 단어를 읽어보세요.", TextToSpeech.QUEUE_ADD, null);
                    while(tts.isSpeaking()){}
                    resultPlag = true;
                    break;
                }
            }
            if(resultPlag==false){
                tts.speak(rs[0] + ", 아니에요. 다시 말해보세요.", TextToSpeech.QUEUE_ADD, null);
                while(tts.isSpeaking())
                {

                }
                readAgainBtn.callOnClick();
            } else {
//                setRandomWord();
                startVoiceListening();
            }
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

    private void setRandomWord(){
        Random random = new Random();
        int randomNum = random.nextInt(s_words.length);

        currentWord = s_words[randomNum];

        TextView tv_currentWord = (TextView) findViewById(R.id.wordForRead);
        tv_currentWord.setText(currentWord);
    }

    private void startVoiceListening(){
        mTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("ZELE","Timer Task run");
                if(ttsErrorState!=0){
                    Log.d("ZELE","Error occured");
                    tts.speak("잘 안들려요. 버튼을 누르고 다시 말해보세요.", TextToSpeech.QUEUE_ADD, null);
                    while (tts.isSpeaking()) {
                    }
//                    mRecognizer.startListening(recogIntent);
                }
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTask, 4000);
        mRecognizer.startListening(recogIntent);
    }
}
