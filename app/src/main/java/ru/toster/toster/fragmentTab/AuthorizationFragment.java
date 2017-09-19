package ru.toster.toster.fragmentTab;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.toster.toster.R;


public class AuthorizationFragment extends Fragment {//https://id.tmtm.ru/login/?consumer=toster&state=9d3b371d-5ed0-4544-bda0-ce521dbb7732

//    query String parameters

    public AuthorizationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authorization, container, false);
        return view;
    }
}
