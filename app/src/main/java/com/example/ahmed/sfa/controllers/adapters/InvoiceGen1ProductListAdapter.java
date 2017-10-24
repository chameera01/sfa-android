package com.example.ahmed.sfa.controllers.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ahmed.sfa.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 10/22/2017.
 */
public class InvoiceGen1ProductListAdapter extends BaseAdapter {

    static Context thisContext;
    private static LayoutInflater inflater = null;
    List<String[]> productData;
    ArrayList<String> selectedProductIds = new ArrayList<String>();
    ArrayList<String[]> productInfo = new ArrayList<String[]>();
    TextView tViewProductName, labelLastInvoiceQty, tViewLastInvoiceQty,
            labelLastThreeMonthAvg, tViewLastThreeMonthAvg, labelCurrentStock,
            tViewCurrentStock, labelDiscount, tViewDiscount;
    TextView labelPrice, tViewPrice, labelProductCode, tviewProductCode;
    private Activity activity;
    private List<String> invoicedProductsForCustomer;
    private String pharmacyId;

    public InvoiceGen1ProductListAdapter(Activity a, List<String[]> pData, ArrayList<String[]> pInfo, String pharmacyId) {
        activity = a;
        productData = pData;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        thisContext = activity.getApplicationContext();
        productInfo = pInfo;
        this.pharmacyId = pharmacyId;
        invoicedProductsForCustomer = getInvoicedProductsForCustomer();

    }

    public int getCount() {
        // TODO Auto-generated method stub
        return productData.size();

    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.invoice_gen_1_product_list, parent, false);

        tViewProductName = (TextView) vi.findViewById(R.id.tvProductName);

        labelLastInvoiceQty = (TextView) vi
                .findViewById(R.id.labelLastInvoiceQty);
        tViewLastInvoiceQty = (TextView) vi.findViewById(R.id.tvLastInvoiceQty);
        labelLastThreeMonthAvg = (TextView) vi
                .findViewById(R.id.labelLastThreeMonthAvg);
        tViewLastThreeMonthAvg = (TextView) vi
                .findViewById(R.id.tvLastThreeMonthAvg);
        labelCurrentStock = (TextView) vi.findViewById(R.id.labelCurrentStock);
        tViewCurrentStock = (TextView) vi.findViewById(R.id.tvCurrentStock);
        labelDiscount = (TextView) vi.findViewById(R.id.labelDiscount);
        tViewDiscount = (TextView) vi.findViewById(R.id.tvDiscount);

        labelPrice = (TextView) vi.findViewById(R.id.labelPrice);
        tViewPrice = (TextView) vi.findViewById(R.id.tvPrice);
        labelProductCode = (TextView) vi.findViewById(R.id.labelProductCode);
        tviewProductCode = (TextView) vi.findViewById(R.id.tvProductCode);


        String[] pData = productData.get(position);

        boolean hasBeenInvoiced = invoicedProductsForCustomer.contains(pData[2]);

        String currentStock = "0";
        ProductRepStore productRepStore = new ProductRepStore(activity);
        productRepStore.openReadableDatabase();
        try {
            currentStock = productRepStore.getQuantitySumByProductCode(pData[2]);

        } catch (Exception e) {
            currentStock = "0";
        } finally {
            productRepStore.closeDatabase();
        }


        // TODO - Set the colors to the background of the view
        setValidColors(hasBeenInvoiced, vi);

        Log.w("productInfoSize igenAdap: ", productInfo.size() + "");


        tViewProductName.setText(pData[8]);
        tViewPrice.setText(pData[13]);
        tviewProductCode.setText(pData[2]);
        try {
            tViewCurrentStock.setText(currentStock);
        } catch (Exception e) {
        }

        return vi;
    }

    public void setValidColors(boolean hasBeenInvoiced, View view) {
        if (hasBeenInvoiced) {
            //Sets the background of the view
            view.setBackgroundColor(Color.parseColor("#33B5E5"));
        } else {
            view.setBackgroundColor(Color.WHITE);
        }
        // setting colours to first row
        tViewProductName.setTextColor(Color.parseColor("#000000"));
        labelLastInvoiceQty.setTextColor(Color.parseColor("#CC0000"));
        tViewLastInvoiceQty.setTextColor(Color.parseColor("#CC0000"));
        labelLastThreeMonthAvg.setTextColor(Color.parseColor("#CC0000"));
        tViewLastThreeMonthAvg.setTextColor(Color.parseColor("#CC0000"));
        labelCurrentStock.setTextColor(Color.parseColor("#CC0000"));
        tViewCurrentStock.setTextColor(Color.parseColor("#CC0000"));


        // Setting colours to second row
        labelPrice.setTextColor(Color.parseColor("#191970"));
        tViewPrice.setTextColor(Color.parseColor("#191970"));
        labelProductCode.setTextColor(Color.parseColor("#191970"));
        tviewProductCode.setTextColor(Color.parseColor("#191970"));
        labelDiscount.setTextColor(Color.parseColor("#191970"));
        tViewDiscount.setTextColor(Color.parseColor("#191970"));


    }

    private List<String> getInvoicedProductsForCustomer() {
        List<String> productIdList = new ArrayList<String>();
        CustomerProductAvg customerProductAvg = new CustomerProductAvg(activity);
        customerProductAvg.openReadableDatabase();
        productIdList = customerProductAvg.getInvoicedProductIds(pharmacyId);
        customerProductAvg.closeDatabase();
        return productIdList;
    }


}
