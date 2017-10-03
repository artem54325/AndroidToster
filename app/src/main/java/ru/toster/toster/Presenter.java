package ru.toster.toster;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import ru.toster.toster.fragmentTab.QuestionFragment;

public interface Presenter {
    void getHttp();
    void viewsPresent(String html);
}
