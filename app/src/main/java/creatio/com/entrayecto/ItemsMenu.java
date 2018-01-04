package creatio.com.entrayecto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import creatio.com.entrayecto.Adapters.ADContact;
import creatio.com.entrayecto.Adapters.ADItemMenu;
import creatio.com.entrayecto.Adapters.ADMenu;
import creatio.com.entrayecto.Objects.OContact;
import creatio.com.entrayecto.Objects.OItemMenu;
import creatio.com.entrayecto.Objects.OMenu;

public class ItemsMenu extends AppCompatActivity {
    private ListView list_item;
    private ArrayList<OItemMenu> data = new ArrayList<>();
    private SharedPreferences prefs;
    private Context context;
    private Bundle extras;
    private String ID_categorie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = ItemsMenu.this;
        extras = getIntent().getExtras();
        ID_categorie = extras.getString("ID_categorie","0");
        LayoutInflater myinflater = getLayoutInflater();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        list_item = findViewById(R.id.list_items);
        //Elementos
        ViewGroup myHeader = (ViewGroup) myinflater.inflate(R.layout.ad_item_header, list_item, false);
        TextView txtTitle = myHeader.findViewById(R.id.txtTitle);
        TextView txtDesc = myHeader.findViewById(R.id.txtDesc);
        txtTitle.setText(extras.getString("name",""));
        txtDesc.setText(extras.getString("description",""));
        list_item.addHeaderView(myHeader, null, false);
        final ImageView bgImage = findViewById(R.id.bgImage);
        getSupportActionBar().setTitle("");
        Glide.with(context)
                .load(extras.getString("image",""))
                .asBitmap()
                .centerCrop()
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(new SimpleTarget<Bitmap>(1600, 800) {

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Bitmap bitmap = resource;
                        bgImage.setImageBitmap(bitmap);
                    }
                });

        ViewCompat.setNestedScrollingEnabled(list_item, true);
        ADItemMenu adapter = new ADItemMenu(context,data,ItemsMenu.this);
        list_item.setAdapter(adapter);
        GetItemsMenu();
    }
    public void GetItemsMenu() {
        data.clear();
        AndroidNetworking.post("http://api.entrayecto.com/GetItemsMenu")
                .addBodyParameter("ID_categorie", ID_categorie)
                .addBodyParameter("apikey", "QuBvJ3w6dhOkC2vawLrf")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("categories menu", response.toString());
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        String ID = object.optString("ID");
                        String ID_branch = object.optString("ID_branch");
                        String name = object.optString("name");
                        String image = object.optString("image");
                        String create_date = object.optString("create_date");
                        String description = object.optString("description");
                        String price = object.optString("price");
                        data.add(new OItemMenu(ID,name,price,create_date,ID_branch,image,description));
                    }
                    ADItemMenu adapter = new ADItemMenu(context, data, ItemsMenu.this);
                    list_item.setAdapter(adapter);
                    list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(context,DetalleItem.class);
                            intent.putExtra("name", data.get(i-1).getName());
                            intent.putExtra("image", data.get(i-1).getImage());
                            intent.putExtra("price", data.get(i-1).getPrice());
                            intent.putExtra("ID", data.get(i-1).getID());
                            intent.putExtra("description", data.get(i-1).getDescription());
                            startActivity(intent);
                        }
                    });

                } catch (JSONException e) {
                    Log.e("Orden error", e.toString());
                }

            }

            @Override
            public void onError(ANError error) {
                // handle error
                Log.e("Orden error", error.toString());
            }
        });
    }
}
