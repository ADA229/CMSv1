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
import android.util.Pair; // Add this import

public class ShopKeeperActivity extends AppCompatActivity {

    private EditText searchCustomer;
    private Button btnAddTransaction;
    private Button btnAddCredit;
    private ListView customerList;
    private ArrayAdapter<String> customerAdapter; // Add this line
    private List<String> customerListData; // Add this line
    private UserDao userDao; // Add this line
    private String selectedCustomer; // Add this line
    private int userId; // Update this line
    private List<Pair<Integer, String>> transactionHistory; // Update this line

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

        
        // Update this block to handle the new return type
        transactionHistory = userDao.getAllTransactionHistory(777); // Update this line
        List<String> transactionDetails = new ArrayList<>();
        for (Pair<Integer, String> transaction : transactionHistory) {
            transactionDetails.add(transaction.second);
        }
        ListView transactionListView = findViewById(R.id.transactionListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, transactionDetails);
        transactionListView.setAdapter(adapter);

        transactionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // Add this block
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pair<Integer, String> selectedTransaction = transactionHistory.get(position);
                int transactionId = selectedTransaction.first; // Get the transaction ID

                showEditTransactionDialog(transactionId); // Open the edit dialog
            }
        });

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
        btnAddCredit.setOnClickListener(new View.OnClickListener() { // Update this block
            @Override
            public void onClick(View v) {
              
            }
        });

        customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() { // Update this block
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCustomer = customerListData.get(position);
                userId = (int) id; // Update this line
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

    // private void showEditCreditDialog() { // Add this method
      
    //     AlertDialog.Builder builder = new AlertDialog.Builder(ShopKeeperActivity.this);
    //     LayoutInflater inflater = getLayoutInflater();
    //     View dialogView = inflater.inflate(R.layout.dialog_add_credit, null);
    //     builder.setView(dialogView);

    //     TextView customerName = dialogView.findViewById(R.id.customer_name);
    //     EditText amount = dialogView.findViewById(R.id.amount);
    //     Button btnSave = dialogView.findViewById(R.id.btn_save);

    //     customerName.setText(selectedCustomer);

    //     AlertDialog dialog = builder.create();

    //     btnSave.setOnClickListener(new View.OnClickListener() {
    //         @Override
    //         public void onClick(View v) {
    //             String amountText = amount.getText().toString();
    //             if (amountText.isEmpty()) {
    //                 Toast.makeText(ShopKeeperActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
    //                 return;
    //             }

    //             // Save credit to database
    //             // userDao.EditCredit(selectedCustomer, amountText, userId); // Update this line
    
    //             dialog.dismiss();
    //         }
    //     });

    //     dialog.show();
    // }

    private void showEditTransactionDialog(int transactionId) { // Add this method
        AlertDialog.Builder builder = new AlertDialog.Builder(ShopKeeperActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_transaction, null); // Ensure this layout exists
        builder.setView(dialogView);

        TextView transactionIdView = dialogView.findViewById(R.id.transaction_id);
        EditText amount = dialogView.findViewById(R.id.amount);
        EditText description = dialogView.findViewById(R.id.description);
        Button btnSave = dialogView.findViewById(R.id.btn_save);

        transactionIdView.setText("Transaction ID: " + transactionId);

        // Fetch existing transaction details if needed
        // For example:
        // Transaction transaction = userDao.getTransactionById(transactionId);
        // amount.setText(transaction.getAmount());
        // description.setText(transaction.getDescription());

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

                // Update transaction in the database
                userDao.EditCredit(amountText,transactionId); // Implement this method

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
