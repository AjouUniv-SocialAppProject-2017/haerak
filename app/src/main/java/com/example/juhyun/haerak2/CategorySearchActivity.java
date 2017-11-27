package com.example.juhyun.haerak2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

public class CategorySearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_search);

        TabHost th = (TabHost) findViewById(R.id.categorytabhost);
        th.setup();

        TabHost.TabSpec dosp = th.newTabSpec("do_tab");
        dosp.setIndicator(getString(R.string.do_tab));
        dosp.setContent(R.id.do_tab);
        th.addTab(dosp);

        TabHost.TabSpec eatsp = th.newTabSpec("eat_tab");
        eatsp.setIndicator(getString(R.string.eat_tab));
        eatsp.setContent(R.id.eat_tab);
        th.addTab(eatsp);

        TabHost.TabSpec havesp = th.newTabSpec("have_tab");
        havesp.setIndicator(getString(R.string.have_tab));
        havesp.setContent(R.id.have_tab);
        th.addTab(havesp);

        TabHost.TabSpec watchsp = th.newTabSpec("watch_tab");
        watchsp.setIndicator(getString(R.string.watch_tab));
        watchsp.setContent(R.id.watch_tab);
        th.addTab(watchsp);

        TabHost.TabSpec gosp = th.newTabSpec("go_tab");
        gosp.setIndicator(getString(R.string.go_tab));
        gosp.setContent(R.id.go_tab);
        th.addTab(gosp);


    }
}
