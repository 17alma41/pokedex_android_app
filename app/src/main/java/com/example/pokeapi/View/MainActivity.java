package com.example.pokeapi.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pokeapi.Model.Pokemon;
import com.example.pokeapi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private List<Pokemon> pokemonList;
    private PokemonAdapter adapter;
    private RequestQueue queue;
    private TextView textName, pokemonOrder, pokemonType;
    private ImageView pokemonImage;
    private String pokemonCry = "";
    private Button btnSearch;
    private  Button backToMenuBtn;
    private EditText inputPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridViewPokemons);
        btnSearch = findViewById(R.id.btnSearch);
        inputPokemon = findViewById(R.id.inputPokemon);
        backToMenuBtn = findViewById(R.id.backToMenuBtn);

        // Inicializacion de la lista de pokemons para pasarsela al gridView como un adaptador
        pokemonList = new ArrayList<>();
        adapter = new PokemonAdapter(this, pokemonList);
        gridView.setAdapter(adapter);

        queue = Volley.newRequestQueue(this); // Inicializa las peticiones a la API


        getPokemonList();  // Llama a la función para obtener Pokémon desde la API

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pokemonName = inputPokemon.getText().toString().toLowerCase();
                if (!pokemonName.isEmpty()){
                    searchPokemon(pokemonName);
                }else if(pokemonName.isEmpty()){
                   getPokemonList();
                }else {
                    Toast.makeText(MainActivity.this, "Ingrese un nombre de Pokémon", Toast.LENGTH_SHORT).show();
                }

            }
        });

        backToMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * Obtiene la lista de Pokémon desde la API y la agrega al GridView.
     */
    private void getPokemonList() {
        String url = "https://pokeapi.co/api/v2/pokemon?limit=1025";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");

                            for (int i = 0; i < results.length(); i++) {
                                JSONObject pokemonObj = results.getJSONObject(i);
                                String name = pokemonObj.getString("name");
                                String detailsUrl = pokemonObj.getString("url");

                                getDetailsOfPokemonList(name, detailsUrl);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error al parsear JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(request);
    }

    /**
     * Obtiene detalles individuales de cada Pokémon para llenar el GridView.
     */
    private void getDetailsOfPokemonList(String name, String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int number = response.getInt("id");
                            String sprite = response.getJSONObject("sprites").getString("front_default");
                            String type = response.getJSONArray("types")
                                    .getJSONObject(0)
                                    .getJSONObject("type")
                                    .getString("name");

                            String cryUrl = response.has("cries") ? response.getJSONObject("cries").getString("latest") : "";


                            Pokemon pokemon = new Pokemon(name, number, type, sprite, cryUrl); //Aqui le paso la información al modelo
                            pokemonList.add(pokemon); // Instancia el objeto pokemon y lo añade al array
                            adapter.notifyDataSetChanged(); // Actualiza el GridView

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error obteniendo detalles de " + name, Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(request);
    }

    private void searchPokemon(String pokemonName){
        String url = "https://pokeapi.co/api/v2/pokemon/" + pokemonName;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int number = jsonObject.getInt("id");
                            String sprite = jsonObject.getJSONObject("sprites").getString("front_default");
                            String type = jsonObject.getJSONArray("types")
                                    .getJSONObject(0)
                                    .getJSONObject("type")
                                    .getString("name");

                            String cryUrl = jsonObject.has("cries") ? jsonObject.getJSONObject("cries").getString("latest") : "";

                            // Agregar el nuevo Pokémon a la lista y actualizar el GridView
                            pokemonList.clear(); // Limpiar lista para mostrar solo el Pokémon buscado
                            pokemonList.add(new Pokemon(pokemonName, number, type, sprite, cryUrl));
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Pokémon no encontrado", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }
}
