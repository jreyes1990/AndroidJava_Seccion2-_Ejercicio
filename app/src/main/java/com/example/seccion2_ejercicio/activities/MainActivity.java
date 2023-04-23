package com.example.seccion2_ejercicio.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.seccion2_ejercicio.R;
import com.example.seccion2_ejercicio.adapters.FruitAdapter;
import com.example.seccion2_ejercicio.models.Fruit;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
  // ListView, GridView y Adapter
  private ListView listView;
  private GridView gridView;
  private FruitAdapter adapterListView;
  private FruitAdapter adapterGridView;

  // Lista de nuestro modelo, fruta
  private List<Fruit> fruits;

  // Items en el option menu
  private MenuItem itemListView;
  private MenuItem itemGridView;

  // Variables
  private int counter = 0;
  private final int SWITCH_TO_LIST_VIEW = 0;
  private final int SWITCH_TO_GRID_VIEW = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Mostrar icono
    this.enforceIconBar();

    this.fruits = getAllFruits();

    this.listView = (ListView) findViewById(R.id.listView);
    this.gridView = (GridView) findViewById(R.id.gridView);

    // Adjuntamos el mismo metodo click para ambos
    this.listView.setOnItemClickListener(this);
    this.gridView.setOnItemClickListener(this);

    this.adapterListView = new FruitAdapter(this, R.layout.list_view_item_fruit, fruits);
    this.adapterGridView = new FruitAdapter(this, R.layout.grid_view_item_fruit, fruits);

    this.listView.setAdapter(adapterListView);
    this.gridView.setAdapter(adapterGridView);

    // Registrar el context menu para ambos
    registerForContextMenu(this.listView);
    registerForContextMenu(this.gridView);
  }

  private void enforceIconBar(){
    getSupportActionBar().setIcon(R.mipmap.ic_launcher_web);
    getSupportActionBar().setDisplayUseLogoEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
  }

  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
    this.clickFruit(fruits.get(position));
  }

  private void clickFruit(Fruit fruit){
    // Diferenciamos entre las frutas conocidas y desconocidas
    if (fruit.getOrigin().equals("Unknown")){
      Toast.makeText(this, "Sorry, we don't have many info about "+ fruit.getName(), Toast.LENGTH_SHORT).show();
    }else{
      Toast.makeText(this, "The best fruit from "+ fruit.getOrigin(), Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflamos la option menu con nuestro layout
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.option_menu, menu);

    // Despues de inflar, recogemos las referencias a los botones que nos interesan
    this.itemListView = menu.findItem(R.id.list_view);
    this.itemGridView = menu.findItem(R.id.grid_view);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    // Eventos para los clicks en los botones del options menu
    switch (item.getItemId()){
      case R.id.add_fruit:
        this.addFruit(new Fruit("Added no. "+(++counter), R.mipmap.ic_new_fruit_web, "Unknown"));
        return true;
      case R.id.list_view:
        this.switchListGridView(this.SWITCH_TO_LIST_VIEW);
        return true;
      case R.id.grid_view:
        this.switchListGridView(this.SWITCH_TO_GRID_VIEW);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);

    // Inflamos el context menu con nuestro layout
    MenuInflater inflater = getMenuInflater();

    // Antes de inflar, le aniadimos el header dependiendo del objeto que se pinche
    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
    menu.setHeaderTitle(this.fruits.get(info.position).getName());

    // Inflamos
    inflater.inflate(R.menu.context_menu_fruits, menu);
  }

  @Override
  public boolean onContextItemSelected(@NonNull MenuItem item) {
    // Obtener info en el context menu del objeto que se pinche
    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

    switch (item.getItemId()){
      case R.id.delete_fruit:
        this.deleteFruit(info.position);
        return true;
      default:
        return super.onContextItemSelected(item);
    }
  }

  @Override
  public void onPointerCaptureChanged(boolean hasCapture) {
    super.onPointerCaptureChanged(hasCapture);
  }

  // CRUD actions - Get, Add, Delete
  private List<Fruit> getAllFruits(){
    List<Fruit> list = new ArrayList<Fruit>(){
      {
        add(new Fruit("Banana", R.mipmap.ic_banana_web, "Gran Canaria"));
        add(new Fruit("Strawberry", R.mipmap.ic_strawberry_web, "Huelva"));
        add(new Fruit("Orange", R.mipmap.ic_orange_web, "Sevilla"));
        add(new Fruit("Apple", R.mipmap.ic_apple_web, "Madrid"));
        add(new Fruit("Cherry", R.mipmap.ic_cherry_web, "Galicia"));
        add(new Fruit("Pear", R.mipmap.ic_pear_web, "Zaragoza"));
        add(new Fruit("Raspberry", R.mipmap.ic_raspberry_web, "Barcelona"));
      }
    };

  return list;
  }

  private void switchListGridView(int option){
    // Metodo para cambiar entre Grid/List view
    if (option == SWITCH_TO_LIST_VIEW){
      // Si queremos cambiar a list view, y el list view esta en modo invisible .....
      if (this.listView.getVisibility() == View.INVISIBLE){
        // Escondemos el grid view, y enseniamos su boton en el menu de opciones
        this.gridView.setVisibility(View.INVISIBLE);
        this.itemGridView.setVisible(true);

        // No olvidemos enseniar el list view, y esconder su boton en el menu de opciones
        this.listView.setVisibility(View.VISIBLE);
        this.itemListView.setVisible(false);
      }
    }else if (option == SWITCH_TO_GRID_VIEW){
      // Si queremos cambiar el grid view, y el grid view esta en modo invisible
      if (this.gridView.getVisibility() == View.INVISIBLE){
        // Escondemos el list view, y enseniamos su boton en el menu de opciones
        this.listView.setVisibility(View.INVISIBLE);
        this.itemListView.setVisible(true);

        // No olvidemos enseniar el grid view, y esconder su boton en el menu de opciones
        this.gridView.setVisibility(View.VISIBLE);
        this.itemGridView.setVisible(false);
      }
    }
  }
  private void addFruit(Fruit fruit){
    this.fruits.add(fruit);

    // Avisamos del cambio en ambos adapters
    this.adapterListView.notifyDataSetChanged();
    this.adapterGridView.notifyDataSetChanged();
  }

  private void deleteFruit(int position){
    this.fruits.remove(position);

    // Avisamos del cambio en ambos adapters
    this.adapterListView.notifyDataSetChanged();
    this.adapterGridView.notifyDataSetChanged();
  }
}