package com.example.ahmed.sfa.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.activities.Dialogs.Alert;
import com.example.ahmed.sfa.controllers.adapters.CollectionNoteAdapter;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;
import com.example.ahmed.sfa.controllers.database.DBHelper;
import com.example.ahmed.sfa.models.CollectionChequeDetails;
import com.example.ahmed.sfa.models.CollectionNote;
import com.example.ahmed.sfa.models.CollectionNoteDetails;
import com.example.ahmed.sfa.models.CollectionNoteHeader;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class InvCollections extends AppCompatActivity {

    private static final String TAG = "COL";
    public Spinner types, routes, reps, customers, invnos;
    private ViewFlipper flipper;
    private ArrayAdapter<String> typeAdapter, routeAdapter, repAdapter, cusAdapter, invAdapter;
    private DBHelper helper;
    private DBAdapter adapter;
    private ArrayList<String> itineraries, repList, cusNames;
    private ArrayList<DBHelper.CollectionCustomer> customerList;
    private ArrayList<DBHelper.CollectionInvoice> invNoList;
    private RecyclerView rv;
    private Button addCash, addCheque, addBoth;
    private String invNo, bank, branch, rdate, cheqNo;
    private double cashValue, chequeValue, balance, remaining;
    private EditText etcash1, etcheque1, etchequeNo1, etBank1, etBranch1, etrdate1, etcash2, etcheque2, etchequeNo2, etBank2, etBranch2, etrdate2;
    public TextView tvcreditAmt;
    private RecyclerView.Adapter rvadapter;
    private RecyclerView.LayoutManager lmanager;

    private double selectedCredit;
    private String selectedInvoice;
    private String selected;
    private String selectedCustomer;
    private String selectedCusNo;
    private String selectedRep;
    private String selectedRoute;

    private static final int UPLOAD_DEFAULT = 0;
    private Date today;
    private ArrayList<CollectionNote> al;
    private int hid;
    private String cnno;
    private com.example.ahmed.sfa.activities.Dialogs.Alert alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_collections);

        init();
        setRouteSpinner();

        //types spinner
        String[] payments = new String[]{"Cash", "Cheque", "Cash and Cheque"};
        typeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, payments);
        types.setAdapter(typeAdapter);

        final int pos = typeAdapter.getPosition("Cash");
        types.setSelection(pos);

        types.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = parent.getItemAtPosition(position).toString();
                switch (selected) {
                    case "Cash":
                        flipper.setDisplayedChild(0);
                        break;

                    case "Cheque":
                        flipper.setDisplayedChild(1);
                        break;

                    default:
                        flipper.setDisplayedChild(2);
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
                Toast.makeText(getApplicationContext(), selectedCustomer, Toast.LENGTH_SHORT).show();
                setInvoiceSpinner(selectedCustomer);
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

    }

    private DialogInterface.OnClickListener dismissDialog = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    private void handleClicks() {

        addCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmpty()) {
                    Log.d("COL", "inside cash btn onclick");
                    CollectionNote cn = new CollectionNote();
                    cn = setGrid(addCash);
                    Log.d("COL", "finished set grid -> display grid");
                    al.add(cn);
                    rvadapter.notifyDataSetChanged();
                    Log.d(TAG, "bal_" + balance);

                    //added as an extra
                    helper.updateCreditBalance(selectedInvoice, balance, InvCollections.this);
                    if (balance > 0.0) {
                        tvcreditAmt.setText(String.valueOf(balance));
                        selectedCredit = balance;
                    }

                    saveToCollectionHeader();
                    helper.updateCollectionNoteNo(InvCollections.this);
                    clear(v);
                    Toast.makeText(InvCollections.this, "Added", Toast.LENGTH_SHORT).show();
                    saveToCashDetails();
                } else {
                    alert.showAlert("Warning!", "One or more fields are empty.", "OK", dismissDialog);
                }
            }
        });

        addCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmpty()) {
                    Log.d("COL", "inside cheque btn onclick");
                    CollectionNote cn = new CollectionNote();
                    cn = setGrid(addCheque);
                    al.add(cn);
                    rvadapter.notifyDataSetChanged();

                    //added as an extra
                    helper.updateCreditBalance(selectedInvoice, balance, InvCollections.this);
                    if (balance > 0.0) {
                        tvcreditAmt.setText(String.valueOf(balance));
                        selectedCredit = balance;
                    }

                    saveToCollectionHeader();
                    helper.updateCollectionNoteNo(InvCollections.this);
                    saveToChequeDetails();
                    clear(v);
                } else {
                    alert.showAlert("Warning!", "One or more fields are empty.", "OK", dismissDialog);
                }
            }
        });

        addBoth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmpty()) {
                    CollectionNote cn = new CollectionNote();
                    cn = setGrid(addBoth);
                    al.add(cn);
                    rvadapter.notifyDataSetChanged();

                    //added as an extra
                    helper.updateCreditBalance(selectedInvoice, balance, InvCollections.this);
                    if (balance > 0.0) {
                        tvcreditAmt.setText(String.valueOf(balance));
                        selectedCredit = balance;
                    }

                    saveToCollectionHeader();
                    helper.updateCollectionNoteNo(InvCollections.this);
                    clear(v);
                    saveToCashDetails();
                    saveToChequeDetails();
                } else {
                    alert.showAlert("Warning!", "One or more fields are empty.", "OK", dismissDialog);
                }
            }
        });

    }

    private void saveToChequeDetails() {

        Log.d("COL", "inside saveToChequeDetails");
        CollectionChequeDetails ccd = new CollectionChequeDetails();

        hid = helper.getLastCollectionHeaderID(this);
        Log.d("COL", "hid_" + String.valueOf(hid));
        if (hid != -1) {
            cnno = helper.getCNoteNofromHeader(hid, this);
            ccd.setHeaderId(hid);
            ccd.setCollectionNoteNo(cnno);
            ccd.setRepId(helper.getSelectedRepId(selectedRep, this));
            ccd.setInvoiceNo(selectedInvoice);
            ccd.setChequeAmt(chequeValue);
            ccd.setChequeNo(cheqNo);
            ccd.setBank(bank);
            ccd.setBranch(branch);
            ccd.setRealizeDate(rdate);
            ccd.setIsUpload(UPLOAD_DEFAULT);

        }

        helper.insertCollectionChequeDetails(ccd, this);

    }

    private void saveToCashDetails() {

        hid = helper.getLastCollectionHeaderID(this);

        CollectionNoteDetails cnd = new CollectionNoteDetails();
        if (hid != -1) {
            cnno = helper.getCNoteNofromHeader(hid, this);
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

        helper.insertCollectionDetails(cnd, this);

    }

    public static void hideSoftKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    private void clear(View v) {

        hideSoftKeyboard(this, v);
        etcash1.setText("");
        etcash2.setText("");
        etcheque1.setText("");
        etcheque2.setText("");
        etchequeNo1.setText("");
        etchequeNo2.setText("");
        etBank1.setText("");
        etBank2.setText("");
        etBranch1.setText("");
        etBranch2.setText("");
        etrdate1.setText("");
        etrdate2.setText("");

    }

    private boolean checkEmpty() {
        if (TextUtils.isEmpty(etcash1.getText()) || TextUtils.isEmpty(etcash2.getText()) || TextUtils.isEmpty(etcheque1.getText()) || TextUtils.isEmpty(etcheque2.getText()) || TextUtils.isEmpty(etBank1.getText()) || TextUtils.isEmpty(etBank2.getText()) || TextUtils.isEmpty(etBranch1.getText()) || TextUtils.isEmpty(etBranch2.getText()) || TextUtils.isEmpty(etrdate1.getText()) || TextUtils.isEmpty(etrdate1.getText())) {
            return false;
        } else return true;
    }


    public CollectionNote setGrid(View v) {

        switch (v.getId()) {
            case R.id.btnAddCash:
                Log.d("COL", "inside set grid");
                cashValue = Double.valueOf(etcash1.getText().toString());
                Log.d("COL", "cash_" + String.valueOf(cashValue));
                chequeValue = 0.0;
                if (selectedCredit > cashValue) {
                    balance = selectedCredit - cashValue;
                    Log.d("COL", "bal_" + String.valueOf(balance));
                    remaining = 0.0;
                } else {
                    balance = 0.0;
                    remaining = cashValue - selectedCredit;
                    Log.d("COL", "remain_" + String.valueOf(remaining));
                }
                break;

            case R.id.btnAddCheque:
                cashValue = 0.0;
                chequeValue = Double.valueOf(etcheque1.getText().toString());
                cheqNo = etchequeNo1.getText().toString();
                bank = etBank1.getText().toString();
                branch = etBranch1.getText().toString();
                rdate = etrdate1.getText().toString();

                if (selectedCredit > chequeValue) {
                    balance = selectedCredit - chequeValue;
                    remaining = 0.0;
                } else {
                    balance = 0.0;
                    remaining = chequeValue - selectedCredit;
                }
                break;

            case R.id.btnAddCash_Cheque:
                cashValue = Double.valueOf(etcash2.getText().toString());
                chequeValue = Double.valueOf(etcheque2.getText().toString());
                cheqNo = etcheque2.getText().toString();
                bank = etBank2.getText().toString();
                branch = etBranch2.getText().toString();
                rdate = etrdate2.getText().toString();

                if (selectedCredit > cashValue) {
                    balance = selectedCredit - cashValue;
                    if (balance > chequeValue) {
                        balance = balance - chequeValue;
                        remaining = 0.0;
                    } else {
                        balance = 0.0;
                        remaining = chequeValue - balance;
                    }
                } else {
                    balance = 0.0;
                    remaining = (cashValue - selectedCredit) + chequeValue;
                }
                break;

        }

        CollectionNote note = new CollectionNote();
        note.setInvoiceNo(selectedInvoice);
        note.setCreditAmt(selectedCredit);
        note.setCashAmt(cashValue);
        note.setChequeAmt(chequeValue);
        note.setBalance(balance);
        note.setRemaining(remaining);

        Log.d("COL", "note obj_" + note.getCreditAmt());

        return note;
    }


    public void saveToCollectionHeader() {

        Log.d("COL", "inside saveToCollectionHeader");
        CollectionNoteHeader cn = new CollectionNoteHeader();
        cn.setCollectionNoteNo(String.valueOf(helper.getCollectionNoteNo(InvCollections.this))); //set value here
        cn.setCollectedDate(String.valueOf(today));
        Log.d("COL", String.valueOf(today));
        cn.setType(selected);
        Log.d("COL", "type_" + selected);
        cn.setCustomerNo(selectedCusNo);
        Log.d("COL", "setCustomerNo_" + selectedCusNo);
        cn.setIsUpload(UPLOAD_DEFAULT);

        helper.insertCollectionHeader(cn, this);

    }

    private void setInvoiceSpinner(String cusName) {

        invNoList = helper.getCollectionInvoiceNos(cusName, this);
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

        if (customerList.size() == 0) {

            customers.setEnabled(false);
            invnos.setEnabled(false);
            tvcreditAmt.setText("");
            Log.d("COL", "empty cus list");
        } else {

            customers.setEnabled(true);
            invnos.setEnabled(true);

            cusNames = new ArrayList<>();

            for (DBHelper.CollectionCustomer cc : customerList) {
                Log.d("COL", cc.getCustomerName());
                cusNames.add(cc.getCustomerName());
            }
            Collections.sort(cusNames);
            String[] cusArray = cusNames.toArray(new String[cusNames.size()]);

            for (int i = 0; i < cusArray.length; i++) {
                Log.d("COL", "Array_" + cusArray[i]);
            }

            cusAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, cusArray);
            customers.setAdapter(cusAdapter);
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

        today = Calendar.getInstance().getTime();

        alert = new Alert(InvCollections.this);

        adapter = new DBAdapter(this);
        helper = new DBHelper(this);
        types = (Spinner) findViewById(R.id.typeSpinner);
        routes = (Spinner) findViewById(R.id.routeSpinner);
        reps = (Spinner) findViewById(R.id.repSpinner);
        customers = (Spinner) findViewById(R.id.customerSpinner);
        invnos = (Spinner) findViewById(R.id.invoiceSpinner);
        flipper = (ViewFlipper) findViewById(R.id.flipper);

        rv = (RecyclerView) findViewById(R.id.rvCollections);

        addCash = (Button) findViewById(R.id.btnAddCash);
        addCheque = (Button) findViewById(R.id.btnAddCheque);
        addBoth = (Button) findViewById(R.id.btnAddCash_Cheque);

        tvcreditAmt = (TextView) findViewById(R.id.creditAmt);

        etcash1 = (EditText) findViewById(R.id.etCash);
        etcash2 = (EditText) findViewById(R.id.etCashInBoth);
        etcheque1 = (EditText) findViewById(R.id.etChqAmount);
        etcheque2 = (EditText) findViewById(R.id.etChqAmountInBoth);
        etchequeNo1 = (EditText) findViewById(R.id.etChequeNo);
        etchequeNo2 = (EditText) findViewById(R.id.etChequeNoInBoth);
        etBank1 = (EditText) findViewById(R.id.etBank);
        etBank2 = (EditText) findViewById(R.id.etBankInBoth);
        etBranch1 = (EditText) findViewById(R.id.etBranch);
        etBranch2 = (EditText) findViewById(R.id.etBranchInBoth);
        etrdate1 = (EditText) findViewById(R.id.etRealizeDate);
        etrdate2 = (EditText) findViewById(R.id.etRealizeDateInBoth);


    }


}
