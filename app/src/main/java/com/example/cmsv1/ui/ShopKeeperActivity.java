package com.example.cmsv1.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cmsv1.R;
import android.text.TextWatcher; // Add this import
import android.text.Editable; // Add this import

public class ShopKeeperActivity extends AppCompatActivity {

    private EditText searchCustomer;
    private Button btnAddTransaction;
    private Button btnAddCredit;
    private ListView customerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeeper);

        searchCustomer = findViewById(R.id.search_customer);
        btnAddTransaction = findViewById(R.id.btn_add_transaction);
        btnAddCredit = findViewById(R.id.btn_add_credit);
        customerList = findViewById(R.id.customer_list);

        btnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle add transaction
            }
        });

        btnAddCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle add new credit
            }
        });

        searchCustomer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Handle search customer
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
