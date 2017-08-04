package com.example.android.miwok;

/**
 * Created by Nokukhanya on 2017/07/11.
 */

public class Word {

    /**
     * Default Translation for te word
     **/
    private String mDefaulTranslation;
    /**
     * Miwork translation for the word
     */
    private String mMiworkTramslation;
    //Image resource ID for the word
    private int mImageResourceId =NO_IMAGE_PROVIDE;

    private static final int NO_IMAGE_PROVIDE=-1;

    private int mAudioResourceId;

    /**
     * create a new word object
     *
     * @param defaulTranslation is the word language the the user is already familiar with
     *                          (such as english)
     * @param miworkTranslation is the word in the Miwork language
     **/

    public Word (String defaulTranslation, String miworkTranslation, int audioResourceId) {
        mDefaulTranslation = defaulTranslation;
        mMiworkTramslation = miworkTranslation;
        mAudioResourceId = audioResourceId;
    }
    /**
     * create a new word object
     *
     * @param defaulTranslation is the word language the the user is already familiar with
     *                          (such as english)
     * @param miworkTranslation is the word in the Miwork language
     * @param imageResourceId   is the drawable resource ID for the image assets
     **/


    public Word(String defaulTranslation, String miworkTranslation, int imageResourceId, int audioResourceId) {
        mDefaulTranslation = defaulTranslation;
        mMiworkTramslation = miworkTranslation;
        mImageResourceId = imageResourceId;
        mAudioResourceId=audioResourceId;
    }

    // Get the default translation of the word

    public String getDefaultTranslation() {
        return mDefaulTranslation;
    }
    // Get the Miwork translation of the word

    public String getMiworkTranslation() {
        return mMiworkTramslation;
    }

    //Return the image resoue ID of the wodr.
    public int getmImageResourceId() {return mImageResourceId;

    }

    /**
     * Return whether or not there is an imsage gor this wors.
     **/

    public boolean hasImage(){
        return mImageResourceId!= NO_IMAGE_PROVIDE;}

    public int getmAudioResourceId(){
        return mAudioResourceId;}
}


