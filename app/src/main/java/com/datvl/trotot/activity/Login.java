package com.datvl.trotot.activity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.datvl.trotot.MainActivity;
import com.datvl.trotot.OnEventListener;
import com.datvl.trotot.R;
import com.datvl.trotot.api.GetApi;
import com.datvl.trotot.common.Common;
import com.datvl.trotot.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class Login extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Post> listPost;
    Common cm;
    public String url = cm.getUrlLogin();
    ProgressBar pb,pb_login;
    EditText edt_username, edt_password;
    Button btn_login, btn_regis;
    TextView txt_alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_username = findViewById(R.id.user_name_login);
        edt_password = findViewById(R.id.password_login);
        btn_login = findViewById(R.id.btn_login);
        btn_regis = findViewById(R.id.btn_regis);
        txt_alert = findViewById(R.id.mess_login);
        pb_login = findViewById(R.id.progress_bar_login);
        pb_login.setVisibility(View.GONE);

        final SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = String.valueOf(edt_username.getText()).equals("")  ?  "user" : String.valueOf(edt_username.getText());
                String password = String.valueOf(edt_password.getText()).equals("")  ? "pass" : String.valueOf(edt_password.getText());
                pb_login.setVisibility(View.VISIBLE);

                editor.putString("username", user_name);
                editor.putString("password", password);

                Log.d("OK", "luu login: " + (Boolean) sharedPreferences.getBoolean("is_login", false));

                GetApi getApi = new GetApi(url + user_name + "/" + password,getApplication(), new OnEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(JSONArray o) {
                        try {
                            JSONObject jsonObject = o.getJSONObject(0);

                            if (jsonObject.getString("is_login").equals("ok")) {
                                String avatar = jsonObject.getString("avatar");
                                String name = jsonObject.getString("name");
                                String user_id = jsonObject.getString("id");
                                editor.putString("user_id", user_id);

                                editor.putString("username", name);
                                txt_alert.setVisibility(View.VISIBLE);
                                txt_alert.setText("Dang nhap thanh cong");
                                pb_login.setVisibility(View.GONE);
                                editor.putBoolean("is_login", true);
                                editor.commit();
                                cm.showToast(getApplication(), "Chào mừng " + name + " quay trở lại", Toast.LENGTH_SHORT);
                                //redirect to MainActiviti
                                final Intent intent= new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);

                            }
                            else {
                                Log.d("not ok", "user");
                                txt_alert.setVisibility(View.VISIBLE);
                                txt_alert.setText("Sai Username hoặc password");
                                pb_login.setVisibility(View.GONE);
                                editor.putBoolean("is_login", false);
                                editor.commit();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                    }
                });
            }
        });

        btn_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplication(), RegisActivity.class);
//                startActivity(intent);
                String user_name = String.valueOf(edt_username.getText()).equals("")  ?  "user" : String.valueOf(edt_username.getText());
                String password = String.valueOf(edt_password.getText()).equals("")  ? "pass" : String.valueOf(edt_password.getText());
                pb_login.setVisibility(View.VISIBLE);

                editor.putString("username", user_name);
                editor.putString("password", password);

                GetApi getApi = new GetApi(cm.getUrlRegis() + user_name + "/" + password,getApplication(), new OnEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(JSONArray o) {
                        try {
                            JSONObject jsonObject = o.getJSONObject(0);

                            if (jsonObject.getString("regis").equals("ok")) {
                                String avatar = jsonObject.getString("avatar");
                                String name = jsonObject.getString("name");
                                String user_id = jsonObject.getString("id");
                                editor.putString("user_id", user_id);

                                editor.putString("username", name);
                                txt_alert.setVisibility(View.VISIBLE);
                                txt_alert.setText("Dang ky thanh cong");
                                pb_login.setVisibility(View.GONE);
                                editor.putBoolean("is_login", true);
                                editor.commit();
                                cm.showToast(getApplication(), "Chào mừng " + name + " quay trở lại", Toast.LENGTH_SHORT);
                                //redirect to MainActiviti
                                final Intent intent= new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);

                            }
                            else {
                                txt_alert.setVisibility(View.VISIBLE);
                                txt_alert.setText("Tài khoàn đã tồn tại");
                                pb_login.setVisibility(View.GONE);
                                editor.putBoolean("is_login", false);
                                editor.commit();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                    }
                });
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        if (!cm.getUserID(getApplication()).equals("0")) {
            final Intent intent= new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}
