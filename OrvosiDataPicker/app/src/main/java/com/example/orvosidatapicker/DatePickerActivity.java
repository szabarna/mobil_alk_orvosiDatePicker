package com.example.orvosidatapicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Objects;

public class DatePickerActivity extends AppCompatActivity {

    private static final String LOG_TAG = DatePickerActivity.class.getName();
    private static final String PREF_KEY = Objects.requireNonNull(DatePickerActivity.class.getPackage()).toString();

    private SharedPreferences userPreferences;
    private FirebaseUser user;


    private RecyclerView mRecyclerView;
    private ArrayList<DataItem> mItems;
    private DataItemAdapter mAdapter;

    private FrameLayout redCircle;
    private TextView contentTextView;

    private int gridNumber = 1;
    private int dateNumbers = 0;

    private NotificationHandler mNotificationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        userPreferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            Log.i(LOG_TAG, "Authenticated User!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated User!");
            finish();
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber ));
        mItems = new ArrayList<>();

        mAdapter = new DataItemAdapter(this, mItems);

        mRecyclerView.setAdapter(mAdapter);

        initializeData();

        mNotificationHandler = new NotificationHandler(this);

    }

    private void initializeData() {
        String[] itemsName = getResources().getStringArray(R.array.item_names);
        String[] itemsType = getResources().getStringArray(R.array.item_types);
        TypedArray itemsRating = getResources().obtainTypedArray(R.array.item_rates);


        mItems.clear();

        for(int i = 0; i < itemsName.length; i++) {
                mItems.add(new DataItem(itemsName[i], itemsType[i], itemsRating.getFloat(i, 0) ));
        }

        itemsRating.recycle();


        mAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.item_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {   return false;    }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(LOG_TAG, newText);
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.appointments:
                Log.i(LOG_TAG, "Dates clicked!");
                return true;
            case R.id.logout_button:
                Log.i(LOG_TAG, "Logout clicked!");
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.appointments);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        contentTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(alertMenuItem);
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    public void updateAlertIcon() {
            dateNumbers = (dateNumbers + 1);
            mNotificationHandler.send("New Appointment Added!");

            String main = String.valueOf(dateNumbers);
            String other = "";
            if(0 < dateNumbers) {
                contentTextView.setText(main);
            } else {
                contentTextView.setText(other);
            }
    }
}