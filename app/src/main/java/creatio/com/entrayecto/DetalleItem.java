package creatio.com.entrayecto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tapadoo.alerter.Alerter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import creatio.com.entrayecto.Adapters.ADCommits;
import creatio.com.entrayecto.Objects.OCommit;

public class DetalleItem extends AppCompatActivity {
    private ListView list_item;
    private ArrayList<OCommit> data = new ArrayList<>();
    private SharedPreferences prefs;
    private Context context;
    private Bundle extras;
    private String ID_item;
    private TextView txtTitleBar, txtCount, txtRate;
    private boolean flagLogin = true;
    ImageButton btnShare, btnClose, btnFav;
    private Button btnSend;
    private RatingBar rtBar;
    private EditText edtCommit;
    private LinearLayout lyCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_item);
        txtTitleBar = findViewById(R.id.txtTitleBar);
        context = DetalleItem.this;
        extras = getIntent().getExtras();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        list_item = findViewById(R.id.list_items);
        btnShare = findViewById(R.id.btnShare);
        btnClose = findViewById(R.id.btnClose);
        btnFav = findViewById(R.id.btnFav);
        //header
        LayoutInflater myinflater = getLayoutInflater();
        ViewGroup myHeader = (ViewGroup) myinflater.inflate(R.layout.ad_datail_header, list_item, false);
        final TextView txtTitle = myHeader.findViewById(R.id.txtTitle);
        Button btnOrdenar = myHeader.findViewById(R.id.btnOrdenar);
        edtCommit = myHeader.findViewById(R.id.edtCommit);
        btnSend = myHeader.findViewById(R.id.btnSend);
        lyCommit = myHeader.findViewById(R.id.lyCalificar);
        btnSend.setVisibility(View.GONE);
        rtBar = myHeader.findViewById(R.id.rtBar);
        rtBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                btnSend.setVisibility(View.VISIBLE);
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidNetworking.post("http://api.entrayecto.com/SetCommit")
                        .addBodyParameter("ID_menu", extras.getString("ID", "0"))
                        .addBodyParameter("ID_user", prefs.getString("ID_user", "0"))
                        .addBodyParameter("rate", String.valueOf(rtBar.getRating()))
                        .addBodyParameter("commit", edtCommit.getText().toString())
                        .addBodyParameter("apikey", "QuBvJ3w6dhOkC2vawLrf")
                        .setPriority(Priority.MEDIUM)
                        .build().getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("SetCommit menu", response.toString());
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(edtCommit.getWindowToken(), 0);
                        Alerter.create(DetalleItem.this)
                                .setTitle("Gracias")
                                .setBackgroundColorRes(R.color.colorPrimary)
                                .setIcon(R.drawable.ic_star)
                                .setText("El comentario ha sido entregado.")
                                .enableVibration(true)
                                .setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //Ir a carrito
                                    }
                                })
                                .show();

                        GetCommits();
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle SetCommit
                        Log.e("SetCommit error", error.toString());
                    }
                });
            }
        });
        txtCount = myHeader.findViewById(R.id.txtCount);
        txtRate = myHeader.findViewById(R.id.txtRate);
        btnOrdenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prefs.getBoolean("login", false)) {
                    String total = extras.getString("price", "0");
                    String ID = extras.getString("ID", "0");
                    String quantity = "1";

                    AndroidNetworking.post("http://api.entrayecto.com/SetCar")
                            .addBodyParameter("ID_menu", ID)
                            .addBodyParameter("total", total)
                            .addBodyParameter("quantity", quantity)
                            .addBodyParameter("ID_user", prefs.getString("ID_user", "0"))
                            .addBodyParameter("apikey", "QuBvJ3w6dhOkC2vawLrf")
                            .setPriority(Priority.MEDIUM)
                            .build().getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("carrito menu", response.toString());
                            Alerter.create(DetalleItem.this)
                                    .setTitle("Hecho")
                                    .setBackgroundColorRes(R.color.colorPrimary)
                                    .setIcon(R.drawable.ic_carrito)
                                    .setText("Se agregó el platillo a tu orden")
                                    .enableVibration(true)
                                    .setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //Ir a carrito
                                        }
                                    })
                                    .show();

                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            Log.e("Orden error", error.toString());
                        }
                    });

                } else {
//                    Toast.makeText(context, "Debes ser usuario para hacer ordenes. ¡Es muy fácil!", Toast.LENGTH_SHORT).show();
                    Alerter.create(DetalleItem.this)
                            .setTitle("Debes ser usuario para hacer ordenes")
                            .setBackgroundColorRes(R.color.colorPrimary)
                            .setIcon(R.drawable.ic_fav_fill)
                            .setText("¡Es muy fácil!, solo debes dar tap aquí")
                            .enableVibration(true)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //Ir a login
                                    Intent intent = new Intent(context, Login.class);
                                    startActivity(intent);
                                }
                            })
                            .show();
                    //startActivity(new Intent(context, Login.class));
                }
            }
        });
        TextView txtDesc = myHeader.findViewById(R.id.txtDesc);
        TextView txtPrice = myHeader.findViewById(R.id.txtPrice);
        txtTitle.setText(extras.getString("name", ""));
        txtPrice.setText(HelperClass.formatDecimal(Double.parseDouble(extras.getString("price", ""))));
        txtTitleBar.setText(extras.getString("name", ""));
        txtDesc.setText(extras.getString("description", ""));
        list_item.addHeaderView(myHeader, null, false);
        final ImageView img = myHeader.findViewById(R.id.img);
        Glide.with(context)
                .load(extras.getString("image", ""))
                .asBitmap()
                .centerCrop()
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(new SimpleTarget<Bitmap>(1600, 800) {

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Bitmap bitmap = resource;
                        img.setImageBitmap(bitmap);
                    }
                });

        //-------------------
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flagLogin) {

                } else {

                }
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "¡Mira lo que voy a comer!, " + txtTitle.getText().toString() + ", Descarga la app " + getResources().getString(R.string.app_name) + " y disfruta de sus comidas. ");
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ADCommits adapter = new ADCommits(context, data, DetalleItem.this);
        list_item.setAdapter(adapter);
        GetCommits();
    }

    public void GetCommits() {
        data.clear();
        AndroidNetworking.post("http://api.entrayecto.com/GetCommits")
                .addBodyParameter("ID_menu", extras.getString("ID", "0"))
                .addBodyParameter("apikey", "QuBvJ3w6dhOkC2vawLrf")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("offers branch", response.toString());
                try {
                    double rates = 0.0;
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        String ID = object.optString("ID");
                        String commit = object.optString("commit");
                        String rate = object.optString("rate");
                        rates = rates + Double.parseDouble(rate);
                        String ID_user = object.optString("ID_user");
                        String name_user = object.optString("name_user");
                        String image_user = object.optString("image_user");
                        String create_date = object.optString("create_date");
                        String ID_menu = object.optString("ID_menu");
                        if (prefs.getString("ID_user","0").equalsIgnoreCase(ID_user)){
                            lyCommit.setVisibility(View.GONE);
                        }
                        data.add(new OCommit(ID, commit, rate, name_user, image_user, create_date, ID_menu));
                    }

                    if (data.size() > 0){
                        rates = rates / data.size();
                        txtRate.setText(rates + "");
                    }

                    ADCommits adapter = new ADCommits(context, data, DetalleItem.this);
                    list_item.setAdapter(adapter);
                    if (data.size() == 1) {
                        txtCount.setText(data.size() + " voto");

                    } else {

                        txtCount.setText(data.size() + " votos");
                    }


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
