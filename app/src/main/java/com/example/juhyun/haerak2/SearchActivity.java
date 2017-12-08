package com.example.juhyun.haerak2;


import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private SearchView searchView;
    private String user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = getIntent().getStringExtra("user");
        searchView = (SearchView)findViewById(R.id.searchText);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Fragment fragment = new SearchListLayout();
                Bundle arg = new Bundle();
                arg.putString("search", query);
                arg.putString("user", user);
                fragment.setArguments(arg);
                getSupportFragmentManager().beginTransaction().replace(R.id.search_category, fragment).commit();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        Button gobutton = (Button) findViewById(R.id.category_go_button);
        Button dobutton = (Button) findViewById(R.id.category_do_button);
        Button watchbutton = (Button) findViewById(R.id.category_watch_button);
        Button eatbutton = (Button) findViewById(R.id.category_eat_button);
        Button havebutton = (Button) findViewById(R.id.category_have_button);

        gobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, CategorySearchActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("tabIndex", 4);
                SearchActivity.this.startActivity(intent);
            }
        });

        dobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, CategorySearchActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("tabIndex", 0);
                SearchActivity.this.startActivity(intent);
            }
        });

        watchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, CategorySearchActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("tabIndex", 2);
                SearchActivity.this.startActivity(intent);
            }
        });

        eatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, CategorySearchActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("tabIndex", 1);
                SearchActivity.this.startActivity(intent);
            }
        });

        havebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, CategorySearchActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("tabIndex", 3);
                SearchActivity.this.startActivity(intent);
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
            {
                super.onBackPressed();
                return true;
            }

//            case R.id.action_search:
//                //검색부분 구현
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //    View v;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        v = inflater.inflate(R.layout.activity_search, container, false);
//        return v;
//    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        getActivity().setTitle("검색");
//    }

}
