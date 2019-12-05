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
import com.example.qunlphngtr.Fragment.FragmentCustomer;
import com.example.qunlphngtr.Model.Customer;
import com.example.qunlphngtr.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpdateCustomerActivity extends AppCompatActivity implements View.OnClickListener {
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
    private Customer customer;
    public static int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_customer);
        initToolbar();
        initView();
        setBtnOnclick();
        setCustomerDetail();
    }

    private void setCustomerDetail() {
        imgCustomer.setImageBitmap(convertImagesFromCustomer(customer.getCustomerImage()));
        edtCustomerPhone.setText(customer.getCustomerPhone());
        edtCustomerName.setText(customer.getCustomerName());
        edtCustomerCMND.setText(customer.getCustomerCMND() + "");
        imgCMNDBefore.setImageBitmap(convertImagesFromCustomer(customer.getCustomerCMNDImgBefore()));
        imgCMNDAfter.setImageBitmap(convertImagesFromCustomer(customer.getCustomerCMNdImgAfter()));
    }

    private void setBtnOnclick() {
        btnsave.setOnClickListener(this);
        rlCMNDBefore.setOnClickListener(this);
        rlCMNDAfter.setOnClickListener(this);
        imgCustomer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                UpdateCustomer();
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

    private void UpdateCustomer() {
        if (edtCustomerPhone.getText().toString().trim().equals("") || edtCustomerName.getText().toString().trim().equals("") || edtCustomerCMND.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
        } else {
            Customer customer1 = new Customer();
            customer1.setCustomerID(customer.getCustomerID());
            customer1.setCustomerPhone(edtCustomerPhone.getText().toString().trim());
            customer1.setCustomerName(edtCustomerName.getText().toString().trim());
            customer1.setCustomerCMND(Integer.parseInt(edtCustomerCMND.getText().toString().trim()));
            if (dataimgCustomer == null) {
               dataimgCustomer=customer.getCustomerImage();
            }if (dataimgCMNDBefore == null) {
                dataimgCMNDBefore=customer.getCustomerCMNDImgBefore();
            }if (dataimgCMNDAfter == null) {
                dataimgCMNDAfter=customer.getCustomerCMNdImgAfter();
            }
            customer1.setCustomerImage(dataimgCustomer);
            customer1.setCustomerCMNDImgBefore(dataimgCMNDBefore);
            customer1.setCustomerCMNdImgAfter(dataimgCMNDAfter);
            customerDAO.updateCustomer(customer1);
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            FragmentCustomer.LoadRecyclerview();
            FragmentCustomer.checkCustomernull();
            finish();
            Animatoo.animateSlideRight(this);
        }
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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
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

    private void initToolbar() {
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thông tin khách hàng");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        Animatoo.animateSlideRight(this);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideRight(this);
    }

    private void initView() {
        customer=FragmentCustomer.customerList.get(pos);
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArray);
        img.setImageBitmap(bitmap);
        return byteArray.toByteArray();
    }

    private byte[] convertImagesFromPhotoLibrary(@Nullable Intent data, ImageView img) {
        Uri uri = data.getData();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = (Bitmap) BitmapFactory.decodeStream(inputStream);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArray);
            img.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return byteArray.toByteArray();
    }

    private Bitmap convertImagesFromCustomer(byte[] img) {
        return BitmapFactory.decodeByteArray(img, 0, img.length);
    }
}
