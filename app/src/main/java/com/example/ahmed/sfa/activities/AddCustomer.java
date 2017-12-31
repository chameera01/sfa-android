package com.example.ahmed.sfa.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.activities.Dialogs.Alert;
import com.example.ahmed.sfa.controllers.DateManager;
import com.example.ahmed.sfa.controllers.ImageManager;
import com.example.ahmed.sfa.controllers.PermissionManager;
import com.example.ahmed.sfa.controllers.RandomNumberGenerator;
import com.example.ahmed.sfa.controllers.adapters.NavigationDrawerMenuManager;
import com.example.ahmed.sfa.controllers.database.DBHelper;
import com.example.ahmed.sfa.models.CustomerStatus;
import com.example.ahmed.sfa.models.District;
import com.example.ahmed.sfa.models.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 3/4/2017.
 */

public class AddCustomer extends AppCompatActivity {
    private Alert alert;

    ImageManager imgMan;
    DBAdapter dbAdapter;

    Bitmap customerImageBitmap;

    ImageView customerImage;
    TextView customerName;
    TextView address;
    Spinner district;
    TextView area;
    TextView town;
    Spinner route;
    Spinner customerStatus;
    TextView ContactNo;
    TextView fax;
    TextView email;
    TextView brNo;
    TextView ownersName;
    TextView ownersContactNo;
    TextView registrationNo;
    TextView latitude;
    TextView longitude;
    Button saveandsubmitbtn;
    Location lastKnownLocation;
    Button dismiss;

    private boolean controlsEnabled=false;

    private Bundle savedInstance;

    private void enableControls(){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        controlsEnabled = true;

        customerName.setEnabled(true);
        address.setEnabled(true);
        district.setEnabled(true);
        area.setEnabled(true);
        town.setEnabled(true);
        route.setEnabled(true);
        customerStatus.setEnabled(true);
        ContactNo.setEnabled(true);
        fax.setEnabled(true);
        email.setEnabled(true);
        ownersName.setEnabled(true);
        brNo.setEnabled(true);
        ownersContactNo.setEnabled(true);
        registrationNo.setEnabled(true);

    }

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        alert = new Alert(this);
        savedInstance = savedInstanceState;
        init();
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationDrawerMenuManager(this);
        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);

        dismiss = (Button) findViewById(R.id.addcustomer_dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddCustomer.this, "Test Dismis button", Toast.LENGTH_LONG).show();
                customerName.setText("");
                customerName.setEnabled(false);
                address.setText("");
                address.setEnabled(false);

                //address.setText("");
                area.setText("");
                area.setEnabled(false);
                town.setText("");
                town.setEnabled(false);
                ContactNo.setText("");
                ContactNo.setEnabled(false);
                fax.setText("");
                fax.setEnabled(false);
                email.setText("");
                email.setEnabled(false);
                brNo.setText("");
                brNo.setEnabled(false);
                ownersName.setText("");
                ownersName.setEnabled(false);
                ownersContactNo.setText("");
                ownersContactNo.setEnabled(false);
                registrationNo.setText("");
                registrationNo.setEnabled(false);
                latitude.setText("");
                latitude.setEnabled(false);
                longitude.setText("");
                longitude.setEnabled(false);

                customerImage.setImageBitmap(null);
                route.setEnabled(false);
                customerStatus.setEnabled(false);
                district.setEnabled(false);
                controlsEnabled=false;

                init();


            }
        });
        /*district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });*/
    }


    private void init(){
        //get Location
        PermissionManager pm = new PermissionManager(this);
        if(pm.checkForLocationPermission()) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            List<String> providers = locationManager.getProviders(true);
            for(String provider : providers){
                Location tempLoc = locationManager.getLastKnownLocation(provider);
                if(tempLoc==null) continue;
                if(lastKnownLocation == null || tempLoc.getAccuracy()>lastKnownLocation.getAccuracy()){
                    lastKnownLocation = tempLoc;
                }
            }
            //lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            /* -----------------------------------------------------------------------------------------------*/
            lastKnownLocation = new Location(LocationManager.GPS_PROVIDER);//this line has to be removed
            /* -----------------------------------------------------------------------------------------------*/
            //this has been added since emmulator failed to send gps points

            if(lastKnownLocation!=null){

                setContentView(R.layout.addcustomerlayout);
                imgMan = new ImageManager(this);
                dbAdapter = new DBAdapter(this);

                customerImage = (ImageView) findViewById(R.id.addcustomer_customerpic);
                customerImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imgMan.capture();
                    }
                });


                //assign controls
                customerName = (TextView) findViewById(R.id.addcustomer_customername);
                address = (TextView) findViewById(R.id.addcustomer_address);
                district = (Spinner) findViewById(R.id.addcustomer_district);
                area = (TextView) findViewById(R.id.addcustomer_area);
                town = (TextView) findViewById(R.id.addcustomer_town);
                route = (Spinner) findViewById(R.id.addcustomer_route);
                customerStatus = (Spinner) findViewById(R.id.addcustomer_customerstatus);
                ContactNo = (TextView) findViewById(R.id.addcustomer_contactno);
                fax = (TextView) findViewById(R.id.addcustomer_fax);
                email = (TextView) findViewById(R.id.addcustomer_email);
                brNo = (TextView) findViewById(R.id.addcustomer_brno);
                ownersName = (TextView) findViewById(R.id.addcustomer_ownersname);
                ownersContactNo = (TextView) findViewById(R.id.addcustomer_ownerscontactno);
                registrationNo = (TextView) findViewById(R.id.addcustomer_regno);
                latitude = (TextView) findViewById(R.id.addcustomer_latitude);
                longitude = (TextView) findViewById(R.id.addcustomer_longitude);

                List<District> districts =dbAdapter.getDistricts();
                /*newly added by Asanka*/
                districts.add(new District("0","Select District"));
                int spinnerPosition =districts.size()-1;/* end of new added*/

                ArrayAdapter<District> districtArrayAdapter = new ArrayAdapter<District>(this,android.R.layout.simple_spinner_item,districts);

                districtArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                district.setAdapter(districtArrayAdapter);
                district.setEnabled(false);
                //set the default according to value
                district.setSelection(spinnerPosition);

                List<CustomerStatus> customerStatuses = dbAdapter.getCustomerStatus();
                /*newly added by Asanka*/
                customerStatuses.add(new CustomerStatus("0","Select Status"));
                int statusSpinnerPosition =customerStatuses.size()-1;/* end of new added*/
                ArrayAdapter<CustomerStatus> customerStatusArrayAdapter = new ArrayAdapter<CustomerStatus>(this,android.R.layout.simple_spinner_item,customerStatuses);

                customerStatusArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                customerStatus.setAdapter(customerStatusArrayAdapter);
                customerStatus.setEnabled(false);
                //set the default according to value
                customerStatus.setSelection(statusSpinnerPosition);


                List<Route> routes = dbAdapter.getRoutes();
                /*newly added by Asanka*/
                routes.add(new Route("0","Select Route"));
                int routeSpinnerPosition =routes.size()-1;/* end of new added*/
                ArrayAdapter<Route> routeArrayAdapter = new ArrayAdapter<Route>(this,android.R.layout.simple_spinner_item,routes);

                routeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                route.setAdapter(routeArrayAdapter);
                //set default selection
                route.setSelection(routeSpinnerPosition);
                route.setEnabled(false);


                latitude.setText(lastKnownLocation.getLatitude()+"");
                longitude.setText(lastKnownLocation.getLongitude()+"");
                saveandsubmitbtn = (Button) findViewById(R.id.addcustomer_submitandsend);

                final DialogInterface.OnClickListener confirmedAddingListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(addDataToDB()) Toast.makeText(AddCustomer.this,"Customer Added",Toast.LENGTH_SHORT).show();
                        saveandsubmitbtn.setEnabled(false);
                    }
                };

                final DialogInterface.OnClickListener dismissAddingListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveandsubmitbtn.setEnabled(true);
                    }
                };

                saveandsubmitbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //validate the input if valid proceed otherwise return error message
                        if (validateInputs()) {
                            alert.showAlert("Confirm", "Are you sure?", "Yes", "No", confirmedAddingListener, dismissAddingListener);
                        }
                        else
                            alert.showAlert("Input Invalid","Please check your inputs and try again!","OK","Back",null,null);

                    }
                });

            }else{
                Toast.makeText(this,"Location unavailable",Toast.LENGTH_SHORT).show();
                setContentView(R.layout.location_not_found_error_layout);
            }

        }else{
            Toast.makeText(this,"Permission Unavailable",Toast.LENGTH_SHORT).show();
            setContentView(R.layout.location_not_found_error_layout);

        }
    }




    private boolean addDataToDB(){
        RandomNumberGenerator rg = new RandomNumberGenerator();
        String imageCode = rg.generateRandomCode(RandomNumberGenerator.GENERATE_ALPHABANUMERIC,"CUSIMG_",17);
        String customerID = rg.generateRandomCode(RandomNumberGenerator.GENERATE_ALPHABANUMERIC,"CUS_",14);
        ContentValues cvFrTrCustomer = new ContentValues();
        cvFrTrCustomer.put("NewCustomerID", customerID);
        cvFrTrCustomer.put("CustomerName", customerName.getText().toString());
        cvFrTrCustomer.put("Address", address.getText().toString());
        cvFrTrCustomer.put("Area", area.getText().toString());
        cvFrTrCustomer.put("District", district.getSelectedItem().toString());
        cvFrTrCustomer.put("Town", town.getText().toString());
        cvFrTrCustomer.put("Telephone", ContactNo.getText().toString());
        cvFrTrCustomer.put("Fax", fax.getText().toString());
        cvFrTrCustomer.put("Email", email.getText().toString());
        cvFrTrCustomer.put("OwnerContactNo", ownersContactNo.getText().toString());
        cvFrTrCustomer.put("OwnerName", ownersName.getText().toString());
        cvFrTrCustomer.put("PharmacyRegNo", registrationNo.getText().toString());
        cvFrTrCustomer.put("CustomerStatusID", ((CustomerStatus) customerStatus.getSelectedItem()).getCustomerStatusID());
        cvFrTrCustomer.put("CustomerStatus", ((CustomerStatus) customerStatus.getSelectedItem()).getCustomerStatus());
        cvFrTrCustomer.put("InsertDate", DateManager.dateToday());
        cvFrTrCustomer.put("RouteID", ((Route) route.getSelectedItem()).getRouteID());
        cvFrTrCustomer.put("RouteName", ((Route) route.getSelectedItem()).getRouteID());
        cvFrTrCustomer.put("ImageID",imageCode);
        cvFrTrCustomer.put("Latitude", Double.parseDouble(latitude.getText().toString()));
        cvFrTrCustomer.put("Longitude", Double.parseDouble(longitude.getText().toString()));
        cvFrTrCustomer.put("isUpload", 0);
        cvFrTrCustomer.put("UploadDate", DateManager.dateToday());
        cvFrTrCustomer.put("ApproveStatus", 1);
        cvFrTrCustomer.put("LastUpdateDate", DateManager.dateToday());

        if(imgMan.saveImage(customerImageBitmap,imageCode) && dbAdapter.insertIntoCustomerImage(imageCode,customerID)){
            cvFrTrCustomer.put("ImageID",imageCode);
            dbAdapter.insertIntoTrCustomer(cvFrTrCustomer);
            Toast.makeText(getApplicationContext(),"Customer :"+customerID+" ImageID "+imageCode,Toast.LENGTH_LONG).show();
            return true;

        }else{
            return false;
        }


    }

    boolean validateInputs(){
        boolean result = true;

        if(!(checkTextView(customerName) && checkTextView(address) && checkTextView(area) && checkTextView(town) && checkTextView(ContactNo))){
            result = false;
        }else{
            return true;
        }

        return result;
    }

    private boolean checkTextView(TextView test){
        if(test.getText().length()==0 || test==null){
            return false;
        }
        return true;
    }




    //this method takes the image captured and initializes it to the instance variable
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if (requestCode == ImageManager.CAMERA_REQUEST) {
            Toast.makeText(getApplicationContext(),"Captured",Toast.LENGTH_SHORT).show();
            if(resultCode==Activity.RESULT_OK) {
                customerImageBitmap = (Bitmap)data.getExtras().get("data");
                setCustomerImage(customerImageBitmap);
            }
        } else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setCustomerImage(Bitmap customerImage) {
        if(customerImage!=null) {
            this.customerImage.setImageBitmap(customerImage);
            if(!controlsEnabled){
                enableControls();
            }
        }
    }

    //this method handles the permission updates
    //camera and location
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults){
        //Toast.makeText(this," permission Received",Toast.LENGTH_SHORT).show();
        switch (requestCode){
            case PermissionManager.MY_PERMISSIONS_REQUEST_LOCATION:
                //Toast.makeText(this," Location",Toast.LENGTH_SHORT).show();
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    init();
                    //Toast.makeText(this,"Location permission Received",Toast.LENGTH_SHORT).show();
                }
                break;
            case PermissionManager.MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imgMan.capture();
                }
                break;
        }
    }

    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
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
    }

    class DBAdapter{
        Context context;
        DBHelper dbHelper;
        SQLiteDatabase db;

        public DBAdapter(Context context){
            this.context = context;
            dbHelper = new DBHelper(context);
        }

        private void openDB(){
            try {
                db = dbHelper.getWritableDatabase();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        private void closeDB(){
            try{
                dbHelper.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        public List<District> getDistricts(){
            ArrayList<District> districts = new ArrayList<>();
            openDB();
            Cursor cursor;
            cursor = db.rawQuery("SELECT DistrictId,DistrictName FROM Mst_District ORDER BY DistrictName asc ;",null);
            while (cursor.moveToNext()){
                District district = new District(cursor.getString(0),cursor.getString(1));
                districts.add(district);
            }
            closeDB();
            return districts;
        }

        public List<CustomerStatus> getCustomerStatus(){
            ArrayList<CustomerStatus> customerStatuses = new ArrayList<>();
            openDB();
            Cursor cursor;
            cursor = db.rawQuery("SELECT StatusID,Status FROM Mst_CustomerStatus ORDER BY Status asc ;",null);
            while (cursor.moveToNext()){
                CustomerStatus cs = new CustomerStatus(cursor.getString(0),cursor.getString(1));
                customerStatuses.add(cs);
            }
            closeDB();
            return customerStatuses;
        }

        public List<Route> getRoutes(){
            ArrayList<Route> routes = new ArrayList<>();
            openDB();
            Cursor cursor;
            cursor = db.rawQuery("SELECT RouteID,Route FROM Mst_Route ORDER BY Route asc ;",null);
            while (cursor.moveToNext()){
                Route r = new Route(cursor.getString(0),cursor.getString(1));
                routes.add(r);
            }
            closeDB();
            return routes;
        }


        public boolean insertIntoTrCustomer(ContentValues cv){
            openDB();
            long tr_newCustomer = db.insert("Tr_NewCustomer", null, cv);
            if(tr_newCustomer>0)
                return true;
            else
                return false;
        }

        public boolean insertIntoCustomerImage(String imageCode,String customerID){
            openDB();
            db.execSQL("INSERT INTO Customer_Images(CustomerNo,CustomerImageName) VALUES ('"+customerID+"','"+imageCode+"')");
            closeDB();
            return true;
        }
    }

}
