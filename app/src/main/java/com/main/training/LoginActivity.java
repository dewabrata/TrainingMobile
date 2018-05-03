package com.main.training;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.main.training.ServiceModel.Authentication.Authentication;
import com.main.training.services.APIClient;
import com.main.training.services.APIInterfacesRest;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 111){

            if(resultCode== 69){
                Toast.makeText(LoginActivity.this,data.getStringExtra("data"),Toast.LENGTH_LONG).show();
            }else if (resultCode ==999){
                Toast.makeText(LoginActivity.this,data.getStringExtra("data"),Toast.LENGTH_LONG).show();
            }

        }
    }

    EditText txtUsername, txtPassword;
    Button btnSubmit, btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        txtUsername = (EditText)findViewById(R.id.txtUsername);
        txtPassword = (EditText)findViewById(R.id.txtPass);

        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

     /*   btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this,"Hello World", Toast.LENGTH_LONG).show();
            }
        });
*/



    }

    @Override
    public void onClick(View view) {

        if (view == btnSubmit){
            //Toast.makeText(LoginActivity.this,"dari Submit",Toast.LENGTH_LONG).show();
      /*      Intent intent = new Intent(LoginActivity.this,WelcomeScreen.class);
            intent.putExtra("username",txtUsername.getText().toString());
            startActivityForResult(intent,111);
            */
      callAuthentication(txtUsername.getText().toString(),txtPassword.getText().toString());

        }else if ((view == btnCancel)){
            Intent intent = new Intent(LoginActivity.this,DetailScreen.class);
            intent.putExtra("username",txtUsername.getText().toString());
            startActivityForResult(intent,111);
        }


    }

    ProgressDialog progressDialog;
    APIInterfacesRest apiInterface;
    public void callAuthentication(final String username, String password){

        apiInterface = APIClient.getClient().create(APIInterfacesRest.class);
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();
        Call<Authentication> call3 = apiInterface.getAuthentication(username,password);
        call3.enqueue(new Callback<Authentication>() {
            @Override
            public void onResponse(Call<Authentication> call, Response<Authentication> response) {
                progressDialog.dismiss();
                Authentication userList = response.body();
                //Toast.makeText(LoginActivity.this,userList.getToken().toString(),Toast.LENGTH_LONG).show();
                if (userList !=null) {

                    Intent intent = new Intent(LoginActivity.this,WelcomeScreen.class);
                    intent.putExtra("username",txtUsername.getText().toString());
                    startActivity(intent);
                }else{

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(LoginActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<Authentication> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Maaf koneksi bermasalah",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });




    }


}
