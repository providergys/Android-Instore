package com.teaera.teaeracafe.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.teaera.teaeracafe.R;
import com.teaera.teaeracafe.app.Application;
import com.teaera.teaeracafe.net.Model.UserInfo;
import com.teaera.teaeracafe.net.Request.UpdateUserInfoRequest;
import com.teaera.teaeracafe.net.Response.UserProfileResponse;
import com.teaera.teaeracafe.preference.UserPrefs;
import com.teaera.teaeracafe.utils.DialogUtils;
import com.teaera.teaeracafe.utils.DownloadImageTask;
import com.teaera.teaeracafe.utils.PermissionUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private ImageButton editPhoneImageButton;
    private ImageButton editImageButton;

    private de.hdodenhof.circleimageview.CircleImageView profileImage;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText paymentEditText;
    private EditText facebookEditText;

    private UserInfo userInfo;
    private SweetAlertDialog loaderDialog;

    private String userChoosenTask;
    private Uri photoUri;

    public static final int REQUEST_CAMERA = 201;
    public static final int SELECT_FILE = 202;
    public static final int PHOTO_CROP = 203;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        init();
    }

    private void init() {
        userInfo = UserPrefs.getUserInfo(getActivity());

        nameEditText = (EditText) getActivity().findViewById(R.id.nameEditText);
        emailEditText = (EditText) getActivity().findViewById(R.id.emailEditText);
        passwordEditText = (EditText) getActivity().findViewById(R.id.passwordEditText);
        paymentEditText = (EditText) getActivity().findViewById(R.id.paymentEditText);
        facebookEditText = (EditText) getActivity().findViewById(R.id.facebookEditText);
        profileImage = (CircleImageView) getActivity().findViewById(R.id.profileImage);

        nameEditText.setText(userInfo.getFirstname() + " " + userInfo.getLastname());
        emailEditText.setText(userInfo.getEmail());
        new DownloadImageTask(profileImage).execute(Application.getServerProfileImagePrefix() + userInfo.getImage());

        editPhoneImageButton = (ImageButton) getActivity().findViewById(R.id.editPhoneImageButton);
        editPhoneImageButton.setOnClickListener(this);

        editImageButton = (ImageButton) getActivity().findViewById(R.id.editImageButton);
        editImageButton.setOnClickListener(this);

        Button photoButton = (Button) getActivity().findViewById(R.id.photoButton);
        photoButton.setOnClickListener(this);

        Button passwordButton = (Button) getActivity().findViewById(R.id.passwordButton);
        passwordButton.setOnClickListener(this);

        Button saveButton = (Button) getActivity().findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        updateEditable(false);
    }

    private void updateEditable(boolean editable) {
        nameEditText.setFocusable(editable);
        nameEditText.setFocusableInTouchMode(editable);
        emailEditText.setFocusable(editable);
        emailEditText.setFocusableInTouchMode(editable);
        passwordEditText.setFocusable(editable);
        passwordEditText.setFocusableInTouchMode(editable);
        paymentEditText.setFocusable(editable);
        paymentEditText.setFocusableInTouchMode(editable);
        facebookEditText.setFocusable(editable);
        facebookEditText.setFocusableInTouchMode(editable);
    }

    private void saveUserInfo() {

        final String username = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();

        if (username.isEmpty()) {
            DialogUtils.showDialog(getActivity(), "Error", getString(R.string.enter_name), null, null);
            return;
        }

        if (email.isEmpty()) {
            DialogUtils.showDialog(getActivity(), "Error", getString(R.string.enter_email), null, null);
            return;
        }

        if (username.equals(userInfo.getFirstname() + " " + userInfo.getLastname()) && email.equals(userInfo.getEmail())) {
            return;
        } else {
            showLoader(R.string.empty);

            Application.getServerApi().updateUserInfo(new UpdateUserInfoRequest(userInfo.getId(), username, email)).enqueue(new Callback<UserProfileResponse>(){

                @Override
                public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                    hideLoader();
                    if (response.body().isError()) {
                        DialogUtils.showDialog(getActivity(), "Error", response.body().getMessage(), null, null);
                    } else {
                        DialogUtils.showDialog(getActivity(), "Confirm", response.body().getMessage(), null, null);
                        userInfo = response.body().getUser();
                        UserPrefs.saveUserInfo(getActivity(), userInfo);
                        updateEditable(false);
                    }
                }

                @Override
                public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                    hideLoader();
                    if (t.getLocalizedMessage() != null) {
                        Log.d("Profile", t.getLocalizedMessage());
                    } else {
                        Log.d("Profile", "Unknown error");
                    }
                }
            });
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                boolean result = PermissionUtils.checkPermission(getActivity());
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
            else if (requestCode == PHOTO_CROP)
                onCropImageResult(data);
        }
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            photoUri = data.getData();
            imageCropFunction();
        }
    }


    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        photoUri = Uri.fromFile(destination);

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
            imageCropFunction();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onCropImageResult(Intent data) {
        if (data != null) {

            Bundle bundle = data.getExtras();
            Bitmap bitmap = bundle.getParcelable("data");
            profileImage.setImageBitmap(bitmap);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            byte[] bitmapdata = stream.toByteArray();
            uploadImage(bitmapdata);

        }
    }

    public void imageCropFunction() {

        // Image Crop Code
        try {
            Intent CropIntent = new Intent("com.android.camera.action.CROP");

            CropIntent.setDataAndType(photoUri, "image/*");

            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 180);
            CropIntent.putExtra("outputY", 180);
            CropIntent.putExtra("aspectX", 3);
            CropIntent.putExtra("aspectY", 3);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, PHOTO_CROP);

        } catch (ActivityNotFoundException e) {

        }
    }


    private void uploadImage(byte[] imageBytes) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
        RequestBody userId = RequestBody.create(okhttp3.MultipartBody.FORM, userInfo.getId());

        showLoader(R.string.empty);

        Application.getServerApi().uploadProfilePhoto(userId, body).enqueue(new Callback<UserProfileResponse>(){

            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                hideLoader();
                if (response.body().isError()) {
                    DialogUtils.showDialog(getActivity(), "Error", response.body().getMessage(), null, null);
                } else {
                    DialogUtils.showDialog(getActivity(), "Confirm", response.body().getMessage(), null, null);
                    userInfo = response.body().getUser();
                    UserPrefs.saveUserInfo(getActivity(), userInfo);
                    updateEditable(false);
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                hideLoader();
                if (t.getLocalizedMessage() != null) {
                    Log.d("Profile", t.getLocalizedMessage());
                } else {
                    Log.d("Profile", "Unknown error");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.photoButton:
                selectImage();
                break;

            case R.id.editImageButton:
                updateEditable(true);
                break;

            case R.id.passwordButton:
                Intent intent = new Intent(getActivity(), UpdatePasswordActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                break;

            case R.id.saveButton:
                saveUserInfo();
                break;

        }
    }

    public void showLoader(int resId) {
        loaderDialog = new SweetAlertDialog(this.getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        loaderDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.loader_color));
        loaderDialog.setTitleText(getString(resId));
        loaderDialog.setCancelable(false);
        loaderDialog.show();
    }

    public void hideLoader() {
        if (loaderDialog != null) {
            loaderDialog.dismissWithAnimation();
        }
    }
}
