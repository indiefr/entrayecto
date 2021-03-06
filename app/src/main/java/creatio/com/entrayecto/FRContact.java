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
import creatio.com.entrayecto.Adapters.ADMenu;
import creatio.com.entrayecto.Objects.OContact;
import creatio.com.entrayecto.Objects.OMenu;

public class FRContact extends Fragment {
    private ListView list_contact;
    private ArrayList<OContact> data = new ArrayList<>();
    private SharedPreferences prefs;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        context = getActivity();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        list_contact = view.findViewById(R.id.list_contact);
        ADContact adapter = new ADContact(context,data);
        list_contact.setAdapter(adapter);
        GetData();
        return view;
    }
    public void GetData() {
        data.clear();
        AndroidNetworking.post("http://api.entrayecto.com/GetContacts")
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
                        String name = object.optString("name_contact");
                        String name_branch = object.optString("name_branch");
                        String ID_branch = object.optString("ID_branch");
                        String location = object.optString("location");
                        String address = object.optString("address");
                        String tel = object.optString("tel");
                        String email = object.optString("email");
                        String service_out = object.optString("service_out");
                        String horario = object.optString("horario");

                        data.add(new OContact(name,name_branch,ID_branch,location,address,tel,email,service_out,horario));
                    }
                    ADContact adapter = new ADContact(context, data);
                    list_contact.setAdapter(adapter);


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
