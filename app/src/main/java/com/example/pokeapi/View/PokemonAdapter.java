package com.example.pokeapi.View;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pokeapi.Model.Pokemon;
import com.example.pokeapi.R;

import java.io.IOException;
import java.util.List;

public class PokemonAdapter extends BaseAdapter {
    private Context context;
    private List<Pokemon> pokemons;
    private MediaPlayer mediaPlayer;

    public PokemonAdapter(Context context, List<Pokemon> pokemons) {
        this.context = context;
        this.pokemons = pokemons;
    }

    @Override
    public int getCount() {
        return pokemons.size();
    }

    @Override
    public Object getItem(int position) {
        return pokemons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_pokemon, parent, false);
        }

        ImageView imageView = view.findViewById(R.id.imagePokemon);
        TextView pokeName = view.findViewById(R.id.pokeName);
        TextView pokeNumber = view.findViewById(R.id.pokeNumber);
        TextView pokeType = view.findViewById(R.id.pokeType);
        ImageButton btnPlaySound = view.findViewById(R.id.btnPlaySound);

        // Obtén el Pokémon correspondiente
        Pokemon pokemon = pokemons.get(position);

        Glide.with(context)
                .load(pokemon.getImageUrl())
                .into(imageView);

        pokeName.setText(pokemon.getName());
        pokeNumber.setText("Nº " + pokemon.getNumber());
        pokeType.setText("Tipo: " + pokemon.getType());

        // Botón de sonido
        btnPlaySound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Libera el MediaPlayer si ya está en uso
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(pokemon.getSound());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error al reproducir sonido", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
