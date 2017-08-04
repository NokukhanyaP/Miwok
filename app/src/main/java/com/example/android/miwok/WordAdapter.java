package com.example.android.miwok;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Nokukhanya on 2017/07/11.
 */

public class WordAdapter extends ArrayAdapter<Word> {

    /**Resource ID for the background color for this list of words*/

    private int mColorResourceId;

    /**
     * create a new {@Link WordAdapter} object
     *
     * @param context is a current contex(i.eActivity) that the adapter is being created in
     *
     */


    public WordAdapter(Context context,ArrayList<Word>words,int colorResourceId){
        super(context,0,words);
        mColorResourceId=colorResourceId;
    }

    public WordAdapter(Context context, ArrayList<Word> words){super(context,0,words);}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Check if an existing view is being reused,otherwise inflate the view

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        Word currrentWord = getItem(position);

        TextView miworkTextView = (TextView) listItemView.findViewById(R.id.miwork_text_view);

        miworkTextView.setText(currrentWord.getMiworkTranslation());

        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.default_text_view);

        defaultTextView.setText(currrentWord.getDefaultTranslation());

        //find the imageView in the list-ITEM.XML LAYOUT WITH THE id IMAGE
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);

        if(currrentWord.hasImage()) {
            //set the imageView to image resource specified in the current word
            imageView.setImageResource(currrentWord.getmImageResourceId());

            //MAKE SURE THE VISIBILITY IS VISIBLE
            imageView.setVisibility(View.VISIBLE);

        }
        else{
            //Otherwise hide the imageView
            imageView.setVisibility(View.GONE);
        }
        //set the them color for the list item
        View textContainer= listItemView.findViewById(R.id.text_container);
        //Find the color that the resource ID maps
        int color= ContextCompat.getColor(getContext(), mColorResourceId);
        //Set the background color of the text contain view
        textContainer.setBackgroundColor(color);

        //return the whole list item layout(containing 2 textviews) so that it can be shown in the list view

        return listItemView;

    }
}
