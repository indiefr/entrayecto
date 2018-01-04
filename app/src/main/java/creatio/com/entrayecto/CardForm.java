package creatio.com.entrayecto;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.CardEditText;
import com.braintreepayments.cardform.view.SupportedCardTypesView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.conekta.conektasdk.Card;
import io.conekta.conektasdk.Conekta;
import io.conekta.conektasdk.Token;

public class CardForm extends AppCompatActivity implements OnCardFormSubmitListener,
        CardEditText.OnCardTypeChangedListener {

    private static final CardType[] SUPPORTED_CARD_TYPES = {CardType.VISA, CardType.MASTERCARD, CardType.DISCOVER,
            CardType.AMEX, CardType.DINERS_CLUB, CardType.JCB, CardType.MAESTRO, CardType.UNIONPAY};
    Bundle extras;
    private SupportedCardTypesView mSupportedCardTypesView;
    private Button btnPagar;
    protected com.braintreepayments.cardform.view.CardForm mCardForm;
    private SharedPreferences pref;
    private boolean oxxo = false;
    private String latlng;
    private boolean updateCard = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_form);

        pref = PreferenceManager.getDefaultSharedPreferences(CardForm.this);

        btnPagar = (Button) findViewById(R.id.btnPagar);
        mSupportedCardTypesView = (SupportedCardTypesView) findViewById(R.id.supported_card_types);
        mSupportedCardTypesView.setSupportedCardTypes(SUPPORTED_CARD_TYPES);
        extras = getIntent().getExtras();

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(CardForm.this), 199);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }


        mCardForm = findViewById(R.id.card_form);
        mCardForm.isCardScanningAvailable();
        mCardForm.getCountryCodeEditText().setText("52");
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .postalCodeRequired(false)
                .cvvRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("\nHola " + pref.getString("nombre", "") + ", tus datos son encriptados y guardados con seguridad.")
                .actionLabel("ENVIAR")
                .setup(this);

        mCardForm.setOnCardTypeChangedListener(this);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(CardForm.this);

                if (mCardForm.isValid()) {
                    Activity activity = CardForm.this;

                    Conekta.setPublicKey("key_BA9Y55Dxqy5UEzvyLz63cxw");
                    Conekta.setApiVersion("1.0.0");
                    Conekta.collectDevice(activity);

                    Card card = new Card(
                            pref.getString("nombre", "No registro") + " " + pref.getString("apellido", "Sin registro"),
                            mCardForm.getCardNumber(), mCardForm.getCvv(),
                            mCardForm.getExpirationMonth(),
                            mCardForm.getExpirationYear());
                    Token token = new Token(activity);

                    token.onCreateTokenListener(new Token.CreateToken() {
                        @Override
                        public void onCreateTokenReady(JSONObject data) {
                            try {
                                try {

                                    if (updateCard) {
                                        Toast.makeText(CardForm.this, "Actualizando tarjeta", Toast.LENGTH_SHORT).show();
                                        AndroidNetworking.post("http://api.entrayecto.com/UpdateCard")
                                                .addBodyParameter("apikey", "QuBvJ3w6dhOkC2vawLrf")
                                                .addBodyParameter("ID_user", pref.getString("id_user", "0"))
                                                .addBodyParameter("token", data.getString("id"))
                                                .addBodyParameter("phone", mCardForm.getMobileNumber())
                                                .setPriority(Priority.IMMEDIATE)
                                                .build().getAsString(new StringRequestListener() {
                                            @Override
                                            public void onResponse(String response) {
                                                Log.e("Dta desc", response);

                                            }

                                            @Override
                                            public void onError(ANError anError) {
                                                Log.e("Tag Error", anError.toString());
                                            }
                                        });
                                    } else {
                                        AndroidNetworking.post("http://api.entrayecto.com/ConektaCustumer")
                                                .addBodyParameter("apikey", "QuBvJ3w6dhOkC2vawLrf")
                                                .addBodyParameter("id_user", pref.getString("ID_user", "0"))
                                                .addBodyParameter("token_id", data.getString("id"))
                                                .addBodyParameter("phone", mCardForm.getMobileNumber())
                                                .setPriority(Priority.IMMEDIATE)
                                                .build().getAsString(new StringRequestListener() {
                                            @Override
                                            public void onResponse(String response) {
                                                Log.e("Dta desc", response);
                                                if (response.contains("id") || response.contains("creado")) {
                                                    SharedPreferences.Editor editor = pref.edit();
                                                    editor.putBoolean("conekta", true);
                                                    editor.apply();
                                                    SaveOrder();

                                                }
                                            }

                                            @Override
                                            public void onError(ANError anError) {

                                            }
                                        });
                                    }
                                } catch (Exception err) {
                                }

                            } catch (Exception err) {
                            }
                        }
                    });

                    token.create(card);
                } else {
                    mCardForm.validate();
                    Toast.makeText(CardForm.this, "Invalido", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 199 && resultCode == -1) {
            //Lanzar alerta metodo de pago
            final Place place = PlacePicker.getPlace(data, this);
            latlng = place.getLatLng().latitude + "," + place.getLatLng().longitude;
            if (pref.getBoolean("conekta", false) && !updateCard) {
                SaveOrder();
            }
        }
    }

    public void SaveOrder() {
        final ProgressDialog dialog = new ProgressDialog(CardForm.this);
        dialog.setMessage("Guardando información");
        dialog.show();
        AndroidNetworking.post("http://api.entrayecto.com/SaveOrder")
                .addBodyParameter("ID_user", pref.getString("ID_user", "0"))
                .addBodyParameter("id_car", extras.getString("id_car", "0"))
                .addBodyParameter("total", extras.getString("total", "0"))
                .addBodyParameter("type", extras.getString("type", "0"))
                .addBodyParameter("ID_branch", extras.getString("ID_branch", "2"))
                .addBodyParameter("is_dom", extras.getString("is_dom", "1"))
                .addBodyParameter("location", latlng)
                .addBodyParameter("apikey", "QuBvJ3w6dhOkC2vawLrf")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("Order ",response.toString());
                HelperClass.SendNotification("2","Nueva orden", "Tienes una nueva orden","0");
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        dialog.dismiss();
                    }
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(ANError error) {
                // handle error
                Log.e("order error", error.toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Helper.ShowAlert(CardForm.this, "¡Gracias por usar nuestro servicio!", "Tu solicitud ha sido guardada exitosamente", 0);

    }

    @Override
    public void onCardTypeChanged(CardType cardType) {
        if (cardType == CardType.EMPTY) {
            mSupportedCardTypesView.setSupportedCardTypes(SUPPORTED_CARD_TYPES);
        } else {
            mSupportedCardTypesView.setSelected(cardType);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

//        if (item.getItemId() == R.id.card_io_item) {
//            mCardForm.scanCard(this);
//            return true;
//        }

        return false;
    }

    @Override
    public void onCardFormSubmit() {

    }
}
