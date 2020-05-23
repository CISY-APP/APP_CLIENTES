package com.example.app_clientes.ui.viajes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_clientes.R;
import com.example.app_clientes.adapter.MiAdapterTusViajes;
import com.example.app_clientes.pojos.Usuario;
import com.example.app_clientes.pojos.Viaje;
import com.example.app_clientes.ui.vehiculos.VehiculosFragment;
import com.example.app_clientes.vistas.VentanaChatIndividual;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

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
