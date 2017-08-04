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

public class FamilyActivity extends AppCompatActivity {
    private MediaPlayer mMediaPlayer;
    //Handles audio focus when playing a sound file
    private AudioManager mAudioManager;

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {

                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        //play the word from the begining when we resume playback

                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);

                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        mMediaPlayer.start();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        releaseMediaPlayer();
                    }
                }
            };

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        // create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        //creating an array of words

        final ArrayList<Word> words = new ArrayList<Word>();

        words.add(new Word("Father", "Epe", R.drawable.family_father, R.raw.family_father));
        words.add(new Word("Mother", "Eta", R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word("Son", "Angsi", R.drawable.family_son, R.raw.family_son));
        words.add(new Word("Daughter", "Tune", R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word("Old brother", "Taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
        words.add(new Word("Young brother", "Chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word("Old sister", "Tete", R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Word("Young sister", "Kollliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        words.add(new Word("Grandmother", "Ama", R.drawable.family_grandmother, R.raw.family_grandmother));
        words.add(new Word("Grandfather", "Paapa", R.drawable.family_grandfather, R.raw.family_grandfather));

        //Find the root view of the whole layout

        WordAdapter adapter = new WordAdapter(this, words, R.color.category_family);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = words.get(position);
                // release media player if it currently esists because we are about to play different sound files
                releaseMediaPlayer();

                //Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        //use the music stream
                        AudioManager.STREAM_MUSIC,
                        //Request pemanent focus.
                        AudioManager.AUDIOFOCUS_GAIN);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //  mAudioManager.registerMediaButtonEvenReceiver(RemoteControlReceiver);
                    //Start playback
                    mMediaPlayer = MediaPlayer.create(FamilyActivity.this, word.getmAudioResourceId());
                    //start the audio file
                    mMediaPlayer.start();

                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });
    }
        // clean up the media player by releasing its resources

    private void releaseMediaPlayer() {
        //When media player is not null then it may currently play sound
        if (mMediaPlayer != null) {

            mMediaPlayer.release();

            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}

