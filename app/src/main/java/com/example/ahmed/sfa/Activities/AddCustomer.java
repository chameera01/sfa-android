package com.example.ahmed.sfa.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

    private boolean controlsEnabled=false;

    private Bundle savedInstance;

    private void enableControls(){

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
        //nvMan = new NavigationItemManager(this);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            //    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        //toggle.syncState();

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(nvMan);


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
            lastKnownLocation = new Location(LocationManager.GPS_PROVIDER);//this line has to be removed
            //this has been added since emmulator failed to send gps points

            if(lastKnownLocation!=null){

                setContentView(R.layout.addcustomerlayout);
                imgMan = new ImageManager(this);
                dbAdapter = new DBAdapter(this);

                customerImage =(ImageView)findViewById(R.id.addcustomer$customerpic);
                customerImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imgMan.capture();
                    }
                });


                //assign controls
                customerName = (TextView)findViewById(R.id.addcustomer$customername);
                address=(TextView)findViewById(R.id.addcustomer$address);
                district =(Spinner)findViewById(R.id.addcustomer$district);
                area=(TextView)findViewById(R.id.addcustomer$area);
                town=(TextView)findViewById(R.id.addcustomer$town);
                route=(Spinner) findViewById(R.id.addcustomer$route);
                customerStatus=(Spinner) findViewById(R.id.addcustomer$customerstatus);
                ContactNo=(TextView)findViewById(R.id.addcustomer$contactno);
                fax=(TextView)findViewById(R.id.addcustomer$fax);
                email=(TextView)findViewById(R.id.addcustomer$email);
                brNo=(TextView)findViewById(R.id.addcustomer$brno);
                ownersName=(TextView)findViewById(R.id.addcustomer$ownersname);
                ownersContactNo=(TextView)findViewById(R.id.addcustomer$ownerscontactno);
                registrationNo=(TextView)findViewById(R.id.addcustomer$regno);
                latitude=(TextView)findViewById(R.id.addcustomer$latitude);
                longitude=(TextView)findViewById(R.id.addcustomer$longitude);

                List<District> districts = dbAdapter.getDistricts();
                ArrayAdapter<District> districtArrayAdapter = new ArrayAdapter<District>(this,android.R.layout.simple_spinner_item,districts);

                districtArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                district.setAdapter(districtArrayAdapter);
                district.setEnabled(false);

                List<CustomerStatus> customerStatuses = dbAdapter.getCustomerStatus();
                ArrayAdapter<CustomerStatus> customerStatusArrayAdapter = new ArrayAdapter<CustomerStatus>(this,android.R.layout.simple_spinner_item,customerStatuses);

                customerStatusArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                customerStatus.setAdapter(customerStatusArrayAdapter);
                customerStatus.setEnabled(false);


                List<Route> routes = dbAdapter.getRoutes();
                ArrayAdapter<Route> routeArrayAdapter = new ArrayAdapter<Route>(this,android.R.layout.simple_spinner_item,routes);

                routeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                route.setAdapter(routeArrayAdapter);
                route.setEnabled(false);

                latitude.setText(lastKnownLocation.getLatitude()+"");
                longitude.setText(lastKnownLocation.getLongitude()+"");
                saveandsubmitbtn = (Button)findViewById(R.id.addcustomer$submitandsend);

                final DialogInterface.OnClickListener confirmedAddingListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(addDataToDB()) Toast.makeText(AddCustomer.this,"Customer Added",Toast.LENGTH_SHORT).show();
                    }
                };

                saveandsubmitbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //validate the input if valid proceed otherwise return error message
                        if(validateInputs())
                            showAlert("Confirm","Are you sure to save this customer","Yes","No",confirmedAddingListener,null);
                        else
                            showAlert("Input Invalid","Please check your inputs and try again!","OK","Back",null,null);

                    }
                });

            }else{
                Toast.makeText(this,"Here 0",Toast.LENGTH_SHORT).show();
                setContentView(R.layout.addcustomer_errorlayout);
            }

        }else{
            Toast.makeText(this,"Here 1",Toast.LENGTH_SHORT).show();
            setContentView(R.layout.addcustomer_errorlayout);

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
        cvFrTrCustomer.put("isUpload", 1);
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

    void showAlert(String title , String message, String positiveButtonCaption, String negativeButtonCaption,
                   DialogInterface.OnClickListener positiveButtonClickListener,DialogInterface.OnClickListener negativeButtonClickListener) {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        DialogInterface.OnClickListener nulllistener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        };

        if(positiveButtonClickListener==null)positiveButtonClickListener = nulllistener;
        if(negativeButtonClickListener==null)negativeButtonClickListener = nulllistener;

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,negativeButtonCaption,negativeButtonClickListener);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,positiveButtonCaption,positiveButtonClickListener);
        alertDialog.show();
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
            super.onBackPressed();
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
            cursor = db.rawQuery("SELECT DistrictId,DistrictName FROM Mst_District;",null);
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
            cursor = db.rawQuery("SELECT StatusID,Status FROM Mst_CustomerStatus;",null);
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
            cursor = db.rawQuery("SELECT RouteID,Route FROM Mst_Route;",null);
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
