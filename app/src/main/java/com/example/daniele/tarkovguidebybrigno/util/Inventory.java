package com.example.daniele.tarkovguidebybrigno.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.daniele.tarkovguidebybrigno.DBHELPER;
import com.example.daniele.tarkovguidebybrigno.R;

public class Inventory extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        ListAdapter mAdapter;
        DBHELPER dbhelper = new DBHELPER(getBaseContext());
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String[] projection = {FeedReaderContract.FeedEntry.COLUMN_NAME_OBJECT};
        Log.d("dato", FeedReaderContract.FeedEntry.COLUMN_NAME_OBJECT);
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_OBJECT + " = ?";
        String[] selectionArgs = {" * "};

        Cursor cursor = db.query(FeedReaderContract.FeedEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
        Log.d("query",""+cursor.getCount());
        String[] items = new String[cursor.getCount()];
        int i = 0;
        while(cursor.moveToNext()){
          String temp = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_OBJECT));
            items[i] = temp;
            Log.d("string",items[i]);
            i++;
        }

        ListView lw = (ListView) findViewById(R.id.listview);
        mAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                items
        );
        lw.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lw.setAdapter(mAdapter);

    }



}

final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "inventory";
        public static final String COLUMN_NAME_OBJECT = "oggetto";
        public static final String COLUMN_CHECK = "check";
    }
}