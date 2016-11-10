package com.seveneow.textspeaker;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Jenny Chen on 2016/11/10.
 * TextSpeaker is a simple module can be used for easily saying something with Android TextToSpeech api
 */

public class TextSpeaker {
  private int repeatTimes = 0;
  private int currentRepeatTimes = 0;
  private int repeatInterval = 700;
  private TextToSpeech tts;
  private SpeechProgressListener listener = null;
  private String currentSpeech = null;
  private Context context = null;
  private Locale locale;


  public TextSpeaker(Context context, final Locale locale, TextSpeaker.SpeechProgressListener listener) {
    this.listener = listener;
    this.context = context;
    this.locale = locale;
    initTTS();
  }

  public TextSpeaker(Context context, TextSpeaker.SpeechProgressListener listener) {
    this.listener = listener;
    this.context = context;
    this.locale = Locale.US;
    initTTS();
  }

  private void initTTS() {
    tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
      @Override
      public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
          // TTS init success.
          tts.setLanguage(Locale.US); //default language.

          // Checks if the specified language as represented by the Locale is available and supported.
          if (tts.isLanguageAvailable(locale) == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
            tts.setLanguage(locale);
          }
          setProgressListener();
          if (listener != null) {
            listener.onTextSpeakerInit();
          }
        }
        else {
          //init failed;
          tts = null;
        }
      }
    });
  }

  private void setProgressListener() {
    tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
      @Override
      public void onStart(String utteranceId) {
        if (listener != null) {
          listener.onSpeechStart(utteranceId);
        }
      }

      @Override
      public void onDone(final String utteranceId) {
        if (currentRepeatTimes < repeatTimes) {
          currentRepeatTimes++;
          try {

            Thread.sleep(repeatInterval);
          }
          catch (InterruptedException e) {
            if (BuildConfig.DEBUG) {
              e.printStackTrace();
            }
          }
          say(currentSpeech, utteranceId);
          return;
        }
        if (listener != null) {
          listener.onSpeechDone(utteranceId);
          currentRepeatTimes = 0;
        }
      }

      @Override
      public void onError(String utteranceId) {
        if (listener != null) {
          listener.onSpeechError(utteranceId);
          currentRepeatTimes = 0;
        }
      }
    });
  }

  public void setSpeechListener(SpeechProgressListener listener) {
    this.listener = listener;
  }

  public void setLocale(Locale locale) {
    if (tts != null) {
      this.locale = locale;
      tts.setLanguage(locale);
    }
  }

  public void say(String something, String speechId) {
    if (tts == null) {
      //try init tts again.
      initTTS();
      if (listener != null)
        listener.onSpeechError(speechId);
      return;
    }
    currentSpeech = String.valueOf(something);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      ttsGreater21(something, speechId);
    }
    else {
      ttsUnder20(something, speechId);
    }
  }

  public void setRepeatTimes(int times) {
    repeatTimes = times;
  }

  public void setRepeatInterval(int milliseconds) {
    this.repeatInterval = milliseconds;
  }

  @SuppressWarnings("deprecation")
  private void ttsUnder20(String text, String messageId) {
    HashMap<String, String> map = new HashMap<>();
    map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, messageId);
    tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void ttsGreater21(String text, String messageId) {
    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, messageId);
  }


  public static interface SpeechProgressListener {
    public void onTextSpeakerInit();

    public void onSpeechStart(String speechId);

    public void onSpeechDone(String speechId);

    public void onSpeechError(String speechId);
  }
}
