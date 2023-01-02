package com.example.fakecall;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Images;
//import android.support.v4.content.CursorLoader;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends Fragment implements  IOnBackPressed{
    private static final int REQUEST_READ_PERMISSION = 786;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    int ON_CHAR_CLICK = 2;
    int ON_CLICK;
    int ON_DIALOG_CLICK = 0;
    int ON_MORE_CLICK = 3;
    int ON_SHADLE_CLICK = 1;
    ImageView callerImage;
    CustomDialog customDialog;
    int dialogId;
    EditText nameEditText;
    EditText phoneEditText;
    Button call , more , danhba;
    CircularImageView circularImageView ;
    TextView ringtoneClick ,characterClick , soundClick, bookPhone  ;
    int picker;
    SharedPreferences sharedPref;
    private static final int CONTACT_PERMISSION_CODE = 8;
    private static final int CONTACT_PICK_CODE = 9;
    private static final int  RESULT_OKE = -1;
//    private AdView mAdView;
//    AdRequest adRequestint;

    public void onResume() {
        super.onResume();
        setCaller();
    }

    void setCaller() {
        String name = sharedPref.getString("name", "");
        String phone = sharedPref.getString("number", "");
        String image = sharedPref.getString("image", "");
        nameEditText.setText(name);
        phoneEditText.setText(phone);
        int obj = -1;
        switch (image.hashCode()) {
            case 0:
                if (image.equals("")) {
                    obj = 0;
                    break;
                }
                break;
            case 48:
                if (image.equals("0")) {
                    obj = 1;
                    break;
                }
                break;
            case 49:
                if (image.equals("1")) {
                    obj = 2;
                    break;
                }
                break;
            case 50:
                if (image.equals("2")) {
                    obj = 3;
                    break;
                }
                break;
            case 51:
                if (image.equals("3")) {
                    obj = 4;
                    break;
                }
                break;
            case 52:
                if (image.equals("4")) {
                    obj = 5;
                    break;
                }
                break;
        }
        switch (obj) {
            case 0:
                callerImage.setImageResource(R.drawable.person_add_grey);
                return;
            case 1:
                callerImage.setImageResource(R.drawable.gallery_btn_0);
                return;
            case 2:
                callerImage.setImageResource(R.drawable.gallery_btn_1);
                return;
            case 3:
                callerImage.setImageResource(R.drawable.gallery_btn_2);
                return;
            case 4:
                callerImage.setImageResource(R.drawable.gallery_btn_3);
                return;
            case 5:
                callerImage.setImageResource(R.drawable.gallery_btn_4);
                return;
            default:
                callerImage.setImageDrawable(Drawable.createFromPath(image));
//                Glide.with(getApplicationContext()).load(image).into(callerImage);
        }
    }

    public void rateUs() {
        final SharedPreferences sharedpreferences =getActivity().getSharedPreferences("MyPREFERENCES", 0);
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext(), androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);//Theme_AppCompat_Light_Dialog_Alert
        alert.setTitle("Rate Us:");
        alert.setMessage(getString(R.string.rate_dialog_message));
        alert.setPositiveButton(getString(R.string.rate_dialog_ok), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getActivity().  getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" +  getActivity(). getPackageName())));
                }
                sharedpreferences.edit().putBoolean("rate", true).apply();
                dialog.dismiss();
                getActivity(). finish();
            }
        });
        alert.setNegativeButton(getString(R.string.rate_dialog_no), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
               getActivity(). finish();
                sharedpreferences.edit().putBoolean("rate", true).apply();
            }
        });
        alert.setNeutralButton(getString(R.string.rate_dialog_cancel), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().finish();
                sharedpreferences.edit().putBoolean("remind", true).apply();
            }
        });
        alert.create();
        alert.show();
    }

    public boolean onBackPressed() {
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("MyPREFERENCES", 0);
        boolean alreadyRated = sharedpreferences.getBoolean("rate", false);
        boolean remindMeLater = sharedpreferences.getBoolean("remind", false);
        if (!alreadyRated) {
            rateUs();
        } else if (alreadyRated && remindMeLater) {
            rateUs();
        } else {
            super.getActivity().onBackPressed();
        }
        return alreadyRated;
    }

//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.test);
@SuppressLint("MissingInflatedId")
@Nullable
@Override
public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.test , container , false) ;
        nameEditText =  view.findViewById(R.id.caller_name);
        phoneEditText = view. findViewById(R.id.caller_number);
        callerImage = view. findViewById(R.id.caller_image);
        danhba = view.findViewById(R.id.danhsach) ;
        danhba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkContectPermission())
                {
                    pickContactInten() ;
                }else {
                    requestContectPermission();
                }
            }
        });
        circularImageView  = view.findViewById(R.id.caller_image) ;
        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CustomDialog cdd = new CustomDialog( getActivity(), 0);
                setDialog(cdd, 0);
                ON_CLICK = ON_DIALOG_CLICK;
                cdd.show();
                cdd.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        onDialogDismiss(cdd.buttonClick, 0);
                    }
                });
            }
        });
        sharedPref = getActivity().getSharedPreferences("file", 0);
//        more = view.findViewById(R.id.button3) ;
//        more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ON_CLICK = ON_MORE_CLICK;
//                Intent intent = new Intent("android.intent.action.VIEW");
//                intent.setData(Uri.parse("https://play.google.com/store/apps/dev?id="+getResources().getString(R.string.developer_id)));
//                startActivity(intent);
//            }
//        });
        //
        ringtoneClick = view .findViewById(R.id.textView_ringtone) ;
         ringtoneClick.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 final CustomDialog cdd = new CustomDialog(getContext(), 2);
                 setDialog(cdd, 2);
                 ON_CLICK = ON_DIALOG_CLICK;
                 cdd.show();
                 cdd.setOnDismissListener(new OnDismissListener() {
                     public void onDismiss(DialogInterface dialog) {
                         onDialogDismiss(cdd.buttonClick, 2);
                     }
                 });
             }
         });
         // call
         call = view.findViewById(R.id.button2) ;
         call.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 ON_CLICK = ON_SHADLE_CLICK;
                 startActivity(new Intent(getActivity(), ScheduleActivity.class));
                 //startActivity(new Intent(getActivity(), Call3.class));
                 getActivity().finish();
             }
         });
         // character
    characterClick = view.findViewById(R.id.textView_charater) ;
    characterClick.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ON_CLICK = ON_CHAR_CLICK;
            startActivityForResult(new Intent( getActivity(), CharacterActivity.class), 1);
        }
    });
    //
    soundClick = view.findViewById(R.id.textView) ;
    soundClick.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final CustomDialog cdd = new CustomDialog( getActivity(), 1);
            setDialog(cdd, 1);
            ON_CLICK = ON_DIALOG_CLICK;
            cdd.show();
            cdd.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    onDialogDismiss(cdd.buttonClick, 1);
                }
            });
        }
    });
    bookPhone = view.findViewById(R.id.bookphone) ;
    bookPhone.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //
            Intent intent = new Intent(getContext() , Call3.class) ;
            startActivity(intent);
        }
    });

        //ADS
//        mAdView = (AdView) findViewById(R.id.banner_AdView);
//        AdRequest adRequest = new AdRequest.Builder().addTestDevice("0224C93FFD644350DCD7F3D7557C6A5C").build();
//        mAdView.loadAd(adRequest);
        onClickRequestPermision();
        if (ON_CLICK == ON_DIALOG_CLICK) {
            if(customDialog != null){
                customDialog.show();
                customDialog.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        onDialogDismiss(customDialog.buttonClick, dialogId);
                    }
                });
            }
        } else if (ON_CLICK == ON_SHADLE_CLICK) {
            startActivity(new Intent(getContext(), ScheduleActivity.class));
            getActivity(). finish();
        } else if (ON_CLICK == ON_MORE_CLICK) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse("https://play.google.com/store/apps/dev?id="+getResources().getString(R.string.developer_id)));
            startActivity(intent);
            getActivity().  finish();
        } else if (ON_CLICK == ON_CHAR_CLICK) {
            startActivityForResult(new Intent(getActivity(), CharacterActivity.class), 1);
        }
        nameEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (!sharedPref.getString("name", "").equals(nameEditText.getText().toString())) {
                    saveName(nameEditText.getText().toString());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        phoneEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (!sharedPref.getString("number", "").equals(phoneEditText.getText().toString())) {
                    savePhone(phoneEditText.getText().toString());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        setCaller();
    return view;
    }

    private void saveName(String name) {
        Editor editor = sharedPref.edit();
        editor.putString("name", name);
        editor.apply();
    }

    private void savePhone(String phone) {
        Editor editor = sharedPref.edit();
        editor.putString("number", phone);
        editor.apply();
    }

    private void saveImg(String img) {
        Editor editor = sharedPref.edit();
        editor.putString("image", img);
        editor.apply();
    }

    void setDialog(CustomDialog c, int dialogId) {
        this.customDialog = c;
        this.dialogId = dialogId;
    }

    public void moreAppsClick(View v) {
        ON_CLICK = ON_MORE_CLICK;
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("https://play.google.com/store/apps/dev?id="+getResources().getString(R.string.developer_id)));
        startActivity(intent);
    }

    public void onDialogDismiss(int button, int id) {
        Editor editor;
        if (id == 1) {
            switch (button) {
                case 0:
                    picker = 0;
                    requestPermission();
                    return;
                case 1:
                    editor = sharedPref.edit();
                    editor.putString("audio", "");
                    editor.apply();
                    if (!sharedPref.getString("audio", "").equals("")) {
                        return;
                    }
                    return;
                case 2:
                    picker = 3;
                    requestPermission();
                    return;
                default:
            }
        } else if (id == 0) {
            switch (button) {
                case 0:
                    picker = 1;
                    requestPermission();
                    return;
                case 1:
                    saveImg("");
                    callerImage.setImageResource(R.drawable.person);
                    return;
                case 2:
                    startActivityForResult(new Intent( getActivity(), CharacterActivity.class), 1);
                    return;
                default:
            }
        } else if (id == 2) {
            editor = sharedPref.edit();
            switch (button) {
                case 0:
                    picker = 2;
                    requestPermission();
                    return;
                case 1:
                    editor.putString("ring", "");
                    editor.commit();
                    return;
                default:
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getContext(), " "+ resultCode, Toast.LENGTH_SHORT).show();
        if(resultCode==-1)
        {
            Toast.makeText (getContext(), "đã chạy vafod dây ", Toast.LENGTH_SHORT).show ();
            switch (requestCode) {
                case CONTACT_PICK_CODE:
                    Toast.makeText (getContext(), "retommmmmm"+requestCode, Toast.LENGTH_SHORT).show ();
                    contactPicked (data);
                    break;
            }
        }
        else
        {
            //Toast.makeText (getContext(), "Failed To pick contact", Toast.LENGTH_SHORT).show ();
        }
        if (requestCode == 1) {
            if (resultCode == -1) {
                String name = data.getStringExtra("name");
                String number = data.getStringExtra("number");
                String img = data.getStringExtra("img");
                saveName(name);
                saveImg(img);
                savePhone(number);
            }
        } else if (requestCode == 2) {
//            if (resultCode == -1) {
//                String audio = getRealPathFromURI(data.getData());
//                Editor editor = sharedPref.edit();
//                editor.putString("audio", audio);
//                editor.apply();
//                if (!sharedPref.getString("audio", "").equals("")) {
//                }
//            }
        } else if (requestCode == 3) {
            if (resultCode == -1) {
                performCrop(data.getData());
            }
        } else if (requestCode == 4) {
            if (resultCode == -1) {
                String ring = getRealPathFromURI(data.getData());
                Editor editor = sharedPref.edit();
                editor.putString("ring", ring);
                editor.apply();
            }
        } else if (requestCode == 5 && resultCode == -1) {
            saveImg(Environment.getExternalStorageDirectory() + "/Image-Caller.jpg");
        }
        //


    }

    private String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = new CursorLoader(getContext(), contentUri, new String[]{"_data"}, null, null, null).loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_PERMISSION && grantResults[0] == 0) {
            pick();
        }
        if (requestCode == 766 && grantResults[0] == 0) {
            RecordDialog recordDialog = new RecordDialog(getContext());
            recordDialog.show();
            recordDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    if (!sharedPref.getString("audio", "").equals("")) {
                    }
                }
            });
        }
        if (requestCode == CONTACT_PERMISSION_CODE)
        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            pickContactInten();
        }
        else {
            Toast.makeText(getContext(), "Permisson denied", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getContext(), "Permission to Access Storage:" + isExternalStorageWritable(), Toast.LENGTH_LONG).show();
    }

    public boolean isExternalStorageWritable() {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }

    void pick()
    {
        if (picker == 0) {
            pickAudio();
        } else if (picker == 1) {
            pickImage();
        } else if (picker == 2) {
            pickRing();
        } else {
            requestPermissionMIC();
        }
    }

    void pickRing() {
        Intent intent = new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity( getActivity(). getPackageManager()) != null) {
            startActivityForResult(intent, 4);
        } else {
            Toast.makeText( getActivity(), "No app found!", Toast.LENGTH_LONG).show();

        }
    }

    void pickAudio() {
        Intent intent = new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity( getActivity(). getPackageManager()) != null) {
            startActivityForResult(intent, 2);
        } else {
            Toast.makeText( getActivity(), "No app found!", Toast.LENGTH_LONG).show();
        }
    }

    void pickImage() {
        Intent intent = new Intent("android.intent.action.PICK", Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity( getActivity(). getPackageManager()) != null) {
            startActivityForResult(intent, 3);
        } else {
            Toast.makeText( getActivity(), "No app found!", Toast.LENGTH_LONG).show();
        }
    }

    private void requestPermission() {
        if (VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, REQUEST_WRITE_PERMISSION);
            //requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, REQUEST_READ_PERMISSION);
            return;
        }
        pick();
    }

    private void requestPermissionMIC() {
        if (VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 766);
            return;
        }
        RecordDialog recordDialog = new RecordDialog( getActivity());
        recordDialog.show();
        recordDialog.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                if (!sharedPref.getString("audio", "").equals("")) {
                }
            }
        });
    }

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 800);
            cropIntent.putExtra("outputY", 800);
            File f = new File(Environment.getExternalStorageDirectory(), "/Image-Caller.jpg");
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Log.e("io", ex.getMessage());
            }
            cropIntent.putExtra("output", Uri.fromFile(f));
            startActivityForResult(cropIntent, 5);
        } catch (ActivityNotFoundException e) {
            Toast.makeText( getActivity(), "Whoops - your device doesn't support the crop action!", Toast.LENGTH_LONG).show();
        }
    }
    void onClickRequestPermision() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            return;
        }
        if ( getActivity(). checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        } else {
            String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permission, REQUEST_WRITE_PERMISSION);
        }
        //
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            return;
        }
        if ( getActivity(). checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        } else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, REQUEST_READ_PERMISSION);
        }
    }
    private boolean checkContectPermission()
    {
        boolean resul = ContextCompat.checkSelfPermission(
                getContext(),
                 Manifest.permission.READ_CONTACTS)== (PackageManager.PERMISSION_GRANTED
        );
        return resul ;
    }
    private void requestContectPermission()
    {
        String[] permission ={ Manifest.permission.READ_CONTACTS } ;
        ActivityCompat.requestPermissions(getActivity() , permission , CONTACT_PERMISSION_CODE);

    }
    private void pickContactInten()
    {
        Intent intent = new Intent(Intent.ACTION_PICK  , ContactsContract.Contacts.CONTENT_URI) ;
        startActivityForResult(intent,CONTACT_PICK_CODE);
    }
    private void contactPicked(Intent data) {
        Cursor cursor = null;

        try {
            //Toast.makeText(getContext() , " đã chạy vào đây " ,  Toast.LENGTH_LONG).show();
            String phoneNo = null;
            String phoneNamee = null ;
            Uri uri = data.getData ();
            cursor = getContext().getContentResolver ().query (uri, null, null,null,null);
            cursor.moveToFirst ();
            Log.e("TAG3" , "" + ContactsContract.CommonDataKinds.Phone.NUMBER) ;
           // Log.e(cursor.get)

            String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
            int hasPhoneIdx = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
            String hasPhone = cursor.getString(hasPhoneIdx);

            if (hasPhone.equalsIgnoreCase("1"))
            {
                Cursor phones = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                phones.moveToFirst();
                int numberIdx = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String cNumber = phones.getString(numberIdx);
                phoneEditText.setText (cNumber);
                Log.e("number", "" + cNumber);
                //Toast.makeText(getApplicationContext(), cNumber, Toast.LENGTH_SHORT).show();

                //String nameContact = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

               // editText.setText(nameContact+ " "+ cNumber);
            }


            int phoneName = cursor.getColumnIndex (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            phoneNamee = cursor.getString (phoneName);
            Log.e("TAG2" , "" + phoneNo);
            nameEditText.setText(phoneNamee);
            //
            int phoneIndex = cursor.getColumnIndex (ContactsContract.CommonDataKinds.Phone.NUMBER);
            phoneNo = cursor.getString (phoneIndex);
            //phoneEditText.setText (cNumber);
            Log.e("TAG2" , "" + phoneIndex) ;
            Toast.makeText(getContext() , " đã chạy vào đây " +phoneIndex,  Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

}