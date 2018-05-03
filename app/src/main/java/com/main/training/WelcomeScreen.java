package com.main.training;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.main.training.NewOrder.Dataorder;
import com.main.training.NewOrder.ModelOrder;
import com.main.training.adapter.AdapterListBasicOrder;
import com.main.training.application.AppController;
import com.main.training.services.APIClient;
import com.main.training.services.APIInterfacesRest;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.queriable.StringQuery;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeScreen extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterListBasicOrder mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        setTitle("List Order");

        final String username = getIntent().getStringExtra("username");

        initComponent();

        getDataOrder(username);

    }

    public List<Dataorder> orderItems;

    public void getDataOrder(String username){
        APIInterfacesRest apiInterface = APIClient.getClient().create(APIInterfacesRest.class);
        Call<ModelOrder> call3;

        call3 = apiInterface.getOrder(username);

        call3.enqueue(new Callback<ModelOrder>() {
            @Override
            public void onResponse(Call<ModelOrder> call, Response<ModelOrder> response) {
                ModelOrder order = response.body();



                if(response.isSuccessful()) {
                    if (response.body()!=null) {
                        //  Toast.makeText(LoginActivity.this,userList.getToken().toString(),Toast.LENGTH_LONG).show();
                        orderItems = (ArrayList<Dataorder>) order.getData().getDataorder();

                        setupAdapterList(orderItems);
                    //    savedb();


                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            ArrayList<Dataorder> orderLists;
            @Override
            public void onFailure(Call<ModelOrder> call, Throwable t) {

                Toast.makeText(getApplicationContext(),"Terjadi gangguan koneksi",Toast.LENGTH_LONG).show();
                call.cancel();









            }

        });

    }


    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

   /*     List<Dataorder> items = DataGenerator.getPeopleData(this);
        items.addAll(DataGenerator.getPeopleData(this));
        items.addAll(DataGenerator.getPeopleData(this));*/

        //set data and list adapter


    }


    public void savedb(){

        FlowManager.getDatabase(AppController.class)
                .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<Dataorder>() {
                            @Override
                            public void processModel(Dataorder orderItem, DatabaseWrapper wrapper) {

                                orderItem.save();


                            }

                        }).addAll(orderItems).build())  // add elements (can also handle multiple)
                .error(new Transaction.Error() {
                    @Override
                    public void onError(Transaction transaction, Throwable error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(Transaction transaction) {
                        Toast.makeText(getApplicationContext(),"Data Tersimpan",Toast.LENGTH_LONG).show();

                    }
                }).build().execute();


    }



    public void setupAdapterList(List<Dataorder>items){


        mAdapter = new AdapterListBasicOrder(this, items);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked



    }




}
