package com.azocar.cesar.androidnodejsauth;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.azocar.cesar.androidnodejsauth.Retrofit.INodeJS;
import com.azocar.cesar.androidnodejsauth.Retrofit.RetrofitClient;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.button.MaterialButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    MaterialEditText edt_email, edt_password;
    MaterialButton btn_register, btn_login;

    @Override
    protected void onStop() {

        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init API
        Retrofit retrofit =RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        //View
        btn_login=(MaterialButton)findViewById(R.id.logBtn);
        btn_register=(MaterialButton)findViewById(R.id.regBtn);

        edt_email = (MaterialEditText)findViewById(R.id.edt_email);
        edt_password = (MaterialEditText)findViewById(R.id.edt_password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(edt_email.getText().toString(),edt_password.getText().toString());
            }

        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser(edt_email.getText().toString(),edt_password.getText().toString());
            }
        });


    }

    private void registerUser(final String email, final String password) {


        final View enter_name_view = LayoutInflater.from(this).inflate(R.layout.enter_name_layout,null);
        new MaterialStyledDialog.Builder(this)
                .setTitle("Register")
                .setDescription("One more step")
                .setCustomView(enter_name_view)
                .setIcon(R.drawable.ic_user)
                .setNegativeText("Cancel")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveText("Register")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        MaterialEditText edt_name = (MaterialEditText)enter_name_view.findViewById(R.id.edt_name);

                        compositeDisposable.add(myAPI.registerUser(email,edt_name.getText().toString(), password)

                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Toast.makeText(MainActivity.this, ""+s, Toast.LENGTH_SHORT).show();
                            }
                        }));
                    }
                }).show();

        compositeDisposable.add(myAPI.loginUser(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (s.contains("encrypted_password"))
                            Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(MainActivity.this, "" + s, Toast.LENGTH_SHORT).show();
                    }
                })

        );

    }

    private void loginUser(String email, String password) {
        compositeDisposable.add(myAPI.loginUser(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (s.contains("encrypted_password"))
                            Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(MainActivity.this, "" + s, Toast.LENGTH_SHORT).show();
                    }
                })

        );

    }


}
