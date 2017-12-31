package com.example.ahmed.sfa.controllers.adapters;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.database.DBHelper;
import com.example.ahmed.sfa.models.SalesInvoiceModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Ahmed on 9/9/2017.
 */

public class InvoiceRecyclerAdapter extends RecyclerView.Adapter<InvoiceRecyclerAdapter.MyViewHolder>
        implements SummaryUpdater {

    private static final String TAG = "INVOICE";
    private List<SalesInvoiceModel> salesInvoice;
    private String principle;
    boolean onBind;
    public final String SHELF = "SHLEF", REQUEST = "REQUEST", ORDER = "RETURN", FREE = "FREE", DISCOUNT = "DISCOUNT", LINEVAL = "LINEVAL";
    //    DBAdapterAsync dbAdapter;
    private Context getContextForAdapter;
    SummaryUpdateListner listner;
    boolean refreshed = true;
    int lastUpdatedRow = -1;
    boolean refresh = false;
    private boolean lostFocus = false;
    private SalesInvoiceModel model;
    private Context context;
    private boolean flag;

    private BroadcastReceiver receiver = new InvoiceSummaryChangeReceiver();

//    private UpdateDBTask task;

    @Override
    public void notifyUpdate() {
        Log.d(TAG, "inside notifyUpdate");
        listner.updateSummary();
    }

    @Override
    public void addListener(SummaryUpdateListner listner) {
        this.listner = listner;
    }


    public void customNotifyDataSetChanged() {
        this.notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView code, product, expiry, unitprice, stock, lineval, batchNum;
        EditText shelf, request, order, free, discount;
        int ref;

        View view;

        public MyViewHolder(View view) {
            super(view);
            this.view = view;
//            dbAdapter = new DBAdapterAsync(view.getContext());
            getContextForAdapter = view.getContext();
            code = (TextView) view.findViewById(R.id.code_e);
            product = (TextView) view.findViewById(R.id.product_e);
            batchNum = (TextView) view.findViewById(R.id.batch_e);
            expiry = (TextView) view.findViewById(R.id.expiry_e);
            unitprice = (TextView) view.findViewById(R.id.unit_price_e);
            stock = (TextView) view.findViewById(R.id.stock_e);
            lineval = (TextView) view.findViewById(R.id.line_val_e);

            shelf = (EditText) view.findViewById(R.id.shelf_e);
            request = (EditText) view.findViewById(R.id.request_e);
            order = (EditText) view.findViewById(R.id.order_e);
            free = (EditText) view.findViewById(R.id.free_e);
            discount = (EditText) view.findViewById(R.id.dsc_e);


        }

        public void setCursor(String view) {
            switch (view) {
                case SHELF:
                    shelf.setSelection(shelf.getText().length());
                    break;
                case REQUEST:
                    request.setSelection(request.getText().length());
                    break;

                case ORDER:
                    order.setSelection(order.getText().length());
                    break;

                case FREE:
                    free.setSelection(free.getText().length());
                    break;
                case DISCOUNT:
                    discount.setSelection(discount.getText().length());
                    break;


            }
        }

        public void setColor(int color) {
            view.setBackgroundColor(color);
        }

    }


    public InvoiceRecyclerAdapter(List<SalesInvoiceModel> list, String principle, Context context) {
        this.salesInvoice = list;
        this.principle = principle;
        this.context = context;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invoicetabel_row, parent, false);

        return new MyViewHolder(itemView);

    }

    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int position, List<Object> payload) {
        //onBindViewHolder(holder,position);

        holder.ref = position;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        onBind = true;

        // data passed from getAllData method aka all from TempInvoice
        SalesInvoiceModel siModel = salesInvoice.get(position);

        holder.code.setText(siModel.getCode());
        holder.product.setText(siModel.getProduct());
        holder.batchNum.setText(siModel.getBatchNumber());
        try {
            Date date = simpleDateFormat.parse(siModel.getExpiryDate());
            holder.expiry.setText(date.toString());
        } catch (Exception ex) {
            holder.expiry.setText("Error");
        }
        holder.expiry.setText(siModel.getExpiryDate());
        //holder.unitprice.setText(siModel.getUnitPrice()+"");
        //this condition is added for specific client
        if (siModel.getDiscountRate() > 0) {
            holder.unitprice.setText(siModel.getRetailPrice() + "");
        } else {
            holder.unitprice.setText(siModel.getUnitPrice() + "");
        }//condition ends here

        holder.stock.setText(siModel.getStock() + "");
        holder.lineval.setText(siModel.getLineValue() + "");

        if (payload == null || payload.size() == 0) {
            holder.shelf.setText(siModel.getShelf() + "");
            holder.request.setText(siModel.getRequest() + "");
            holder.order.setText(siModel.getOrder() + "");
            holder.free.setText(siModel.getFree() + "");
            holder.discount.setText(siModel.getDiscountRate() + "");
        } else {

            for (Object editText : payload) {
                //String val = (String)payload.get(0);
                String val = (String) editText;
                switch (val) {
                    case SHELF:
                        //added by shani
                        if (lostFocus) {
                            holder.shelf.setText(salesInvoice.get(position).getShelf() + "");
                        }
                        break;
                    case REQUEST:
                        //added by shani
                        if (lostFocus) {
                            holder.request.setText(salesInvoice.get(position).getRequest() + "");
                        }
                        break;
                    case ORDER:
                        if (lostFocus) {
                            holder.order.setText(salesInvoice.get(position).getOrder() + "");
                        }
                        break;
                    case FREE:
                        if (lostFocus) {
                            holder.free.setText(salesInvoice.get(position).getFree() + "");
                        }
                        break;
                    case DISCOUNT:
                        if (lostFocus) {
                            holder.discount.setText(salesInvoice.get(position).getDiscountRate() + "");
                        }
                        break;

                    case LINEVAL:
                        holder.lineval.setText(salesInvoice.get(position).getLineValue() + "");
                        break;
                    default:
                        onBindViewHolder(holder, position);
                        break;
                }
            }

        }

        //ADD TEXT WATCHERS


        holder.shelf.setOnFocusChangeListener(new FocusChangeListener());
        holder.shelf.addTextChangedListener(new GenericTextWatcher() {
            @Override
            public void afterTextChanged(String s) {

                int pos = holder.ref;
                if (!onBind) {
                    if (!s.equals("")) {
                        notifyItemChanged(pos, SHELF);
                        Log.d(TAG, "inside text change typed shelf_" + s);
                        salesInvoice.get(pos).setShelf(Integer.parseInt(s + ""));


                    } else {
                        salesInvoice.get(pos).setShelf(0);
                    }
                    lastUpdatedRow = pos;
                }
            }
        });

        holder.request.setOnFocusChangeListener(new FocusChangeListener());
        holder.request.addTextChangedListener(new GenericTextWatcher() {
            @Override
            public void afterTextChanged(String s) {
                boolean valHasChanged = false;
                boolean freeHasChanged = false;
                int pos = holder.ref;
                if (!onBind) {

                    if (!(s.equals(""))) {

                        int val = Integer.parseInt((s));
                        int stock = salesInvoice.get(pos).getStock();
                        // if stock does not have the particular product
                        if (val > stock) {
                            val = stock;
                            valHasChanged = true;
                        }
                        salesInvoice.get(pos).setRequest(val);
                        if (!refresh) salesInvoice.get(pos).setOrder(val);

                        if (salesInvoice.get(pos).getFree() + salesInvoice.get(pos).getOrder() > salesInvoice.get(pos).getStock()) {
                            salesInvoice.get(pos).setFree(0);
                            freeHasChanged = true;

                        }

                    } else {
                        salesInvoice.get(pos).setRequest(0);
                    }

                    notifyItemChanged(pos, ORDER);

                    if (valHasChanged) {
                        notifyItemChanged(pos, REQUEST);
                    }
                    if (freeHasChanged) {
                        notifyItemChanged(pos, FREE);
                    }

                    lastUpdatedRow = pos;
                }

            }
        });

        holder.order.setOnFocusChangeListener(new FocusChangeListener());
        holder.order.addTextChangedListener(new GenericTextWatcher() {
            @Override
            public void afterTextChanged(String s) {
                boolean valHasChanged = false;
                boolean freeHasChanged = false;
                int pos = holder.ref;


                if (!onBind) {
                    if (!(s.equals(""))) {

                        int val = Integer.parseInt(s);
                        int stock = salesInvoice.get(pos).getStock();

                        if (val > stock) {
                            val = stock;
                            valHasChanged = true;

                        }
                        salesInvoice.get(pos).setOrder(val);//make sure we set returnQty before
                        //checking the total of returnQty and free against stock

                        if (salesInvoice.get(pos).getOrder() + salesInvoice.get(pos).getFree() > salesInvoice.get(pos).getStock()) {
                            salesInvoice.get(pos).setFree(0);
                            freeHasChanged = true;
                        }

                        //notifyItemChanged(position);

                    } else {
                        salesInvoice.get(pos).setOrder(0);
                    }

                    if (valHasChanged) {
                        notifyItemChanged(pos, ORDER);//notify the adapter that value changed and refresh the view mentioned by the string
                    }
                    if (freeHasChanged) notifyItemChanged(pos, FREE);
                    notifyItemChanged(pos, LINEVAL);

                    lastUpdatedRow = pos;

                }


            }
        });

        holder.free.setOnFocusChangeListener(new FocusChangeListener());
        holder.free.addTextChangedListener(new GenericTextWatcher() {
            @Override
            public void afterTextChanged(String s) {
                boolean valChanged = false;
                int pos = holder.ref;
                if (!onBind) {
                    if ((!s.equals("")) && (!(s.equals("0")))) {

                        int val = Integer.parseInt(s);
                        salesInvoice.get(pos).setFree(val);
                        holder.discount.setEnabled(false);

                        if (salesInvoice.get(pos).getOrder() + salesInvoice.get(pos).getFree() > salesInvoice.get(pos).getStock()) {
                            salesInvoice.get(pos).setFree(0);
                            valChanged = true;
                            holder.discount.setEnabled(true);
                        }
                        //initTable();
                        //notifyItemChanged(position);
                    } else {
                        salesInvoice.get(pos).setFree(0);

                        holder.discount.setEnabled(true);
                    }

                    notifyItemChanged(pos, LINEVAL);
                    if (valChanged) notifyItemChanged(pos, FREE);
                    holder.setCursor(FREE);

                    lastUpdatedRow = pos;

                }

            }
        });

        holder.discount.setOnFocusChangeListener(new FocusChangeListener());
        holder.discount.addTextChangedListener(new GenericTextWatcher() {
            @Override
            public void afterTextChanged(String s) {
                int pos = holder.ref;
                if (!(s.equals(salesInvoice.get(pos).getDiscountRate() + ""))) {
                    if ((!s.equals("")) && (!(s.equals("0"))) && (!(s.equals("0.0"))) && (!(s.equals("0.")))) {
                        Double rate = Double.parseDouble(s.toString().trim());
                        Log.i(" RAte ", rate + "");
                        salesInvoice.get(pos).setDiscountRate(rate);
                        //notifyItemChanged(position);
                        holder.free.setEnabled(false);


                    } else {
                        salesInvoice.get(pos).setDiscountRate(0.0);
                        holder.free.setEnabled(true);

                    }

                    if (!onBind) {
                        notifyItemChanged(pos, LINEVAL);

                        lastUpdatedRow = pos;

                    }
                }
            }
        });


        if (position % 2 == 0) {
            holder.setColor(Color.LTGRAY);
        } else {
            holder.setColor(Color.GRAY);
        }

        onBind = false;
        notifyUpdate();
        Log.d("ASY", "before db call");
        DBAdapterAsync dbAdapter = new DBAdapterAsync(getContextForAdapter);
        dbAdapter.execute(salesInvoice.get(holder.ref));
        Log.d("ASY", "after db call");

    }


    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return salesInvoice.size();
    }


    class GenericTextWatcher implements TextWatcher {

        public void afterTextChanged(String s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            afterTextChanged(s.toString());
        }
    }


//    public void customOnPause(){
//        context.unregisterReceiver(receiver);
//    }
//
//    public void customOnResume(){
////        context.registerReceiver(receiver,new IntentFilter("afterTextChanged"));
//    }


    class FocusChangeListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            EditText view = (EditText) v;

            Log.d(TAG, "inside FocusChangeListener onFocusChange");
            Log.d(TAG, "view ID_" + String.valueOf(v.getId()));
            if (!onBind) {
                Log.d(TAG, "inside if (!onBind)");
                if (hasFocus) {
                    Log.d(TAG, "inside hasFocus");
                    view.setBackgroundColor(Color.BLACK);
                    (view).setTextColor(Color.WHITE);
                    //added by shani
                    if (view.getText().toString().trim().equals("0")) {
                        view.setText("");
                    }
                    view.setSelection(view.getText().length());

                } else if (salesInvoice.size() > 0) {
                    Log.d(TAG, "inside (salesInvoice.size() > 0)");
                    v.setBackgroundColor(Color.TRANSPARENT);
                    (view).setTextColor(Color.BLACK);
                } else if (!hasFocus && view.getText() == null) {
                    Log.d(TAG, "inside (view.getText() == null)");
                    //added by shani
                    lostFocus = true;
                }
            }

        }
    }


    @SuppressLint("StaticFieldLeak")
    private class DBAdapterAsync extends AsyncTask<SalesInvoiceModel, Void, String> {

        private SQLiteDatabase db;
        private DBHelper dbHelper;

        DBAdapterAsync(Context context) {
            this.dbHelper = new DBHelper(context);
            Log.d("ASY", "inside constructor");

        }

        @Override
        protected void onPreExecute() {
            try {
                db = dbHelper.getWritableDatabase();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                dbHelper.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(SalesInvoiceModel... models) {

            SalesInvoiceModel model = models[0];

            Log.d(TAG, "inside updateInvoiceData_");
            Log.d(TAG, "shelf_" + model.getShelf());
            Log.d(TAG, "order_" + model.getOrder());
            Log.d(TAG, "free_" + model.getFree());

            String sql = "UPDATE temp_invoice SET" +
                    " Shelf=" + model.getShelf() + " , Request=" + model.getRequest()
                    + " , OrderQty=" + model.getOrder() + " , Free=" + model.getFree()
                    + " , Disc=" + model.getDiscountRate() + " , LineVal=" + model.getLineValue()
                    + ", RetailPriceLineVal=" + model.getRetailLineVal()
                    + " WHERE _id=" + model.getId();
            db.execSQL(sql);
            Log.d(TAG, "DB method finished,");
            Log.d("ASY", "after do in background");
            return null;
        }
    }

}
