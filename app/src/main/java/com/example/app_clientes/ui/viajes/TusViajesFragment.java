package com.example.app_clientes.ui.viajes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.app_clientes.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TusViajesFragment extends Fragment{


    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener bottonNavMethod = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment fragment=null;
                    switch (menuItem.getItemId()){
                        case R.id.disfrutados:
                            fragment= new TusViajesDisfrutadosFragment();
                            break;
                        case R.id.publicados:
                            fragment= new TusViajesPublicadosFragment();
                            break;
                    }
                    getChildFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                    return true;
                }
            };


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tusviajesvacio, container, false);

        Fragment fragment= new TusViajesDisfrutadosFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();

        bottomNavigationView = view.findViewById(R.id.bottonNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottonNavMethod);
        return view;
    }






}
