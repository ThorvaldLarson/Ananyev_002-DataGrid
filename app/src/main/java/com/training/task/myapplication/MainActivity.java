package com.training.task.myapplication;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GridLayout container = (GridLayout) findViewById(R.id.container);

        if (Build.VERSION.SDK_INT < 21) {
            final ArrayList<TextView> cells = createCells(container, new SpecCreatorBeforeApi21());

            container.post(new Runnable() {
                @Override
                public void run() {
                    int margin = (int) getResources().getDimension(R.dimen.data_cell_margin) * 2;
                    int cols = getResources().getInteger(R.integer.data_columns);
                    int rows = getResources().getInteger(R.integer.data_rows);
                    int width = container.getWidth() / cols - margin;
                    int height = container.getHeight() / rows - margin;

                    for (TextView textview: cells) {
                        textview.setWidth(width);
                        textview.setHeight(height);
                    }
                }
            });
        } else {
            createCells(container, new SpecCreator());
        }

        findViewById(R.id.empty).setOnClickListener(this);
        findViewById(R.id.dial).setOnClickListener(this);
        findViewById(R.id.sms).setOnClickListener(this);
    }

    private ArrayList<TextView> createCells(GridLayout container, ISpecCreator specCreator) {
        ArrayList<TextView> cells = new ArrayList<>();
        int cols = getResources().getInteger(R.integer.data_columns);
        int rows = getResources().getInteger(R.integer.data_rows);
        TextView textView;
        GridLayout.LayoutParams layoutParams;
        int margin = (int) getResources().getDimension(R.dimen.data_cell_margin);

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                layoutParams = new GridLayout.LayoutParams();
                layoutParams.columnSpec = specCreator.getSpec(i);
                layoutParams.rowSpec = specCreator.getSpec(j);
                layoutParams.setMargins(margin, margin, margin, margin);

                textView = (TextView) View.inflate(this, R.layout.cell, null);
                textView.setLayoutParams(layoutParams);

                cells.add(textView);
                container.addView(textView);
            }
        }

        return cells;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(this, R.string.settings, Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;

        switch (view.getId()) {
            case R.id.dial:
                intent = new Intent(Intent.ACTION_DIAL);
                break;
            case R.id.empty:
                intent = new Intent(this, SecondaryActivity.class);
                break;
            case R.id.sms:
                try {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setType("vnd.android-dir/mms-sms");
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    String smsDefaultApp = Settings.Secure.getString(getContentResolver(), "sms_default_application");
                    PackageManager pm = getPackageManager();
                    intent = pm.getLaunchIntentForPackage(smsDefaultApp);
                }
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}