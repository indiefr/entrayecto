package creatio.com.entrayecto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import creatio.com.entrayecto.Adapters.ADCar;
import creatio.com.entrayecto.Adapters.ADContact;

public class FRAccount extends Fragment {
    private TextView txtName;
    private Button btnLogin;
    private SharedPreferences prefs;
    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        context = getActivity();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        btnLogin = view.findViewById(R.id.btnLogin);
        txtName = view.findViewById(R.id.txtName);
        txtName.setText(prefs.getString("nombre",""));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Login.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (prefs.getBoolean("login",false)){
            btnLogin.setVisibility(View.GONE);
        }else{
            btnLogin.setVisibility(View.VISIBLE);
        }
    }
}
