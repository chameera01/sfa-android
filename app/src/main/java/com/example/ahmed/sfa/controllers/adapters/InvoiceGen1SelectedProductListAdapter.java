package com.example.ahmed.sfa.controllers.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.sfa.Activities.InvoiceGen;
import com.example.ahmed.sfa.R;

import java.util.ArrayList;

/**
 * Created by DELL on 10/22/2017.
 */
public class InvoiceGen1SelectedProductListAdapter extends BaseAdapter {

    ArrayList<String[]> selectedProducts = new ArrayList<String[]>();
    TextView labelTotalValue, labelTotalQty, tViewProductName, tViewTotalValue, tViewTotalQty, tViewDiscount, tViewFree, tViewNormal, tViewRequestQty, tViewExpire, tViewProBatch;
    ImageButton iBtnEditProductQuantities, btnRemoveProduct;
    int currentStock;
    boolean isTemporaryCustomer = false;
    Dialog editProductQuantitiesDialog, shelfQtyDialog;
    Button btnCancelPopup;
    TextView tViewLastInvoiceQty, tViewLastThreeMonthAverage, tViewCurrentStock, tViewShelfQty,
            tViewpopUpDiscount, tViewExpiryDate, tViewPrice, tViewBatch, tViewPopUpProductCode;
    private Activity activity;
    private LayoutInflater inflater = null;
    private InvoiceGen invoiceGen1Activity;


    public InvoiceGen1SelectedProductListAdapter(Activity a, ArrayList<String[]> sProducts, InvoiceGen iG1, boolean isTempCust) {
        activity = a;
        selectedProducts = sProducts;
        invoiceGen1Activity = iG1;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        isTemporaryCustomer = isTempCust;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return selectedProducts.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View vi;

        if (convertView == null) {
            vi = inflater.inflate(R.layout.invoice_gen_1_selected_product_list, null);
        } else {
            vi = (View) convertView;
        }

        labelTotalValue = (TextView) vi.findViewById(R.id.labelTotalValue);
        labelTotalQty = (TextView) vi.findViewById(R.id.labelTotalQty);
        tViewProductName = (TextView) vi.findViewById(R.id.tvProductName);
        tViewTotalValue = (TextView) vi.findViewById(R.id.tvTotalValue);
        tViewTotalQty = (TextView) vi.findViewById(R.id.tvTotalQty);
        tViewDiscount = (TextView) vi.findViewById(R.id.tvDiscount);
        tViewFree = (TextView) vi.findViewById(R.id.tvFree);
        tViewNormal = (TextView) vi.findViewById(R.id.tvNormal);
        tViewRequestQty = (TextView) vi.findViewById(R.id.tvQuantity);
        tViewExpire = (TextView) vi.findViewById(R.id.tvExpiryDate);
        tViewProBatch = (TextView) vi.findViewById(R.id.tvBatch);
        tViewShelfQty = (TextView) vi.findViewById(R.id.tvshelfQty);
        btnRemoveProduct = (ImageButton) vi.findViewById(R.id.bRemoveProduct);
        iBtnEditProductQuantities = (ImageButton) vi.findViewById(R.id.bEditProduct);

        tViewTotalValue.setTextColor(Color.parseColor("#669900"));
        tViewTotalQty.setTextColor(Color.parseColor("#669900"));
        labelTotalQty.setTextColor(Color.parseColor("#669900"));
        labelTotalValue.setTextColor(Color.parseColor("#669900"));

        iBtnEditProductQuantities.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                final String[] productDetails = selectedProducts.get(position);
                currentStock = Integer.parseInt(productDetails[4]);

                ArrayList<String[]> productsByBatch = new ArrayList<String[]>();
                ProductRepStore productRepStoreObject = new ProductRepStore(activity);
                productRepStoreObject.openReadableDatabase();
                productsByBatch = productRepStoreObject.getProductDetailsByProductCode(productDetails[2]);
                Log.w("productsByBatch.size(): ", productsByBatch.size() + "");
                Log.w("Position", position + "");
                productRepStoreObject.closeDatabase();
                editProductQuantitiesDialog = new Dialog(invoiceGen1Activity);
                editProductQuantitiesDialog.setContentView(R.layout.invoice_gen_1_edit_product_popup);
                editProductQuantitiesDialog.setTitle("Edit Product");
                editProductQuantitiesDialog.setCanceledOnTouchOutside(false);

                final EditText txtRequestQty = (EditText) editProductQuantitiesDialog.findViewById(R.id.etRequestQty);
                final EditText txtFree = (EditText) editProductQuantitiesDialog.findViewById(R.id.etFree);
                final EditText txtNormal = (EditText) editProductQuantitiesDialog.findViewById(R.id.etNormal);
                final EditText txtDiscount = (EditText) editProductQuantitiesDialog.findViewById(R.id.etDiscount);
                final EditText txtShelfQuantity = (EditText) editProductQuantitiesDialog.findViewById(R.id.etShelfQuantity);
                tViewLastInvoiceQty = (TextView) editProductQuantitiesDialog.findViewById(R.id.tvLastInvoiceQty);
                tViewLastThreeMonthAverage = (TextView) editProductQuantitiesDialog.findViewById(R.id.tvLastThreeMonthAvg);
                tViewCurrentStock = (TextView) editProductQuantitiesDialog.findViewById(R.id.tvCurrentStock);
                tViewpopUpDiscount = (TextView) editProductQuantitiesDialog.findViewById(R.id.tvDiscount);
                tViewExpiryDate = (TextView) editProductQuantitiesDialog.findViewById(R.id.tvExpiryDate);
                tViewPrice = (TextView) editProductQuantitiesDialog.findViewById(R.id.tvPrice);
                tViewBatch = (TextView) editProductQuantitiesDialog.findViewById(R.id.tvBatch);
                tViewPopUpProductCode = (TextView) editProductQuantitiesDialog.findViewById(R.id.tvProductCode);
//				btnDone = (Button) editProductQuantitiesDialog.findViewById(R.id.bDone);
                btnCancelPopup = (Button) editProductQuantitiesDialog.findViewById(R.id.bCancel);


                int requestQty = 0;
                try {
                    requestQty = Integer.parseInt(productDetails[7]);
                } catch (NumberFormatException e) {
                    Log.e("selectedProductlist", e.toString());
                }
                if (requestQty == 0) {
                    txtFree.setEnabled(false);
                } else {
                    txtFree.setEnabled(true);
                }


                if (isTemporaryCustomer) {
                    txtShelfQuantity.setEnabled(false);
                } else {
                    txtShelfQuantity.setEnabled(true);
                }

                if (productDetails[3].contentEquals("0")) {
                    if (!isTemporaryCustomer) {
                        shelfQtyDialog = new Dialog(invoiceGen1Activity);
                        shelfQtyDialog.setTitle("Set shelf quantity");
                        shelfQtyDialog.setContentView(R.layout.invoice_gen_1_shelf_qty_popup);
                        final EditText txtShelfQtyPopup = (EditText) shelfQtyDialog.findViewById(R.id.etShelfQty);
                        Button btnDoneShelfQty = (Button) shelfQtyDialog.findViewById(R.id.bDone);

                        txtShelfQtyPopup.setText(productDetails[13]);

                        btnDoneShelfQty.setOnClickListener(new View.OnClickListener() {

                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                if (!txtShelfQtyPopup.getText().toString().isEmpty()) {
                                    if (!txtShelfQtyPopup.getText().toString().contentEquals("0")) {
                                        String tempData[] = selectedProducts.get(position);
                                        tempData[13] = txtShelfQtyPopup.getText().toString();
                                        selectedProducts.remove(position);
                                        selectedProducts.add(position, tempData);
                                        invoiceGen1Activity.setSelectedProducts(selectedProducts);
                                        InvoiceGen1SelectedProductListAdapter.this.notifyDataSetChanged();
                                        txtShelfQtyPopup.setText(null);
                                        shelfQtyDialog.cancel();
                                    }
                                }

                            }
                        });


                        txtShelfQtyPopup.setOnKeyListener(new View.OnKeyListener() {

                            public boolean onKey(View v, int keyCode, KeyEvent event) {
                                // TODO Auto-generated method stub
                                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                                    if (!txtShelfQtyPopup.getText().toString().isEmpty()) {
                                        if (!txtShelfQtyPopup.getText().toString().isEmpty()) {
                                            if (!txtShelfQtyPopup.getText().toString().contentEquals("0")) {
                                                String tempData[] = selectedProducts.get(position);
                                                tempData[13] = txtShelfQtyPopup.getText().toString();
                                                selectedProducts.remove(position);
                                                selectedProducts.add(position, tempData);
                                                invoiceGen1Activity.setSelectedProducts(selectedProducts);
                                                InvoiceGen1SelectedProductListAdapter.this.notifyDataSetChanged();
                                                txtShelfQtyPopup.setText(null);
                                                shelfQtyDialog.cancel();
                                            }
                                        }
                                    }
                                }
                                return false;
                            }
                        });

                        shelfQtyDialog.show();
                    }
                } else {
                    try {
                        txtRequestQty.setText(productDetails[7]);
                        txtFree.setText(productDetails[8]);
                        txtNormal.setText(productDetails[9]);
                        txtDiscount.setText(productDetails[10]);
                        txtShelfQuantity.setText(productDetails[13]);
                        tViewCurrentStock.setText(productDetails[4]);
                        editProductQuantitiesDialog.setTitle(productDetails[11]);
                        txtNormal.setText(productDetails[9].toString());
                        txtRequestQty.setText(productDetails[7]);
                        if (!productDetails[5].contentEquals("0")) {
                            tViewExpiryDate.setText(productDetails[5].substring(0, 10));
                        }
                        tViewPrice.setText(productDetails[12]);
                        tViewBatch.setText(productDetails[3]);
                        tViewPopUpProductCode.setText(productDetails[2]);
                        editProductQuantitiesDialog.show();

                    } catch (Exception e) {
                        Log.w("IG1 selected product list adapter: ", e.toString());
                    }

                    txtShelfQuantity.setOnKeyListener(new View.OnKeyListener() {

                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                                if (!txtShelfQuantity.getText().toString().isEmpty()) {
                                    txtRequestQty.setFocusable(true);
                                    txtRequestQty.requestFocus();
                                    return true;

                                } else {
                                    txtShelfQuantity.setFocusable(true);
                                    txtShelfQuantity.requestFocus();

                                    Toast enterShelfQty = Toast.makeText(invoiceGen1Activity, "Enter shelf quantity!", Toast.LENGTH_SHORT);
                                    enterShelfQty.setGravity(Gravity.TOP, 100, 100);
                                    enterShelfQty.show();

                                    return true;
                                }

                            } else {
                                return false;
                            }
                        }
                    });

                    txtRequestQty.addTextChangedListener(new TextWatcher() {

                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            // TODO Auto-generated method stub
                            if (!s.toString().isEmpty()) {
                                int requestQty = 0;
                                try {
                                    requestQty = Integer.parseInt(txtRequestQty.getText().toString());
                                } catch (NumberFormatException e) {
                                    Log.e("SelectedProductList", e.toString());
                                }

                                if (requestQty == 0) {
                                    txtFree.setEnabled(false);
                                    txtFree.setText("0");
                                } else {
                                    txtFree.setEnabled(true);
                                }
                                long requestQuantity = Long.parseLong(s.toString());
                                Toast requestQtyNotAvailable = Toast.makeText(invoiceGen1Activity, "Requested Quantity is greater than current stock", Toast.LENGTH_SHORT);
                                requestQtyNotAvailable.setGravity(Gravity.TOP, 200, 100);
                                if (requestQuantity <= currentStock) {
                                    txtNormal.setText(s.toString());
                                    requestQtyNotAvailable.cancel();
                                } else {
                                    requestQtyNotAvailable.show();
                                    txtNormal.setText(String.valueOf(currentStock));
                                }


                                int pos = txtFree.length();
                                Editable fTxt = txtFree.getText();
                                Selection.setSelection(fTxt, pos);
                                txtFree.setSelection(0, pos);
                            } else {
                                txtRequestQty.setText("0");
                                int pos = txtRequestQty.length();
                                Editable rTxt = txtRequestQty.getText();
                                Selection.setSelection(rTxt, pos);
                                txtRequestQty.setSelection(0, pos);
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
                            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                                currentStock = Integer.parseInt(productDetails[4]);
                                if (!isTemporaryCustomer) {
                                    if (!txtRequestQty.getText().toString().isEmpty()) {
                                        if (!txtShelfQuantity.getText().toString().isEmpty()) {
                                            if (Integer.parseInt(txtRequestQty.getText().toString()) <= currentStock) {
                                                txtNormal.setText(txtRequestQty.getText().toString());
                                                String[] sd = new String[5];
                                                sd[0] = txtRequestQty.getText().toString();
                                                sd[1] = txtFree.getText().toString();
                                                sd[2] = txtNormal.getText().toString();
                                                sd[3] = txtDiscount.getText().toString();
                                                sd[4] = txtShelfQuantity.getText().toString();

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

                                                String tempData[] = selectedProducts.get(position);
                                                tempData[7] = sd[0];
                                                tempData[8] = sd[1];
                                                tempData[9] = sd[2];
                                                tempData[10] = sd[3];
                                                tempData[13] = sd[4];
                                                selectedProducts.remove(position);
                                                selectedProducts.add(position, tempData);
                                                invoiceGen1Activity.setSelectedProducts(selectedProducts);
                                                InvoiceGen1SelectedProductListAdapter.this.notifyDataSetChanged();
                                                txtRequestQty.setText(null);
                                                txtFree.setText(null);
                                                txtNormal.setText(null);
                                                txtDiscount.setText(null);
                                                txtRequestQty.setFocusable(true);
                                                txtRequestQty.requestFocus();
                                                editProductQuantitiesDialog.cancel();
                                            } else {
                                                txtNormal.setText(String.valueOf(currentStock));
                                                Toast RequestQtyNotAvailable = Toast.makeText(invoiceGen1Activity, "Requested Quantity is greater than current stock", Toast.LENGTH_SHORT);
                                                RequestQtyNotAvailable.setGravity(Gravity.TOP, 200, 100);
                                                RequestQtyNotAvailable.show();

                                                String[] sd = new String[5];
                                                sd[0] = txtRequestQty.getText().toString();
                                                sd[1] = txtFree.getText().toString();
                                                sd[2] = txtNormal.getText().toString();
                                                sd[3] = txtDiscount.getText().toString();
                                                sd[4] = txtShelfQuantity.getText().toString();

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

                                                String tempData[] = selectedProducts.get(position);
                                                tempData[7] = sd[0];
                                                tempData[8] = sd[1];
                                                tempData[9] = sd[2];
                                                tempData[10] = sd[3];
                                                tempData[13] = sd[4];
                                                selectedProducts.remove(position);
                                                selectedProducts.add(position, tempData);
                                                invoiceGen1Activity.setSelectedProducts(selectedProducts);
                                                InvoiceGen1SelectedProductListAdapter.this.notifyDataSetChanged();
                                                txtRequestQty.setText(null);
                                                txtFree.setText(null);
                                                txtNormal.setText(null);
                                                txtDiscount.setText(null);
                                                txtRequestQty.setFocusable(true);
                                                txtRequestQty.requestFocus();
                                                editProductQuantitiesDialog.cancel();

                                            }
                                        }
                                    }
                                } else {
                                    if (!txtRequestQty.getText().toString().isEmpty()) {
                                        if (Integer.parseInt(txtRequestQty.getText().toString()) <= currentStock) {
                                            txtNormal.setText(txtRequestQty.getText().toString());
                                            String[] sd = new String[5];
                                            sd[0] = txtRequestQty.getText().toString();
                                            sd[1] = txtFree.getText().toString();
                                            sd[2] = txtNormal.getText().toString();
                                            sd[3] = txtDiscount.getText().toString();
                                            sd[4] = txtShelfQuantity.getText().toString();

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

                                            String tempData[] = selectedProducts.get(position);
                                            tempData[7] = sd[0];
                                            tempData[8] = sd[1];
                                            tempData[9] = sd[2];
                                            tempData[10] = sd[3];
                                            tempData[13] = sd[4];
                                            selectedProducts.remove(position);
                                            selectedProducts.add(position, tempData);
                                            invoiceGen1Activity.setSelectedProducts(selectedProducts);
                                            InvoiceGen1SelectedProductListAdapter.this.notifyDataSetChanged();
                                            txtRequestQty.setText(null);
                                            txtFree.setText(null);
                                            txtNormal.setText(null);
                                            txtDiscount.setText(null);
                                            txtRequestQty.setFocusable(true);
                                            txtRequestQty.requestFocus();
                                            editProductQuantitiesDialog.cancel();
                                        } else {
                                            txtNormal.setText(String.valueOf(currentStock));
                                            Toast RequestQtyNotAvailable = Toast.makeText(invoiceGen1Activity, "Requested Quantity is greater than current stock", Toast.LENGTH_SHORT);
                                            RequestQtyNotAvailable.setGravity(Gravity.TOP, 200, 100);
                                            RequestQtyNotAvailable.show();

                                            String[] sd = new String[5];
                                            sd[0] = txtRequestQty.getText().toString();
                                            sd[1] = txtFree.getText().toString();
                                            sd[2] = txtNormal.getText().toString();
                                            sd[3] = txtDiscount.getText().toString();
                                            sd[4] = txtShelfQuantity.getText().toString();

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

                                            String tempData[] = selectedProducts.get(position);
                                            tempData[7] = sd[0];
                                            tempData[8] = sd[1];
                                            tempData[9] = sd[2];
                                            tempData[10] = sd[3];
                                            tempData[13] = sd[4];
                                            selectedProducts.remove(position);
                                            selectedProducts.add(position, tempData);
                                            invoiceGen1Activity.setSelectedProducts(selectedProducts);
                                            InvoiceGen1SelectedProductListAdapter.this.notifyDataSetChanged();
                                            txtRequestQty.setText(null);
                                            txtFree.setText(null);
                                            txtNormal.setText(null);
                                            txtDiscount.setText(null);
                                            txtRequestQty.setFocusable(true);
                                            txtRequestQty.requestFocus();
                                            editProductQuantitiesDialog.cancel();

                                        }
                                    }
                                }
                            }
                            return false;
                        }
                    });

                    txtFree.setOnKeyListener(new View.OnKeyListener() {

                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            // TODO Auto-generated method stub
                            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                                currentStock = Integer.parseInt(productDetails[4]);
                                if (!isTemporaryCustomer) {
                                    if (!txtRequestQty.getText().toString().isEmpty()) {
                                        if (!txtShelfQuantity.getText().toString().isEmpty()) {
                                            if (Integer.parseInt(txtRequestQty.getText().toString()) <= currentStock) {
                                                txtNormal.setText(txtRequestQty.getText().toString());
                                                String[] sd = new String[5];
                                                sd[0] = txtRequestQty.getText().toString();
                                                sd[1] = txtFree.getText().toString();
                                                sd[2] = txtNormal.getText().toString();
                                                sd[3] = txtDiscount.getText().toString();
                                                sd[4] = txtShelfQuantity.getText().toString();

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

                                                String tempData[] = selectedProducts.get(position);
                                                tempData[7] = sd[0];
                                                tempData[8] = sd[1];
                                                tempData[9] = sd[2];
                                                tempData[10] = sd[3];
                                                tempData[13] = sd[4];
                                                selectedProducts.remove(position);
                                                selectedProducts.add(position, tempData);
                                                invoiceGen1Activity.setSelectedProducts(selectedProducts);
                                                InvoiceGen1SelectedProductListAdapter.this.notifyDataSetChanged();
                                                txtRequestQty.setText(null);
                                                txtFree.setText(null);
                                                txtNormal.setText(null);
                                                txtDiscount.setText(null);
                                                txtRequestQty.setFocusable(true);
                                                txtRequestQty.requestFocus();
                                                editProductQuantitiesDialog.cancel();
                                            } else {
                                                txtNormal.setText(String.valueOf(currentStock));
                                                Toast RequestQtyNotAvailable = Toast.makeText(invoiceGen1Activity, "Requested Quantity is greater than current stock", Toast.LENGTH_SHORT);
                                                RequestQtyNotAvailable.setGravity(Gravity.TOP, 200, 100);
                                                RequestQtyNotAvailable.show();

                                                String[] sd = new String[5];
                                                sd[0] = txtRequestQty.getText().toString();
                                                sd[1] = txtFree.getText().toString();
                                                sd[2] = txtNormal.getText().toString();
                                                sd[3] = txtDiscount.getText().toString();
                                                sd[4] = txtShelfQuantity.getText().toString();

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

                                                String tempData[] = selectedProducts.get(position);
                                                tempData[7] = sd[0];
                                                tempData[8] = sd[1];
                                                tempData[9] = sd[2];
                                                tempData[10] = sd[3];
                                                tempData[13] = sd[4];
                                                selectedProducts.remove(position);
                                                selectedProducts.add(position, tempData);
                                                invoiceGen1Activity.setSelectedProducts(selectedProducts);
                                                InvoiceGen1SelectedProductListAdapter.this.notifyDataSetChanged();
                                                txtRequestQty.setText(null);
                                                txtFree.setText(null);
                                                txtNormal.setText(null);
                                                txtDiscount.setText(null);
                                                txtRequestQty.setFocusable(true);
                                                txtRequestQty.requestFocus();
                                                editProductQuantitiesDialog.cancel();
                                            }
                                        }
                                    }
                                } else {
                                    if (!txtRequestQty.getText().toString().isEmpty()) {
                                        if (Integer.parseInt(txtRequestQty.getText().toString()) <= currentStock) {
                                            txtNormal.setText(txtRequestQty.getText().toString());
                                            String[] sd = new String[5];
                                            sd[0] = txtRequestQty.getText().toString();
                                            sd[1] = txtFree.getText().toString();
                                            sd[2] = txtNormal.getText().toString();
                                            sd[3] = txtDiscount.getText().toString();
                                            sd[4] = txtShelfQuantity.getText().toString();

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

                                            String tempData[] = selectedProducts.get(position);
                                            tempData[7] = sd[0];
                                            tempData[8] = sd[1];
                                            tempData[9] = sd[2];
                                            tempData[10] = sd[3];
                                            tempData[13] = sd[4];
                                            selectedProducts.remove(position);
                                            selectedProducts.add(position, tempData);
                                            invoiceGen1Activity.setSelectedProducts(selectedProducts);
                                            InvoiceGen1SelectedProductListAdapter.this.notifyDataSetChanged();
                                            txtRequestQty.setText(null);
                                            txtFree.setText(null);
                                            txtNormal.setText(null);
                                            txtDiscount.setText(null);
                                            txtRequestQty.setFocusable(true);
                                            txtRequestQty.requestFocus();
                                            editProductQuantitiesDialog.cancel();
                                        } else {
                                            txtNormal.setText(String.valueOf(currentStock));
                                            Toast RequestQtyNotAvailable = Toast.makeText(invoiceGen1Activity, "Requested Quantity is greater than current stock", Toast.LENGTH_SHORT);
                                            RequestQtyNotAvailable.setGravity(Gravity.TOP, 200, 100);
                                            RequestQtyNotAvailable.show();

                                            String[] sd = new String[5];
                                            sd[0] = txtRequestQty.getText().toString();
                                            sd[1] = txtFree.getText().toString();
                                            sd[2] = txtNormal.getText().toString();
                                            sd[3] = txtDiscount.getText().toString();
                                            sd[4] = txtShelfQuantity.getText().toString();

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

                                            String tempData[] = selectedProducts.get(position);
                                            tempData[7] = sd[0];
                                            tempData[8] = sd[1];
                                            tempData[9] = sd[2];
                                            tempData[10] = sd[3];
                                            tempData[13] = sd[4];
                                            selectedProducts.remove(position);
                                            selectedProducts.add(position, tempData);
                                            invoiceGen1Activity.setSelectedProducts(selectedProducts);
                                            InvoiceGen1SelectedProductListAdapter.this.notifyDataSetChanged();
                                            txtRequestQty.setText(null);
                                            txtFree.setText(null);
                                            txtNormal.setText(null);
                                            txtDiscount.setText(null);
                                            txtRequestQty.setFocusable(true);
                                            txtRequestQty.requestFocus();
                                            editProductQuantitiesDialog.cancel();

                                        }
                                    }
                                }
                            }
                            return false;
                        }
                    });

                    txtFree.addTextChangedListener(new TextWatcher() {

                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            // TODO Auto-generated method stub
                            currentStock = Integer.parseInt(productDetails[4]);

                            if (!txtRequestQty.getText().toString().isEmpty()) {
                                int freeTemp = 0;
                                int normalTemp = 0;

                                try {
                                    freeTemp = Integer.parseInt(txtFree.getText().toString());
                                    normalTemp = Integer.parseInt(txtNormal.getText().toString());
                                } catch (NumberFormatException e) {
                                    Log.w("Unable to parse Free - IG1 selected product adapter list", e.toString());
                                    freeTemp = 0;
                                    normalTemp = 0;
                                }
                                if (s.toString().isEmpty()) {
                                    txtFree.setText("0");
                                    int pos = txtFree.length();
                                    Editable fTxt = txtFree.getText();
                                    Selection.setSelection(fTxt, pos);
                                    txtFree.setSelection(0, pos);
                                } else if ((freeTemp + normalTemp) > currentStock) {
                                    txtFree.setText("0");
                                    int pos = txtFree.length();
                                    Editable fTxt = txtFree.getText();
                                    Selection.setSelection(fTxt, pos);
                                    txtFree.setSelection(0, pos);

                                    Toast FreeGreaterThanCurrentStock = Toast.makeText(invoiceGen1Activity, "Free amount cannot be greater than " + (currentStock - normalTemp), Toast.LENGTH_SHORT);
                                    FreeGreaterThanCurrentStock.setGravity(Gravity.TOP, 200, 100);
                                    FreeGreaterThanCurrentStock.show();
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

                    txtNormal.setOnKeyListener(new View.OnKeyListener() {

                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            // TODO Auto-generated method stub
                            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                                currentStock = Integer.parseInt(productDetails[4]);
                                if (!isTemporaryCustomer) {
                                    if (!txtRequestQty.getText().toString().isEmpty()) {
                                        if (!txtShelfQuantity.getText().toString().isEmpty()) {
                                            if (Integer.parseInt(txtNormal.getText().toString()) <= currentStock) {
                                                txtNormal.setText(txtRequestQty.getText().toString());
                                                txtRequestQty.setText(txtNormal.getText().toString());
                                                String[] sd = new String[5];
                                                sd[0] = txtRequestQty.getText().toString();
                                                sd[1] = txtFree.getText().toString();
                                                sd[2] = txtNormal.getText().toString();
                                                sd[3] = txtDiscount.getText().toString();
                                                sd[4] = txtShelfQuantity.getText().toString();

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

                                                String tempData[] = selectedProducts.get(position);
                                                tempData[7] = sd[0];
                                                tempData[8] = sd[1];
                                                tempData[9] = sd[2];
                                                tempData[10] = sd[3];
                                                tempData[13] = sd[4];
                                                selectedProducts.remove(position);
                                                selectedProducts.add(position, tempData);
                                                invoiceGen1Activity.setSelectedProducts(selectedProducts);
                                                InvoiceGen1SelectedProductListAdapter.this.notifyDataSetChanged();
                                                txtRequestQty.setText(null);
                                                txtFree.setText(null);
                                                txtNormal.setText(null);
                                                txtDiscount.setText(null);
                                                txtRequestQty.setFocusable(true);
                                                txtRequestQty.requestFocus();
                                                editProductQuantitiesDialog.cancel();
                                            } else {
                                                txtRequestQty.setText(txtNormal.getText().toString());
                                                txtNormal.setText(String.valueOf(currentStock));
                                                Toast normalQtyNotAvailable = Toast.makeText(invoiceGen1Activity, "Normal Quantity is greater than current stock", Toast.LENGTH_SHORT);
                                                normalQtyNotAvailable.setGravity(Gravity.TOP, 200, 100);
                                                normalQtyNotAvailable.show();


                                                String[] sd = new String[5];
                                                sd[0] = txtRequestQty.getText().toString();
                                                sd[1] = txtFree.getText().toString();
                                                sd[2] = txtNormal.getText().toString();
                                                sd[3] = txtDiscount.getText().toString();
                                                sd[4] = txtShelfQuantity.getText().toString();

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

                                                String tempData[] = selectedProducts.get(position);
                                                tempData[7] = sd[0];
                                                tempData[8] = sd[1];
                                                tempData[9] = sd[2];
                                                tempData[10] = sd[3];
                                                tempData[13] = sd[4];
                                                selectedProducts.remove(position);
                                                selectedProducts.add(position, tempData);
                                                invoiceGen1Activity.setSelectedProducts(selectedProducts);
                                                InvoiceGen1SelectedProductListAdapter.this.notifyDataSetChanged();
                                                txtRequestQty.setText(null);
                                                txtFree.setText(null);
                                                txtNormal.setText(null);
                                                txtDiscount.setText(null);
                                                txtRequestQty.setFocusable(true);
                                                txtRequestQty.requestFocus();
                                                editProductQuantitiesDialog.cancel();

                                            }
                                        }
                                    }
                                } else {
                                    if (!txtRequestQty.getText().toString().isEmpty()) {
                                        if (Integer.parseInt(txtNormal.getText().toString()) <= currentStock) {
                                            txtNormal.setText(txtRequestQty.getText().toString());
                                            txtRequestQty.setText(txtNormal.getText().toString());
                                            String[] sd = new String[5];
                                            sd[0] = txtRequestQty.getText().toString();
                                            sd[1] = txtFree.getText().toString();
                                            sd[2] = txtNormal.getText().toString();
                                            sd[3] = txtDiscount.getText().toString();
                                            sd[4] = txtShelfQuantity.getText().toString();

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

                                            String tempData[] = selectedProducts.get(position);
                                            tempData[7] = sd[0];
                                            tempData[8] = sd[1];
                                            tempData[9] = sd[2];
                                            tempData[10] = sd[3];
                                            tempData[13] = sd[4];
                                            selectedProducts.remove(position);
                                            selectedProducts.add(position, tempData);
                                            invoiceGen1Activity.setSelectedProducts(selectedProducts);
                                            InvoiceGen1SelectedProductListAdapter.this.notifyDataSetChanged();
                                            txtRequestQty.setText(null);
                                            txtFree.setText(null);
                                            txtNormal.setText(null);
                                            txtDiscount.setText(null);
                                            txtRequestQty.setFocusable(true);
                                            txtRequestQty.requestFocus();
                                            editProductQuantitiesDialog.cancel();
                                        } else {
                                            txtRequestQty.setText(txtNormal.getText().toString());
                                            txtNormal.setText(String.valueOf(currentStock));
                                            Toast normalQtyNotAvailable = Toast.makeText(invoiceGen1Activity, "Normal Quantity is greater than current stock", Toast.LENGTH_SHORT);
                                            normalQtyNotAvailable.setGravity(Gravity.TOP, 200, 100);
                                            normalQtyNotAvailable.show();


                                            String[] sd = new String[5];
                                            sd[0] = txtRequestQty.getText().toString();
                                            sd[1] = txtFree.getText().toString();
                                            sd[2] = txtNormal.getText().toString();
                                            sd[3] = txtDiscount.getText().toString();
                                            sd[4] = txtShelfQuantity.getText().toString();

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

                                            String tempData[] = selectedProducts.get(position);
                                            tempData[7] = sd[0];
                                            tempData[8] = sd[1];
                                            tempData[9] = sd[2];
                                            tempData[10] = sd[3];
                                            tempData[13] = sd[4];
                                            selectedProducts.remove(position);
                                            selectedProducts.add(position, tempData);
                                            invoiceGen1Activity.setSelectedProducts(selectedProducts);
                                            InvoiceGen1SelectedProductListAdapter.this.notifyDataSetChanged();
                                            txtRequestQty.setText(null);
                                            txtFree.setText(null);
                                            txtNormal.setText(null);
                                            txtDiscount.setText(null);
                                            txtRequestQty.setFocusable(true);
                                            txtRequestQty.requestFocus();
                                            editProductQuantitiesDialog.cancel();

                                        }
                                    }
                                }
                            }
                            return false;
                        }
                    });

                    txtDiscount.addTextChangedListener(new TextWatcher() {

                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            double discount = 0.0;
                            try {
                                discount = Double.parseDouble(s.toString());
                            } catch (NumberFormatException e) {
                                Log.e("SelectedProductListAdapter", e.toString());
                            }

                            if (discount < 0 || discount > 100) {
                                txtDiscount.setText("0.0");
                                hightLightText(txtDiscount);
                                Toast toast = Toast.makeText(invoiceGen1Activity, "Discount has to be between 0 and 100!", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 50, 50);
                                toast.show();
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

                    txtDiscount.setOnKeyListener(new View.OnKeyListener() {

                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            // TODO Auto-generated method stub
                            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                                currentStock = Integer.parseInt(productDetails[4]);
                                if (!isTemporaryCustomer) {
                                    if (!txtRequestQty.getText().toString().isEmpty()) {
                                        if (!txtShelfQuantity.getText().toString().isEmpty()) {
                                            if (Integer.parseInt(txtRequestQty.getText().toString()) <= currentStock) {
                                                txtNormal.setText(txtRequestQty.getText().toString());
                                                String[] sd = new String[5];
                                                sd[0] = txtRequestQty.getText().toString();
                                                sd[1] = txtFree.getText().toString();
                                                sd[2] = txtNormal.getText().toString();
                                                sd[3] = txtDiscount.getText().toString();
                                                sd[4] = txtShelfQuantity.getText().toString();

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

                                                String tempData[] = selectedProducts.get(position);
                                                tempData[7] = sd[0];
                                                tempData[8] = sd[1];
                                                tempData[9] = sd[2];
                                                tempData[10] = sd[3];
                                                tempData[13] = sd[4];
                                                selectedProducts.remove(position);
                                                selectedProducts.add(position, tempData);
                                                invoiceGen1Activity.setSelectedProducts(selectedProducts);
                                                InvoiceGen1SelectedProductListAdapter.this.notifyDataSetChanged();
                                                txtRequestQty.setText(null);
                                                txtFree.setText(null);
                                                txtNormal.setText(null);
                                                txtDiscount.setText(null);
                                                txtRequestQty.setFocusable(true);
                                                txtRequestQty.requestFocus();
                                                editProductQuantitiesDialog.cancel();
                                            } else {
                                                txtNormal.setText(String.valueOf(currentStock));
                                                Toast RequestQtyNotAvailable = Toast.makeText(invoiceGen1Activity, "Requested Quantity is greater than current stock", Toast.LENGTH_SHORT);
                                                RequestQtyNotAvailable.setGravity(Gravity.TOP, 200, 100);
                                                RequestQtyNotAvailable.show();

                                                String[] sd = new String[5];
                                                sd[0] = txtRequestQty.getText().toString();
                                                sd[1] = txtFree.getText().toString();
                                                sd[2] = txtNormal.getText().toString();
                                                sd[3] = txtDiscount.getText().toString();
                                                sd[4] = txtShelfQuantity.getText().toString();

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

                                                String tempData[] = selectedProducts.get(position);
                                                tempData[7] = sd[0];
                                                tempData[8] = sd[1];
                                                tempData[9] = sd[2];
                                                tempData[10] = sd[3];
                                                tempData[13] = sd[4];
                                                selectedProducts.remove(position);
                                                selectedProducts.add(position, tempData);
                                                invoiceGen1Activity.setSelectedProducts(selectedProducts);
                                                InvoiceGen1SelectedProductListAdapter.this.notifyDataSetChanged();
                                                txtRequestQty.setText(null);
                                                txtFree.setText(null);
                                                txtNormal.setText(null);
                                                txtDiscount.setText(null);
                                                txtRequestQty.setFocusable(true);
                                                txtRequestQty.requestFocus();
                                                editProductQuantitiesDialog.cancel();
                                            }
                                        }
                                    }
                                } else {
                                    if (!txtRequestQty.getText().toString().isEmpty()) {
                                        if (Integer.parseInt(txtRequestQty.getText().toString()) <= currentStock) {
                                            txtNormal.setText(txtRequestQty.getText().toString());
                                            String[] sd = new String[5];
                                            sd[0] = txtRequestQty.getText().toString();
                                            sd[1] = txtFree.getText().toString();
                                            sd[2] = txtNormal.getText().toString();
                                            sd[3] = txtDiscount.getText().toString();
                                            sd[4] = txtShelfQuantity.getText().toString();

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

                                            String tempData[] = selectedProducts.get(position);
                                            tempData[7] = sd[0];
                                            tempData[8] = sd[1];
                                            tempData[9] = sd[2];
                                            tempData[10] = sd[3];
                                            tempData[13] = sd[4];
                                            selectedProducts.remove(position);
                                            selectedProducts.add(position, tempData);
                                            invoiceGen1Activity.setSelectedProducts(selectedProducts);
                                            InvoiceGen1SelectedProductListAdapter.this.notifyDataSetChanged();
                                            txtRequestQty.setText(null);
                                            txtFree.setText(null);
                                            txtNormal.setText(null);
                                            txtDiscount.setText(null);
                                            txtRequestQty.setFocusable(true);
                                            txtRequestQty.requestFocus();
                                            editProductQuantitiesDialog.cancel();
                                        } else {
                                            txtNormal.setText(String.valueOf(currentStock));
                                            Toast RequestQtyNotAvailable = Toast.makeText(invoiceGen1Activity, "Requested Quantity is greater than current stock", Toast.LENGTH_SHORT);
                                            RequestQtyNotAvailable.setGravity(Gravity.TOP, 200, 100);
                                            RequestQtyNotAvailable.show();

                                            String[] sd = new String[5];
                                            sd[0] = txtRequestQty.getText().toString();
                                            sd[1] = txtFree.getText().toString();
                                            sd[2] = txtNormal.getText().toString();
                                            sd[3] = txtDiscount.getText().toString();
                                            sd[4] = txtShelfQuantity.getText().toString();

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

                                            String tempData[] = selectedProducts.get(position);
                                            tempData[7] = sd[0];
                                            tempData[8] = sd[1];
                                            tempData[9] = sd[2];
                                            tempData[10] = sd[3];
                                            tempData[13] = sd[4];
                                            selectedProducts.remove(position);
                                            selectedProducts.add(position, tempData);
                                            invoiceGen1Activity.setSelectedProducts(selectedProducts);
                                            InvoiceGen1SelectedProductListAdapter.this.notifyDataSetChanged();
                                            txtRequestQty.setText(null);
                                            txtFree.setText(null);
                                            txtNormal.setText(null);
                                            txtDiscount.setText(null);
                                            txtRequestQty.setFocusable(true);
                                            txtRequestQty.requestFocus();
                                            editProductQuantitiesDialog.cancel();
                                        }
                                    }
                                }
                            }
                            return false;
                        }
                    });

                    btnCancelPopup.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            editProductQuantitiesDialog.cancel();
                        }
                    });
                }
            }
        });

        btnRemoveProduct.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                // Previous implementation removed by the position in the list, now im passing product ID and batch id
                //so that i can iterate and delete only that productss
                String[] productData = selectedProducts.get(position);
                String productId = productData[2];
                String batch = productData[3];
                invoiceGen1Activity.removeItemAt(productId, batch);
                InvoiceGen1SelectedProductListAdapter.this.notifyDataSetChanged();
            }
        });

        if (selectedProducts.size() >= position) {

            String[] productData = selectedProducts.get(position);

            tViewRequestQty.setText(productData[7]);
            Log.w("FROM list AdaPTER ####", productData[7]);
            tViewFree.setText(productData[8]);
            tViewNormal.setText(productData[9]);
            tViewDiscount.setText(productData[10]);
            if (!productData[5].contentEquals("0")) {
                tViewExpire.setText(productData[5].substring(0, 10));
            }
            tViewProBatch.setText(productData[3]);
            tViewShelfQty.setText(productData[13]);

            double totalP = 0.0;
            double normalP = 0.0;
            double discountP = 0.0;
            double price = 0.0;
            int totalQ = 0;
            int normalQ = 0;
            int freeQ = 0;

            try {
                if (productData[9].length() > 0) {
                    normalQ = Integer.parseInt(productData[9]);
                }
                if (productData[8].length() > 0) {
                    freeQ = Integer.parseInt(productData[8]);
                }
                if (productData[10].length() > 0) {
                    discountP = Double.parseDouble(productData[10]);
                }
                if (productData[12].length() > 0) {
                    price = Double.parseDouble(productData[12]);
                }
            } catch (Exception e) {
                Log.w("ig1SPA", e.toString());
            }


            totalQ = normalQ + freeQ;
            tViewTotalQty.setText(String.valueOf(totalQ));

            String totalPrice = null;
            normalP = price * normalQ;

            if (discountP != 0) {

                totalP = normalP * ((100 - discountP) / 100);
                totalPrice = String.format("%.2f", totalP);
                tViewTotalValue.setText(totalPrice);
            } else {
                totalP = normalP;
                totalPrice = String.format("%.2f", totalP);
                tViewTotalValue.setText(totalPrice);
            }


            if (productData[11].length() > 40) {
                String name = productData[11].substring(0, 40);
                tViewProductName.setText(name);

            } else {
                tViewProductName.setText(productData[11]);
            }

        }

        return vi;
    }

    private void hightLightText(EditText editText) {
        int position = editText.length();
        Editable etext = editText.getText();
        Selection.setSelection(etext, position);
        editText.setSelection(0, position);
    }
}
