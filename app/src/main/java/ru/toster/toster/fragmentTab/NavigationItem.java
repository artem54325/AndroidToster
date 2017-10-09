package ru.toster.toster.fragmentTab;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ru.toster.toster.NewsActivity;


public class NavigationItem implements NavigationView.OnNavigationItemSelectedListener {
    private AppCompatActivity activity;

    public NavigationItem(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(activity.getApplicationContext(), NewsActivity.class);
        intent.putExtra("id", item.getItemId());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        return true;
    }
}
