package com.ViktorDikov.BetrayalCharacterStats;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;


public class ReorderCharsActivity extends FragmentActivity 
{
    private String mTag = "reorderCharsFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reorder_chars);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.char_item, new ReorderCharsFragment(), mTag).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_character_reorder, menu);
        return true;
    }
}
