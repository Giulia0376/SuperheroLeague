package com.example.superheroleague.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.superheroleague.R
import com.example.superheroleague.adapters.SuperheroAdapter
import com.example.superheroleague.data.Superhero
import com.example.superheroleague.utils.SuperheroService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: SuperheroAdapter

    var superheroList: List<Superhero> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)

        adapter = SuperheroAdapter(superheroList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this,2)

        searchSuperheroes("a")

        }

    fun searchSuperheroes(query:String) {
        // Llamada en un hilo secundario
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = SuperheroService.getInstance()
                val response = service.findSuperheroesByName(query)
                superheroList = response.results

                // Volvemos al hilo principal
                CoroutineScope(Dispatchers.Main).launch {
                    adapter.updateItems(superheroList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}