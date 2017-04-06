package com.example.ahmed.sfa.Activities;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ahmed.sfa.R;
import com.example.ahmed.sfa.controllers.adapters.DBAdapter;
import com.example.ahmed.sfa.controllers.database.DBHelper;
import com.example.ahmed.sfa.service.JsonObjGenerate;
import com.example.ahmed.sfa.service.JsonRequestListerner;
import com.example.ahmed.sfa.service.SyncReturn;

public class Login extends AppCompatActivity implements JsonRequestListerner {
    AppCompatButton logBtn;
    EditText pass;
    EditText repID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_login);

        logBtn=(AppCompatButton) findViewById(R.id.btn_login);

        pass=(EditText) findViewById(R.id.input_password);
        repID=(EditText) findViewById(R.id.input_repID);
    }


    private void setListeners(){
        final String passWord=pass.getText().toString();
        String rep_id=repID.getText().toString();

        /*loginbutton listener*/
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /**/
                DBHelper db= new DBHelper(Login.this);
                Cursor res=db.getData("select * from DeviceCheckController where Password ='"+passWord+"'");

                if(res!=null) {
                    while (res.moveToNext()) {
                        res.getString(res.getColumnIndex("Password"));
                    }
                    Toast.makeText(Login.this,"not null cursor",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Login.this,"null cursor",Toast.LENGTH_LONG).show();
                }


            }
        });
    }



    private  void checkLogin(){



        }

    @Override
    public void receiveData(String result,String filter) {

    }
}
