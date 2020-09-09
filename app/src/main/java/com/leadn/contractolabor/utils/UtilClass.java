package com.leadn.contractolabor.utils;


import android.Manifest;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.leadn.contractolabor.R;
import com.leadn.contractolabor.ui.credentials.model.UserResponse;
import com.leadn.contractolabor.utils.shared_preferences.SharedPreferenceHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UtilClass {

    public static void pushFragment(Fragment fragment, AppCompatActivity activity, int container, Boolean hasTransition) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (hasTransition) {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.replace(container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
        } else {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.replace(container, fragment, fragment.getClass().getSimpleName()).commit();
        }
//        fragmentTransaction.replace(container, fragment, fragment.getClass().getSimpleName()).commit();
    }

    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static boolean NetworkConnectionAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            for (NetworkInfo networkInfo : info) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void networkNotAvailable(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("No Internet Alert");
        builder.setMessage("You have no internet connection! \nPlease try again.\n");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "OK",
                (dialog, id) -> {
                    dialog.dismiss();
                    System.exit(0);
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public static String getFileExtension(Uri uri, Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public static Calendar initDatePicker(Context context, TextView textView, String dateType) {

        Calendar calendar = Calendar.getInstance();
        if (dateType.equalsIgnoreCase("to"))
            calendar.add(Calendar.DAY_OF_MONTH, 15);
        updateLabel(textView, calendar);

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(textView, calendar);
        };

        textView.setOnClickListener(v -> new DatePickerDialog(context, date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());
        return calendar;
    }

    private static void updateLabel(TextView textView, Calendar calendar) {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        textView.setText(sdf.format(calendar.getTime()));
    }

    public static boolean checkImagePermissions(AppCompatActivity context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            return false;
        } else
            return true;


    }

    public static void startCountAnimation(float fromValue, float toValue, TextView textView) {
        textView.setText(String.valueOf(toValue));
        ValueAnimator animator = ValueAnimator.ofFloat(fromValue, toValue);
        animator.setDuration(200);
        animator.addUpdateListener(animation -> textView.setText(animation.getAnimatedValue().toString()));
        animator.start();
    }

    public static String getCurrentUserId() {
        Integer userId = 0;
        if (SharedPreferenceHelper.getHelper().getUserLoggedInData() != null) {
            userId = new Gson().fromJson(SharedPreferenceHelper.getHelper().getUserLoggedInData(), UserResponse.class).getSeqId();
        }
        return String.valueOf(userId);
    }
}
