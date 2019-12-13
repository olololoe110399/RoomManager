package com.example.qunlphngtr.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.qunlphngtr.Activities.MainActivity;
import com.example.qunlphngtr.Database.ManagerUsers;
import com.example.qunlphngtr.Model.Users;
import com.example.qunlphngtr.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class FragmentUser extends Fragment {
    private View view;
    private ImageButton imgbtnedit;
    private ScrollView lndetail, lnupdate;
    private TextView tvFullname, tvUserEmail, tvUserPhone, tvUserFullName, tvUserAdress, tvUserEmailupdate;
    private EditText edtUserPhone, edtUserFullName, edtUserAddress;
    private ImageView imgUserimg, imgUserImgupdate;
    private Button btnUpdate;
    private AccessToken token;
    private SharedPreferences pref;
    private String UserNameSp;
    private ManagerUsers managerUsers;
    private Users users;
    private byte[] imgAvater;
    private int REQUEST_CODE_CAMERA_IMG = 11, REQUEST_CODE_PHOTO_IMG = 12;
    private BottomSheetDialog mBottomDialogNotificationAction;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        initView();
        initObject();
        setOnclick();
        setUserDetail();
        return view;
    }

    private void initObject() {
        managerUsers = new ManagerUsers(getActivity());
        users = new Users();
        token = AccessToken.getCurrentAccessToken();
        pref = getActivity().getSharedPreferences("USER_FILE", MODE_PRIVATE);
        UserNameSp = pref.getString("USERNAME", "");
    }

    private void setUserDetail() {
        if (token == null) {
            setProfileSqlite();
        } else {
            if (isOnline(getActivity())) {
                setProfileFb();
            } else {
                Toast.makeText(getActivity(), R.string.error_internet, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setProfileFb() {
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("JSON1", response.getJSONObject().toString());

                try {
                    showImage(response.getJSONObject().getJSONObject("picture")
                            .getJSONObject("data").getString("url"));
                    tvFullname.setText(object.getString("name"));
                    tvUserFullName.setText(object.getString("name"));
                    tvUserEmail.setText(object.getString("email"));
                    tvUserAdress.setText(object.getString("location"));
                    tvUserAdress.setText(object.getJSONObject("location").getString("name"));
                    tvUserPhone.setText("Không thể xem");
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

    public void showImage(String profilePicUrl) {
        Picasso.get().load(profilePicUrl).placeholder(R.drawable.avatar)// Place holder image from drawable folder
                .error(R.drawable.ic_launcher_background).resize(200, 200).centerCrop()
                .into(imgUserimg);
    }

    private void setProfileSqlite() {
        users = managerUsers.getUserById(UserNameSp);
        if (users.getUserFullName() == null) {
            tvFullname.setText("Chua cập nhật");
            tvUserFullName.setText("Chưa cập nhật");
        } else {
            tvFullname.setText(users.getUserFullName());
            tvUserFullName.setText(users.getUserFullName());
        }
        if (users.getUserAddress() == null) {
            tvUserAdress.setText("Chưa cập nhật");
        } else {
            tvUserAdress.setText(users.getUserAddress());
        }
        tvUserEmailupdate.setText(users.getUserName());
        tvUserEmail.setText(users.getUserName());
        if (users.getUserPhone() == null) {
            tvUserPhone.setText("Chưa cập nhật");
        } else {
            tvUserPhone.setText(users.getUserPhone());
        }
        if (!(users.getUserAvater() == null)) {
            imgUserimg.setImageBitmap(LoadingImgArayByte(users.getUserAvater()));
        }

    }

    private void setProfileSqliteUpdate() {
        if (!(users.getUserFullName() == null)) {
            edtUserFullName.setText(users.getUserFullName());
        }
        if (!(users.getUserAddress() == null)) {
            edtUserAddress.setText(users.getUserAddress());
        }
        tvUserEmailupdate.setText(users.getUserName());
        if (!(users.getUserPhone() == null)) {
            edtUserPhone.setText(users.getUserPhone());
        }
        if (!(users.getUserAvater() == null)) {
            imgUserImgupdate.setImageBitmap(LoadingImgArayByte(users.getUserAvater()));
        }

    }


    private void setOnclick() {
        imgbtnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (token == null) {
                    lndetail.setVisibility(View.GONE);
                    lnupdate.setVisibility(View.VISIBLE);
                    setProfileSqliteUpdate();
                } else
                    Toast.makeText(getActivity(), "Không thể sửa thông tin Facrbook ở đây!", Toast.LENGTH_SHORT).show();

            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
                setProfileSqlite();
            }


        });
        imgUserImgupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuImage(REQUEST_CODE_CAMERA_IMG, REQUEST_CODE_PHOTO_IMG);
            }
        });
    }

    private void updateUserProfile() {
        if (edtUserFullName.getText().toString().trim().equals("") || edtUserPhone.getText().toString().trim().equals("") || edtUserAddress.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Không được để trống", Toast.LENGTH_SHORT).show();
        } else {
            if (users.getUserAvater() == null) {
                if (imgAvater == null) {
                    Toast.makeText(getActivity(), "Không được để trống ảnh đại diện", Toast.LENGTH_SHORT).show();
                } else {
                    users = new Users();
                    users.setUserName(UserNameSp);
                    users.setUserFullName(edtUserFullName.getText().toString().trim());
                    users.setUserPhone(edtUserPhone.getText().toString().trim());
                    users.setUserAddress(edtUserAddress.getText().toString().trim());
                    users.setUserAvater(imgAvater);
                    if (managerUsers.updateUser(users) > 0) {
                        Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        lndetail.setVisibility(View.VISIBLE);
                        lnupdate.setVisibility(View.GONE);
                        MainActivity.setProfileSqlite();
                    } else {
                        Toast.makeText(getActivity(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                if (imgAvater == null) {
                    imgAvater = users.getUserAvater();
                }
                users = new Users();
                users.setUserName(UserNameSp);
                users.setUserFullName(edtUserFullName.getText().toString().trim());
                users.setUserPhone(edtUserPhone.getText().toString().trim());
                users.setUserAddress(edtUserAddress.getText().toString().trim());
                users.setUserAvater(imgAvater);
                if (managerUsers.updateUser(users) > 0) {
                    Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    lndetail.setVisibility(View.VISIBLE);
                    lnupdate.setVisibility(View.GONE);
                    MainActivity.setProfileSqlite();
                } else {
                    Toast.makeText(getActivity(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void initView() {
        btnUpdate = view.findViewById(R.id.btnUpdate);
        edtUserPhone = view.findViewById(R.id.edtUserPhone);
        edtUserFullName = view.findViewById(R.id.edtUserFullName);
        edtUserAddress = view.findViewById(R.id.edtUserAddress);
        tvUserEmailupdate = view.findViewById(R.id.tvUserEmailupdate);
        imgUserImgupdate = view.findViewById(R.id.imgUserImgupdate);
        imgbtnedit = view.findViewById(R.id.imgbtnEdit);
        lndetail = view.findViewById(R.id.fragment_user_detail);
        lnupdate = view.findViewById(R.id.fragment_user_update);
        tvFullname = view.findViewById(R.id.tvFullName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        tvUserPhone = view.findViewById(R.id.tvUserPhone);
        tvUserFullName = view.findViewById(R.id.tvUserFullName);
        tvUserAdress = view.findViewById(R.id.tvUserAdress);
        imgUserimg = view.findViewById(R.id.imgUserImg);

    }

    private boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private Bitmap LoadingImgArayByte(byte[] img) {
        return BitmapFactory.decodeByteArray(img, 0, img.length);
    }

    private void menuImage(int REQUEST_CODE_CAMERA, int REQUEST_CODE_PHOTO) {
        View view = getLayoutInflater().inflate(R.layout.dialog_menu_add_images_bottomsheet, null);
        mBottomDialogNotificationAction = new BottomSheetDialog(getActivity());
        mBottomDialogNotificationAction.setContentView(view);
        mBottomDialogNotificationAction.show();

        // Remove default white color background
        FrameLayout bottomSheet = (FrameLayout) mBottomDialogNotificationAction.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackground(null);
        //initView
        LinearLayout lnPhotolibrary, lnchoosephoto, lndismiss;
        lndismiss = view.findViewById(R.id.lndismiss);
        lnPhotolibrary = view.findViewById(R.id.lnPhotolibrary);
        lnchoosephoto = view.findViewById(R.id.lnchoosephoto);
        lnPhotolibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoLibrary(REQUEST_CODE_PHOTO);
                mBottomDialogNotificationAction.dismiss();

            }
        });
        lnchoosephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto(REQUEST_CODE_CAMERA);
                mBottomDialogNotificationAction.dismiss();

            }
        });
        lndismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomDialogNotificationAction.dismiss();
            }
        });
    }

    private void choosePhoto(int REQUEST_CODE) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
           requestPermissions( new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    private void photoLibrary(int REQUEST_CODE) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 11:
                if (requestCode == REQUEST_CODE_CAMERA_IMG && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CODE_CAMERA_IMG);
                } else {
                    Toast.makeText(getActivity(), "Không cho phép mở CAMERA", Toast.LENGTH_SHORT).show();
                }
                break;
            case 12:
                if (requestCode == REQUEST_CODE_PHOTO_IMG && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_PHOTO_IMG);
                } else {
                    Toast.makeText(getActivity(), "Không cho phép mở thư viện ảnh", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 11:
                if (resultCode == RESULT_OK && data != null) {
                    imgAvater = convertImagesFromCamera(data, imgUserImgupdate);
                }
                break;
            case 12:
                if (resultCode == RESULT_OK && data != null) {
                    imgAvater = convertImagesFromPhotoLibrary(data, imgUserImgupdate);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private byte[] convertImagesFromCamera(@Nullable Intent data, ImageView img) {
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArray);
        img.setImageBitmap(bitmap);
        return byteArray.toByteArray();
    }

    private byte[] convertImagesFromPhotoLibrary(@Nullable Intent data, ImageView img) {
        Uri uri = data.getData();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
            Bitmap bitmap = (Bitmap) BitmapFactory.decodeStream(inputStream);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArray);
            img.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return byteArray.toByteArray();
    }
}
