package creatio.com.entrayecto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Splash extends AppCompatActivity {
    private SharedPreferences prefs;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = Splash.this;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        //Extraer valores
        AndroidNetworking.post("http://api.entrayecto.com/GetConfiguration")
                .addBodyParameter("ID_business", "1")
                .addBodyParameter("apikey", "QuBvJ3w6dhOkC2vawLrf")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("configuration branch", response.toString());
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        String name = object.optString("name");
                        String id = object.optString("ID_package");
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putString("name,"+id, name);
                        edit.putString("ID_business", "1");
                        edit.apply();
                    }
                    new CountDownTimer(3000, 1000) {

                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            Intent intent = new Intent(Splash.this, Main.class);
                            intent.putExtra("bag", false);
                            startActivity(intent);
                            finish();
                        }
                    }.start();
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
