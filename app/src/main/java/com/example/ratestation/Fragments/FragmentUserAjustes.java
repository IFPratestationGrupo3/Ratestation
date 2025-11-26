package com.example.ratestation.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.ratestation.Activities.Login.Activity_Login;
import com.example.ratestation.R;
import com.google.firebase.auth.FirebaseAuth;


public class FragmentUserAjustes extends Fragment {
    protected RadioGroup fontSizeRadioGroup;
    protected RadioButton fontNormal;
    protected RadioButton fontLarge;

    protected Button btnCerrarSesion;
    private FirebaseAuth mAuth; // Variable para la autenticación de Firebase

    public FragmentUserAjustes() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_ajustes, container, false);

        // --- MODO OSCURO ---
        SwitchCompat darkModeSwitch = view.findViewById(R.id.darkModeSwitch);
        int currentNightMode = AppCompatDelegate.getDefaultNightMode();
        darkModeSwitch.setChecked(currentNightMode == AppCompatDelegate.MODE_NIGHT_YES);

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            requireActivity().recreate(); // Recargar para aplicar tema
        });

        // --- TAMAÑO DE FUENTE ---
        fontSizeRadioGroup = view.findViewById(R.id.fontSizeRadioGroup);
        fontNormal = view.findViewById(R.id.fontNormal);
        fontLarge = view.findViewById(R.id.fontLarge);

        // Recarga preferencia guardada
        SharedPreferences prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        String fontSize = prefs.getString("font_size", "normal");

        if (fontSize.equals("large")) {
            fontLarge.setChecked(true);
        } else {
            fontNormal.setChecked(true);
        }

        fontSizeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            SharedPreferences.Editor editor = prefs.edit();
            if (checkedId == R.id.fontLarge) {
                editor.putString("font_size", "large");
            } else {
                editor.putString("font_size", "normal");
            }
            editor.apply();
            requireActivity().recreate(); // Recargar para aplicar tamaño de fuente
        });


        //-- BOTÓN CERRAR SESIÓN --

        // Se inicializa Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        btnCerrarSesion = (Button) view.findViewById(R.id.btnCerrarSesion);

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();

            }

            private void cerrarSesion(){
                mAuth.signOut();


                // Usamos getActivity() para obtener el contexto de la actividad que contiene el fragmento
                Intent intent = new Intent(getActivity(), Activity_Login.class);

                // 6. Se Limpia el historial de actividades para que el usuario no pueda volver atrás y el logout sea correcto
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);

                if (getActivity() != null) {
                    getActivity().finish();
                }
            }

        });

        return view;
    }
}