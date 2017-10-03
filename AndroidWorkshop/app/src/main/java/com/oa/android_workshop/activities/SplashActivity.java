package com.oa.android_workshop.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.oa.android_workshop.R;
import com.oa.android_workshop.application.WorkshopApplication;
import com.oa.android_workshop.models.Company;
import com.oa.android_workshop.network.ServiceHandler;
import com.overactiveinc.oarestapi.network.RestApiCallback;

import java.util.List;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ServiceHandler serviceHandler = new ServiceHandler(this);
        serviceHandler.doGetCompanies(new RestApiCallback<List<Company>>() {
            @Override
            public void onSuccess(List<Company> object) {
                WorkshopApplication.setmCompanies(object);
                SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                String Username = settings.getString("Username","");
                String Pass = settings.getString("Password","");

                //Validamos que se obtengan el usuario y contraseña desde las Sharedpreferences
                if(Username  != null && Pass != null){
                    //Toast.makeText(SplashActivity.this, "Message is: " + Username.toString(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(SplashActivity.this, "Message is: " + Pass.toString(), Toast.LENGTH_SHORT).show();
                    //Ahora llamamos a la API para realizar la autenticación
                    ServiceHandler serviceHandler = new ServiceHandler(SplashActivity.this);
                    serviceHandler.doPostLogin(Username, Pass,
                            new RestApiCallback<String>() {
                                @Override
                                public void onSuccess(String object) {
                                    //Si la autenticacion contra la api da OK, saltamos la pantalla de Login y vamos a la sigueinte pantalla
                                    Intent auxIntent = new Intent(SplashActivity.this, MainActivity.class);
                                    startActivity(auxIntent);
                                    finish();
                                    //vProgressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError(String message) {
                                    //vProgressBar.setVisibility(View.GONE);
                                    Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    //Si la autenticación contra la API falla, enviamos a la pantalla de login por mas que este recordado el usuario y contraseña
                    //Toast.makeText(SplashActivity.this, "Message: NO HICE NADA " , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });


    }
}
