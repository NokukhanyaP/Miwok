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

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.media.AudioManager.AUDIOFOCUS_LOSS;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;

public class NumbersActivity extends AppCompatActivity {
    //Handles playback of all the sound file
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
    private MediaPlayer.OnCompletionListener mCompletionListener= (new MediaPlayer.OnCompletionListener() {
        @Override
                public void onCompletion (MediaPlayer mediaPlayer){
            releaseMediaPlayer();
        }
    });

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.word_list);
            // create and setup the {@link AudioManager} to request audio focus
            mAudioManager= (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            //creating an array of words
            final ArrayList<Word> words = new ArrayList<Word>();
            words.add(new Word("One","Lutti",R.drawable.number_one,R.raw.number_one));
            words.add(new Word("Two", "Otiiko", R.drawable.number_two, R.raw.number_two));
            words.add(new Word("Three", "Tolookosu", R.drawable.number_three, R.raw.number_three));
            words.add(new Word("Four", "Oyyisa", R.drawable.number_four, R.raw.number_four));
            words.add(new Word("Five", "Massokka", R.drawable.number_five, R.raw.number_five));
            words.add(new Word("Six", "Temmokka", R.drawable.number_six, R.raw.number_six));
            words.add(new Word("Seven", "Kenekaku", R.drawable.number_seven, R.raw.number_seven));
            words.add(new Word("Eight", "Kawinta", R.drawable.number_eight, R.raw.number_eight));
            words.add(new Word("Nine", "Wo'e", R.drawable.number_nine, R.raw.number_nine));
            words.add(new Word("Ten", "Na'aacha", R.drawable.number_ten, R.raw.number_ten));

            //Fimd the root view of the whole layout
            WordAdapter adapter = new WordAdapter(this, words, R.color.category_numbers);

            ListView listView = (ListView) findViewById(R.id.list);

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
                        mMediaPlayer = MediaPlayer.create(NumbersActivity.this, word.getmAudioResourceId());
                        //start the audio file
                        mMediaPlayer.start();

                        mMediaPlayer.setOnCompletionListener(mCompletionListener);
                    }
                }
            });
        }
        private void releaseMediaPlayer() {
            if (mMediaPlayer != null) {
                mMediaPlayer.release();
                mMediaPlayer = null;
                //abandon audio focus when playback complete
                mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
            }
        }
    };
