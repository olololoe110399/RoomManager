package com.example.qunlphngtr.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.qunlphngtr.Database.CustomerDAO;
import com.example.qunlphngtr.Database.RoomDAO;
import com.example.qunlphngtr.Fragment.FragmentCustomer;
import com.example.qunlphngtr.Model.Customer;
import com.example.qunlphngtr.Model.Room;
import com.example.qunlphngtr.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddCustomerActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Button btnsave;
    private EditText edtCustomerPhone, edtCustomerName, edtCustomerCMND;
    private ImageView imgCustomer, imgCMNDBefore, imgCMNDAfter;
    private RelativeLayout rlCMNDBefore, rlCMNDAfter;
    private CustomerDAO customerDAO;
    private int REQUEST_CODE_CAMERA_IMG = 11, REQUEST_CODE_CAMERA_IMG_CMND_BEFORE = 21, REQUEST_CODE_CAMERA_IMG_CMND_AFTER = 31;
    private int REQUEST_CODE_PHOTO_IMG = 12, REQUEST_CODE_PHOTO_IMG_CMND_BEFORE = 22, REQUEST_CODE_PHOTO_IMG_CMND_AFTER = 32;
    private byte[] dataimgCustomer, dataimgCMNDBefore, dataimgCMNDAfter;
    private BottomSheetDialog mBottomDialogNotificationAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        initToolbar();
        initView();
        setBtnOnclick();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thêm khách hàng");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        Animatoo.animateSlideRight(this);
        return true;
    }

    private void setBtnOnclick() {
        btnsave.setOnClickListener(this);
        rlCMNDBefore.setOnClickListener(this);
        rlCMNDAfter.setOnClickListener(this);
        imgCustomer.setOnClickListener(this);
    }

    private void initView() {
        btnsave = findViewById(R.id.btnSave);
        customerDAO = new CustomerDAO(this);
        edtCustomerPhone = findViewById(R.id.edtPhone);
        edtCustomerName = findViewById(R.id.edtName);
        edtCustomerCMND = findViewById(R.id.edtCMND);
        imgCustomer = findViewById(R.id.imgCustomer);
        imgCMNDBefore = findViewById(R.id.imgCMNDBefore);
        imgCMNDAfter = findViewById(R.id.imgCMNDAfter);
        rlCMNDBefore = findViewById(R.id.rlCMNDBefore);
        rlCMNDAfter = findViewById(R.id.rlCMNDAfter);
    }

    private void AddCustomer() {
        if (edtCustomerPhone.getText().toString().trim().equals("") || edtCustomerName.getText().toString().trim().equals("") || edtCustomerCMND.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
        } else {
            if (dataimgCustomer==null||dataimgCMNDBefore==null||dataimgCustomer==null) {
                dialogSuccessfully();
            } else {
                Customer customer = new Customer();
                customer.setCustomerPhone(edtCustomerPhone.getText().toString().trim());
                customer.setCustomerName(edtCustomerName.getText().toString().trim());
                customer.setCustomerCMND(Integer.parseInt(edtCustomerCMND.getText().toString().trim()));
                customer.setCustomerImage(dataimgCustomer);
                customer.setCustomerCMNDImgBefore(dataimgCMNDBefore);
                customer.setCustomerCMNdImgAfter(dataimgCMNDAfter);

                if (customerDAO.addCustomer(customer) > 0) {
                    Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    FragmentCustomer.LoadRecyclerview();
                    FragmentCustomer.checkCustomernull();
                    finish();
                } else {
                    Toast.makeText(this, "Không thêm được", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
    private void dialogSuccessfully() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Lưu ý");
        mBuilder.setMessage("Không được bỏ trống hình đại diện và CMND để tiện cho việc liên hệ sau này!");
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();


    }


    private void menuImage(int REQUEST_CODE_CAMERA, int REQUEST_CODE_PHOTO) {
        View view = getLayoutInflater().inflate(R.layout.dialog_menu_add_images_bottomsheet, null);
        mBottomDialogNotificationAction = new BottomSheetDialog(this);
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    private void photoLibrary(int REQUEST_CODE) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                AddCustomer();
                break;
            case R.id.imgCustomer:
                menuImage(REQUEST_CODE_CAMERA_IMG, REQUEST_CODE_PHOTO_IMG);
                break;
            case R.id.rlCMNDBefore:
                menuImage(REQUEST_CODE_CAMERA_IMG_CMND_BEFORE, REQUEST_CODE_PHOTO_IMG_CMND_BEFORE);
                break;
            case R.id.rlCMNDAfter:
                menuImage(REQUEST_CODE_CAMERA_IMG_CMND_AFTER, REQUEST_CODE_PHOTO_IMG_CMND_AFTER);
                break;
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
                    Toast.makeText(this, "Không cho phép mở CAMERA", Toast.LENGTH_SHORT).show();
                }
                break;
            case 12:
                if (requestCode == REQUEST_CODE_PHOTO_IMG && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_PHOTO_IMG);
                } else {
                    Toast.makeText(this, "Không cho phép mở thư viện ảnh", Toast.LENGTH_SHORT).show();
                }
                break;
            case 21:
                if (requestCode == REQUEST_CODE_CAMERA_IMG_CMND_BEFORE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CODE_CAMERA_IMG_CMND_BEFORE);
                } else {
                    Toast.makeText(this, "Không cho phép mở CAMERA", Toast.LENGTH_SHORT).show();
                }
                break;
            case 22:
                if (requestCode == REQUEST_CODE_PHOTO_IMG_CMND_BEFORE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_PHOTO_IMG_CMND_BEFORE);
                } else {
                    Toast.makeText(this, "Không cho phép mở thư viện ảnh", Toast.LENGTH_SHORT).show();
                }
                break;
            case 31:
                if (requestCode == REQUEST_CODE_CAMERA_IMG_CMND_AFTER && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CODE_CAMERA_IMG_CMND_AFTER);
                } else {
                    Toast.makeText(this, "Không cho phép mở CAMERA", Toast.LENGTH_SHORT).show();
                }
                break;
            case 32:
                if (requestCode == REQUEST_CODE_PHOTO_IMG_CMND_AFTER && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_PHOTO_IMG_CMND_AFTER);
                } else {
                    Toast.makeText(this, "Không cho phép mở thư viện ảnh", Toast.LENGTH_SHORT).show();
                }
                break;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 11:
                if (resultCode == RESULT_OK && data != null) {
                    dataimgCustomer = convertImagesFromCamera(data, imgCustomer);
                }
                break;
            case 12:
                if (resultCode == RESULT_OK && data != null) {
                    dataimgCustomer = convertImagesFromPhotoLibrary(data, imgCustomer);
                }
                break;
            case 21:
                if (resultCode == RESULT_OK && data != null) {
                    dataimgCMNDBefore = convertImagesFromCamera(data, imgCMNDBefore);

                }
                break;
            case 22:
                if (resultCode == RESULT_OK && data != null) {
                    dataimgCMNDBefore = convertImagesFromPhotoLibrary(data, imgCMNDBefore);
                }
                break;
            case 31:
                if (resultCode == RESULT_OK && data != null) {
                    dataimgCMNDAfter = convertImagesFromCamera(data, imgCMNDAfter);
                }
                break;
            case 32:
                if (resultCode == RESULT_OK && data != null) {
                    dataimgCMNDAfter = convertImagesFromPhotoLibrary(data, imgCMNDAfter);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private byte[] convertImagesFromCamera(@Nullable Intent data, ImageView img) {
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
        img.setImageBitmap(bitmap);
        return byteArray.toByteArray();
    }

    private byte[] convertImagesFromPhotoLibrary(@Nullable Intent data, ImageView img) {
        Uri uri = data.getData();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = (Bitmap) BitmapFactory.decodeStream(inputStream);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
            img.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return byteArray.toByteArray();
    }
}
