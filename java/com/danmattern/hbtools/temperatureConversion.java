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
import android.widget.RadioGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link temperatureConversion.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link temperatureConversion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class temperatureConversion extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static View rootView;

    private OnFragmentInteractionListener mListener;

    private static final String degreeSymbol = "\u00b0";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment temperatureConversion.
     */
    // TODO: Rename and change types and number of parameters
    public static temperatureConversion newInstance(String param1, String param2) {
        temperatureConversion fragment = new temperatureConversion();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public temperatureConversion() {
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
        rootView = inflater.inflate(R.layout.fragment_temperature_conversion, container, false);

        final TextView outputText = (TextView)rootView.findViewById(R.id.outputText);
        final EditText inputText = (EditText)rootView.findViewById(R.id.degreeInput);
        final RadioGroup degreeUnitRadioGroup = (RadioGroup)rootView.findViewById(R.id.degreeUnitRadioGroup);
        degreeUnitRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioPosition = degreeUnitRadioGroup.indexOfChild(getView().findViewById(checkedId));
                String inputValue = inputText.getText().toString();
                double convertedTemperature;
                String roundedTemp;
                switch (radioPosition) {
                    case 0:
                        //Farenheit
                        if (inputValue.isEmpty() == true) {
                            outputText.setText("0" + degreeSymbol + "C");
                        } else {
                            convertedTemperature = convertFToC(Double.parseDouble(inputValue));
                            roundedTemp = new BigDecimal(Double.toString(convertedTemperature)).setScale(2, RoundingMode.HALF_UP).toString();
                            outputText.setText(roundedTemp + degreeSymbol + "C");
                        }
                        break;
                    case 1:
                        //Celsius
                        if (inputValue.isEmpty() == true) {
                            outputText.setText("0" + degreeSymbol + "F");
                        } else {
                            convertedTemperature = convertCToF(Double.parseDouble(inputValue));
                            roundedTemp = new BigDecimal(Double.toString(convertedTemperature)).setScale(2, RoundingMode.HALF_UP).toString();
                            outputText.setText(roundedTemp + degreeSymbol + "F");
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        TextWatcher inputDegreeTextWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) {
                if( inputText.getText().toString().isEmpty() == true ){
                    return;
                }
                String inputValue = inputText.getText().toString();
                double convertedTemperature;
                String roundedTemp;
                int degreeUnit = getDegreeUnit();
                switch (degreeUnit) {
                    case 0:
                        //Farenheit
                        if (inputValue.isEmpty() == true) {
                            outputText.setText("0" + degreeSymbol + "C");
                        } else {
                            convertedTemperature = convertFToC(Double.parseDouble(inputValue));
                            roundedTemp = new BigDecimal(Double.toString(convertedTemperature)).setScale(2, RoundingMode.HALF_UP).toString();
                            outputText.setText(roundedTemp + degreeSymbol + "C");
                        }
                        break;
                    case 1:
                        //Celsius
                        if (inputValue.isEmpty() == true) {
                            outputText.setText("0" + degreeSymbol + "F");
                        } else {
                            convertedTemperature = convertCToF(Double.parseDouble(inputValue));
                            roundedTemp = new BigDecimal(Double.toString(convertedTemperature)).setScale(2, RoundingMode.HALF_UP).toString();
                            outputText.setText(roundedTemp + degreeSymbol + "F");
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        inputText.addTextChangedListener(inputDegreeTextWatcher);
        outputText.setText("0" + degreeSymbol + "C");

        return rootView;
    }

    private int getDegreeUnit(){
        RadioGroup tempUnitRadioGroup = (RadioGroup)rootView.findViewById(R.id.degreeUnitRadioGroup);
        View checkedRadioButton = rootView.findViewById(tempUnitRadioGroup.getCheckedRadioButtonId());
        return tempUnitRadioGroup.indexOfChild(checkedRadioButton);
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

    private double convertFToC(double input){
        return ( input - 32.0 ) * ( 5.0 / 9.0 );
    }
    private double convertCToF(double input){
        return (input * ( 9.0 / 5.0 ) ) + 32.0;
    }

}
