package com.seveneow.textspeaker.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.seveneow.textspeaker.TextSpeaker;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TextSpeaker.SpeechProgressListener, SampleRecyclerViewAdapter.OnClickListener {
  private TextSpeaker textSpeaker;
  private RecyclerView recyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    textSpeaker = new TextSpeaker(this, this);
    textSpeaker.setRepeatTimes(0);
    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b){
          textSpeaker.setRepeatTimes(2);
        }else{
          textSpeaker.setRepeatTimes(0);
        }
      }
    });

    SampleRecyclerViewAdapter adapter = new SampleRecyclerViewAdapter(this, getSampleList());
    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    recyclerView.setLayoutManager(layoutManager);
    adapter.setClickListener(this);
    recyclerView.setAdapter(adapter);
  }

  @Override
  public void onTextSpeakerInit() {
  }

  @Override
  public void onSpeechStart(String speechId) {

  }

  @Override
  public void onSpeechDone(String speechId) {

  }

  @Override
  public void onSpeechError(String speechId) {

  }

  @Override
  public void onItemClick(String message) {
    textSpeaker.say(message, "345");
  }

  private ArrayList<String> getSampleList(){
    ArrayList<String> sampleList = new ArrayList<>();
    sampleList.add("hello");
    sampleList.add("voice");
    sampleList.add("hi");
    sampleList.add("louisa");
    sampleList.add("Jervis");
    sampleList.add("Jia");
    sampleList.add("sai");
    return sampleList;
  }
}
