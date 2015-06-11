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

    private static final int NO_ETHANOL = 0;
    private static final int ETHANOL = 1;

    private static View rootView;

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
    protected double convertBetweenBrixAndSg(double input, int unitType, int ethanolSwitch){
        double output = 0;
        switch( unitType ){
            case BRIX:
                if(ethanolSwitch == NO_ETHANOL) {
                    output = 1.000019 + ((0.003865613 * input) + (0.00001296425 * input) + (0.00000005701128 * input));
                }
                else if( ethanolSwitch == ETHANOL ){

                }
                break;
            case SG:

                break;
        }
        return 0;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_brix_to_sg, container, false);
        final EditText inputTextField = (EditText) rootView.findViewById(R.id.inputText);
        final EditText originalBrix = (EditText)rootView.findViewById(R.id.originalBrix);
        final RadioGroup conversionTypeRadioGroup = (RadioGroup) rootView.findViewById(R.id.brix_sg_radio_group);
        final RadioGroup ethanolRadioGroup = (RadioGroup)rootView.findViewById(R.id.ethanolRadioGroup);
        conversionTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioPosition = conversionTypeRadioGroup.indexOfChild(getView().findViewById(checkedId));
                Log.d(null, "Conversion type setting is " + Integer.toString(radioPosition));
                switch(radioPosition){
                    case SG:
                        toggleBrixFields(false);
                        break;
                    case BRIX:
                        toggleBrixFields(true);
                        break;
                }
            }
        });
        ethanolRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            public void onCheckedChanged(RadioGroup group, int checkedId){
                int radioPosition = ethanolRadioGroup.indexOfChild(getView().findViewById(checkedId));
                Log.d(null,"Ethanol setting is " + Integer.toString(radioPosition));
                switch(radioPosition){
                    case NO_ETHANOL:
                        originalBrix.setEnabled(false);
                        originalBrix.setInputType(InputType.TYPE_NULL);
                        originalBrix.setFocusable(false);
                        break;
                    case ETHANOL:
                        originalBrix.setEnabled(true);
                        originalBrix.setFocusable(true);
                        break;
                }

            }
        });
        TextWatcher textWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(null,"Hello");
            }
            public void afterTextChanged(Editable s) {
                double input = Double.parseDouble(inputTextField.getText().toString());
                Log.d(null,"You entered " + Double.toString(input));
            }
        };
        inputTextField.addTextChangedListener(textWatcher);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    private void toggleBrixFields(boolean hide){
        RadioGroup ethanolRadioGroup = (RadioGroup) rootView.findViewById(R.id.ethanolRadioGroup);
        EditText originalBrix = (EditText) rootView.findViewById(R.id.originalBrix);

        for(int i = 0; i < ethanolRadioGroup.getChildCount(); i++){
            ((RadioButton)ethanolRadioGroup.getChildAt(i)).setEnabled(hide);
        }
        Log.d(null,"Ethanol status is set to " + Integer.toString(checkEthanolStatus()));
        if(checkEthanolStatus() == ETHANOL && hide == false ) {
            originalBrix.setEnabled(hide);
        }
    }
    private int checkEthanolStatus(){
        RadioGroup ethanolRadioGroup = (RadioGroup) rootView.findViewById(R.id.ethanolRadioGroup);
        return 1;
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

}
