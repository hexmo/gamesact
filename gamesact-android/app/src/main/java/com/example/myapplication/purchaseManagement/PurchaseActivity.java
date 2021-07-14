package com.example.myapplication.purchaseManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.esewa.android.sdk.payment.ESewaConfiguration;
import com.esewa.android.sdk.payment.ESewaPayment;
import com.esewa.android.sdk.payment.ESewaPaymentActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.GameModel;
import com.example.myapplication.model.PurchaseModel;
import com.example.myapplication.userManagement.LogInActivity;
import com.example.myapplication.utils.Validator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class PurchaseActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PAYMENT = 1;
    private GameModel gameModel;
    private int priceId;
    private TextInputLayout gamerTag;
    private ImageView imageView;
    private TextView purchaseText;
    private ESewaConfiguration eSewaConfiguration;
    private String gameId;
    private String productName;
    private String nepaliPrice;

    private PurchaseModel purchaseModel;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    // esewa credentials
    String client_id = "JB0BBQ4aD0UqIThFJwAKBgAXEUkEGQUBBAwdOgABHD4DChwUAB0R";
    String client_secret = "BhwIWQQADhIYSxILExMcAgFXFhcOBwAKBgAXEQ==";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        eSewaConfiguration = new ESewaConfiguration()
                .clientId(client_id)
                .secretKey(client_secret)
                .environment(ESewaConfiguration.ENVIRONMENT_TEST);


        gameModel = (GameModel) getIntent().getSerializableExtra("GameObject");
        priceId = (int) getIntent().getIntExtra("pricePosition", 0);
        gameId = (String) getIntent().getStringExtra("gameId");

        gamerTag = findViewById(R.id.gamer_tag);
        imageView = findViewById(R.id.imageView_purchase);
        purchaseText = findViewById(R.id.purchase_text);

        // https://github.com/bumptech/glide
        Glide.with(this).load(gameModel.getCover())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        String prices = gameModel.getPrices().get(priceId);

        String[] comboPrices = prices.split(":");
        String itemValue = comboPrices[0];
        nepaliPrice = comboPrices[1];

        productName = gameModel.getName() + " " + itemValue + " "
                + gameModel.getCurrency();

        String billingTitle = "You are purchasing " + itemValue + " "
                + gameModel.getCurrency()
                + " for Nrs. " + nepaliPrice;


        purchaseText.setText(billingTitle);


    }

    public void initiatePurchase(View view) {
        if (Validator.validateDataNotNull(gamerTag)) {

            purchaseModel = new PurchaseModel(firebaseUser.getUid(), gameModel.getName()
                    , productName, nepaliPrice
                    , false, gamerTag.getEditText().getText().toString().trim());

            ESewaPayment eSewaPayment = new ESewaPayment(nepaliPrice,
                    productName, gameId + System.nanoTime(),  "https://somecallbackurl.com");

            Intent intent = new Intent(PurchaseActivity.this, ESewaPaymentActivity.class);
            intent.putExtra(ESewaConfiguration.ESEWA_CONFIGURATION, eSewaConfiguration);

            intent.putExtra(ESewaPayment.ESEWA_PAYMENT, eSewaPayment);
            startActivityForResult(intent, REQUEST_CODE_PAYMENT);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                String s = data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE);
                Log.i("Proof of Payment", s);

                db.collection("orders").add(purchaseModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Successfully made payment.", Toast.LENGTH_LONG).show();

                    }

                });



                //Toast.makeText(this, "SUCCESSFUL PAYMENT", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Payment Cancelled.", Toast.LENGTH_LONG).show();
            } else if (resultCode == ESewaPayment.RESULT_EXTRAS_INVALID) {
                String s = data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE);
                Log.i("Proof of Payment", s);
            }
        }
    }


    public void cancel(View view) {
        finish();
    }

}

//  eSewa ID: 9806800002
//  Password: Nepal@123



