package com.example.tunematev2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri; // Uri 클래스 import 추가
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.RecognitionListener;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView emotionResult;  // 음성 인식 결과를 표시할 텍스트뷰
    private Button spotifyPlaylistBtn;
    private SpeechRecognizer speechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // 레이아웃 파일 설정

        // UI 요소 초기화
        Button voiceCommandBtn = findViewById(R.id.voiceCommandBtn);
        emotionResult = findViewById(R.id.emotionResult);  // 결과 표시할 TextView
        spotifyPlaylistBtn = findViewById(R.id.spotifyPlaylistBtn);

        // 음성 인식 객체 초기화
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        // 음성 인식 리스너 설정
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                emotionResult.setText("음성 입력 중...");  // 음성 인식 준비 중일 때
            }

            @Override
            public void onBeginningOfSpeech() {
                emotionResult.setText("음성 입력 시작...");  // 음성 인식이 시작될 때
            }

            @Override
            public void onRmsChanged(float rmsdB) {
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
            }

            @Override
            public void onEndOfSpeech() {
                emotionResult.setText("음성 입력 완료");  // 음성 인식이 끝났을 때
            }

            @Override
            public void onError(int error) {
                emotionResult.setText("오류 발생: 다시 시도하세요.");  // 오류가 발생했을 때
            }

            @Override
            public void onResults(Bundle results) {
                // 음성 인식 결과를 리스트로 받음
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String recognizedText = matches.get(0);  // 가장 첫 번째 결과를 사용
                    emotionResult.setText("입력된 텍스트: " + recognizedText);  // 결과 텍스트 표시

                    // 감정 분석 처리 (여기서는 단순히 텍스트 표시로 대체)
                    String emotionAnalysis = analyzeEmotion(recognizedText);  // 감정 분석 함수 호출
                    emotionResult.append("\n감정 분석 결과: " + emotionAnalysis);  // 감정 분석 결과 추가 표시

                    // Spotify 플레이리스트 버튼 활성화
                    spotifyPlaylistBtn.setVisibility(Button.VISIBLE);
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
            }
        });

        // 음성 인식 시작 버튼 리스너 설정
        voiceCommandBtn.setOnClickListener(v -> startSpeechRecognition());

        // Spotify 플레이리스트 버튼 리스너
        spotifyPlaylistBtn.setOnClickListener(v -> {
            // Spotify 플레이리스트 링크로 이동
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://spotify.com/your-playlist-link"));
            startActivity(browserIntent);
        });
    }

    // 음성 인식 시작 함수
    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREAN);  // 한국어 설정
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "음성을 입력하세요");

        speechRecognizer.startListening(intent);  // 음성 인식 시작
    }

    // 감정 분석 함수 (예시)
    private String analyzeEmotion(String text) {
        // 실제 서버와 통신하여 감정 분석 결과를 받는 로직이 여기에 있어야 함
        // 현재는 단순히 긍정적으로 반환
        return "긍정적";  // 예시로 감정 분석 결과를 긍정으로 반환
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();  // 음성 인식 객체 제거
        }
    }
}
