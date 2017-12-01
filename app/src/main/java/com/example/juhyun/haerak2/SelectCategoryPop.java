package com.example.juhyun.haerak2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * Created by juhyun on 2017. 11. 18..
 */

public class SelectCategoryPop extends AppCompatActivity {

    private ImageButton cate_do, cate_eat, cate_watch, cate_have, cate_go;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectcategorypop);

        cate_do = (ImageButton) findViewById(R.id.docateButton);
        cate_eat = (ImageButton) findViewById(R.id.eatcateButton);
        cate_watch = (ImageButton) findViewById(R.id.watchcateButton);
        cate_have = (ImageButton) findViewById(R.id.havecateButton);
        cate_go = (ImageButton) findViewById(R.id.gocateButton);
    }

    public void selectCategory(View v){
        String categoryNum = v.getTag().toString();
        Intent intent = new Intent();
        intent.putExtra("category", categoryNum);
        setResult(RESULT_OK, intent);
        finish();
    }
}
