package com.example.ratestation.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.example.ratestation.R;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.fragment.app.Fragment;


import com.example.ratestation.Adapters.AmigosAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentUserAmigos extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private EditText editTextSearchUser;
    private Button buttonSearchUser;
    private LinearLayout layoutSearchResults;
    private RecyclerView recyclerViewFriends;

    private AmigosAdapter amigosAdapter;
    private List<String> friendsList = new ArrayList<>();

    public FragmentUserAmigos() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_amigos, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextSearchUser = view.findViewById(R.id.editTextSearchUser);
        buttonSearchUser = view.findViewById(R.id.buttonSearchUser);
        layoutSearchResults = view.findViewById(R.id.layoutSearchResults);
        recyclerViewFriends = view.findViewById(R.id.recyclerViewFriends);

        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        amigosAdapter = new AmigosAdapter(friendsList);
        recyclerViewFriends.setAdapter(amigosAdapter);

        buttonSearchUser.setOnClickListener(v -> buscarUsuario());
        cargarAmigos();

        return view;
    }

    private void buscarUsuario() {
        String nombre = editTextSearchUser.getText().toString().trim();
        if (nombre.isEmpty()) {
            Toast.makeText(getContext(), "Introduce un nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        // Limpia resultados anteriores
        layoutSearchResults.removeAllViews();

        String currentUserId = mAuth.getCurrentUser().getUid();

        // Obtén la lista de amigos actual desde Firestore
        db.collection("usuarios").document(currentUserId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    // Conversión segura de la lista de amigos
                    List<String> friendsIds = new ArrayList<>();
                    Object friendsObj = documentSnapshot.get("friends");
                    if (friendsObj instanceof List<?>) {
                        for (Object obj : (List<?>) friendsObj) {
                            if (obj instanceof String) {
                                friendsIds.add((String) obj);
                            }
                        }
                    }

                    // Busca usuario por nombre
                    db.collection("usuarios")
                            .whereEqualTo("nombre", nombre)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        String foundUserId = document.getId();
                                        String foundUserName = document.getString("nombre");

                                        if (friendsIds.contains(foundUserId)) {
                                            // Si ya es amigo, solo mostramos el nombre
                                            TextView tv = new TextView(getContext());
                                            tv.setText(foundUserName + " ya es tu amigo");
                                            tv.setTextSize(16f);
                                            layoutSearchResults.addView(tv);
                                        } else {
                                            // Layout horizontal: nombre + botón
                                            LinearLayout itemLayout = new LinearLayout(getContext());
                                            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                                            itemLayout.setPadding(0, 8, 0, 8);

                                            TextView tv = new TextView(getContext());
                                            tv.setText(foundUserName);
                                            tv.setTextSize(16f);
                                            tv.setLayoutParams(new LinearLayout.LayoutParams(
                                                    0,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    1f)); // peso 1 para ocupar espacio

                                            Button btnAgregar = new Button(getContext());
                                            btnAgregar.setText("Agregar");
                                            btnAgregar.setOnClickListener(v -> agregarAmigo(foundUserId));

                                            itemLayout.addView(tv);
                                            itemLayout.addView(btnAgregar);

                                            layoutSearchResults.addView(itemLayout);
                                        }
                                    }
                                } else {
                                    TextView tv = new TextView(getContext());
                                    tv.setText("No se encontró ningún usuario con ese nombre");
                                    tv.setTextSize(16f);
                                    layoutSearchResults.addView(tv);
                                }
                            })
                            .addOnFailureListener(e -> Log.e("Firebase", "Error buscando usuario", e));
                })
                .addOnFailureListener(e -> Log.e("Firebase", "Error cargando amigos", e));
    }

    private void agregarAmigo(String friendId) {
        String currentUserId = mAuth.getCurrentUser().getUid();

        DocumentReference userRef = db.collection("usuarios").document(currentUserId);
        userRef.update("friends", com.google.firebase.firestore.FieldValue.arrayUnion(friendId))
                .addOnSuccessListener(aVoid -> {
                    db.collection("usuarios").document(friendId)
                            .update("friends", com.google.firebase.firestore.FieldValue.arrayUnion(currentUserId));

                    Toast.makeText(getContext(), "Amigo agregado correctamente", Toast.LENGTH_SHORT).show();
                    cargarAmigos();
                })
                .addOnFailureListener(e -> Log.e("Firebase", "Error agregando amigo", e));
    }


    private void cargarAmigos() {
        String currentUserId = mAuth.getCurrentUser().getUid();

        db.collection("usuarios").document(currentUserId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    friendsList.clear();
                    List<String> friendsIds = (List<String>) documentSnapshot.get("friends");

                    if (friendsIds != null && !friendsIds.isEmpty()) {
                        for (String id : friendsIds) {
                            db.collection("usuarios").document(id).get()
                                    .addOnSuccessListener(friendDoc -> {
                                        String friendName = friendDoc.getString("nombre");
                                        friendsList.add(friendName);
                                        amigosAdapter.notifyDataSetChanged();
                                    });
                        }
                    }
                });
    }
}









//package com.example.ratestation.Fragments;
//
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.ratestation.R;
//
//public class FragmentUserAmigos extends Fragment {
//
//    public FragmentUserAmigos() {
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_user_amigos, container, false);
//    }
//}