package creatio.com.entrayecto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import creatio.com.entrayecto.Adapters.ADLists;
import creatio.com.entrayecto.Adapters.ADMenu;
import creatio.com.entrayecto.Objects.OLists;
import creatio.com.entrayecto.Objects.OMenu;

public class FRMenu extends Fragment {
    private ListView list_menu;
    private ArrayList<OMenu> data = new ArrayList<>();
    private SharedPreferences prefs;
    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        context = getActivity();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        list_menu = view.findViewById(R.id.list_menu);
        ADMenu adapter = new ADMenu(context,data);
        list_menu.setAdapter(adapter);
        GetCategories();
        return view;
    }
    public void GetCategories() {
        data.clear();
        AndroidNetworking.post("http://api.entrayecto.com/GetCategories")
                .addBodyParameter("ID_business", prefs.getString("ID_business", "0"))
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
                        String name = object.optString("name");
                        String image = object.optString("image");
                        String create_date = object.optString("create_date");
                        String description = object.optString("description");
                        data.add(new OMenu(ID,name,description,image,create_date));
                    }
                    ADMenu adapter = new ADMenu(context, data);
                    list_menu.setAdapter(adapter);


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
