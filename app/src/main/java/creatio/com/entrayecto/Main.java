package creatio.com.entrayecto;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main extends AppCompatActivity {
    public static Context context;
    private SharedPreferences prefs;
    private boolean login;
    private BottomBar bottomBar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = Main.this;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        login = prefs.getBoolean("login", false);

        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_init);
        bottomBar.setBadgesHideWhenActive(false);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_init) {
                    FRInit newFragment = new FRInit();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.contentContainer, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    getSupportActionBar().setTitle("Inicio");
                }
                if (tabId == R.id.tab_ship) {
                    FRPedidos newFragment = new FRPedidos();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.contentContainer, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    getSupportActionBar().setTitle("Mis pedidos");
                }
                if (tabId == R.id.tab_account) {
                    FRAccount newFragment = new FRAccount();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.contentContainer, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    getSupportActionBar().setTitle("Mi cuenta");
                }
                if (tabId == R.id.tab_car) {
                    FRCar newFragment = new FRCar();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.contentContainer, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    getSupportActionBar().setTitle("Mis ordenes");

                }


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        GetBadgeCar();
    }

    public void GetBadgeCar() {
        AndroidNetworking.post("http://api.entrayecto.com/GetBadgeCar")
                .addBodyParameter("ID_user", prefs.getString("ID_user", "0"))
                .addBodyParameter("apikey", "QuBvJ3w6dhOkC2vawLrf")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("badgecar", response.toString());
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        String count = object.optString("count");
                        BottomBarTab nearby = bottomBar.getTabWithId(R.id.tab_car);

                        nearby.setBadgeCount(Integer.parseInt(count));
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
