package com.stormpath.sdk.ui;


import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BaseFragment extends Fragment {

    //in all fragments
    protected StormpathLoginConfig loginConfig;

    protected ImageView logo;

    protected EditText usernameInput;

    protected EditText passwordInput;

    protected ProgressBar progressBar;

    protected Button loginButton;

    protected Button registerButton;

    protected Button resetPasswordButton;

    protected EditText firstNameEditText;

    protected EditText surnameEditText;

    protected EditText emailEditText;

    protected EditText passwordEditText;

    protected Button sendButton;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart(){
        super.onStart();

       if(getView()!=null && loginConfig!=null) { //
           getView().setBackgroundColor(loginConfig.getBackgroundColor());
       }

        if(logo!=null && isResourceIdInPackage(getActivity().getPackageName(),loginConfig.getIconResource())){
            logo.setImageResource(loginConfig.getIconResource());
        }

        if(usernameInput!=null){ //will accept default underline color change

        }

        if(passwordInput!=null){ //will accept default underline color change

        }

        if(progressBar!=null){ //change anything? color change

        }

        if(loginButton!=null){ //will accept color change

        }

        if(registerButton!=null){ //will accept color change

        }

        if(resetPasswordButton!=null){ //will accept color change

        }

        if(firstNameEditText!=null){ //will accept color change

        }

        if(surnameEditText!=null){ //will accept color change

        }

        if(emailEditText!=null){ //will accept color change

        }

        if(passwordEditText!=null){ //will accept color change

        }

        if(sendButton!=null){ //will accept color change

        }

    }


    protected boolean isResourceIdInPackage(String packageName, int resId){
        if(packageName == null || resId == 0){
            return false;
        }

        Resources res = null;
        if(packageName.equals(getActivity().getPackageName())){
            res = getActivity().getResources();
        }else{
            try{
                res = getActivity().getPackageManager().getResourcesForApplication(packageName);
            }catch(PackageManager.NameNotFoundException e){
                //Log.w("", packageName + "does not contain " + resId + " ... " + e.getMessage());
            }
        }

        if(res == null){
            return false;
        }

        return isResourceIdInResources(res, resId);
    }

    private boolean isResourceIdInResources(Resources res, int resId){

        try{
            getActivity().getResources().getResourceName(resId);

            //Didn't catch so id is in res
            return true;

        }catch (Resources.NotFoundException e){
            return false;
        }
    }



}
