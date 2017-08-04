/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {
    private MediaPlayer mMediaPlayer;
    //Handles audio focus when playing a sound file
    private AudioManager mAudioManager;

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener=
            new AudioManager.OnAudioFocusChangeListener(){

                public void onAudioFocusChange (int focusChange) {
                    if (focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {

                        /**The AUDIOFOCUS_LOSS_TRANSIENT case means that we"ve lost audio focus short amount of time.
                         * AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means our app is allowed to continue playing sound but
                         * at a lower volume.when both cases the same way because our app is playing short sound file
                         */
                        //Pause playback and reset player to the start of the file. That way
                        //play the word from the begining when we resume playback

                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);

                    } else if (focusChange==AudioManager.AUDIOFOCUS_GAIN){
                        mMediaPlayer.start();
                    }else if (focusChange==AudioManager.AUDIOFOCUS_LOSS){
                        releaseMediaPlayer();
                    }
                }
            };

    private MediaPlayer.OnCompletionListener mCompletionListener= new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion (MediaPlayer mediaPlayer){
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);


        // create and setup the {@link AudioManager} to request audio focus
        mAudioManager= (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        //creating an array of words

     final ArrayList<Word> words=new ArrayList<Word>();

        words.add(new Word("Where are you going?","minto wuksusu",R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?","tinne oyeese'ne",R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is.....","oyaaset",R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?","michekses?",R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I'm feeling good","kuchi achit",R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?","eenes'aa",R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I'm coming","hee'eenem",R.raw.phrase_yes_im_coming));
        words.add(new Word("I'm coming?","eenem",R.raw.phrase_im_coming));
        words.add(new Word("Let's go","yoowitis",R.raw.phrase_lets_go));
        words.add(new Word("Come here.","enni'nem",R.raw.phrase_come_here));

        //Fimd the root view of the whole layout

        WordAdapter adapter= new WordAdapter(this, words, R.color.category_phrases );

        ListView listView=(ListView)findViewById(R.id.list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = words.get(position);
                // release media player if it currently esists because we are about to play different sound files
                releaseMediaPlayer();

                //Request audio focus for playback
                int result= mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        //use the music stream
                        AudioManager.STREAM_MUSIC,
                        //Request pemanent focus.
                        AudioManager.AUDIOFOCUS_GAIN);

                if (result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //  mAudioManager.registerMediaButtonEvenReceiver(RemoteControlReceiver);
                    //Start playback
                    mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, word.getmAudioResourceId());
                    //start the audio file
                    mMediaPlayer.start();

                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });
    }

    // clean up the media player by releasing its resources

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {

            mMediaPlayer.release();

            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
};