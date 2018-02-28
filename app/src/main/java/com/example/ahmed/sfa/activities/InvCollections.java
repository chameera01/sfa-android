package com.example.ahmed.sfa.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.activities.Dialogs.Alert;
import com.example.ahmed.sfa.activities.Dialogs.ChequeDetailsPopup;
import com.example.ahmed.sfa.controllers.adapters.CollectionNoteAdapter;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;
import com.example.ahmed.sfa.controllers.database.DBHelper;
import com.example.ahmed.sfa.models.Cheque;
import com.example.ahmed.sfa.models.CollectionChequeDetails;
import com.example.ahmed.sfa.models.CollectionNote;
import com.example.ahmed.sfa.models.CollectionNoteDetails;
import com.example.ahmed.sfa.models.CollectionNoteHeader;
import com.example.ahmed.sfa.models.InvoiceType;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class InvCollections extends AppCompatActivity {

    private static final String TAG = "COL";
    private static final int CHEQUE_DETAILS_REQUEST_CODE = 1;
    private static final String CASH = "Cash";
    private static final String CHEQUE = "Cheque";
    private static final String CASH_CHEQUE = "Cash and Cheque";
    private static final int UPLOAD_DEFAULT = 0;
    public static Spinner customers;
    public static RecyclerView.Adapter rvadapter;
    public static ArrayList<CollectionNote> al;
    public static EditText cash;
    public static ImageButton popup;
    public static double chequeTotal = 0.0;
    public static String lastSelectedCustomer;
    public static double lastInvoiceRemaining;
    public Spinner types, routes, reps, invnos;
    public TextView tvcreditAmt;
    private ArrayAdapter<String> typeAdapter, routeAdapter, repAdapter, cusAdapter, invAdapter;
    private DBHelper helper;
    private DBAdapter adapter;
    private ArrayList<String> itineraries, repList, cusNames, cusNos;
    private ArrayList<DBHelper.CollectionCustomer> customerList;
    private ArrayList<DBHelper.CollectionInvoice> invNoList;
    private RecyclerView rv;
    private Button clear, addgrid, savedb;
    private String invNo, bank, branch, rdate, cheqNo;
    private double cashValue, chequeValue, balance, remaining;
    private String colNo;
    private RecyclerView.LayoutManager lmanager;
    private double selectedCredit;
    private String selectedInvoice;
    private String selected;
    private String selectedCustomer;
    private String selectedCusNo;
    private String selectedRep;
    private String selectedRoute;
    private Date today;
    private int hid;
    private String cnno;
    private com.example.ahmed.sfa.activities.Dialogs.Alert alert;
    private TextView chequeVal;
    private String lastSelectedInvoice;
    private ArrayList<CollectionNoteDetails> cashDetailsList;
    private ArrayList<CollectionChequeDetails> chequeDetailsList;
    private String lastCustomer;
    private ArrayList<String> sessionInvoiceList;
    private String date = "";
    private DialogInterface.OnClickListener dismissDialog = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    public static void hideSoftKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Going back will erase all changes, are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        // super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_collections);

        init();

        setRouteSpinner();

        //types spinner
        String[] payments = new String[]{"Cash", "Cheque", "Cash and Cheque"};
        typeAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, payments);
        types.setAdapter(typeAdapter);

        final int pos = typeAdapter.getPosition("Cash");
        types.setSelection(pos);

        types.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = parent.getItemAtPosition(position).toString();
                if (!addgrid.isEnabled()) {
                    addgrid.setEnabled(true);
                }
                switch (selected) {
                    case "Cash":
                        cash.setEnabled(true);
                        chequeVal.setText("");
                        chequeVal.setEnabled(false);
                        popup.setEnabled(false);
                        popup.setClickable(false);
                        break;

                    case "Cheque":
                        cash.setText("");
                        cash.setEnabled(false);
                        popup.setEnabled(true);
                        popup.setClickable(true);
                        chequeVal.setEnabled(true);
                        break;

                    default:
                        cash.setText("");
                        cash.setEnabled(true);
                        chequeVal.setEnabled(true);
                        popup.setEnabled(true);
                        popup.setClickable(true);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //routes spinner
        routes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRoute = routes.getSelectedItem().toString();
                setRepSpinner(selectedRoute);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //reps spinner
        reps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRep = reps.getSelectedItem().toString();
                Toast.makeText(getApplicationContext(), selectedRep, Toast.LENGTH_SHORT).show();
                setCustomerSpinner(selectedRep);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //customer spinner
        customers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCustomer = customerList.get(position).getCustomerName();
                selectedCusNo = customerList.get(position).getCustomerNo();
                lastSelectedCustomer = customerList.get(position).getCustomerName();
                Toast.makeText(getApplicationContext(), selectedCustomer, Toast.LENGTH_SHORT).show();
                setInvoiceSpinner(selectedCustomer);
                Log.d(TAG, "lastSelectedCustomer: " + lastSelectedCustomer);
                clearData(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //invoice no spinner
        invnos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCredit = invNoList.get(position).getCcredit();
                selectedInvoice = invNoList.get(position).getInvNo();
                DecimalFormat df = new DecimalFormat("#.00");
                tvcreditAmt.setText(String.valueOf(df.format(selectedCredit)));
                if (!addgrid.isEnabled()) {
                    addgrid.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        al = new ArrayList<>();

        rvadapter = new CollectionNoteAdapter(InvCollections.this, al);
        lmanager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(lmanager);
        rv.setAdapter(rvadapter);
        rv.setHasFixedSize(true);

        handleClicks();
        popup.setEnabled(false);
        popup.setClickable(false);

    }

    private void handleClicks() {

        final ArrayList<InvoiceType> invoices = new ArrayList<>();

        addgrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("COL", "inside add to grid btn onclick");

                String type = types.getSelectedItem().toString();
                if (checkEmpty(type)) {
                    Log.d("COL", "inside cash btn onclick");

                    String lastCNNo = helper.checkLastCNNo();
                    colNo = setNewCNoteNo(helper.getCollectionNoteNo(InvCollections.this));

                    if (helper.getCollectionPaymentType(selectedInvoice).size() == 0) {
                        //there are no invoices in the table/no collection notes added in that session/new collection note
//
                        Log.d("COLL", "inside addgrid 1_lastcust: " + lastCustomer);
                        Log.d("COLL", "inside addgrid_1 customers.getSelectedItem(): " + customers.getSelectedItem());

                        //removed session list and collection no validation
//                        if ((lastCustomer == "" && !sessionInvoiceList.contains(selectedInvoice) ) || (!lastCustomer.equals(customers.getSelectedItem()) && !sessionInvoiceList.contains(selectedInvoice) ) || (lastCustomer.equals(customers.getSelectedItem()) && !sessionInvoiceList.contains(selectedInvoice) )) {

                        if ((lastCustomer == "") || (!lastCustomer.equals(customers.getSelectedItem())) || (lastCustomer.equals(customers.getSelectedItem()))) {
                            CollectionNote cn = new CollectionNote();
                            cn = setGrid(type);
                            Log.d("COL", "finished set grid -> display grid");
                            al.add(cn);
                            rvadapter.notifyDataSetChanged();
                            //new
                            InvoiceType it = new InvoiceType();
                            it.setInvoice(selectedInvoice);
                            it.setType(type);
                            invoices.add(it);

                            Log.d(TAG, "bal_if a new invoice/type: " + balance);
                            hideSoftKeyboard(InvCollections.this, v);
                            if (adapter.getLastRemaining(customers.getSelectedItem().toString()) != 0.0) {
                                cash.setEnabled(false);
                                popup.setEnabled(false);
                            } else {
                                clearData(v);
                            }
                            saveToDB(type);
                            lastCustomer = selectedCustomer;
                            sessionInvoiceList.add(selectedInvoice);

                        } else {
                            alert.showAlert("Warning!", "You have already added the same details for " + selectedCustomer + "!", "OK", dismissDialog);
                            addgrid.setEnabled(false);
                        }

                    } else {

                        if ((helper.getCollectionPaymentType(selectedInvoice).get(0).equals(type)) && lastCNNo.equals(colNo)) {
                            //there's an invoice saved with the same no. and the type
                            alert.showAlert("Warning!", "You have already settled in " + type + " for the invoice " + selectedInvoice + "!", "OK", dismissDialog);
                            addgrid.setEnabled(false);
                        } else {
                            //there's no invoice saved with the same no. and the type


                            if ((!lastCustomer.equals(customers.getSelectedItem()) && !lastCNNo.equals(colNo)) || (lastCustomer.equals(customers.getSelectedItem()) && !lastCNNo.equals(colNo))) {

                                CollectionNote cn = new CollectionNote();
                                cn = setGrid(type);
                                Log.d("COL", "finished set grid -> display grid");
                                al.add(cn);
                                rvadapter.notifyDataSetChanged();

                                //new
                                InvoiceType it = new InvoiceType();
                                it.setInvoice(selectedInvoice);
                                it.setType(type);
                                invoices.add(it);

//                                lastSelectedCustomer = customers.getSelectedItem().toString();

                                Log.d(TAG, "bal_if a new invoice/type: " + balance);
                                hideSoftKeyboard(InvCollections.this, v);
                                if (adapter.getLastRemaining(customers.getSelectedItem().toString()) == 0.0) {
                                    clearData(v);
                                }
                                saveToDB(type);

                                lastCustomer = selectedCustomer;
                                sessionInvoiceList.add(selectedInvoice);
                            } else {
                                alert.showAlert("Warning!", "You have already added the same details for " + selectedCustomer + "!", "OK", dismissDialog);
                                addgrid.setEnabled(false);
                            }
                        }
                    }


                } else {
                    alert.showAlert("Warning!", "One or more fields are empty.", "OK", dismissDialog);
                }
            }
        });

        savedb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (al.size() > 0) {
                    String type = types.getSelectedItem().toString();
                    saveToCollectionHeader();

                    int headerId = helper.getLastCollectionHeaderID(InvCollections.this);
                    colNo = helper.getCNoteNofromHeader(headerId, InvCollections.this);

                    helper.updateCollectionNoteNo(InvCollections.this);
                    if (helper.setCollectionPaymentType(invoices, cnno, InvCollections.this)) {
                        invoices.clear();
                    }
                    clearData(v);


                    switch (type) {
                        case "Cash":
                            if (helper.insertCollectionDetails(cashDetailsList, headerId, colNo, InvCollections.this)) {
                                Alert alert = new Alert(InvCollections.this);
                                alert.showAlert("Success", "Saved successfully!", null, null);
                                al.clear();
                                rvadapter.notifyDataSetChanged();
                            }
                            break;

                        case "Cheque":
                            if (helper.insertCollectionChequeDetails(chequeDetailsList, headerId, colNo, InvCollections.this)) {
                                Alert alert = new Alert(InvCollections.this);
                                alert.showAlert("Success", "Saved successfully!", null, null);
                                al.clear();
                                rvadapter.notifyDataSetChanged();
                            }
                            break;

                        case "Cash and Cheque":
                            if ((helper.insertCollectionDetails(cashDetailsList, headerId, colNo, InvCollections.this)) && (helper.insertCollectionChequeDetails(chequeDetailsList, headerId, colNo, InvCollections.this))) {
                                Alert alert = new Alert(InvCollections.this);
                                alert.showAlert("Success", "Saved successfully!", null, null);
                                al.clear();
                                rvadapter.notifyDataSetChanged();
                            }
                            break;

                        default:
                            Log.d(TAG, "type: " + type);
                            Alert alert = new Alert(InvCollections.this);
                            alert.showAlert("Error", "Saving failed!", null, null);
                            break;
                    }

                    helper.removeData(InvCollections.this);

                } else {
                    Alert alert = new Alert(InvCollections.this);
                    alert.showAlert("Error", "No data to save!", null, null);

                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData(v);
            }
        });

        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InvCollections.this, ChequeDetailsPopup.class);
                intent.putExtra("SELECTED_INVOICE", invnos.getSelectedItem().toString());
                startActivityForResult(intent, CHEQUE_DETAILS_REQUEST_CODE);
            }
        });

    }

    private void saveToDB(String type) {
        //create the object based on the type
        switch (type) {
            case "Cash":
                Log.d("COL", "inside saveToDB_calling saveToCashDetails");
                saveToCashDetails();
                break;

            case "Cheque":
                Log.d("COL", "inside saveToDB_calling saveToChequeDetails");
                saveToChequeDetails();
                break;

            default:
                Log.d("COL", "inside saveToDB_calling saveToCashDetails & saveToChequeDetails");
                saveToCashDetails();
                saveToChequeDetails();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == CHEQUE_DETAILS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Cheque c = data.getParcelableExtra("CHEQUE_DETAILS");
                chequeValue = c.getChequeVal();
                cheqNo = c.getChequeNum();
                bank = c.getBank();
                branch = c.getBranch();
                rdate = c.getRealizedDate();

                if (lastSelectedInvoice == null) {
                    chequeTotal = chequeValue;
                    chequeVal.setText(String.format(Locale.getDefault(), "%.2f", chequeTotal));
                } else {
                    if (invnos.getSelectedItem().toString().equals(lastSelectedInvoice)) {
                        chequeTotal = chequeTotal + chequeValue;
                        chequeVal.setText(String.format(Locale.getDefault(), "%.2f", chequeTotal));
                    } else {
//                        chequeTotal = 0.0;
//                        chequeVal.setText(String.format(Locale.getDefault(), "%.2f", chequeValue));
                        chequeTotal = chequeValue;
                        chequeVal.setText(String.format(Locale.getDefault(), "%.2f", chequeTotal));
                    }
                }
                lastSelectedInvoice = invnos.getSelectedItem().toString();
                Toast.makeText(InvCollections.this, "Values are saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(InvCollections.this, "Invalid inputs!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveToChequeDetails() {

        Log.d("COL", "inside saveToChequeDetails");
        CollectionChequeDetails ccd = new CollectionChequeDetails();

        hid = helper.getLastCollectionHeaderID(this);
        if (hid == -1) {
            hid = 1;
        }

        Log.d("COL", "hid_" + String.valueOf(hid));

        if (hid != -1) {
            if (helper.getCNoteNofromHeader(hid, this) != null) {
                cnno = helper.getCNoteNofromHeader(hid, this);
            } else {
                cnno = setNewCNoteNo(helper.getCollectionNoteNo(InvCollections.this));
            }

            ccd.setHeaderId(hid);
            ccd.setCollectionNoteNo(cnno);
            ccd.setRepId(helper.getSelectedRepId(selectedRep, this));
            ccd.setInvoiceNo(selectedInvoice);
            ccd.setChequeAmt(chequeTotal);
            ccd.setChequeNo(cheqNo);
            ccd.setBank(bank);
            ccd.setBranch(branch);
            ccd.setRealizeDate(rdate);
            ccd.setIsUpload(UPLOAD_DEFAULT);

        }

        chequeDetailsList.add(ccd);

    }

    private void saveToCashDetails() {

        Log.d("COL", "inside saveToCashDetails");
        hid = helper.getLastCollectionHeaderID(this);
        Toast.makeText(this, "hid: " + hid, Toast.LENGTH_LONG).show();
        if (hid == -1) {
            hid = 1;
        }
        Log.d("COL", "hid_" + String.valueOf(hid));
        Toast.makeText(this, "hid after: " + hid, Toast.LENGTH_LONG).show();
        CollectionNoteDetails cnd = new CollectionNoteDetails();

        if (hid != -1) {

            if ((helper.getCNoteNofromHeader(hid, InvCollections.this)) != null) {
                cnno = helper.getCNoteNofromHeader(hid, InvCollections.this);
                Log.d("COL", "cnno long not null: " + helper.getCNoteNofromHeader(hid, InvCollections.this));
                Log.d("COL", "cnno not null: " + cnno);
            } else {
                cnno = setNewCNoteNo(helper.getCollectionNoteNo(InvCollections.this));
                Log.d("COL", "cnno null: " + cnno);
            }


            cnd.setHeaderId(hid);
            cnd.setCollectionNoteNo(cnno);
            cnd.setRepId(helper.getSelectedRepId(selectedRep, this));
            cnd.setRoute(selectedRoute);
            cnd.setInvoiceNo(selectedInvoice);
            cnd.setCredit(selectedCredit);
            cnd.setCash(cashValue);
            cnd.setCheque(chequeValue);
            cnd.setBalance(balance);
            cnd.setRemaining(remaining);
            cnd.setIsUpload(UPLOAD_DEFAULT);
        }

        cashDetailsList.add(cnd);

    }

    private void clearData(View v) {

        hideSoftKeyboard(this, v);

        if (types.getSelectedItem().toString().equals("Cash")) {
            cash.setText("");
        } else if (types.getSelectedItem().toString().equals("Cheque")) {
            chequeVal.setText("");
        } else {
            cash.setText("");
            chequeVal.setText("");
        }


    }

    private boolean checkEmpty(String type) {

        Log.d("COL", "inside check empty");
        if (type.equals("Cash")) {
            if (TextUtils.isEmpty(cash.getText())) {
                Log.d("COL", "inside cash empty");
                return false;
            } else return true;
        } else if (type.equals("Cheque")) {
            if (TextUtils.isEmpty(chequeVal.getText())) {
                Log.d("COL", "inside cheque empty");
                return false;
            } else return true;
        } else if (type.equals("Cash and Cheque")) {
            if (TextUtils.isEmpty(cash.getText()) || TextUtils.isEmpty(chequeVal.getText())) {
                Log.d("COL", "inside both empty");
                return false;
            } else return true;
        } else {
            Toast.makeText(InvCollections.this, "Selected Type: " + type, Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    public CollectionNote setGrid(String type) {

        lastInvoiceRemaining = adapter.getLastRemaining(customers.getSelectedItem().toString());
        switch (type) {

            case "Cash":
                Log.d(TAG, "inside set grid");
                Log.d(TAG, "lastInvoiceRemaining: " + lastInvoiceRemaining);
                Log.d(TAG, "lastSelectedCustomer: " + lastSelectedCustomer);
                Log.d(TAG, "customers.getSelectedItem().toString(): " + customers.getSelectedItem().toString());

                if (lastSelectedCustomer.equals(customers.getSelectedItem().toString()) && lastInvoiceRemaining > 0.0) {
                    cashValue = lastInvoiceRemaining;
                    Log.d(TAG, "lastInvoiceRemaining: " + lastInvoiceRemaining);
                    Log.d(TAG, "cashValue: " + cashValue);
                } else {
                    cashValue = Double.valueOf(cash.getText().toString());
                    Log.d(TAG, "cashValue: " + cashValue);
                }
                Log.d("COL", "cash_" + String.valueOf(cashValue));
                chequeTotal = 0.0;
                if (selectedCredit > cashValue) {
                    balance = selectedCredit - cashValue;
                    Log.d("COL", "bal_" + String.valueOf(balance));
                    remaining = 0.0;
                } else {
                    balance = 0.0;
                    remaining = cashValue - selectedCredit;
                    cashValue = selectedCredit;
                    Log.d("COL", "remain_" + String.valueOf(remaining));
                }

//                lastInvoiceRemaining = remaining;
                adapter.setLastRemaining(customers.getSelectedItem().toString(), remaining);
                break;

            case "Cheque":
                cashValue = 0.0;
                if (lastSelectedCustomer.equals(customers.getSelectedItem().toString()) && lastInvoiceRemaining > 0.0) {
                    chequeTotal = lastInvoiceRemaining;
                }
                if (selectedCredit > chequeTotal) {
                    balance = selectedCredit - chequeTotal;
                    remaining = 0.0;
                } else {
                    balance = 0.0;
                    remaining = chequeTotal - selectedCredit;
                    chequeTotal = selectedCredit;
                }
//                lastInvoiceRemaining = remaining;
                adapter.setLastRemaining(customers.getSelectedItem().toString(), remaining);
                break;

            case "Cash and Cheque":
                cashValue = Double.valueOf(cash.getText().toString());
                if (lastSelectedCustomer.equals(customers.getSelectedItem().toString()) && lastInvoiceRemaining > 0.0) {
                    cashValue = lastInvoiceRemaining;
                }

                if (selectedCredit > cashValue) {
                    balance = selectedCredit - cashValue;
                    if (balance >= chequeTotal) {
                        balance = balance - chequeTotal;
                        remaining = 0.0;
                    } else {
                        remaining = chequeTotal - balance;
                        balance = 0.0;
                    }
                } else {
                    balance = 0.0;
                    remaining = (cashValue - selectedCredit) + chequeTotal;
                    cashValue = selectedCredit;
                }
//                lastInvoiceRemaining = remaining;
                adapter.setLastRemaining(customers.getSelectedItem().toString(), remaining);
                break;

            default:
                Log.d(TAG, "type: " + type);

        }

        CollectionNote note = new CollectionNote();
        note.setInvoiceNo(selectedInvoice);
        note.setCreditAmt(selectedCredit);
        note.setCashAmt(cashValue);
        note.setChequeAmt(chequeTotal);
        note.setBalance(balance);
        note.setRemaining(remaining);

        return note;
    }


    public void saveToCollectionHeader() {

        Log.d("COL", "inside saveToCollectionHeader");
        CollectionNoteHeader cn = new CollectionNoteHeader();

        colNo = setNewCNoteNo(helper.getCollectionNoteNo(InvCollections.this));
        cn.setCollectionNoteNo(colNo); //set value here

        cn.setCollectedDate(date);
        Log.d("COL", String.valueOf(today));
        cn.setType(selected);
        Log.d("COL", "type_" + selected);
        cn.setCustomerNo(selectedCusNo);
        Log.d("COL", "setCustomerNo_" + selectedCusNo);
        cn.setIsUpload(UPLOAD_DEFAULT);

        helper.insertCollectionHeader(cn, this);

    }

    private String setNewCNoteNo(int collectionNoteNo) {
        String cno = selectedRep + String.valueOf(collectionNoteNo);
        return cno;
    }

    private void setInvoiceSpinner(String cusName) {

        String rep = reps.getSelectedItem().toString();

        invNoList = helper.getCollectionInvoiceNos(cusName, rep, this);
        String[] invNoArray = new String[invNoList.size()];

        for (int i = 0; i < invNoList.size(); i++) {
            invNoArray[i] = invNoList.get(i).getInvNo();
        }

        invAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, invNoArray);
        invnos.setAdapter(invAdapter);
    }

    private void setCustomerSpinner(String rep) {

        Log.d("COL", "inside setcusspinner_rep_" + rep);
        customerList = helper.getCollectionCustomers(rep, this);

        try {
            if (customerList.size() == 0) {

                cash.setEnabled(false);
                popup.setEnabled(false);
                addgrid.setEnabled(false);
                tvcreditAmt.setText("");
                customers.setEnabled(false);
                invNoList.clear();
                invnos.setEnabled(false);

                Log.d("COL", "empty cus list");
            } else {
                customers.setEnabled(true);
                invnos.setEnabled(true);
                cash.setEnabled(true);
                popup.setEnabled(true);
                addgrid.setEnabled(true);
                cusNames = new ArrayList<>();
                cusNos = new ArrayList<>();

                for (DBHelper.CollectionCustomer cc : customerList) {
                    Log.d("COL", cc.getCustomerName());
                    cusNames.add(cc.getCustomerName());
                    cusNos.add(cc.getCustomerNo());
                }
//            Collections.sort(cusNames);
                String[] cusArray = cusNames.toArray(new String[cusNames.size()]);

                for (int i = 0; i < cusArray.length; i++) {
                    Log.d("COL", "Array_" + cusArray[i]);
                }

                cusAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, cusArray);
                customers.setAdapter(cusAdapter);
            }
        } catch (Exception e) {
            Log.d(TAG, "Ex: " + e.getLocalizedMessage());
            Alert alert = new Alert(InvCollections.this);
            alert.showAlert("Error", "Please check whether data has been updated!", null, null);
        }
    }


    private void setRepSpinner(String route) {
        //temporarily hardcoded
        repList = helper.getCollectionReps(this);
        Collections.sort(repList);
        String[] repArray = repList.toArray(new String[repList.size()]);

        repAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, repArray);
        reps.setAdapter(repAdapter);
    }

    private void setRouteSpinner() {
        itineraries = helper.getCollectionRoutes();
        Collections.sort(itineraries);
        String[] routeArray = itineraries.toArray(new String[itineraries.size()]);

        routeAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, routeArray);
        routes.setAdapter(routeAdapter);
    }

    private void init() {

        lastCustomer = "";
        sessionInvoiceList = new ArrayList<>();

        cashDetailsList = new ArrayList<>();
        chequeDetailsList = new ArrayList<>();

        today = Calendar.getInstance().getTime();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        date = dt.format(today);

        Log.d(TAG, "date: " + date);

        alert = new Alert(InvCollections.this);

        adapter = new DBAdapter(this);
        helper = new DBHelper(this);

        types = (Spinner) findViewById(R.id.typeSpinner);
        routes = (Spinner) findViewById(R.id.routeSpinner);
        reps = (Spinner) findViewById(R.id.repSpinner);
        customers = (Spinner) findViewById(R.id.customerSpinner);
        invnos = (Spinner) findViewById(R.id.invoiceSpinner);

        rv = (RecyclerView) findViewById(R.id.rvCollections);

        clear = (Button) findViewById(R.id.btnClearData);
        addgrid = (Button) findViewById(R.id.btnAddToGrid);
        savedb = (Button) findViewById(R.id.btnSaveData);

        tvcreditAmt = (TextView) findViewById(R.id.creditAmt);

        cash = (EditText) findViewById(R.id.etCashValue);
        chequeVal = (TextView) findViewById(R.id.etChequeValue);
        popup = (ImageButton) findViewById(R.id.ibCheque);


    }


}
