package com.stormpath.sdk.ui;


import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseFragment extends Fragment {

    protected StormpathLoginConfig loginConfig;
    protected ImageView logo;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart(){
        super.onStart();

       if(getView()!=null && loginConfig!=null) {
           getView().setBackgroundColor(loginConfig.getBackgroundColor());
       }

        if(logo!=null && isResourceIdInPackage(getActivity().getPackageName(),loginConfig.getIconResource())){
            logo.setImageResource(loginConfig.getIconResource());
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
