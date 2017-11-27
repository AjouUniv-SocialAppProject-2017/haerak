package com.example.juhyun.haerak2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class RealBucketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_bucket);

        TabHost th = (TabHost) findViewById(R.id.categorytabhost);
        th.setup();

        TabHost.TabSpec infosp = th.newTabSpec("info_tab");
        infosp.setIndicator(getString(R.string.info_tab));
        infosp.setContent(R.id.info_tab);
        th.addTab(infosp);

        TabHost.TabSpec communitysp = th.newTabSpec("community_tab");
        communitysp.setIndicator(getString(R.string.community_tab));
        communitysp.setContent(R.id.community_tab);
        th.addTab(communitysp);

    }
}
