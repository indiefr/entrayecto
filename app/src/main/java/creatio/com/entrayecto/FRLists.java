package creatio.com.entrayecto;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import creatio.com.entrayecto.Adapters.ADContact;
import creatio.com.entrayecto.Adapters.ADLists;
import creatio.com.entrayecto.Objects.OLists;

public class FRLists extends Fragment {
    private ListView list;
    private ArrayList<OLists> data = new ArrayList<>();
    private SharedPreferences prefs;
    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lists, container, false);
        context = getActivity();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        list = view.findViewById(R.id.list);
        ADLists adapter = new ADLists(getActivity(),data);
        list.setAdapter(adapter);
        GetOffers();
        return view;
    }
    public void GetOffers(){
        data.clear();
        AndroidNetworking.post("http://api.entrayecto.com/GetOffers")
                .addBodyParameter("ID_business", prefs.getString("ID_business","0"))
                .addBodyParameter("apikey", "QuBvJ3w6dhOkC2vawLrf")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("offers branch",response.toString());
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        String ID_branch = object.optString("ID_branch");
                        String name = object.optString("name");
                        String name_branch = object.optString("name_branch");
                        String image = object.optString("image");
                        String create_date = object.optString("create_date");
                        String finish_date = object.optString("finish_date");
                        String menu_relation = object.optString("menu_relation");
                        String web_relation = object.optString("web_relation");
                        String status = object.optString("status");
                        String quantity = object.optString("quantity");
                        data.add(new OLists(name,create_date,quantity,image,ID_branch,name_branch,finish_date,menu_relation,web_relation,status));
                    }
                    ADLists adapter = new ADLists(getActivity(),data);
                    list.setAdapter(adapter);


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
