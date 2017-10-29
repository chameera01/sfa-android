package com.example.ahmed.sfa.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.DateManager;
import com.example.ahmed.sfa.controllers.adapters.CustomerProduct;
import com.example.ahmed.sfa.controllers.adapters.CustomerProductAvg;
import com.example.ahmed.sfa.controllers.adapters.Customers;
import com.example.ahmed.sfa.controllers.adapters.ExpireWarning;
import com.example.ahmed.sfa.controllers.adapters.InvoiceGen1ProductListAdapter;
import com.example.ahmed.sfa.controllers.adapters.InvoiceGen1SelectedProductListAdapter;
import com.example.ahmed.sfa.controllers.adapters.ItineraryAdp;
import com.example.ahmed.sfa.controllers.adapters.NavigationDrawerMenuManager;
import com.example.ahmed.sfa.controllers.adapters.ProductRepStore;
import com.example.ahmed.sfa.controllers.adapters.Products;
import com.example.ahmed.sfa.controllers.adapters.ShelfQuantity;
import com.example.ahmed.sfa.models.ReturnProduct;
import com.example.ahmed.sfa.models.SelectedProduct;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;




/*

*SEARCH ALL STOP  COMMENTS
* branch test
 */
public class InvoiceGen extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_invoice_gen);
//    }
//
private static final String TAG = "InvoiceGen1Activity";
    ListView lViewProductList;
    ListView lViewSelectedProductList;
    TextView tviewTitle;
    Button btnNext, btnCancel, btnSaveShelfQty, btnDoneShelfQty;
    ImageButton iBtnRemoveProductSearchString, iBtnRemoveInvoicedProductSearchString;
    AutoCompleteTextView autoTvProductSearch;
    InvoiceGen1ProductListAdapter productListAdapter;
    InvoiceGen1SelectedProductListAdapter selectedProductListAdapter;
    ArrayList<String[]> productInformation = new ArrayList<String[]>();
    ArrayList<String[]> combinedArrayList = new ArrayList<String[]>();
    ArrayList<SelectedProduct> selectedProductsArray = new ArrayList<SelectedProduct>();
    ArrayAdapter<String> products;
    String rowId, pharmacyId, selectedBatch;
    int selectedRow;
    ListView lViewBatch;
    Context context = this;
    int normal = 0;
    AlertDialog.Builder alertCancel;
    HashMap<String, String> batchesForProductList = new HashMap<String, String>();
    CheckBox cBoxSelectedProduct;
    boolean selectedProductSearch = true;
    boolean flag = true;
    boolean isTemporaryCustomer = false;
    boolean shelfQuantityNeeded = false;
    boolean chequeEnabled = false;
    List<String[]> productList;
    String[] productDetails;
    ArrayList<String[]> selectedProductList = new ArrayList<String[]>();
    HashMap<String, String[]> openedProducts;
    Dialog addProductDialog, addProductDialog1;
    Dialog shelfQtyDialog;
    List<String[]> newProductList = new ArrayList<String[]>();
    ArrayList<String> selectedProductNamesArray = new ArrayList<String>();
    ArrayList<ReturnProduct> returnProductsArray = new ArrayList<ReturnProduct>();//model class
    ArrayList<String[]> tempSelectedProductList = new ArrayList<String[]>();
    String collectionDate = "", releaseDate = "", chequeNumber = "";
    private int previousProductInvoiceTotal = 0;

    @SuppressWarnings("unused")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invoice_gen);

        lViewProductList = (ListView) findViewById(R.id.lvProducts);
        lViewSelectedProductList = (ListView) findViewById(R.id.lvSelectedProducts);
        btnNext = (Button) findViewById(R.id.bNext);
        btnCancel = (Button) findViewById(R.id.bCancel);
        autoTvProductSearch = (AutoCompleteTextView) findViewById(R.id.etProductSearch);
        iBtnRemoveProductSearchString = (ImageButton) findViewById(R.id.bRemoveProductSearchString);
        btnSaveShelfQty = (Button) findViewById(R.id.bSaveShelfQuantity);
        cBoxSelectedProduct = (CheckBox) findViewById(R.id.cbSelectedProduct);

        if (savedInstanceState != null) {
            getDataFromPreviousActivity(savedInstanceState);
        } else {
            getDataFromPreviousActivity(getIntent().getExtras());
        }
        isTemporaryCustomer = getCustomerType();
        populateSelectedProductList(selectedProductList);
        populateProductListFromDb();
        populateProductInformationFromDb();

        updateProductList();

        alertCancel = new AlertDialog.Builder(this)
                .setTitle("Warning")
                .setMessage("Are you sure you want Cancel this Invoice?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent customerItineraryListIntent = new Intent("com.EdnaMedicals.channelbridge.ITINERARYLIST");
                                finish();
                                startActivity(customerItineraryListIntent);
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

        List<String[]> searchedProduct = new ArrayList<String[]>();

        setAdapterToProductSearchBar(getProductNameList());
        cBoxSelectedProduct.setChecked(true);
        cBoxSelectedProduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                selectedProductSearch = isChecked;
                populateProductListFromDb();
                updateProductList();
                populateSelectedProductList(selectedProductList);
                tempSelectedProductList.clear();

                if (isChecked) {
                    autoTvProductSearch.setText(null);
                    autoTvProductSearch.setHint(R.string.search_products);
                    setAdapterToProductSearchBar(getProductNameList());
                } else {
                    autoTvProductSearch.setText(null);
                    autoTvProductSearch.setHint(R.string.search_selected_products);
                    setAdapterToProductSearchBar(getSelectedProductNames());
                }
            }
        });


        autoTvProductSearch.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                String product = s.toString();
                if (selectedProductSearch) {
                    if (!product.isEmpty()) {
                        productList = searchProducts(s.toString());
                        updateProductList();
                    } else if (product.isEmpty()) {
                        populateProductListFromDb();
                        updateProductList();
                    }
                } else {
                    getSelectedProductNames();
                    ArrayList<Integer> ids = new ArrayList<Integer>();
                    tempSelectedProductList.clear();
                    if (!product.isEmpty()) {
                        for (int j = 0; j < selectedProductNamesArray.size(); j++) {
                            String productName = selectedProductNamesArray.get(j);
                            if (productName.toLowerCase().contains(product.toLowerCase())) {
                                tempSelectedProductList.add(selectedProductList.get(j));
                            }
                        }
                        populateSelectedProductList(tempSelectedProductList);
                    } else {
                        populateSelectedProductList(selectedProductList);
                        tempSelectedProductList.clear();
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        iBtnRemoveProductSearchString.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                autoTvProductSearch.setText(null);

            }
        });

        btnSaveShelfQty.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!isTemporaryCustomer) {
                    if (!selectedProductList.isEmpty()) {
                        boolean flag = false;
                        boolean shelf = false;
                        for (String[] selectedProductData : selectedProductList) {
                            if (Integer.parseInt(selectedProductData[7]) != 0) {
                                flag = true;
                            }
                        }


                        for (String[] selectedProductData : selectedProductList) {
                            if (Integer.parseInt(selectedProductData[13]) != 0) {
                                shelf = true;
                            }
                        }

                        if (flag) {
                            if (shelf) {
                                AlertDialog.Builder alertSaveShelfQty = new AlertDialog.Builder(InvoiceGen.this);

                                alertSaveShelfQty.setTitle("Alert").setMessage("You have added normal quantities to this invoice, are you sure you want to save the shelf quantities ONLY and exit?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                ShelfQuantity shelfQuantity = new ShelfQuantity(InvoiceGen.this);
                                                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
                                                for (String[] selectedProductData : selectedProductList) {
                                                    shelfQuantity.openWritableDatabase();
                                                    shelfQuantity.insertShelfQuantity("not_invoiced", timeStamp, pharmacyId, selectedProductData[2], selectedProductData[3], selectedProductData[13], timeStamp, "false");
                                                    shelfQuantity.closeDatabase();
                                                }

                                                Intent customerItineraryListIntent = new Intent("com.EdnaMedicals.channelbridge.ITINERARYLIST");
                                                finish();
                                                startActivity(customerItineraryListIntent);
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                return;
                                            }
                                        });
                                alertSaveShelfQty.create();
                                alertSaveShelfQty.show();
                            } else {
                                Toast noShelfQty = Toast.makeText(getApplicationContext(), "No Shelf quantities have been saved!", Toast.LENGTH_SHORT);
                                noShelfQty.setGravity(Gravity.TOP, 100, 100);
                                noShelfQty.show();
                            }
                        } else {
                            ShelfQuantity shelfQuantity = new ShelfQuantity(InvoiceGen.this);
                            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
                            for (String[] selectedProductData : selectedProductList) {
                                shelfQuantity.openWritableDatabase();
                                shelfQuantity.insertShelfQuantity("not_invoiced", timeStamp, pharmacyId, selectedProductData[2], selectedProductData[3], selectedProductData[13], timeStamp, "false");
                                shelfQuantity.closeDatabase();
                            }

                            Intent customerItineraryListIntent = new Intent("com.EdnaMedicals.channelbridge.ITINERARYLIST");
                            finish();
                            startActivity(customerItineraryListIntent);
                        }
                    } else {
                        Toast selectedProductsEmpty = Toast.makeText(getApplicationContext(), "No Products have been selected", Toast.LENGTH_SHORT);
                        selectedProductsEmpty.setGravity(Gravity.TOP, 100, 100);
                        selectedProductsEmpty.show();
                    }
                } else {
                    Toast customerIsPendingApproval = Toast.makeText(getApplicationContext(), "This customer is still pending approval!", Toast.LENGTH_SHORT);
                    customerIsPendingApproval.setGravity(Gravity.TOP, 100, 100);
                    customerIsPendingApproval.show();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                boolean flag = false;
                if (!selectedProductList.isEmpty()) {
                    for (String[] selectedProductData : selectedProductList) {
                        if (Integer.parseInt(selectedProductData[7]) != 0) {
                            flag = true;
                        }
                    }


                    if (flag) {
                        Intent invoiceGen2Intent = new Intent(
                                "com.EdnaMedicals.channelbridge.INVOICEGEN2ACTIVITY");

                        Bundle bundleToView = new Bundle();
                        bundleToView.putString("Id", rowId);
                        bundleToView.putString("PharmacyId", pharmacyId);

                        if (chequeEnabled) {
                            bundleToView.putString("ChequeNumber", chequeNumber);
                            bundleToView.putString("CollectionDate", collectionDate);
                            bundleToView.putString("ReleaseDate", releaseDate);
                        }

                        Log.w("invoicegen2", "selectedProductList size : " + selectedProductList.size());

                        ArrayList<SelectedProduct> selectedProductsArray = new ArrayList<SelectedProduct>();

                        for (String[] selectedProductData : selectedProductList) {
                            SelectedProduct product = new SelectedProduct();

                            product.setRowId(Integer.parseInt(selectedProductData[0]));
                            product.setProductId(String.valueOf(selectedProductData[1]));
                            product.setProductCode(String.valueOf(selectedProductData[2]));
                            product.setProductBatch(String.valueOf(selectedProductData[3]));
                            product.setQuantity(Integer.parseInt(selectedProductData[4]));
                            product.setExpiryDate(String.valueOf(selectedProductData[5]));
                            product.setTimeStamp(String.valueOf(selectedProductData[6]));

                            product.setRequestedQuantity(Integer.parseInt(selectedProductData[7]));
                            product.setFree(Integer.parseInt(selectedProductData[8]));
                            product.setNormal(Integer.parseInt(selectedProductData[9]));
                            product.setDiscount(Double.parseDouble(selectedProductData[10]));
                            product.setShelfQuantity(Integer.parseInt(selectedProductData[13]));
                            Log.w("next Button 090928340283423098", selectedProductData[13]);

                            product.setProductDescription(String.valueOf(selectedProductData[11]));
                            product.setPrice(Double.parseDouble(selectedProductData[12]));

                            Log.w("SelectedProduct Data", selectedProductData[0]);
                            Log.w("SelectedProduct Data", selectedProductData[1]);
                            Log.w("SelectedProduct Data", selectedProductData[2]);
                            Log.w("SelectedProduct Data", selectedProductData[3]);
                            Log.w("SelectedProduct Data 4", selectedProductData[4]);
                            Log.w("SelectedProduct Data", selectedProductData[5]);
                            Log.w("SelectedProduct Data", selectedProductData[6]);
                            Log.w("SelectedProduct Data", selectedProductData[7]);
                            Log.w("SelectedProduct Data", selectedProductData[8]);
                            Log.w("SelectedProduct Data", selectedProductData[9]);
                            Log.w("SelectedProduct Data", selectedProductData[10]);
                            Log.w("SelectedProduct Data", selectedProductData[11]);
                            Log.w("SelectedProduct Data", selectedProductData[12]);
                            Log.w("SelectedProduct Data", selectedProductData[13]);

                            selectedProductsArray.add(product);

                        }

                        bundleToView.putParcelableArrayList("SelectedProducts", selectedProductsArray);
                        bundleToView.putParcelableArrayList("ReturnProducts", returnProductsArray);
                        invoiceGen2Intent.putExtras(bundleToView);
                        finish();
                        startActivity(invoiceGen2Intent);
                    } else {
                        Toast onlyShelfQty = Toast.makeText(InvoiceGen.this, "You have only entered shelf quantity!", Toast.LENGTH_SHORT);
                        onlyShelfQty.setGravity(Gravity.TOP, 100, 100);
                        onlyShelfQty.show();
                    }

                } else {
                    Toast selectedProductsEmpty = Toast.makeText(getApplicationContext(), "No Products have been selected", Toast.LENGTH_SHORT);
                    selectedProductsEmpty.show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (selectedProductList.isEmpty()) {
                    Intent customerItineraryListIntent = new Intent("com.EdnaMedicals.channelbridge.ITINERARYLIST");
                    finish();
                    startActivity(customerItineraryListIntent);
                } else {
                    alertCancel.show();
                }


            }
        });

        lViewProductList.setClickable(true);
        lViewProductList.setVerticalFadingEdgeEnabled(true);
        lViewProductList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long clickedItem) {

               /*expired item warning function have inactive
                        if need you can active  this */
              /*  boolean expireAvalable = showExpireWarningmesage(clickedItem);//sk

                if (expireAvalable == false) {
                    callInvocepopUp(clickedItem);//sk
                }*/

                callInvocepopUp(clickedItem);


            }

        });

        lViewSelectedProductList.setClickable(true);




        ///
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();


        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationDrawerMenuManager(this);
        //getRepAndDeviceId();
        /////
    }

    private boolean showExpireWarningmesage(final long clickedItem) {//skk
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        String deviceId = sharedPreferences.getString("DeviceId", "-1");
        final String repId = sharedPreferences.getString("RepId", "-1");
        String delerId = null;
        String cutomerName = null;
        boolean bb = false;
        try {


            Object productListItem = lViewProductList.getItemAtPosition(Integer.parseInt(String.valueOf(clickedItem)));
            String[] productListSelectedItem = getSelectedRowdetails(productListItem);


            ArrayList<String[]> batchesByProductCode = new ArrayList<String[]>();

            ProductRepStore productRepStoreObject = new ProductRepStore(context);
            productRepStoreObject.openReadableDatabase();
            batchesByProductCode = productRepStoreObject.getProductDetailsByProductCode(productListSelectedItem[2]);
            productRepStoreObject.closeDatabase();

            String productInfo[] = batchesByProductCode.get(0);
            String productExDate = productInfo[5];

            String exMonth = productExDate.split("-")[1];
            String exyear = productExDate.split("-")[0];
            String productExDate1 = exyear + "-" + exMonth;

            Date date = DateManager.getNetMonth(3);
            String intMonth = (String) android.text.format.DateFormat.format("MM", date); //06
            String year = (String) android.text.format.DateFormat.format("yyyy", date); //2013
            String calExDate = year + "-" + intMonth;

            if (productExDate1.contentEquals(calExDate)) {//sk01
                selectedRow = Integer.parseInt(String.valueOf(clickedItem));
                addProductDialog1 = new Dialog(InvoiceGen.this);
                addProductDialog1.setContentView(R.layout.expire_warning_popup_messge);
                addProductDialog1.setCanceledOnTouchOutside(false);

                final TextView pname = (TextView) addProductDialog1.findViewById(R.id.pname);
                final TextView exdate = (TextView) addProductDialog1.findViewById(R.id.exdate);
                final EditText stock = (EditText) addProductDialog1.findViewById(R.id.stock);
                final TextView batch = (TextView) addProductDialog1.findViewById(R.id.batch);
                final TextView pcode = (TextView) addProductDialog1.findViewById(R.id.pcode);
                final TextView cusName = (TextView) addProductDialog1.findViewById(R.id.cusName);
                final EditText contactnumber = (EditText) addProductDialog1.findViewById(R.id.contactnumber);

                final Button ImformToOutlet = (Button) addProductDialog1.findViewById(R.id.ImformtoOutlet);
                Products products1 = new Products(context);
                products1.openReadableDatabase();
                String product[] = products1.getProductDetailsByProductCode(productInfo[2]);
                products1.closeDatabase();


                Customers customers = new Customers(InvoiceGen.this);
                customers.openReadableDatabase();
// STOP               String cusinfo[] = customers.getCustomerDetailsByPharmacyId(ItineraryDetailsFragment.pharmacyId1);
//                if (cusinfo.length > 0) {
//                    delerId = cusinfo[3];
//                    cutomerName = cusinfo[5];
//                }
                customers.closeDatabase();

                exdate.setText(productInfo[5].split("T")[0]);
                cusName.setText(cutomerName);
                batch.setText(productInfo[3]);
                pcode.setText(productInfo[2]);
                pname.setText(product[6]);
//STOP               contactnumber.setText(ItineraryDetailsFragment.contactNumber);

                addProductDialog1.show();

                final String finalDelerId = delerId;
                ImformToOutlet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String cNumber = contactnumber.getText().toString();
                            String stock1 = stock.getText().toString();
                            String pname1 = pname.getText().toString();
                            String exdate1 = exdate.getText().toString();
                            String ppcode = pcode.getText().toString();
                            String batchcode = batch.getText().toString();
                            if (cNumber != null && cNumber.length() == 10 && stock1 != null && pname1 != null && finalDelerId != null && ppcode != null) {

                                String message = "Hi. You have " + stock1 + " of " + pname1 + " which is expiring on " + exdate1 + " . Pls hold responsible for not expiring";
//STOP                                boolean bb = SMSSender.sendMessage(cNumber, message);

// STOP                               if (bb == true) {
//
//                                    ExpireWarning expireWarning = new ExpireWarning(context);
//                                    expireWarning.openWritableDatabase();
//                                    String currentdate = DateManager.dateToday();//DataFormat.getCurrentDate(
//                                    try {
//                                        long b = expireWarning.insert_Branch(ItineraryDetailsFragment.pharmacyId1, finalDelerId, repId, currentdate, ppcode, stock1, exdate1, batchcode, cNumber, "false");
//
//                                        UploadExpiredWarning uploadExpiredWarning = new UploadExpiredWarning(InvoiceGen1Activity.this);//sk
//                                        uploadExpiredWarning.execute();
//
//
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    expireWarning.closeDatabase();
//                                    callInvocepopUp(clickedItem);//skk
//
//                                }


                            } else {
//STOP                          showMessage showMessage = new showMessage(context);
//                                showMessage.customiseAlertMessage("please check the contact number..");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
                bb = true;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return bb;
    }

    private void callInvocepopUp(long clickedItem) {

        updateProductList();
        Object productListItem = lViewProductList.getItemAtPosition(Integer.parseInt(String.valueOf(clickedItem)));
        String[] productListSelectedItem = getSelectedRowdetails(productListItem);
        Log.w("Clicked index", String.valueOf(clickedItem) + "");
        selectedRow = Integer.parseInt(String.valueOf(clickedItem));


        addProductDialog = new Dialog(InvoiceGen.this);
        addProductDialog.setContentView(R.layout.invoice_gen_1_add_product_popup);
        addProductDialog.setCanceledOnTouchOutside(false);

        tviewTitle = (TextView) addProductDialog.findViewById(R.id.tvTitle);
        final EditText txtRequestQty = (EditText) addProductDialog.findViewById(R.id.etRequestQty);
        final EditText txtFree = (EditText) addProductDialog.findViewById(R.id.etFree);
        final EditText txtNormal = (EditText) addProductDialog.findViewById(R.id.etNormal);
        final EditText txtDiscount = (EditText) addProductDialog.findViewById(R.id.etDiscount);
        final EditText txtShelfQty = (EditText) addProductDialog.findViewById(R.id.etShelfQuantity);
//							final Button btnDone = (Button) addProductDialog.findViewById(R.id.bDone);
        final Button btnCancelPopup = (Button) addProductDialog.findViewById(R.id.bCancel);

        final TextView tViewLastInvoiceQty = (TextView) addProductDialog.findViewById(R.id.tvLastInvoiceQty);
        final TextView tViewLastThreeMonthAverage = (TextView) addProductDialog.findViewById(R.id.tvLastThreeMonthAvg);
        final TextView tViewCurrentStock = (TextView) addProductDialog.findViewById(R.id.tvCurrentStock);
        final TextView tViewDiscount = (TextView) addProductDialog.findViewById(R.id.tvDiscount);
        final TextView tViewExpiryDate = (TextView) addProductDialog.findViewById(R.id.tvExpiryDate);
        final TextView tViewPrice = (TextView) addProductDialog.findViewById(R.id.tvPrice);
        final TextView tViewBatch = (TextView) addProductDialog.findViewById(R.id.tvBatch);
        final TextView ProductCode = (TextView) addProductDialog.findViewById(R.id.tvProductCode);

        txtRequestQty.setText("0");
        int position = txtRequestQty.length();
        Editable etext = txtRequestQty.getText();
        Selection.setSelection(etext, position);
        txtRequestQty.setSelection(0, position);
        txtFree.setEnabled(false);

        if (isTemporaryCustomer) {
            txtShelfQty.setEnabled(false);
            txtRequestQty.requestFocus();
            shelfQuantityNeeded = false;
        } else {
            txtShelfQty.setEnabled(true);
            txtShelfQty.requestFocus();
            shelfQuantityNeeded = true;
        }

//							txtNormal.setText("0");
//							txtFree.setText("0");
//							txtDiscount.setText("0.0");

        //DEACTIVATED THE DONE BUTTON AS A TEMPORARRY FIX FOR GALAXY TAB2
//							btnDone.setEnabled(false);


        ArrayList<String[]> batchesByProductCode = new ArrayList<String[]>();

        ProductRepStore productRepStoreObject = new ProductRepStore(context);
        productRepStoreObject.openReadableDatabase();
        batchesByProductCode = productRepStoreObject.getProductDetailsByProductCode(productListSelectedItem[2]);
        productRepStoreObject.closeDatabase();

        ArrayList<Integer> idsToRemove = new ArrayList<Integer>();

//							//GETTING ALL IDs OF PRODUCTS WHERE THE BATCHES HAVE BEEN ALREADY BEEN ADDED
//							for (int i = 0; i < batchesByProductCode.size(); i++) {
//								String[] data = batchesByProductCode.get(i);
//
//								for (int j = 0; j < selectedProductList.size(); j++) {
//									String[] selectedData = selectedProductList.get(j);
//									if (selectedData[3].contentEquals(data[3])) {
//										idsToRemove.add(String.valueOf(data[0]));
//									}
//								}
//							}
//
//							//REMOVE ALL ADDED BATCHES FROM batchesByProduct BY rowID
//							for (String i: idsToRemove) {
//								for (int j = 0; j < batchesByProductCode.size(); j++) {
//									String[] data = batchesByProductCode.get(j);
//									if (data[0].contentEquals(i)) {
//										batchesByProductCode.remove(j);
//										Log.w("Batch Removed:", data[3]);
//									}
//								}
//							}

//							idsToRemove.clear();
        //PRODUCTS WITH 0 QUANTITY
        for (int i = 0; i < batchesByProductCode.size(); i++) {
            String[] data = batchesByProductCode.get(i);
            if (Integer.parseInt(data[4]) < 1) {
                idsToRemove.add(i);
                Log.w("ZERO quantity products", data[3]);
                Log.w("ZERO quantity stock", data[4]);
            }
        }
        Log.w("ids to remove size", idsToRemove.size() + "");
        //REMOVING PRODUCTS WITH 0 QUANTITY
        for (int i = idsToRemove.size() - 1; i >= 0; i--) {
            int id = idsToRemove.get(i);
            Log.w("ID $%^&*", id + "");
            for (int j = batchesByProductCode.size() - 1; j >= 0; j--) {
                String[] data = batchesByProductCode.get(j);
                if (id == j) {

                    Log.w("Id to remove     data [0]:", id + "     " + j);

                    //Log.w("Batch Removed:",batchesByProductCode.contains(id) + "");
                    batchesByProductCode.remove(id);
                    Log.w("Batch Removed:", data[3]);
                }
            }
        }
        Log.w("batch list size :", batchesByProductCode.size() + "");

//							setPopUpBatchListAdapter(batchesByProductCode);
//							lViewBatch.setChoiceMode(1);
//							lViewBatch.setItemChecked(0, true);
//							lViewBatch.setSelection(0);

        openedProducts = new HashMap<String, String[]>();


        try {
            final Object preBatchListItem1 = lViewBatch.getItemAtPosition(0);
            final Object preProductListItem1 = lViewProductList.getItemAtPosition(Integer.parseInt(String.valueOf(clickedItem)));
            String[] preTempSelectedProduct = getSelectedRowdetails(preProductListItem1);
            ProductRepStore productRepStoreObject1 = new ProductRepStore(context);
            productRepStoreObject1.openReadableDatabase();
            productDetails = productRepStoreObject1.getProductDetailsByProductBatchAndProductCode(preBatchListItem1.toString(), String.valueOf(preTempSelectedProduct[2]));
            productRepStoreObject1.closeDatabase();
            tViewExpiryDate.setText(productDetails[5].substring(0, 10));
            tViewPrice.setText(preTempSelectedProduct[13]);
            tViewBatch.setText(preBatchListItem1.toString());
            ProductCode.setText(preTempSelectedProduct[2]);
            tViewCurrentStock.setText(productDetails[4]);

        } catch (Exception e) {
            Log.w("IG1 error setting preselect details", e.toString());

        }

        if (!batchesByProductCode.isEmpty()) {


            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            boolean invoiceQtySuggestion = preferences.getBoolean("cbPrefProductAvg", true);

            Log.i("invoiceQtySuggestion", invoiceQtySuggestion + "");

								/*invoiceQtySuggestion  = true means :
                                 * Get the average of invoiced quantities
								 */
            previousProductInvoiceTotal = 0;
            if (invoiceQtySuggestion) {
                CustomerProductAvg customerProductAvg = new CustomerProductAvg(InvoiceGen.this);
                customerProductAvg.openReadableDatabase();
                previousProductInvoiceTotal = customerProductAvg.getAverage(pharmacyId, productListSelectedItem[2]);
                customerProductAvg.closeDatabase();
            } else {
                CustomerProduct customerProduct = new CustomerProduct(InvoiceGen.this);
                customerProduct.openReadableDatabase();
                previousProductInvoiceTotal = customerProduct.getInvoicedQuantityForCustomer(pharmacyId, productListSelectedItem[2]);
                Log.e("Prev invoiced amount: ", previousProductInvoiceTotal + "");
                customerProduct.closeDatabase();
            }


            setPopUpBatchListAdapter(batchesByProductCode);
            try {
                Log.w("batchlist selected", lViewBatch.isSelected() + "");
                if (!lViewBatch.isSelected()) {

//									lViewBatch.setChoiceMode(1);
//									lViewBatch.setItemChecked(0, true);
//									lViewBatch.setSelection(0);

                    txtShelfQty.setFocusable(true);
                    txtShelfQty.requestFocus();

                    final Object productListItem1 = lViewProductList.getItemAtPosition(Integer.parseInt(String.valueOf(clickedItem)));
                    String[] tempSelectedProduct = getSelectedRowdetails(productListItem1);

                    final Object batchListItem1 = lViewBatch.getItemAtPosition(0);

                    selectedBatch = batchListItem1.toString();

                    Log.w("invoice gen :", "batchListItem.toString() : " + batchListItem1.toString());

                    if (!openedProducts.containsKey(batchListItem1.toString())) {
                        ProductRepStore productRepStoreObject1 = new ProductRepStore(context);
                        productRepStoreObject1.openReadableDatabase();
                        productDetails = productRepStoreObject1.getProductDetailsByProductBatchAndProductCode(batchListItem1.toString(), tempSelectedProduct[2]);
                        productRepStoreObject1.closeDatabase();

                        txtNormal.setText("0");

                    } else {
                        productDetails = openedProducts.get(batchListItem1.toString());
                        txtRequestQty.setText("0");
                        int pos = txtRequestQty.length();
                        Editable rTxt = txtRequestQty.getText();
                        Selection.setSelection(rTxt, pos);
                        txtFree.setText(productDetails[8]);
                        txtNormal.setText(productDetails[9]);
                        txtDiscount.setText(productDetails[10]);
                    }

                    tViewCurrentStock.setText(productDetails[4]);
                    normal = Integer.parseInt(productDetails[4]);
                    Log.w("normal current stock: ", normal + "");
                    tViewExpiryDate.setText(productDetails[5].substring(0, 10));
                    tViewPrice.setText(tempSelectedProduct[13]);
                    tViewBatch.setText(batchListItem1.toString());
                    ProductCode.setText(tempSelectedProduct[2]);
                    for (int i = 0; i < batchesByProductCode.size(); i++) {
                        String[] data = batchesByProductCode.get(i);
                        Log.w("InvoiceGen1 ", "productsByBatch: data[3] : " + data[3]);

                        for (int j = 0; j < selectedProductList.size(); j++) {
                            String[] selectedData = selectedProductList.get(j);
                            Log.w("InvoiceGen1 ", "selectedProduct: data[3] : " + selectedData[3]);
                            if (selectedData[3].equals(data[3])) {
                                batchesByProductCode.remove(i);
                                Log.w("productsByBatch remove", i + "Removed");

                            }
                        }
                    }

                    for (int i = 0; i < batchesByProductCode.size(); i++) {
                        String[] data = batchesByProductCode.get(i);
                        if (Integer.parseInt(data[4]) == 0) {
                            batchesByProductCode.remove(i);
                        }
                    }
//									setPopUpBatchListAdapter(batchesByProductCode);
//									if (!batchesByProductCode.isEmpty()) {
//										previousProductInvoiceTotal = 0;
//										CustomerProduct customerProduct = new CustomerProduct(InvoiceGen1Activity.this);
//										customerProduct.openReadableDatabase();
//										previousProductInvoiceTotal = customerProduct.getInvoicedQuantityForCustomer(pharmacyId, tempSelectedProduct[2]);
//										Log.e("Prev invoiced amount: ", previousProductInvoiceTotal + "");
//										customerProduct.closeDatabase();
//									}

                }
            } catch (Exception e) {
                Log.w("IG1 initial batch sel error", e.toString());
            }


            try {


                lViewBatch.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        flag = false;

                        txtShelfQty.setFocusable(true);
                        txtShelfQty.requestFocus();
                        Log.w("OPENED PRODUCTS.SIZE()", openedProducts.size() + "");
                        final Object productListItem = lViewProductList.getItemAtPosition(selectedRow);
                        String[] tempSelectedProduct = getSelectedRowdetails(productListItem);
                        final Object batchListItem = lViewBatch.getItemAtPosition(arg2);


                        txtFree.setEnabled(false);


                        String[] sd = new String[5];
                        sd[0] = txtRequestQty.getText().toString();
                        sd[1] = txtFree.getText().toString();
                        sd[2] = txtNormal.getText().toString();
                        sd[3] = txtDiscount.getText().toString();
                        sd[4] = txtShelfQty.getText().toString();

                        if (sd[0].isEmpty()) {
                            sd[0] = "0";
                        }
                        if (sd[1].isEmpty()) {
                            sd[1] = "0";
                        }
                        if (sd[2].isEmpty()) {
                            sd[2] = "0";
                        }
                        if (sd[3].isEmpty()) {
                            sd[3] = "0";
                        }
                        if (sd[4].isEmpty()) {
                            sd[4] = "0";
                        }
                        if (isTemporaryCustomer) {
                            sd[4] = "0";
                        }
                        if (!sd[0].contentEquals("0") || !sd[4].contentEquals("0")) {
                            productDetails[7] = sd[0];
                            productDetails[8] = sd[1];
                            productDetails[9] = sd[2];
                            productDetails[10] = sd[3];
                            productDetails[13] = sd[4];

                            Log.w("productDetails[7]", productDetails[7]);
                            if (openedProducts.containsKey(selectedBatch)) {
                                openedProducts.remove(selectedBatch);
                                openedProducts.put(selectedBatch, productDetails);
                                Log.w("Request Qty: THis batch is already there", productDetails[7]);
                            } else {
                                openedProducts.put(selectedBatch, productDetails);
                            }
                        } else {
                            openedProducts.remove(selectedBatch);
                        }

                        Log.w("free amount", productDetails[8]);

                        Log.w("PREVIOUS BATCH", selectedBatch);
                        selectedBatch = batchListItem.toString();
                        Log.w("NEW SELECTED BAtch", selectedBatch);

                        Log.w("invoice gen :", "batchListItem.toString() : " + batchListItem.toString());

                        if (!openedProducts.containsKey(batchListItem.toString())) {
                            ProductRepStore productRepStoreObject = new ProductRepStore(context);
                            productRepStoreObject.openReadableDatabase();
                            productDetails = productRepStoreObject.getProductDetailsByProductBatchAndProductCode(batchListItem.toString(), tempSelectedProduct[2]);
                            productRepStoreObject.closeDatabase();

                            txtRequestQty.setText("0");
                            int pos = txtRequestQty.length();
                            Editable rTxt = txtRequestQty.getText();
                            Selection.setSelection(rTxt, pos);
                            txtFree.setText("0");
                            txtNormal.setText(null);
                            txtDiscount.setText("0");
                            txtNormal.setText("0");
                            txtShelfQty.setText(null);

                        } else {

                            Log.w("free amount 1", productDetails[8]);
                            productDetails = openedProducts.get(batchListItem.toString());
                            Log.w("free amount 2", productDetails[8]);
                            txtShelfQty.setText(productDetails[13]);
                            txtRequestQty.setText(productDetails[7]);
                            int pos = txtRequestQty.length();
                            Editable rTxt = txtRequestQty.getText();
                            Selection.setSelection(rTxt, pos);
                            txtRequestQty.setSelection(0, pos);
                            txtFree.setText(productDetails[8]);
                            txtNormal.setText(productDetails[9]);
                            txtDiscount.setText(productDetails[10]);

                        }

                        tViewCurrentStock.setText(productDetails[4]);

                        normal = Integer.parseInt(productDetails[4]);

                        tViewExpiryDate.setText(productDetails[5].substring(0, 10));
                        tViewPrice.setText(tempSelectedProduct[13]);
                        tViewBatch.setText(batchListItem.toString());
                        ProductCode.setText(tempSelectedProduct[2]);
                        flag = true;
                    }
                });

            } catch (Exception e) {
                Log.w("batchListItemError:", e.toString());
            }

            txtShelfQty.setOnKeyListener(new View.OnKeyListener() {

                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                        if (!txtShelfQty.getText().toString().isEmpty()) {
                            txtRequestQty.setFocusable(true);
                            txtRequestQty.requestFocus();
                            String[] sd = new String[5];
                            sd[0] = txtRequestQty.getText().toString();
                            sd[1] = txtFree.getText().toString();
                            sd[2] = txtNormal.getText().toString();
                            sd[3] = txtDiscount.getText().toString();
                            sd[4] = txtShelfQty.getText().toString();

                            if (sd[0].isEmpty()) {
                                sd[0] = "0";
                            }
                            if (sd[1].isEmpty()) {
                                sd[1] = "0";
                            }
                            if (sd[2].isEmpty()) {
                                sd[2] = "0";
                            }
                            if (sd[3].isEmpty()) {
                                sd[3] = "0";
                            }
                            if (sd[4].isEmpty()) {
                                sd[4] = "0";
                            }
                            if (isTemporaryCustomer) {
                                sd[4] = "0";
                            }

                            productDetails[7] = sd[0];
                            productDetails[8] = sd[1];
                            productDetails[9] = sd[2];
                            productDetails[10] = sd[3];
                            productDetails[13] = sd[4];

                            Log.w("productDetails[7]", productDetails[7]);
                            openedProducts.remove(selectedBatch);
                            openedProducts.put(selectedBatch, productDetails);
                            return true;

                        } else {
                            txtShelfQty.setFocusable(true);
                            txtShelfQty.requestFocus();

                            Toast enterShelfQty = Toast.makeText(context, "Enter shelf quantity!", Toast.LENGTH_SHORT);
                            enterShelfQty.setGravity(Gravity.TOP, 100, 100);
                            enterShelfQty.show();

                            return true;
                        }

                    } else if (!(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DEL)) {
                        return false;
                    }
                    return false;
                }
            });

            txtShelfQty.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // TODO Auto-generated method stub
                    if (!s.toString().isEmpty() && flag) {

                        int requestQty = 0;
                        try {
                            requestQty = Integer.parseInt(txtRequestQty.getText().toString());
                        } catch (NumberFormatException e) {
                            Log.e(TAG, e.toString());
                        }

                        if (requestQty == 0) {
                            txtFree.setEnabled(false);
                        } else {
                            txtFree.setEnabled(true);
                        }

                        String[] sd = new String[5];
                        sd[0] = txtRequestQty.getText().toString();
                        sd[1] = txtFree.getText().toString();
                        sd[2] = txtNormal.getText().toString();
                        sd[3] = txtDiscount.getText().toString();
                        sd[4] = txtShelfQty.getText().toString();


                        //TODO - Important! Uncomment when predict request qty functionality required.
                        if (previousProductInvoiceTotal != 0) {
                            int currentShelfQuantity = Integer.parseInt(s.toString());
                            if (currentShelfQuantity <= previousProductInvoiceTotal) {
                                int remainingQuantity = previousProductInvoiceTotal - currentShelfQuantity;
                                txtRequestQty.setText(String.valueOf(remainingQuantity));
                                hightLightText(txtRequestQty);
                            } else {
                                txtRequestQty.setText("0");
                                hightLightText(txtRequestQty);
                            }
                        }


                        if (sd[0].isEmpty()) {
                            sd[0] = "0";
                        }
                        if (sd[1].isEmpty()) {
                            sd[1] = "0";
                        }
                        if (sd[2].isEmpty()) {
                            sd[2] = "0";
                        }
                        if (sd[3].isEmpty()) {
                            sd[3] = "0";
                        }
                        if (sd[4].isEmpty()) {
                            sd[4] = "0";
                        }
                        if (isTemporaryCustomer) {
                            sd[4] = "0";
                        }

                        productDetails[7] = sd[0];
                        productDetails[8] = sd[1];
                        productDetails[9] = sd[2];
                        productDetails[10] = sd[3];
                        productDetails[13] = sd[4];
                        Log.w("shelfwty key listener", productDetails[13]);

                        Log.w("productDetails[7]", productDetails[7]);
                        if (openedProducts.containsKey(selectedBatch)) {
                            openedProducts.remove(selectedBatch);
                            openedProducts.put(selectedBatch, productDetails);
                            Log.w("THis batch is already there", productDetails[7]);
                        } else {
                            openedProducts.put(selectedBatch, productDetails);
                        }

                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // TODO Auto-generated method stub

                }

                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub

                }
            });

            txtRequestQty.setOnKeyListener(new View.OnKeyListener() {

                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // TODO Auto-generated method stub
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                        if (!txtRequestQty.getText().toString().isEmpty()) {
                            Log.w("selectedProduct 0", productDetails[0] + "");
                            Log.w("selectedProduct 1", productDetails[1] + "");
                            Log.w("selectedProduct 2", productDetails[2] + "");
                            Log.w("selectedProduct 3", productDetails[3] + "");
                            Log.w("selectedProduct 4", productDetails[4] + "");
                            Log.w("selectedProduct 5", productDetails[5] + "");
                            Log.w("selectedProduct 6", productDetails[6] + "");
                            Log.w("selectedProduct 7", productDetails[7] + "");
                            Log.w("selectedProduct 8", productDetails[8] + "");
                            Log.w("selectedProduct 9", productDetails[9] + "");
                            Log.w("selectedProduct 10", productDetails[10] + "");
                            Log.w("selectedProduct 11", productDetails[11] + "");
                            Log.w("selectedProduct 12", productDetails[12] + "");

                            final Object productListItem = lViewProductList.getItemAtPosition(selectedRow);
                            String[] tempSelectedProduct = getSelectedRowdetails(productListItem);


                            try {
                                for (String key : openedProducts.keySet()) {
                                    String[] data = openedProducts.get(key);
                                    if ((!data[7].contentEquals("0")) || (!data[13].contentEquals("0"))) {
                                        Log.w("opened data[7].size()", data[7] + "");
                                        data[11] = tempSelectedProduct[8];
                                        data[12] = tempSelectedProduct[13];
                                        addToSelectedProductList(data);
                                        updateProductList();
                                    }

                                }
                            } catch (Exception e) {
                                Log.w("opened products empty", e.toString());
                                Log.w("opened data[11].size()", tempSelectedProduct[8] + "");
                                Log.w("opened data[12].size()", tempSelectedProduct[12] + "");
                            }

                            autoTvProductSearch.setText(null);
                            populateSelectedProductList(selectedProductList);
                            txtShelfQty.setFocusable(true);
                            txtShelfQty.requestFocus();
                            openedProducts.clear();
                            addProductDialog.cancel();
                            tViewExpiryDate.setText(null);
                            tViewPrice.setText(null);
                            tViewBatch.setText(null);
                            ProductCode.setText(null);
                            tViewCurrentStock.setText(null);
                            return true;
                        }

                    } else if (!(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DEL)) {
                        return false;
                    }
                    return false;
                }
            });

            txtRequestQty.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // TODO Auto-generated method stub
                    int requestQty = 0;
                    try {
                        requestQty = Integer.parseInt(s.toString());
                    } catch (NumberFormatException e) {
                        Log.e(TAG, e.toString());
                    }

                    if (requestQty == 0) {
                        txtFree.setEnabled(false);
                        txtFree.setText("0");
                    } else {
                        txtFree.setEnabled(true);
                    }


                    if (flag) {
                        if (!isTemporaryCustomer) {
                            if (!txtShelfQty.getText().toString().isEmpty()) {
                                if (!txtRequestQty.getText().toString().isEmpty()) {

                                    if (Integer.parseInt(txtRequestQty.getText().toString()) <= normal) {
                                        txtNormal.setText(txtRequestQty.getText().toString());
                                    } else {

                                        txtNormal.setText(String.valueOf(normal));
                                        Toast RequestQtyNotAvailable = Toast.makeText(getApplication(), "Requested Quantity is greater than current stock", Toast.LENGTH_SHORT);
                                        RequestQtyNotAvailable.setGravity(Gravity.TOP, 200, 100);
                                        RequestQtyNotAvailable.show();
                                    }


                                    int pos = txtFree.length();
                                    Editable fTxt = txtFree.getText();
                                    Selection.setSelection(fTxt, pos);
                                    txtFree.setSelection(0, pos);

                                    String[] sd = new String[5];
                                    sd[0] = txtRequestQty.getText().toString();
                                    sd[1] = txtFree.getText().toString();
                                    sd[2] = txtNormal.getText().toString();
                                    sd[3] = txtDiscount.getText().toString();
                                    sd[4] = txtShelfQty.getText().toString();

                                    if (sd[0].isEmpty()) {
                                        sd[0] = "0";
                                    }
                                    if (sd[1].isEmpty()) {
                                        sd[1] = "0";
                                    }
                                    if (sd[2].isEmpty()) {
                                        sd[2] = "0";
                                    }
                                    if (sd[3].isEmpty()) {
                                        sd[3] = "0";
                                    }
                                    if (sd[4].isEmpty()) {
                                        sd[4] = "0";
                                    }
                                    if (isTemporaryCustomer) {
                                        sd[4] = "0";
                                    }

                                    productDetails[7] = sd[0];
                                    productDetails[8] = sd[1];
                                    productDetails[9] = sd[2];
                                    productDetails[10] = sd[3];
                                    productDetails[13] = sd[4];

                                    Log.w("productDetails[7]", productDetails[7]);
                                    if (openedProducts.containsKey(selectedBatch)) {
                                        openedProducts.remove(selectedBatch);
                                        openedProducts.put(selectedBatch, productDetails);
                                        Log.w("Request Qty: THis batch is already there", productDetails[7]);
                                    } else {
                                        openedProducts.put(selectedBatch, productDetails);
                                    }
                                } else {
                                    txtRequestQty.setText("0");
                                    int pos = txtRequestQty.length();
                                    Editable rTxt = txtRequestQty.getText();
                                    Selection.setSelection(rTxt, pos);
                                    txtRequestQty.setSelection(0, pos);
                                }
                            } else {
                                Toast enterShelfQty = Toast.makeText(context, "Enter shelf quantity!", Toast.LENGTH_SHORT);
                                enterShelfQty.setGravity(Gravity.TOP, 100, 100);
                                enterShelfQty.show();
                                int pos = txtRequestQty.length();
                                Editable rTxt = txtRequestQty.getText();
                                Selection.setSelection(rTxt, pos);
                                txtShelfQty.setFocusable(true);
                                txtShelfQty.requestFocus();
                            }
                        } else {
                            if (!txtRequestQty.getText().toString().isEmpty()) {

                                if (Integer.parseInt(txtRequestQty.getText().toString()) <= normal) {
                                    txtNormal.setText(txtRequestQty.getText().toString());
                                } else {

                                    txtNormal.setText(String.valueOf(normal));
                                    Toast RequestQtyNotAvailable = Toast.makeText(getApplication(), "Requested Quantity is greater than current stock", Toast.LENGTH_SHORT);
                                    RequestQtyNotAvailable.setGravity(Gravity.TOP, 200, 100);
                                    RequestQtyNotAvailable.show();
                                }

                                txtFree.setText("0");
                                int pos = txtFree.length();
                                Editable fTxt = txtFree.getText();
                                Selection.setSelection(fTxt, pos);
                                txtFree.setSelection(0, pos);

                                String[] sd = new String[5];
                                sd[0] = txtRequestQty.getText().toString();
                                sd[1] = txtFree.getText().toString();
                                sd[2] = txtNormal.getText().toString();
                                sd[3] = txtDiscount.getText().toString();
                                sd[4] = txtShelfQty.getText().toString();

                                if (sd[0].isEmpty()) {
                                    sd[0] = "0";
                                }
                                if (sd[1].isEmpty()) {
                                    sd[1] = "0";
                                }
                                if (sd[2].isEmpty()) {
                                    sd[2] = "0";
                                }
                                if (sd[3].isEmpty()) {
                                    sd[3] = "0";
                                }
                                if (sd[4].isEmpty()) {
                                    sd[4] = "0";
                                }
                                if (isTemporaryCustomer) {
                                    sd[4] = "0";
                                }

                                productDetails[7] = sd[0];
                                productDetails[8] = sd[1];
                                productDetails[9] = sd[2];
                                productDetails[10] = sd[3];
                                productDetails[13] = sd[4];

                                Log.w("productDetails[7]", productDetails[7]);
                                if (openedProducts.containsKey(selectedBatch)) {
                                    openedProducts.remove(selectedBatch);
                                    openedProducts.put(selectedBatch, productDetails);
                                    Log.w("Request Qty: THis batch is already there", productDetails[7]);
                                } else {
                                    openedProducts.put(selectedBatch, productDetails);
                                }
                            } else {
                                txtRequestQty.setText("0");
                                int pos = txtRequestQty.length();
                                Editable rTxt = txtRequestQty.getText();
                                Selection.setSelection(rTxt, pos);
                                txtRequestQty.setSelection(0, pos);
                            }
                        }
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // TODO Auto-generated method stub

                }

                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub

                }
            });

            txtFree.setOnKeyListener(new View.OnKeyListener() {

                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // TODO Auto-generated method stub
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                        if (!txtRequestQty.getText().toString().isEmpty()) {

                            final Object productListItem = lViewProductList.getItemAtPosition(selectedRow);
                            String[] tempSelectedProduct = getSelectedRowdetails(productListItem);


                            try {
                                for (String key : openedProducts.keySet()) {
                                    String[] data = openedProducts.get(key);

                                    if ((!data[7].contentEquals("0")) || (!data[13].contentEquals("0"))) {
                                        Log.w("opened data[7].size()", data[7] + "");
                                        data[11] = tempSelectedProduct[8];
                                        data[12] = tempSelectedProduct[13];
                                        addToSelectedProductList(data);
                                        updateProductList();
                                    }

                                }
                            } catch (Exception e) {
                                Log.w("opened products empty", e.toString());
                                Log.w("opened data[11].size()", tempSelectedProduct[8] + "");
                                Log.w("opened data[12].size()", tempSelectedProduct[12] + "");
                            }

                            autoTvProductSearch.setText(null);
                            populateSelectedProductList(selectedProductList);

                            int pos = txtRequestQty.length();
                            Editable rTxt = txtRequestQty.getText();
                            Selection.setSelection(rTxt, pos);
//												autoTvProductSearch.setText(null);
                            populateSelectedProductList(selectedProductList);
                            txtShelfQty.setFocusable(true);
                            txtShelfQty.requestFocus();
                            openedProducts.clear();
                            addProductDialog.cancel();
                            tViewExpiryDate.setText(null);
                            tViewPrice.setText(null);
                            tViewBatch.setText(null);
                            ProductCode.setText(null);
                            tViewCurrentStock.setText(null);
                            return true;
                        }

                    } else if (!(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DEL)) {

                    }
                    return false;
                }
            });

            txtFree.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // TODO Auto-generated method stub
                    if (flag) {
                        if (!isTemporaryCustomer) {
                            if (!txtShelfQty.getText().toString().isEmpty()) {
                                Log.w("FREE", "TextChangeListener");
                                if (!txtRequestQty.getText().toString().isEmpty()) {

                                    if (Integer.parseInt(txtRequestQty.getText().toString()) <= normal) {
                                        txtNormal.setText(txtRequestQty.getText().toString());
                                    } else {

//															txtNormal.setText(String.valueOf(normal));
//															Toast RequestQtyNotAvailable = Toast.makeText(getApplication(), "Requested Quantity is greater than current stock", Toast.LENGTH_SHORT);
//															RequestQtyNotAvailable.setGravity(Gravity.TOP, 200, 100);
//															RequestQtyNotAvailable.show();
                                    }

                                    int freeTemp = 0;
                                    int normalTemp = 0;

                                    try {
                                        freeTemp = Integer.parseInt(txtFree.getText().toString());
                                        normalTemp = Integer.parseInt(txtNormal.getText().toString());
                                    } catch (NumberFormatException e) {
                                        Log.w("Unable to parse Free - IG1", e.toString());
                                        freeTemp = 0;
                                        normalTemp = 0;
                                    }

                                    if (s.toString().isEmpty()) {
                                        txtFree.setText("0");
                                        int pos = txtFree.length();
                                        Editable fTxt = txtFree.getText();
                                        Selection.setSelection(fTxt, pos);
                                        txtFree.setSelection(0, pos);
                                    } else if ((freeTemp + normalTemp) > normal) {
                                        txtFree.setText("0");
                                        int pos = txtFree.length();
                                        Editable fTxt = txtFree.getText();
                                        Selection.setSelection(fTxt, pos);
                                        txtFree.setSelection(0, pos);

                                        Toast FreeGreaterThanCurrentStock = Toast.makeText(getApplication(), "Free amount cannot be greater than " + (normal - normalTemp), Toast.LENGTH_SHORT);
                                        FreeGreaterThanCurrentStock.setGravity(Gravity.TOP, 200, 100);
                                        FreeGreaterThanCurrentStock.show();
                                    }


                                    String[] sd = new String[5];
                                    sd[0] = txtRequestQty.getText().toString();
                                    sd[1] = txtFree.getText().toString();
                                    sd[2] = txtNormal.getText().toString();
                                    sd[3] = txtDiscount.getText().toString();
                                    sd[4] = txtShelfQty.getText().toString();

                                    if (sd[0].isEmpty()) {
                                        sd[0] = "0";
                                    }
                                    if (sd[1].isEmpty()) {
                                        sd[1] = "0";
                                    }
                                    if (sd[2].isEmpty()) {
                                        sd[2] = "0";
                                    }
                                    if (sd[3].isEmpty()) {
                                        sd[3] = "0";
                                    }
                                    if (sd[4].isEmpty()) {
                                        sd[4] = "0";
                                    }
                                    if (isTemporaryCustomer) {
                                        sd[4] = "0";
                                    }

                                    productDetails[7] = sd[0];
                                    productDetails[8] = sd[1];
                                    productDetails[9] = sd[2];
                                    productDetails[10] = sd[3];
                                    productDetails[13] = sd[4];

                                    Log.w("BATCH", selectedBatch);
                                    Log.w("productDetails[3]", productDetails[3]);
                                    if (openedProducts.containsKey(selectedBatch)) {
                                        openedProducts.remove(selectedBatch);
                                        openedProducts.put(selectedBatch, productDetails);
                                        Log.w("Request Qty: THis batch is already there", productDetails[7]);
                                    } else {
                                        openedProducts.put(selectedBatch, productDetails);
                                    }

                                }
                            } else {
                                Toast enterShelfQty = Toast.makeText(context, "Enter shelf quantity!", Toast.LENGTH_SHORT);
                                enterShelfQty.setGravity(Gravity.TOP, 100, 100);
                                enterShelfQty.show();
                                //											txtDiscount.setText("");
                                txtShelfQty.setFocusable(true);
                                txtShelfQty.requestFocus();
                            }
                        } else {
                            Log.w("FREE", "TextChangeListener");
                            if (!txtRequestQty.getText().toString().isEmpty()) {

                                if (Integer.parseInt(txtRequestQty.getText().toString()) <= normal) {
                                    txtNormal.setText(txtRequestQty.getText().toString());
                                } else {

//														txtNormal.setText(String.valueOf(normal));
//														Toast RequestQtyNotAvailable = Toast.makeText(getApplication(), "Requested Quantity is greater than current stock", Toast.LENGTH_SHORT);
//														RequestQtyNotAvailable.setGravity(Gravity.TOP, 200, 100);
//														RequestQtyNotAvailable.show();
                                }

                                int freeTemp = 0;
                                int normalTemp = 0;

                                try {
                                    freeTemp = Integer.parseInt(txtFree.getText().toString());
                                    normalTemp = Integer.parseInt(txtNormal.getText().toString());
                                } catch (NumberFormatException e) {
                                    Log.w("Unable to parse Free - IG1", e.toString());
                                    freeTemp = 0;
                                    normalTemp = 0;
                                }
                                if (s.toString().isEmpty()) {
                                    txtFree.setText("0");
                                    int pos = txtFree.length();
                                    Editable fTxt = txtFree.getText();
                                    Selection.setSelection(fTxt, pos);
                                    txtFree.setSelection(0, pos);
                                } else if ((freeTemp + normalTemp) > normal) {
                                    txtFree.setText("0");
                                    int pos = txtFree.length();
                                    Editable fTxt = txtFree.getText();
                                    Selection.setSelection(fTxt, pos);
                                    txtFree.setSelection(0, pos);

                                    Toast FreeGreaterThanCurrentStock = Toast.makeText(getApplication(), "Free amount cannot be greater than " + (normal - normalTemp), Toast.LENGTH_SHORT);
                                    FreeGreaterThanCurrentStock.setGravity(Gravity.TOP, 200, 100);
                                    FreeGreaterThanCurrentStock.show();
                                }


                                String[] sd = new String[5];
                                sd[0] = txtRequestQty.getText().toString();
                                sd[1] = txtFree.getText().toString();
                                sd[2] = txtNormal.getText().toString();
                                sd[3] = txtDiscount.getText().toString();
                                sd[4] = txtShelfQty.getText().toString();

                                if (sd[0].isEmpty()) {
                                    sd[0] = "0";
                                }
                                if (sd[1].isEmpty()) {
                                    sd[1] = "0";
                                }
                                if (sd[2].isEmpty()) {
                                    sd[2] = "0";
                                }
                                if (sd[3].isEmpty()) {
                                    sd[3] = "0";
                                }
                                if (sd[4].isEmpty()) {
                                    sd[4] = "0";
                                }
                                if (isTemporaryCustomer) {
                                    sd[4] = "0";
                                }

                                productDetails[7] = sd[0];
                                productDetails[8] = sd[1];
                                productDetails[9] = sd[2];
                                productDetails[10] = sd[3];
                                productDetails[13] = sd[4];

                                Log.w("BATCH", selectedBatch);
                                Log.w("productDetails[3]", productDetails[3]);
                                if (openedProducts.containsKey(selectedBatch)) {
                                    openedProducts.remove(selectedBatch);
                                    openedProducts.put(selectedBatch, productDetails);
                                    Log.w("Request Qty: THis batch is already there", productDetails[7]);
                                } else {
                                    openedProducts.put(selectedBatch, productDetails);
                                }
                            }
                        }
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {

                }

                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub

                }
            });

            txtNormal.setOnKeyListener(new View.OnKeyListener() {

                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // TODO Auto-generated method stub
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                        if (!txtRequestQty.getText().toString().isEmpty()) {
                            final Object productListItem = lViewProductList.getItemAtPosition(selectedRow);
                            String[] tempSelectedProduct = getSelectedRowdetails(productListItem);


                            try {
                                for (String key : openedProducts.keySet()) {
                                    String[] data = openedProducts.get(key);

                                    if ((!data[7].contentEquals("0")) || (!data[13].contentEquals("0"))) {
                                        Log.w("opened data[7].size()", data[7] + "");
                                        data[11] = tempSelectedProduct[8];
                                        data[12] = tempSelectedProduct[13];
                                        addToSelectedProductList(data);
                                        updateProductList();
                                    }

                                }
                            } catch (Exception e) {
                                Log.w("opened products empty", e.toString());
                                Log.w("opened data[11].size()", tempSelectedProduct[8] + "");
                                Log.w("opened data[12].size()", tempSelectedProduct[12] + "");
                            }

                            autoTvProductSearch.setText(null);
                            populateSelectedProductList(selectedProductList);
                            int pos = txtRequestQty.length();
                            Editable rTxt = txtRequestQty.getText();
                            Selection.setSelection(rTxt, pos);
//												txtFree.setText(null);
                            txtNormal.setText(null);
//												txtDiscount.setText(null);
                            txtShelfQty.setFocusable(true);
                            txtShelfQty.requestFocus();
                            openedProducts.clear();
                            addProductDialog.cancel();
                            tViewExpiryDate.setText(null);
                            tViewPrice.setText(null);
                            tViewBatch.setText(null);
                            ProductCode.setText(null);
                            tViewCurrentStock.setText(null);
                            txtNormal.setText(null);
                            return true;
                        }

                    } else if (!(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DEL)) {

                    }
                    return false;
                }
            });

            txtDiscount.setOnKeyListener(new View.OnKeyListener() {

                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // TODO Auto-generated method stub
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                        if (!txtRequestQty.getText().toString().isEmpty()) {
                            final Object productListItem = lViewProductList.getItemAtPosition(selectedRow);
                            String[] tempSelectedProduct = getSelectedRowdetails(productListItem);


                            try {
                                for (String key : openedProducts.keySet()) {
                                    String[] data = openedProducts.get(key);

                                    if ((!data[7].contentEquals("0")) || (!data[13].contentEquals("0"))) {
                                        Log.w("opened data[7].size()", data[7] + "");
                                        data[11] = tempSelectedProduct[8];
                                        data[12] = tempSelectedProduct[13];
                                        addToSelectedProductList(data);
                                        updateProductList();
                                    }

                                }
                            } catch (Exception e) {
                                Log.w("opened products empty", e.toString());
                                Log.w("opened data[11].size()", tempSelectedProduct[8] + "");
                                Log.w("opened data[12].size()", tempSelectedProduct[12] + "");
                            }

                            autoTvProductSearch.setText(null);
                            populateSelectedProductList(selectedProductList);
                            int pos = txtRequestQty.length();
                            Editable rTxt = txtRequestQty.getText();
                            Selection.setSelection(rTxt, pos);
//												txtFree.setText(null);
                            txtNormal.setText(null);
//												txtDiscount.setText(null);
                            txtShelfQty.setFocusable(true);
                            txtShelfQty.requestFocus();
                            openedProducts.clear();
                            addProductDialog.cancel();
                            tViewExpiryDate.setText(null);
                            tViewPrice.setText(null);
                            tViewBatch.setText(null);
                            ProductCode.setText(null);
                            tViewCurrentStock.setText(null);
                            txtNormal.setText(null);
                            return true;
                        }

                    } else if (!(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DEL)) {
                        Log.w("ACTION EVENT", event.toString());
                    }
                    return false;
                }
            });

            txtDiscount.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // TODO Auto-generated method stub
                    int requestQty = 0;
                    double discount = 0.0;
                    try {
                        requestQty = Integer.parseInt(txtRequestQty.getText().toString());
                        discount = Double.parseDouble(s.toString());
                    } catch (NumberFormatException e) {
                        Log.e(TAG, e.toString());
                    }

                    if (discount < 0 || discount > 100) {
                        txtDiscount.setText("0.0");
                        hightLightText(txtDiscount);
                        Toast toast = Toast.makeText(InvoiceGen.this, "Discount has to be between 0 and 100!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 50, 50);
                        toast.show();
                    }

                    if (flag) {
                        if (!isTemporaryCustomer && requestQty != 0) {
                            if (!txtShelfQty.getText().toString().isEmpty()) {

                                if (!txtRequestQty.getText().toString().isEmpty()) {

                                    if (Integer.parseInt(txtRequestQty.getText().toString()) <= normal) {
                                        txtNormal.setText(txtRequestQty.getText().toString());
                                    } else {

                                        txtNormal.setText(String.valueOf(normal));
                                        Toast RequestQtyNotAvailable = Toast.makeText(getApplication(), "Requested Quantity is greater than current stock", Toast.LENGTH_SHORT);
                                        RequestQtyNotAvailable.setGravity(Gravity.TOP, 200, 100);
                                        RequestQtyNotAvailable.show();
                                    }

                                    String[] sd = new String[5];
                                    sd[0] = txtRequestQty.getText().toString();
                                    sd[1] = txtFree.getText().toString();
                                    sd[2] = txtNormal.getText().toString();
                                    sd[3] = txtDiscount.getText().toString();
                                    sd[4] = txtShelfQty.getText().toString();

                                    if (sd[0].isEmpty()) {
                                        sd[0] = "0";
                                    }
                                    if (sd[1].isEmpty()) {
                                        sd[1] = "0";
                                    }
                                    if (sd[2].isEmpty()) {
                                        sd[2] = "0";
                                    }
                                    if (sd[3].isEmpty()) {
                                        sd[3] = "0";
                                    }
                                    if (sd[4].isEmpty()) {
                                        sd[4] = "0";
                                    }
                                    if (isTemporaryCustomer) {
                                        sd[4] = "0";
                                    }

                                    productDetails[7] = sd[0];
                                    productDetails[8] = sd[1];
                                    productDetails[9] = sd[2];
                                    productDetails[10] = sd[3];
                                    productDetails[13] = sd[4];

                                    Log.w("productDetails[7]", productDetails[7]);
                                    Log.w("productDetails[7]", productDetails[7]);
                                    if (openedProducts.containsKey(selectedBatch)) {
                                        openedProducts.remove(selectedBatch);
                                        openedProducts.put(selectedBatch, productDetails);
                                        Log.w("Request Qty: THis batch is already there", productDetails[7]);
                                    } else {
                                        openedProducts.put(selectedBatch, productDetails);
                                    }

                                }
                            } else {
                                Toast enterShelfQty = Toast.makeText(context, "Enter shelf quantity!", Toast.LENGTH_SHORT);
                                enterShelfQty.setGravity(Gravity.TOP, 100, 100);
                                enterShelfQty.show();
                                //											txtDiscount.setText("");
                                txtShelfQty.setFocusable(true);
                                txtShelfQty.requestFocus();
                            }
                        } else {
                            if (!txtRequestQty.getText().toString().isEmpty()) {

                                if (Integer.parseInt(txtRequestQty.getText().toString()) <= normal) {
                                    txtNormal.setText(txtRequestQty.getText().toString());
                                } else {

                                    txtNormal.setText(String.valueOf(normal));
                                    Toast RequestQtyNotAvailable = Toast.makeText(getApplication(), "Requested Quantity is greater than current stock", Toast.LENGTH_SHORT);
                                    RequestQtyNotAvailable.setGravity(Gravity.TOP, 200, 100);
                                    RequestQtyNotAvailable.show();
                                }

                                String[] sd = new String[5];
                                sd[0] = txtRequestQty.getText().toString();
                                sd[1] = txtFree.getText().toString();
                                sd[2] = txtNormal.getText().toString();
                                sd[3] = txtDiscount.getText().toString();
                                sd[4] = txtShelfQty.getText().toString();

                                if (sd[0].isEmpty()) {
                                    sd[0] = "0";
                                }
                                if (sd[1].isEmpty()) {
                                    sd[1] = "0";
                                }
                                if (sd[2].isEmpty()) {
                                    sd[2] = "0";
                                }
                                if (sd[3].isEmpty()) {
                                    sd[3] = "0";
                                }
                                if (sd[4].isEmpty()) {
                                    sd[4] = "0";
                                }
                                if (isTemporaryCustomer) {
                                    sd[4] = "0";
                                }

                                productDetails[7] = sd[0];
                                productDetails[8] = sd[1];
                                productDetails[9] = sd[2];
                                productDetails[10] = sd[3];
                                productDetails[13] = sd[4];

                                Log.w("productDetails[7]", productDetails[7]);
                                Log.w("productDetails[7]", productDetails[7]);
                                if (openedProducts.containsKey(selectedBatch)) {
                                    openedProducts.remove(selectedBatch);
                                    openedProducts.put(selectedBatch, productDetails);
                                    Log.w("Request Qty: THis batch is already there", productDetails[7]);
                                } else {
                                    openedProducts.put(selectedBatch, productDetails);
                                }

                            }
                        }
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // TODO Auto-generated method stub

                }

                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub

                }
            });


//								btnDone.setOnClickListener(new View.OnClickListener() {
//
//									public void onClick(View v) {
//										if (!txtRequestQty.getText().toString().isEmpty()) {
//
//											Log.w("selectedProduct 0", productDetails[0] + "");
//											Log.w("selectedProduct 1", productDetails[1] + "");
//											Log.w("selectedProduct 2", productDetails[2] + "");
//											Log.w("selectedProduct 3", productDetails[3] + "");
//											Log.w("selectedProduct 4", productDetails[4] + "");
//											Log.w("selectedProduct 5", productDetails[5] + "");
//											Log.w("selectedProduct 6", productDetails[6] + "");
//											Log.w("selectedProduct 7", productDetails[7] + "");
//											Log.w("selectedProduct 8", productDetails[8] + "");
//											Log.w("selectedProduct 9", productDetails[9] + "");
//											Log.w("selectedProduct 10", productDetails[10] + "");
//											Log.w("selectedProduct 11", productDetails[11] + "");
//											Log.w("selectedProduct 12", productDetails[12] + "");
//
////											if (!(Integer.parseInt(productDetails[7]) == 0)) {
//
//												final Object productListItem = lViewProductList.getItemAtPosition(selectedRow);
//												String[] tempSelectedProduct = getSelectedRowdetails(productListItem);
//
//
//												try {
//													for (String key : openedProducts.keySet()) {
//														String[] data = openedProducts.get(key);
//
//														if (data[7].isEmpty()) {
//
//														} else {
//															Log.w("opened data[7].size()", data[7] + "");
//															data[11] = tempSelectedProduct[8];
//															data[12] = tempSelectedProduct[13];
//															addToSelectedProductList(data);
//														}
//
//													}
//												} catch (Exception e) {
//													Log.w("opened products empty", e.toString());
//													Log.w("opened data[11].size()", tempSelectedProduct[8] + "");
//													Log.w("opened data[12].size()", tempSelectedProduct[12] + "");
//												}
//
//												autoTvProductSearch.setText(null);
//												populateSelectedProductList();
//												txtRequestQty.setText(null);
//												txtFree.setText(null);
//												txtNormal.setText(null);
//												txtDiscount.setText(null);
//												txtShelfQty.setFocusable(true);
//												txtShelfQty.requestFocus();
//												openedProducts.clear();
//												addProductDialog.cancel();
//												tViewExpiryDate.setText(null);
//												tViewPrice.setText(null);
//												tViewBatch.setText(null);
//												ProductCode.setText(null);
//												tViewCurrentStock.setText(null);
//												txtNormal.setText(null);
////											}
//										}
//									}
//								});

            btnCancelPopup.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method
                    // stub
                    txtShelfQty
                            .setFocusable(true);
                    txtShelfQty.requestFocus();
                    addProductDialog.cancel();
                    lViewBatch.setSelection(1);

                }
            });


            addProductDialog
                    .setTitle(productListSelectedItem[8]);
            addProductDialog.show();

        } else {
            if (!isTemporaryCustomer) {
                boolean flag = true;
                for (String[] s : selectedProductList) {
                    if (s[2].equals(productListSelectedItem[2])) {
                        flag = false;
                        Toast productAlreadyAdded = Toast.makeText(
                                getApplicationContext(),
                                productListSelectedItem[8]
                                        + " has already been added!",
                                Toast.LENGTH_SHORT);
                        productAlreadyAdded.setGravity(Gravity.TOP, 50, 100);
                        productAlreadyAdded.show();
                    }
                }

                if (flag) {
                    Toast productOutOfStock = Toast.makeText(
                            getApplicationContext(),
                            productListSelectedItem[8]
                                    + " is out of Stock!",
                            Toast.LENGTH_SHORT);
                    productOutOfStock.setGravity(Gravity.TOP, 50, 100);
                    productOutOfStock.show();

                    shelfQtyDialog = new Dialog(InvoiceGen.this);
                    shelfQtyDialog.setTitle("Set shelf quantity");
                    shelfQtyDialog.setContentView(R.layout.invoice_gen_1_shelf_qty_popup);
                    final EditText txtShelfQtyPopup = (EditText) shelfQtyDialog.findViewById(R.id.etShelfQty);
                    btnDoneShelfQty = (Button) shelfQtyDialog.findViewById(R.id.bDone);
                    shelfQtyDialog.setCanceledOnTouchOutside(false);

                    btnDoneShelfQty.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub]
                            if (!txtShelfQtyPopup.getText().toString().isEmpty()) {
                                if (!txtShelfQtyPopup.getText().toString().contentEquals("0")) {
                                    final Object productListItem = lViewProductList.getItemAtPosition(selectedRow);
                                    String[] tempSelectedProduct = getSelectedRowdetails(productListItem);

                                    String[] shelfQtyDetails = new String[14];
                                    shelfQtyDetails[0] = "-1";
                                    shelfQtyDetails[1] = tempSelectedProduct[1];
                                    shelfQtyDetails[2] = tempSelectedProduct[2];
                                    Log.w("shelfQtyDetails[2]`", shelfQtyDetails[2] + "");
                                    shelfQtyDetails[3] = "0";
                                    shelfQtyDetails[4] = "0";
                                    shelfQtyDetails[5] = "0";
                                    shelfQtyDetails[6] = "0";
                                    shelfQtyDetails[7] = "0";
                                    shelfQtyDetails[8] = "0";
                                    shelfQtyDetails[9] = "0";
                                    shelfQtyDetails[10] = "0";
                                    shelfQtyDetails[11] = tempSelectedProduct[8];
                                    shelfQtyDetails[12] = tempSelectedProduct[12];
                                    ;
                                    shelfQtyDetails[13] = txtShelfQtyPopup.getText().toString();
                                    Log.w("Shelf Qty", txtShelfQtyPopup.getText().toString());

                                    addToSelectedProductList(shelfQtyDetails);
                                    updateProductList();
                                    autoTvProductSearch.setText(null);

                                    txtShelfQty.setFocusable(true);
                                    txtShelfQty.requestFocus();
                                    tViewExpiryDate.setText(null);
                                    tViewPrice.setText(null);
                                    tViewBatch.setText(null);
                                    ProductCode.setText(null);
                                    tViewCurrentStock.setText(null);
                                    txtNormal.setText(null);
                                    shelfQtyDialog.dismiss();
                                }
                            }

                        }
                    });

                    txtShelfQtyPopup.setOnKeyListener(new View.OnKeyListener() {

                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            // TODO Auto-generated method stub
                            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                                if (!txtShelfQtyPopup.getText().toString().isEmpty()) {
                                    if (!txtShelfQtyPopup.getText().toString().contentEquals("0")) {
                                        final Object productListItem = lViewProductList.getItemAtPosition(selectedRow);
                                        String[] tempSelectedProduct = getSelectedRowdetails(productListItem);

                                        String[] shelfQtyDetails = new String[14];
                                        shelfQtyDetails[0] = "-1";
                                        shelfQtyDetails[1] = tempSelectedProduct[1];
                                        shelfQtyDetails[2] = tempSelectedProduct[2];
                                        shelfQtyDetails[3] = "0";
                                        shelfQtyDetails[4] = "0";
                                        shelfQtyDetails[5] = "0";
                                        shelfQtyDetails[6] = "0";
                                        shelfQtyDetails[7] = "0";
                                        shelfQtyDetails[8] = "0";
                                        shelfQtyDetails[9] = "0";
                                        shelfQtyDetails[10] = "0";
                                        shelfQtyDetails[11] = tempSelectedProduct[8];
                                        shelfQtyDetails[12] = tempSelectedProduct[12];
                                        ;
                                        shelfQtyDetails[13] = txtShelfQtyPopup.getText().toString();

                                        Log.w("Shelf Qty", txtShelfQtyPopup.getText().toString());

                                        addToSelectedProductList(shelfQtyDetails);
                                        updateProductList();
                                        autoTvProductSearch.setText(null);

                                        txtShelfQty.setFocusable(true);
                                        txtShelfQty.requestFocus();
                                        tViewExpiryDate.setText(null);
                                        tViewPrice.setText(null);
                                        tViewBatch.setText(null);
                                        ProductCode.setText(null);
                                        tViewCurrentStock.setText(null);
                                        txtNormal.setText(null);
                                        shelfQtyDialog.dismiss();
                                    }

                                }
                            }
                            return false;
                        }
                    });

                    shelfQtyDialog.show();
                }
            } else {
                Toast customerIsPendingApproval = Toast.makeText(getApplicationContext(), "This customer is still pending approval!", Toast.LENGTH_SHORT);
                customerIsPendingApproval.setGravity(Gravity.TOP, 100, 100);
                customerIsPendingApproval.show();
            }

        }


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub

        Log.w("onSaveInstanceState", "#### onSaveInstanceState");

//		Bundle outState = new Bundle();

        super.onSaveInstanceState(outState);

        outState.putString("Id", rowId);
        outState.putString("PharmacyId", pharmacyId);

        if (chequeEnabled) {
            outState.putString("ChequeNumber", chequeNumber);
            outState.putString("CollectionDate", collectionDate);
            outState.putString("ReleaseDate", releaseDate);
        }

        Log.w("invoicegen2", "selectedProductList size : " + selectedProductList.size());

        ArrayList<SelectedProduct> selectedProductsArray = new ArrayList<SelectedProduct>();

        for (String[] selectedProductData : selectedProductList) {
            SelectedProduct product = new SelectedProduct();

            product.setRowId(Integer.parseInt(selectedProductData[0]));
            product.setProductId(String.valueOf(selectedProductData[1]));
            product.setProductCode(String.valueOf(selectedProductData[2]));
            product.setProductBatch(String.valueOf(selectedProductData[3]));
            product.setQuantity(Integer.parseInt(selectedProductData[4]));
            product.setExpiryDate(String.valueOf(selectedProductData[5]));
            product.setTimeStamp(String.valueOf(selectedProductData[6]));

            product.setRequestedQuantity(Integer.parseInt(selectedProductData[7]));
            product.setFree(Integer.parseInt(selectedProductData[8]));
            product.setNormal(Integer.parseInt(selectedProductData[9]));
            product.setDiscount(Double.parseDouble(selectedProductData[10]));
            product.setShelfQuantity(Integer.parseInt(selectedProductData[13]));
            Log.w("next Button 090928340283423098", selectedProductData[13]);

            product.setProductDescription(String.valueOf(selectedProductData[11]));
            product.setPrice(Double.parseDouble(selectedProductData[12]));

            Log.w("SelectedProduct Data", selectedProductData[0]);
            Log.w("SelectedProduct Data", selectedProductData[1]);
            Log.w("SelectedProduct Data", selectedProductData[2]);
            Log.w("SelectedProduct Data", selectedProductData[3]);
            Log.w("SelectedProduct Data", selectedProductData[4]);
            Log.w("SelectedProduct Data", selectedProductData[5]);
            Log.w("SelectedProduct Data", selectedProductData[6]);
            Log.w("SelectedProduct Data", selectedProductData[7]);
            Log.w("SelectedProduct Data", selectedProductData[8]);
            Log.w("SelectedProduct Data", selectedProductData[9]);
            Log.w("SelectedProduct Data", selectedProductData[10]);
            Log.w("SelectedProduct Data", selectedProductData[11]);
            Log.w("SelectedProduct Data", selectedProductData[12]);
            Log.w("SelectedProduct Data", selectedProductData[13]);

            selectedProductsArray.add(product);

        }

        outState.putParcelableArrayList("SelectedProducts", selectedProductsArray);
        outState.putParcelableArrayList("ReturnProducts", returnProductsArray);
//		invoiceGen2Intent.putExtras(outState);


    }

    private boolean getCustomerType() {
        // TODO Auto-generated method stub
        boolean temp = false;
        ItineraryAdp itinerary = new ItineraryAdp(this);//Itinerary change to ItineraryAdp
        itinerary.openReadableDatabase();
        String status = itinerary.getItineraryStatus(rowId);
        itinerary.closeDatabase();

        Log.w("Status from DB", status);

        if (!status.contentEquals("null")) {
            if (status.contentEquals("true")) {
                temp = true;
            } else if (status.contentEquals("false")) {
                temp = false;
            }
        }
        Log.w("CUSTOMER STATUS", temp + "");
        return temp;
    }

    private void populateProductInformationFromDb() {
        // TODO Auto-generated method stub
        ProductRepStore productRepStoreObject = new ProductRepStore(this);
        productRepStoreObject.openReadableDatabase();
        productInformation = productRepStoreObject.getAllProductRepstore();
        Log.w("productInfoSize: ", productInformation.size() + "");
        productRepStoreObject.closeDatabase();

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {


            if (selectedProductList.isEmpty()) {
                Intent customerItineraryListIntent = new Intent("com.EdnaMedicals.channelbridge.ITINERARYLIST");
                finish();
                startActivity(customerItineraryListIntent);
            } else {
                alertCancel.show();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getDataFromPreviousActivity(Bundle extras) {

        try {
//			Bundle extras = getIntent().getExtras();
            rowId = extras.getString("Id");
            pharmacyId = extras.getString("PharmacyId");

            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(getBaseContext());
            chequeEnabled = preferences.getBoolean("cbPrefEnableCheckDetails", true);


            if (chequeEnabled) {
                if (extras.containsKey("ChequeNumber")) {
                    chequeNumber = extras.getString("ChequeNumber");
                }
                if (extras.containsKey("CollectionDate")) {
                    collectionDate = extras.getString("CollectionDate");
                }
                if (extras.containsKey("ReleaseDate")) {
                    releaseDate = extras.getString("ReleaseDate");
                }
                Log.w("cheque details", chequeNumber + " # " + collectionDate + " # " + releaseDate);

            }


            Log.w("parcelable tiyenawada: ", extras.containsKey("SelectedProducts") + "");
            if (extras.containsKey("ReturnProducts")) {
                returnProductsArray = extras.getParcelableArrayList("ReturnProducts");
            }
            if (extras.containsKey("SelectedProducts") == true) {
                selectedProductsArray = extras.getParcelableArrayList("SelectedProducts");
                Log.w("tempDataFromIG2.size()", selectedProductsArray.size() + "");
                for (SelectedProduct selectedProduct : selectedProductsArray) {
                    Log.w("IG1", "Inside For loop (get data from prev activity)" + pharmacyId);
                    String[] tempData = new String[14];
                    tempData[0] = String.valueOf(selectedProduct.getRowId());
                    tempData[1] = String.valueOf(selectedProduct.getProductId());
                    tempData[2] = String.valueOf(selectedProduct.getProductCode());
                    tempData[3] = String.valueOf(selectedProduct.getProductBatch());
                    tempData[4] = String.valueOf(selectedProduct.getQuantity());
                    tempData[5] = String.valueOf(selectedProduct.getExpiryDate());
                    tempData[6] = String.valueOf(selectedProduct.getTimeStamp());
                    tempData[7] = String.valueOf(selectedProduct.getRequestedQuantity());
                    tempData[8] = String.valueOf(selectedProduct.getFree());
                    tempData[9] = String.valueOf(selectedProduct.getNormal());
                    tempData[10] = String.valueOf(selectedProduct.getDiscount());
                    tempData[11] = String.valueOf(selectedProduct.getProductDescription());
                    tempData[12] = String.valueOf(selectedProduct.getPrice());
                    tempData[13] = String.valueOf(selectedProduct.getShelfQuantity());
                    Log.w("tempDataFromIG2[0]", tempData[0]);
                    Log.w("tempDataFromIG2[1]", tempData[1]);
                    Log.w("tempDataFromIG2[2]", tempData[2]);
                    Log.w("tempDataFromIG2[3]", tempData[3]);
                    Log.w("tempDataFromIG2[4]", tempData[4]);
                    Log.w("tempDataFromIG2[5]", tempData[5]);
                    Log.w("tempDataFromIG2[6]", tempData[6]);
                    Log.w("tempDataFromIG2[7]", tempData[7]);
                    Log.w("tempDataFromIG2[8]", tempData[8]);
                    Log.w("tempDataFromIG2[10]", tempData[10]);
                    Log.w("tempDataFromIG2[11]", tempData[11]);
                    Log.w("tempDataFromIG2[12]", tempData[12]);
                    selectedProductList.add(tempData);

                }
            }
            Log.w("IG1", "Pharmacy id " + pharmacyId);
            Log.w("IG1", "rowId " + rowId);

        } catch (Exception e) {
            Log.w("InvoiceGen1: ", e.toString());
        }
    }

    public void addToSelectedProductList(String[] dataToBeAdded) {

        selectedProductList.add(dataToBeAdded);
        batchesForProductList.put(dataToBeAdded[2], dataToBeAdded[3]);
    }

    public String[] getSelectedProductNames() {
        int i = 0;
        selectedProductNamesArray = new ArrayList<String>();
        String[] selectedProductNames = new String[selectedProductList.size()];
        for (String[] selectedProduct : selectedProductList) {
            selectedProductNames[i] = selectedProduct[11];
            selectedProductNamesArray.add(selectedProduct[11]);
            i++;
        }
        return selectedProductNames;
    }

    public void populateProductList(List<String[]> prodList) {
        productListAdapter = new InvoiceGen1ProductListAdapter(this,
                prodList, productInformation, pharmacyId);
        lViewProductList.setAdapter(productListAdapter);
    }

    public void populateSelectedProductList(ArrayList<String[]> pList) {

        selectedProductListAdapter = new InvoiceGen1SelectedProductListAdapter(this, pList, this, isTemporaryCustomer);
        lViewSelectedProductList.setAdapter(selectedProductListAdapter);
    }

    public void removeItemAt(String productId, String batch) {

        if (selectedProductList != null && !selectedProductList.isEmpty()) {
            for (int i = 0; i < selectedProductList.size(); i++) {
                String[] productDetails = selectedProductList.get(i);
                if (productDetails[2].contentEquals(productId)) {
                    if (productDetails[3].contentEquals(batch)) {
                        selectedProductList.remove(i);
                        break;
                    }
                }
            }
        }
        populateProductListFromDb();
        updateProductList();
        populateSelectedProductList(selectedProductList);
    }

    public void populateProductListFromDb() {

        Products productObject = new Products(this);
        productObject.openReadableDatabase();
        productList = productObject.getAllProducts();
        productObject.closeDatabase();
    }

    public void setAdapterToProductSearchBar(String[] prodNames) {

        ArrayAdapter<String> productSearchAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line,
                prodNames);
        ((AutoCompleteTextView) autoTvProductSearch)
                .setAdapter(productSearchAdapter);

    }

    private String[] getProductNameList() {

        Products productsObject = new Products(this);
        productsObject.openReadableDatabase();
        String[] productNames = productsObject.getProductNames();
        productsObject.closeDatabase();
        return productNames;
    }

    private List<String[]> searchProducts(String name) {

        Products productsObject = new Products(this);
        productsObject.openReadableDatabase();
        List<String[]> products = productsObject.searchProducts(name);
        productsObject.closeDatabase();

        return products;
    }

    @SuppressWarnings("null")
    public String[] getSelectedProductNameList() {
        String[] selectedProductNames = null;
        try {
            int i = 0;
            for (String[] g : selectedProductList) {
                selectedProductNames[i] = g[11];
                i++;
            }
            return selectedProductNames;
        } catch (Exception e) {
            Log.w("error getting selected productNames", e.toString());
        }
        return selectedProductNames;
    }

    private void setPopUpBatchListAdapter(ArrayList<String[]> productsByBatch) {
        // TODO Auto-generated method stub
        try {
            String[] batches = new String[productsByBatch.size()];
            int count = 0;
            for (String[] g : productsByBatch) {
                batches[count] = g[3];
                Log.w("batch: ", g[3]);
                count++;
            }

            ArrayAdapter<String> batchListAdapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_list_item_activated_1,
                    batches);
            lViewBatch = (ListView) addProductDialog.findViewById(R.id.lvBatch);
            lViewBatch.setAdapter(batchListAdapter);
            lViewBatch.setChoiceMode(1);
            lViewBatch.setItemChecked(0, true);
            lViewBatch.setSelection(0);
            batchListAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            Log.w("error adding batchlist adapter", e.toString());
        }
    }

    public void setSelectedProducts(ArrayList<String[]> selProd) {
        // TODO Auto-generated method stub

        if (tempSelectedProductList.isEmpty()) {
            selectedProductList = selProd;
        } else {
            for (int i = 0; i < selProd.size(); i++) {
                for (int j = 0; j < selectedProductList.size(); j++) {
                    String[] tempListProduct = selProd.get(i);
                    String[] selectedProduct = selectedProductList.get(j);

                    if (tempListProduct[2].contentEquals(selectedProduct[2])) {
                        selectedProductList.remove(j);
                        selectedProductList.add(j, tempListProduct);
                    }
                }
            }
        }

        updateProductList();
        productListAdapter.notifyDataSetChanged();
        populateSelectedProductList(selectedProductList);
    }

    private void updateProductList() {
        // TODO Auto-generated method stub
        newProductList = new ArrayList<String[]>();
        ArrayList<String> ids = new ArrayList<String>();

        for (int i = 0; i < productList.size(); i++) {
            for (int j = 0; j < selectedProductList.size(); j++) {
                String[] product = productList.get(i);
                String[] selectedProduct = selectedProductList.get(j);

                if (product[2].equals(selectedProduct[2])) {
                    ids.add(String.valueOf(i));
                } else {

                }
            }
        }

        newProductList = productList;
        if (!ids.isEmpty()) {
            for (int i = ids.size() - 1; i >= 0; i--) {

//				if (newProductList.contains(Integer.parseInt(ids.get(i)))) {
                newProductList.remove(Integer.parseInt(ids.get(i)));
//				}
            }
        }


        if (selectedProductList.isEmpty()) {
            populateProductList(productList);
            productListAdapter.notifyDataSetChanged();
        } else {
            populateProductList(newProductList);
            productListAdapter.notifyDataSetChanged();
        }

    }

    private String[] getSelectedRowdetails(Object prodListItem) {
        // TODO Auto-generated method stub
        String[] tempSelectedProduct;
        Log.w("getSelectedRowdetails: ", "prodListItem.toString() : " + prodListItem.toString());
        if (newProductList.size() < 1) {
            tempSelectedProduct = productList.get(Integer.parseInt(prodListItem.toString()));
            Log.w("PRODUCT NAME prod list empty:", tempSelectedProduct[8]);
        } else {
            tempSelectedProduct = newProductList.get(Integer.parseInt(prodListItem.toString()));
            Log.w("PRODUCT NAME:", tempSelectedProduct[8]);
        }
        return tempSelectedProduct;
    }

    private void hightLightText(EditText editText) {
        int position = editText.length();
        Editable etext = editText.getText();
        Selection.setSelection(etext, position);
        editText.setSelection(0, position);
    }

}
