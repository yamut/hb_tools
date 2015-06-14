package com.danmattern.hbtools;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link alphaAcidUnitConversion.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link alphaAcidUnitConversion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class alphaAcidUnitConversion extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View rootView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment alphaAcidUnitConversion.
     */
    // TODO: Rename and change types and number of parameters
    public static alphaAcidUnitConversion newInstance(String param1, String param2) {
        alphaAcidUnitConversion fragment = new alphaAcidUnitConversion();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public alphaAcidUnitConversion() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.fragment_alpha_acid_unit_conversion, container, false);
        EditText aau = (EditText)rootView.findViewById(R.id.aaUnitQuantity);
        EditText aaPercentage = (EditText)rootView.findViewById(R.id.aaPercentage);

        TextWatcher aauTextWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) {
                if( getAau() == 0 || getAaPercentage() == 0 ){
                    setOutputText(0.0);
                }
                else{
                    setOutputText(aauCalculation(getAau(),getAaPercentage()));
                }
            }
        };
        TextWatcher aaPercentageTextWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) {
                if( getAau() == 0 || getAaPercentage() == 0 ){
                    setOutputText(0.0);
                }
                else{
                    setOutputText(aauCalculation(getAau(),getAaPercentage()));
                }
            }
        };

        aau.addTextChangedListener(aauTextWatcher);
        aaPercentage.addTextChangedListener(aaPercentageTextWatcher);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private double aauCalculation(double aau, double aaPercent){
        if( aau == 0 || aaPercent == 0 ){
            return 0;
        }
        return aau / aaPercent;
    }

    private double getAau(){
        EditText aau = (EditText)rootView.findViewById(R.id.aaUnitQuantity);
        if( aau.getText().toString().isEmpty() == true ){
            return 0.0;
        }
        return Double.parseDouble(aau.getText().toString());
    }
    private double getAaPercentage(){
        EditText aaPercentage = (EditText)rootView.findViewById(R.id.aaPercentage);
        if( aaPercentage.getText().toString().isEmpty() == true ){
            return 0.0;
        }
        return Double.parseDouble(aaPercentage.getText().toString());
    }

    private void setOutputText(double ounces ){
        TextView outputText = (TextView)rootView.findViewById(R.id.outputTextLabel);
        outputText.setText(new BigDecimal(Double.toString(ounces)).setScale(2, RoundingMode.HALF_UP).toString() + "oz");
    }

}
