package creatio.com.entrayecto;

import android.content.Context;
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
import creatio.com.entrayecto.Adapters.ADContact;
import creatio.com.entrayecto.Adapters.ADPedidos;
import creatio.com.entrayecto.Objects.OCar;
import creatio.com.entrayecto.Objects.OPEdidos;

public class FRPedidos extends Fragment {
   ;
    private ListView list;
    private ArrayList<OPEdidos> data = new ArrayList<>();
    private SharedPreferences prefs;
    private Context context;
    private LinearLayout lyNoMsj;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pedidos, container, false);
        context = getActivity();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        list = view.findViewById(R.id.list);
        lyNoMsj = view.findViewById(R.id.lyNoMsj);

        GetPedidos();
        return view;
    }
    public void GetPedidos() {
        data.clear();
        AndroidNetworking.post("http://api.entrayecto.com/GetPedidos")
                .addBodyParameter("ID_user", prefs.getString("ID_user", "0"))
                .addBodyParameter("apikey", "QuBvJ3w6dhOkC2vawLrf")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("pedidos", response.toString());
                try {
                    double totalgral = 0.0;
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        String ID = object.optString("ID");
                        String create_date = object.optString("create_date");
                        String status = object.optString("status");
                        String location = object.optString("location");
                        String total = object.optString("total");
                        String type = object.optString("type");
                        String is_dom = object.optString("is_dom");
                        String location_branch = object.optString("location_branch");
                        String name_branch = object.optString("name_branch");

                        data.add(new OPEdidos(ID,create_date,status,location,total,type,is_dom,location_branch,name_branch));
                    }
                    ADPedidos adapter = new ADPedidos(context, data, FRPedidos.this);
                    list.setAdapter(adapter);
                    if (data.size() == 0){
                        lyNoMsj.setVisibility(View.VISIBLE);
                    }else{
                        lyNoMsj.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    Log.e("pedidos error", e.toString());
                }


            }

            @Override
            public void onError(ANError error) {
                // handle error
                Log.e("pedidos error", error.toString());
            }
        });
    }

}
