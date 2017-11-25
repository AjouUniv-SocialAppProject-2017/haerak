package com.example.juhyun.haerak2;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private User user;
    private TextView userid;
    private NavigationView navigationView;
    private GridView gridView;
    private BucketListAdapter adapter;
    private DatabaseReference databaseReference;
    private LinearLayout item;

    private ValueEventListener getbucketList = new ValueEventListener(){

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                String key = snapshot.getKey();
                Bucket bucket = snapshot.getValue(Bucket.class);

                adapter.addBucket(key, bucket.getTitle(), bucket.getWriter(), bucket.getLimitNumber());
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = (User) getIntent().getSerializableExtra("user");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View view) {
                if(user == null){
                    Toast.makeText(getApplicationContext(), "로그인 후 이용가능합니다!", Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(getApplicationContext(),AddBucketActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //displaySelectedScreen(R.id.content_main);

        navigationView = (NavigationView)findViewById(R.id.nav_view);
        View navigation_header = navigationView.getHeaderView(0);
        userid = (TextView)navigation_header.findViewById(R.id.profile_userId);

        if(user == null){
            //둘러보기일 경우,
            userid.setText("로그인 후 이용해주세요.");
        }else{
            userid.setText(user.getNickName());
        }

        adapter = new BucketListAdapter();

        gridView = (GridView) findViewById(R.id.all_bucketList);
        gridView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Buckets");
        databaseReference.addListenerForSingleValueEvent(getbucketList);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in Android Manifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_search){
//            Fragment fragment Ac= new SearchActivity();
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.content_main, fragment).commit();

            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            this.startActivity(intent);

            return true;
        }

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }


    private void displaySelectedScreen(int id){
        Fragment fragment = null;
            switch(id){
                case R.id.nav_home:
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    this.startActivity(intent);
                    break;
                case R.id.nav_mylist_layout:
                    fragment = new MylistLayout();
                    break;
                case R.id.nav_grouplist_layout:
                    fragment = new GrouplistLayout();
                    break;
            }

        if(fragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        FragmentManager manager = getFragmentManager();
//
//        if (id == R.id.nav_mylist_layout) {
//            manager.beginTransaction().replace(R.id.content_main, new MylistLayout()).commit();
//        } else if (id == R.id.nav_grouplist_layout) {
//            manager.beginTransaction().replace(R.id.content_main, new GrouplistLayout()).commit();
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
        displaySelectedScreen(id);


        return true;
    }

}
