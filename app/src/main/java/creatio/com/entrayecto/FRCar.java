package creatio.com.entrayecto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import creatio.com.entrayecto.Adapters.ADCar;
import creatio.com.entrayecto.Adapters.ADCommits;
import creatio.com.entrayecto.Adapters.ADContact;
import creatio.com.entrayecto.Adapters.ADMenu;
import creatio.com.entrayecto.Objects.OCar;
import creatio.com.entrayecto.Objects.OMenu;

public class FRCar extends Fragment {
    private ListView list_car;
    private ArrayList<OCar> data = new ArrayList<>();
    private SharedPreferences prefs;
    private Context context;
    private TextView txtTotal;
    private Button btnBuy;
    private String total;
    private String id_car;
    private LinearLayout ly;
    private LinearLayout lyNoMsj;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_car, container, false);
        context = getActivity();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        list_car = view.findViewById(R.id.list_car);
        txtTotal = view.findViewById(R.id.txtTotal);
        btnBuy = view.findViewById(R.id.btnBuy);
        lyNoMsj = view.findViewById(R.id.lyNoMsj);
        ly = view.findViewById(R.id.ly);

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prefs.getBoolean("login",false)){
                    //Show Alert

                    HelperClass.ShowAlertSwitch(getActivity(),total,id_car);

                }else{
                    Intent intent =  new Intent(context,Login.class);
                    startActivity(intent);
                }
            }
        });
        ADCar adapter = new ADCar(getActivity(),data,FRCar.this);
        list_car.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        GetCar();
        ((Main)context).GetBadgeCar();
        super.onResume();
    }

    public void GetCar() {
        data.clear();
        AndroidNetworking.post("http://api.entrayecto.com/GetCar")
                .addBodyParameter("ID_user", prefs.getString("ID_user", "0"))
                .addBodyParameter("apikey", "QuBvJ3w6dhOkC2vawLrf")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("car", response.toString());
                try {
                    double totalgral = 0.0;
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        String ID = object.optString("ID_line");
                        String ID_menu = object.optString("ID_menu");
                        String name_menu = object.optString("name");
                        String price_menu = object.optString("price");
                        String quantity = object.optString("quantity");
                        String total = object.optString("total");
                        String ID_car = object.optString("ID_car");
                        id_car = ID_car;
                        String image = object.optString("image");
                        totalgral = totalgral  + (Double.parseDouble(total) * Double.parseDouble(quantity));
                        data.add(new OCar(ID,ID_menu,name_menu,price_menu,quantity,total,ID_car,image));
                    }
                    ADCar adapter = new ADCar(context, data, FRCar.this);
                    list_car.setAdapter(adapter);
                    txtTotal.setText(HelperClass.formatDecimal(totalgral));
                    total = String.valueOf(totalgral);


                } catch (JSONException e) {
                    Log.e("Orden error", e.toString());
                }
                if (data.size() == 0){
                    ly.setVisibility(View.GONE);
                    lyNoMsj.setVisibility(View.VISIBLE);
                }else{
                    ly.setVisibility(View.VISIBLE);
                    lyNoMsj.setVisibility(View.GONE);
                }

            }

            @Override
            public void onError(ANError error) {
                // handle error
                Log.e("Orden error", error.toString());
                ly.setVisibility(View.GONE);
            }
        });
    }


}
