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
import android.widget.ArrayAdapter; // Add this import
import com.example.cmsv1.database.UserDao; // Add this import
import java.util.ArrayList; // Add this import
import java.util.List; // Add this import
import android.widget.AdapterView; // Add this import
import android.widget.Toast; // Add this import
import android.app.AlertDialog; // Add this import
import android.view.LayoutInflater; // Add this import
import android.widget.TextView; // Add this import

public class ShopKeeperActivity extends AppCompatActivity {

    private EditText searchCustomer;
    private Button btnAddTransaction;
    private Button btnAddCredit;
    private ListView customerList;
    private ArrayAdapter<String> customerAdapter; // Add this line
    private List<String> customerListData; // Add this line
    private UserDao userDao; // Add this line
    private String selectedCustomer; // Add this line
    private String userId; // Add this line

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopkeeper);

        searchCustomer = findViewById(R.id.search_customer);
        btnAddTransaction = findViewById(R.id.btn_add_transaction);
        btnAddCredit = findViewById(R.id.btn_add_credit); // Fix this line
        customerList = findViewById(R.id.customer_list);

        userDao = new UserDao(this); // Add this line
        customerListData = new ArrayList<>(); // Add this line

        customerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, customerListData); // Add this line
        customerList.setAdapter(customerAdapter); // Add this line

        
        // Add this block to display transaction history
        List<String> transactionHistory = userDao.getAllTransactionHistory(777);
        ListView transactionListView = findViewById(R.id.transactionListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, transactionHistory);
        transactionListView.setAdapter(adapter);

        // add new
        btnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCustomer == null) {
                    Toast.makeText(ShopKeeperActivity.this, "Please select a customer first", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(ShopKeeperActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_add_transaction, null);
                builder.setView(dialogView);

                TextView customerName = dialogView.findViewById(R.id.customer_name);
                EditText amount = dialogView.findViewById(R.id.amount);
                EditText description = dialogView.findViewById(R.id.description);
                Button btnSave = dialogView.findViewById(R.id.btn_save);

                customerName.setText(selectedCustomer);

                AlertDialog dialog = builder.create();

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String amountText = amount.getText().toString();
                        String descriptionText = description.getText().toString();
                        if (amountText.isEmpty() || descriptionText.isEmpty()) {
                            Toast.makeText(ShopKeeperActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Save transaction to database
                        userDao.addTransaction(selectedCustomer, amountText, descriptionText); // Add this line

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        // Edit credit
        btnAddCredit.setOnClickListener(new View.OnClickListener() { // Fix this line
            @Override
            public void onClick(View v) {
                if (selectedCustomer == null) {
                    Toast.makeText(ShopKeeperActivity.this, "Please select a customer first", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(ShopKeeperActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_add_credit, null);
                builder.setView(dialogView);

                TextView customerName = dialogView.findViewById(R.id.customer_name);
                EditText amount = dialogView.findViewById(R.id.amount);
                Button btnSave = dialogView.findViewById(R.id.btn_save);

                customerName.setText(selectedCustomer);

                AlertDialog dialog = builder.create();

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String amountText = amount.getText().toString();
                        if (amountText.isEmpty()) {
                            Toast.makeText(ShopKeeperActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Save credit to database
                        userDao.EditCredit(selectedCustomer, amountText); // Add this line

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() { // Add this block
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCustomer = customerListData.get(position);
                Toast.makeText(ShopKeeperActivity.this, "Selected: " + selectedCustomer, Toast.LENGTH_SHORT).show();
                searchCustomer.setText(selectedCustomer); // Add this line
            }
        });

        searchCustomer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                customerListData.clear();
                List<String> results = userDao.searchCustomers(s.toString());
                customerListData.addAll(results);
                customerAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
