
package com.lordcard.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.lordcard.ui.base.BaseActivity;
import com.lordcard.ui.personal.PersonnalDoudizhuActivity;
import com.ylly.playcard.R;

public class SelectBaseScoreActivity extends BaseActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_base_score);

        findViewById(R.id.base_two).setOnClickListener(this);
        findViewById(R.id.base_four).setOnClickListener(this);
        findViewById(R.id.base_six).setOnClickListener(this);
        findViewById(R.id.base_eight).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent(this, PersonnalDoudizhuActivity.class);
        switch (id) {
            case R.id.base_two:
                intent.putExtra(PersonnalDoudizhuActivity.TRANS_NAME, 200);
                break;
            case R.id.base_four:
                intent.putExtra(PersonnalDoudizhuActivity.TRANS_NAME, 400);
                break;
            case R.id.base_six:
                intent.putExtra(PersonnalDoudizhuActivity.TRANS_NAME, 600);
                break;
            case R.id.base_eight:
                intent.putExtra(PersonnalDoudizhuActivity.TRANS_NAME, 800);
                break;
            default:
                break;
        }

        startActivity(intent);
    }

}
