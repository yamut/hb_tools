package com.danmattern.hbtools2;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link brixToSg.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link brixToSg#newInstance} factory method to
 * create an instance of this fragment.
 */
public class brixToSg extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int SG = 1;
    private static final int BRIX = 0;

    private static final boolean HIDE = false;
    private static final boolean SHOW = true;

    private static View rootView;

    private double wortCorrectionFactor = 1.04;

    private Map<Integer, String>  unitTypeNameMap = new HashMap(){{
        put(1, "Brix");
        put(0, "SG");
    }};

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment brixToSg.
     */
    // TODO: Rename and change types and number of parameters
    public static brixToSg newInstance(String param1, String param2) {
        brixToSg fragment = new brixToSg();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public brixToSg() {
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
    protected double convertBetweenBrixAndSg(double input, int unitType){
        double output = 0;
        if( ( unitType == BRIX && ( input < 1 || input > 100 ) ) || ( unitType == SG && ( input < 1 || input > 2 ) ) ){
            //Should probably trigger an error here, the values are most likely out of range or the abv is so fucking high that im not sure how to calculate it
            return output;
        }
        switch( unitType ){
            case BRIX:
                output = convertBrixToSg( input );
                break;
            case SG:
                output = convertSgToBrix( input );
                break;
        }
        return output;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_brix_to_sg, container, false);
        final EditText inputTextField = (EditText) rootView.findViewById(R.id.inputText);
        final RadioGroup conversionTypeRadioGroup = (RadioGroup) rootView.findViewById(R.id.brix_sg_radio_group);
        final EditText abvCalcOgField = (EditText)rootView.findViewById(R.id.abvOgInputText);
        final EditText abvCalcFgField = (EditText)rootView.findViewById(R.id.abvFgInputText);


        conversionTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioPosition = conversionTypeRadioGroup.indexOfChild(getView().findViewById(checkedId));
                Log.d(null, "passing " + Double.parseDouble(inputTextField.getText().toString()) + " and " + Integer.toString(radioPosition));
                double convertedValue = convertBetweenBrixAndSg(Double.parseDouble(inputTextField.getText().toString()), radioPosition);
                Log.d(null, "here radioPosition is " + Integer.toString(radioPosition) + " value is " + Double.toString(convertedValue));
                setOutputText(radioPosition, convertedValue);
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) {
                int radioPosition = getConversionType();
                Log.d(null,"value of input?" + Boolean.toString(inputTextField.getText().toString().isEmpty()));
                if(inputTextField.getText().toString().isEmpty() == true){
                    setOutputText(radioPosition, 0);
                    return;
                }
                double input = Double.parseDouble(inputTextField.getText().toString());

                double convertedValue = convertBetweenBrixAndSg(input, radioPosition);
                setOutputText(radioPosition, convertedValue);
            }
        };
        inputTextField.addTextChangedListener(textWatcher);

        TextWatcher abvCalcOgTextWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) {
                if(abvCalcOgField.getText().toString().isEmpty() == true || abvCalcFgField.getText().toString().isEmpty() == true ){
                    setAbvOutputText(0);
                    return;
                }
                setAbvOutputText(calculateAbv(Double.parseDouble(abvCalcOgField.getText().toString()), Double.parseDouble(abvCalcFgField.getText().toString())));
            }
        };
        TextWatcher abvCalcFgTextWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) {
                if(abvCalcFgField.getText().toString().isEmpty() == true || abvCalcOgField.getText().toString().isEmpty() == true ){
                    setAbvOutputText(0);
                    return;
                }
                setAbvOutputText(calculateAbv(Double.parseDouble(abvCalcOgField.getText().toString()), Double.parseDouble(abvCalcFgField.getText().toString())));
            }
        };
        abvCalcOgField.addTextChangedListener(abvCalcOgTextWatcher);
        abvCalcFgField.addTextChangedListener(abvCalcFgTextWatcher);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * Returns selected conversion type mapped to static SG/BRIX values
     * @return int BRIX/SG
     */
    private int getConversionType(){
        final RadioGroup conversionTypeRadioGroup = (RadioGroup) rootView.findViewById(R.id.brix_sg_radio_group);
        int checkedRadioButtonId = conversionTypeRadioGroup.getCheckedRadioButtonId();
        View checkedRadioButton = rootView.findViewById(checkedRadioButtonId);
        return conversionTypeRadioGroup.indexOfChild(checkedRadioButton);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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

    private double convertBrixToSg(double brix){
        double output = 0;
        if( brix >= 1 && brix <= 100 ){
            double correctedBrix = brix / wortCorrectionFactor;
            output = 1 + ( correctedBrix / ( 258.6 - ( ( correctedBrix / 258.2 ) * 227.1 ) ) );
        }
        return output;
    }
    private double convertSgToBrix(double sg ){
        double output = 0;
        if( sg > 1 ){
            output = -616.868 + ( 1111.14 * sg ) - ( 630.272 * sg * sg ) + ( 135.997 * sg * sg * sg );
        }
        return output;
    }

    private double calculateAbv( double og, double fg ){
        double output = 0;
        if( og > 1 && og < 2 && fg > 1 && fg < 2 ){
            output = ( 76.08 * ( og - fg ) / ( 1.775 - og ) ) * ( fg / 0.794 );
        }
        return output;
    }

    private void setOutputText(int unitType, double outputValue ){
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        decimalFormat.setRoundingMode(RoundingMode.CEILING);
        String unitName = unitTypeNameMap.get(unitType);
        TextView outputTextField = (TextView)rootView.findViewById(R.id.outputText);
        String outputValueString;
        Log.d(null,"output string length is " + Double.toString(outputValue).length() );
        if( Double.toString(outputValue).length() >= 7 ){
            outputValueString = Double.toString(outputValue).substring(0,6);
        }
        else{
            outputValueString = Double.toString(outputValue);
        }
        outputTextField.setText(unitName + ": " + outputValueString);
    }
    private void setAbvOutputText(double abv){
        String abvOutput = "0";
        if(Double.toString(abv).length() > 4 ){
            abvOutput = Double.toString(abv).substring(0,4);
        }
        else{
            abvOutput = Double.toString(abv);
        }
        TextView abvOutputTextField = (TextView)rootView.findViewById(R.id.outputAbvText);
        abvOutputTextField.setText("ABV: " + abvOutput + "%");
    }
}
