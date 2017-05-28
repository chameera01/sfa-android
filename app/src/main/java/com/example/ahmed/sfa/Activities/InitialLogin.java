package com.example.ahmed.sfa.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;
import com.example.ahmed.sfa.models.DeviceCheckController;
import com.example.ahmed.sfa.service.JsonFilter_Send;
import com.example.ahmed.sfa.service.JsonHelper;
import com.example.ahmed.sfa.service.JsonObjGenerate;
import com.example.ahmed.sfa.service.JsonRequestListerner;
import com.example.ahmed.sfa.service.SyncReturn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class InitialLogin extends AppCompatActivity implements JsonRequestListerner, LoaderCallbacks<Cursor> {


    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mDeviceId;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private  TextView result_view;
    private Activity activity;
    Context contxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mDeviceId = (AutoCompleteTextView) findViewById(R.id.email);
        result_view=(TextView) findViewById(R.id.result_Json);
        populateAutoComplete();


        contxt=this;
        /*database tesst*/
        /*DBAdapter adp= new DBAdapter(this);
        adp.setMst_ProductBrandManagement();*/
        /*delete above code after test*/

        activity=this;

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
                Intent intent = new Intent(activity,AndroidDatabaseManager.class);
                //Intent intent = new Intent(activity, AndroidDatabaseManager.class);
                activity.startActivity(intent);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mDeviceId, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mDeviceId.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mDeviceId.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mDeviceId.setError(getString(R.string.error_field_required));
            focusView = mDeviceId;
            cancel = true;
        }/*
         *no need to chech email
         * else if (!isEmailValid(email)) {
            mDeviceId.setError(getString(R.string.error_invalid_email));
            focusView = mDeviceId;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);

            final String deviceId=email;
            final String pass=password;

            new JsonHelper.JsonDataCallback() {
                @Override
                public void receiveData(Object object) {
                    String tmpData = (String) object;
                    result_view.setText(tmpData);
                /*universal metho to filter Json Data from Json Array*/
                    ///filterType="deviceid_pass";

                /*end unit methos*/
                    try {
                        JSONArray jsonArray = new JSONArray(tmpData);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                    /*recieveData.setStatus(jsonObject.optString("ACTIVESTATUS"));
                    recieveData.setDevice_id(jsonObject.optString("DeviceID"));
                    recieveData.setPass(jsonObject.optString("Password"));*/
                        DeviceCheckController devCheck = new DeviceCheckController();
                        devCheck.setDevice_id(deviceId);
                        devCheck.setPass(password);
                        devCheck.setStatus(jsonObject.optString("ACTIVESTATUS"));

                        Toast.makeText(InitialLogin.this, jsonObject.toString() + "*", Toast.LENGTH_LONG).show();
                        result_view.setText(jsonObject.optString("ACTIVESTATUS"));

                        DBAdapter adp = new DBAdapter(InitialLogin.this);
                        adp.insertDeviceCheckController(devCheck);


                        if (jsonObject.optString("ACTIVESTATUS").equals("YES")) {
                            Toast.makeText(InitialLogin.this, "Activated", Toast.LENGTH_SHORT).show();
                            
                            repDetails_update();//download rep deatials table
                            supplet_tbl_update();//download suppler table
                            productBrand_tbl_update();//download brand table
                            customer_status_tbl_download();
                            distric_table_download();
                            territory_tbl_download();
                            route_tbl_download();
                            reason_tbl_download();
                            check_incheck_out_tbl_download();
                            customer_master_tbl_download();

                        } else {
                            Toast.makeText(InitialLogin.this, "Device ID or password is incorrect. Please check and tryagain", Toast.LENGTH_SHORT).show();
                        }
                        //setLonding(false);
                        //filterJsonData(tmpData,"deviceid_pass") ;

                        //getMstProductData("devideId","pass");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }.execute("http://www.bizmapexpert.com/api/DeviceCheck/DeviceCheckController?DeviceID=" + deviceId + "&Password=" + pass + "", null, null);



        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(InitialLogin.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mDeviceId.setAdapter(adapter);
    }

    @Override
    public void receiveData(String result, String filter) {
        //-----------------------------------------receive data
        Toast.makeText(this, "came inside recieve data", Toast.LENGTH_LONG).show();
        if(result!=null){
            String josnString=result;
            Toast.makeText(this, "result:" + josnString, Toast.LENGTH_LONG).show();
            try{
                JsonFilter_Send josnFilter= new JsonFilter_Send(InitialLogin.this.getApplicationContext());
                josnFilter.filterJsonData(josnString,filter);

            }catch (Exception e) {
                Toast.makeText(this,"RecieveData:"+ e.getMessage(),Toast.LENGTH_LONG ).show();
            }
        }else{
            Toast.makeText(this,"is nulllll",Toast.LENGTH_LONG ).show();
        }
        Toast.makeText(InitialLogin.this,"method_complete",Toast.LENGTH_LONG).show();
    }
    //-----------------------------------------------------------------------------------


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mDevideID;
        private final String mPassword;
        private DeviceCheckController initialDetails=null;

        UserLoginTask(String devid_id, String password) {
            mDevideID = devid_id;
            mPassword = password;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            // Simulate network access.
            //Thread.sleep(1000);
            //
            try {

                JsonHelper jh = new JsonHelper(contxt,result_view);
                initialDetails=jh.initialLoging(mDevideID, mPassword);


               /* DBAdapter adp=new DBAdapter(InitialLogin.this);
                adp.insertDeviceCheckController(initialDetails);*/


                //launch login UI if Status is active
               /*if(initialDetails.getStatus()=="YES"){
                    Intent ui=new Intent(InitialLogin.this,Login.class );
                    InitialLogin.this.startActivity(ui);

                }*/



            }catch (Exception e){
                Toast.makeText(activity, "err"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mDevideID)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            //if (initialDetails.getStatus().equals("YES")  ){

            return true;

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            Toast.makeText(InitialLogin.this, "initialDetails_Status:"+initialDetails.getStatus(), Toast.LENGTH_SHORT).show();
            if (success) {
               // finish();
                //result_view.setText(statusStrg+"working");
                //Toast.makeText(InitialLogin.class,"finished",Toast.LENGTH_LONG).show();

               /* product_update();//download product deatials table
                repDetails_update();//download rep deatials table
                supplet_tbl_update();//download suppler table
                productBrand_tbl_update();//download brand table
                customer_status_tbl_download();
                distric_table_download();
                territory_tbl_download();
                route_tbl_download();
                reason_tbl_download();
                check_incheck_out_tbl_download();
                customer_master_tbl_download();

                Intent next_ui= new Intent(InitialLogin.this,Login.class);
                InitialLogin.this.startActivity(next_ui);*/


            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private void customer_master_tbl_download() {
        try {
            JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/api/Mst_Customermaster/SelectMst_Customermaster?DeviceID=T1&RepID=93",InitialLogin.this);
            jObjGen.setFilterType("Customer");

            SyncReturn io = new SyncReturn();
            io.execute(jObjGen);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(InitialLogin.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
        }
    }

    private void check_incheck_out_tbl_download() {
        try {
            JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/api/Mst_CheckInOutPoints/SelectMst_CheckInOutPoints?DeviceID=T1&RepID=93",InitialLogin.this);
            jObjGen.setFilterType("CheckInOutPoints");

            SyncReturn io = new SyncReturn();
            io.execute(jObjGen);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(InitialLogin.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
        }
    }

    private void reason_tbl_download() {
        try {
            JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/api/Mst_Reasons/SelectMst_Reasons?DeviceID=T1&RepID=93",InitialLogin.this);
            jObjGen.setFilterType("Reason");

            SyncReturn io = new SyncReturn();
            io.execute(jObjGen);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(InitialLogin.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
        }
    }

    private void route_tbl_download() {
        try {
            JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/api/Mst_Route/SelectMst_Route?DeviceID=T1&RepID=93",InitialLogin.this);
            jObjGen.setFilterType("route");

            SyncReturn io = new SyncReturn();
            io.execute(jObjGen);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(InitialLogin.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
        }
    }

    private void territory_tbl_download() {
        try {
            JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/api/Mst_Territory/SelectMst_Territory?DeviceID=T1&RepID=93",InitialLogin.this);
            jObjGen.setFilterType("territory");

            SyncReturn io = new SyncReturn();
            io.execute(jObjGen);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(InitialLogin.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
        }
    }

    private void distric_table_download() {
        try {
            JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/api/Mst_District/SelectMst_District?DeviceID=T1&RepID=93",InitialLogin.this);
            jObjGen.setFilterType("district");

            SyncReturn io = new SyncReturn();
            io.execute(jObjGen);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(InitialLogin.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
        }
    }

    private void customer_status_tbl_download() {

        try{
            JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/api/Mst_CustomerStatus/SelectMst_CustomerStatus?DeviceID=T1&RepID=93",InitialLogin.this);
            jObjGen.setFilterType("CustomerStatus");

            SyncReturn io = new SyncReturn();
            io.execute(jObjGen);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(InitialLogin.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
        }
    }

    //    public void runJsonCLass(){
//        try {
//            JsonHelper jh = new JsonHelper(result_view);
//            jh.sendInitialData("deviceid", "password");
//        }catch (Exception e){
//            Toast.makeText(this,"Error_found:"+e.getMessage(),Toast.LENGTH_LONG).show();
//        }
//
//    }
    //initial update of data tables;
    public void product_update(){
        try {
            JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/api/ProductDetails/SelectProductDetails?DeviceID=T1&RepID=93",InitialLogin.this);
            jObjGen.setFilterType("ProductDetails");
            SyncReturn io = new SyncReturn();
            io.execute(jObjGen);

        }catch (Exception e){
            e.printStackTrace();
            //Toast.makeText(InitialLogin.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
        }
    }
    public  void repDetails_update(){
        try {
            JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/api/GetRepDetails/SelectGetRepDetails?DeviceID=T1&RepID=93",InitialLogin.this);
            jObjGen.setFilterType("RepDetails");

            SyncReturn io = new SyncReturn();
            io.execute(jObjGen);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(InitialLogin.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
        }
    }
    public  void supplet_tbl_update(){
        try {
            JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/api/Mst_SupplierTable/SelectProductMst_SupplierTable?DeviceID=T1&RepID=93",InitialLogin.this);
            jObjGen.setFilterType("SupplierTable");

            SyncReturn io = new SyncReturn();
            io.execute(jObjGen);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(InitialLogin.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
        }
    }
    public  void productBrand_tbl_update(){
        try {
            JsonObjGenerate jObjGen = new JsonObjGenerate("http://www.bizmapexpert.com/api/ProductBrandManagement/SelectProductBrandManagement?DeviceID=T1&RepID=93",InitialLogin.this);
            jObjGen.setFilterType("ProductBrandManagement");

            SyncReturn io = new SyncReturn();
            io.execute(jObjGen);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(InitialLogin.this,"clck.ExceptionCalled",Toast.LENGTH_LONG).show();
        }
    }
}

