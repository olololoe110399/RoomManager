package com.example.qunlphngtr.Fragment.Fragment_login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.qunlphngtr.Activities.MainActivity;
import com.example.qunlphngtr.Database.ManagerUsers;
import com.example.qunlphngtr.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_Signin extends Fragment {
    CallbackManager callbackManager;
    View view;
    LoginButton loginButton;
    String iduser, name, email;
    Button btnsubmit;
    EditText edtuser, edtpass;
    CheckBox checkBox;
    ManagerUsers managerUsers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signin, container, false);

        initView();
        if (isNetworkAvailable()) {
            callbackManager = CallbackManager.Factory.create();
            // If using in a fragment
            loginButton.setFragment(this);
            // Callback registration
            loginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_birthday", "user_location"));
            set_Loginbtn();
        } else {
            Toast.makeText(getActivity(), "Kiểm tra lại kết nối mạng!", Toast.LENGTH_SHORT).show();
        }

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtpass.getText().toString().equals("") || edtuser.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Không được để trống thông tin!", Toast.LENGTH_SHORT).show();
                } else {
                    if (!isValidEmail(edtuser.getText().toString())) {
                        Toast.makeText(getActivity(), "Không đúng định dạng Email!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (managerUsers.checkLogin(edtuser.getText().toString(), edtpass.getText().toString()) > 0) {
                            Toast.makeText(getActivity(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            rememberUser(edtuser.getText().toString(), edtpass.getText().toString(), checkBox.isChecked());
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            Animatoo.animateSlideLeft(getActivity());
                            getActivity().finish();
                        } else {
                            edtuser.setText(null);
                            edtpass.setText(null);
                            Toast.makeText(getActivity(), "Tên tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });


        return view;
    }

    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void set_Loginbtn() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("JSON", response.getJSONObject().toString());

                        try {

//                            showImage(response.getJSONObject().getJSONObject("picture")
//                                    .getJSONObject("data").getString("url"));
                            iduser = object.getString("id");
                            name = object.getString("name");
                            email = object.getString("email");
                            if (!name.equals("")) {
                                dialogremembermefb();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,birthday,location,picture.width(200).height(200)");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();


            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getActivity(), "Kiểm tra lại kết nối mạng!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void dialogremembermefb() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Xin chào " + name);
        builder.setMessage("Lưu trạng thái đăng nhập này cho lần sau ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                rememberUser(email, "", false);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                Animatoo.animateSlideLeft(getActivity());
                getActivity().finish();
            }
        });
        builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                rememberUser(email, "", true);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                Animatoo.animateSlideLeft(getActivity());
                getActivity().finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void initView() {
        loginButton = view.findViewById(R.id.login_button);
        edtuser = view.findViewById(R.id.edtuser);
        edtpass = view.findViewById(R.id.edtpassword);
        btnsubmit = view.findViewById(R.id.btnsubmit);
        checkBox = view.findViewById(R.id.checkbox);
        managerUsers = new ManagerUsers(getActivity());

    }

    public void rememberUser(String u, String p, boolean status) {
        SharedPreferences pref = getActivity().getSharedPreferences("USER_FILE", MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();

        if (!status) {
            edit.clear();
            edit.putString("USERNAME", u);

        } else {
            edit.putString("USERNAME", u);
            edit.putString("PASSWORD", p);
            edit.putBoolean("REMEMBER", status);
        }
        edit.commit();
    }


    @Override
    public void onStart() {
        LoginManager.getInstance().logOut();
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

//    public void showImage(String profilePicUrl) {
//        Dialog builder = new Dialog(getActivity());
//        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        builder.getWindow().setBackgroundDrawable(
//                new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                //nothing;
//            }
//        });
//
//        ImageView imageView = new ImageView(getActivity());
//        Picasso.get().load(profilePicUrl).placeholder(R.drawable.avatar)// Place holder image from drawable folder
//                .error(R.drawable.ic_launcher_background).resize(200,200).centerCrop()
//                .into(imageView);
//        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
//        builder.show();
//    }
}
