package creatio.com.entrayecto.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

import creatio.com.entrayecto.FRCar;
import creatio.com.entrayecto.HelperClass;
import creatio.com.entrayecto.Objects.OCar;
import creatio.com.entrayecto.R;

/**
 * Created by Layge on 18/12/2017.
 */

public class ADCar extends BaseAdapter {
    Context context;
    ArrayList<OCar> data = new ArrayList<>();
    FRCar fragment;

    public ADCar(Context context, ArrayList<OCar> data, FRCar fragment) {
        this.context = context;
        this.data = data;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View item = inflater.inflate(R.layout.ad_car, viewGroup, false);
        TextView txtName = item.findViewById(R.id.txtName);
        TextView txtQ = item.findViewById(R.id.txtQ);
        TextView txtTotal = item.findViewById(R.id.txtTotal);
        final ImageButton btnMenu = item.findViewById(R.id.btnMenu);
        final ImageView img = item.findViewById(R.id.img);
        txtName.setText(data.get(i).getName_menu());
        if (data.get(i).getQuantity().equalsIgnoreCase("1")) {
            txtQ.setText(data.get(i).getQuantity() + " platillo");

        } else {

            txtQ.setText(data.get(i).getQuantity() + " platillos");
        }
        double total = Double.parseDouble(data.get(i).getPrice_menu()) * Double.parseDouble(data.get(i).getQuantity());
        txtTotal.setText(HelperClass.formatDecimal(total));
        Glide.with(context)
                .load(data.get(i).getImage())
                .asBitmap()
                .centerCrop()
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(new SimpleTarget<Bitmap>(800, 400) {

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Bitmap bitmap = resource;
                        img.setImageBitmap(resource);

                    }
                });
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popup = new PopupMenu(context, btnMenu);
                popup.getMenuInflater().inflate(R.menu.context_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int ix = item.getItemId();
                        if (ix == R.id.eliminar) {
                            //do something
                            DeleteLine(data.get(i).getID());
                            return true;
                        } else {
                            return onMenuItemClick(item);
                        }
                    }
                });

                popup.show();
            }
        });
        return item;
    }

    public void DeleteLine(final String ID) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage("El platillo será eliminado del carrito");
        alert.setTitle("¿Estás seguro?");
        alert.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

                AndroidNetworking.post("http://api.entrayecto.com/DeleteLine")
                        .addBodyParameter("ID", ID)
                        .addBodyParameter("ID_user", prefs.getString("ID_user","0"))
                        .addBodyParameter("apikey", "QuBvJ3w6dhOkC2vawLrf")
                        .setPriority(Priority.MEDIUM)
                        .build().getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("line", response.toString());

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                String ID = object.optString("ID");

                            }

                            fragment.onResume();


                        } catch (JSONException e) {
                            Log.e("line exe", e.toString());
                        }


                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("line error", error.toString());
                    }
                });
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();

    }
}
