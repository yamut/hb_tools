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
 * {@link ogEstimates.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ogEstimates#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ogEstimates extends Fragment {
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
     * @return A new instance of fragment ogEstimates.
     */
    // TODO: Rename and change types and number of parameters
    public static ogEstimates newInstance(String param1, String param2) {
        ogEstimates fragment = new ogEstimates();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ogEstimates() {
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
        rootView = inflater.inflate(R.layout.fragment_og_estimates, container, false);

        TextView abvText = (TextView)rootView.findViewById(R.id.potentialAbvLabel);
        TextView abwText = (TextView)rootView.findViewById(R.id.potentialAbwLabel);

        EditText inputOg = (EditText)rootView.findViewById(R.id.ogInput);
        EditText attenuationPercent = (EditText)rootView.findViewById(R.id.attenuationPercent);

        TextWatcher inputOgTextWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) {
                if( getOgInputValue() == 0 || getAttenuationPercent() == 0 ){
                    setAbvLabelText(0);
                    setAbwLabelText(0);
                }
                else{
                    setAbvLabelText(calculatePotentialAlcoholByVolume(getOgInputValue(),getAttenuationPercent()));
                    setAbwLabelText(calculatePotentialAlcoholByWeight(calculatePotentialAlcoholByVolume(getOgInputValue(),getAttenuationPercent())));
                }
            }
        };
        TextWatcher attenuationPercentTextWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) {
                if( getOgInputValue() == 0 || getAttenuationPercent() == 0 ){
                    setAbvLabelText(0);
                    setAbwLabelText(0);
                }
                else{
                    setAbvLabelText(calculatePotentialAlcoholByVolume(getOgInputValue(),getAttenuationPercent()));
                    setAbwLabelText(calculatePotentialAlcoholByWeight(calculatePotentialAlcoholByVolume(getOgInputValue(),getAttenuationPercent())));
                }
            }
        };

        inputOg.addTextChangedListener(inputOgTextWatcher);
        attenuationPercent.addTextChangedListener(attenuationPercentTextWatcher);

        return rootView;
    }

    private double getOgInputValue(){
        EditText inputOg = (EditText)rootView.findViewById(R.id.ogInput);
        return (inputOg.getText().toString().isEmpty() == true ) ? 0.0 : Double.parseDouble(inputOg.getText().toString());
    }
    private double getAttenuationPercent(){
        EditText attenuationPercent = (EditText)rootView.findViewById(R.id.attenuationPercent);
        return (attenuationPercent.getText().toString().isEmpty() == true ) ? 0.0 : Double.parseDouble(attenuationPercent.getText().toString());
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

    private double calculatePotentialAlcoholByVolume(double og, double attenuation){
        double adjusted_final_gravity = ( og - 1.0 ) * ( attenuation / 100.0 );
        return ( ( 76.08 * ( og - adjusted_final_gravity ) / ( 1.775 - og ) ) * ( adjusted_final_gravity / 0.794 ) );
    }

    private double calculatePotentialAlcoholByWeight( double abv ){
        return abv * 0.79336;
    }

    private void setAbvLabelText(double abv){
        TextView abvTextLabel = (TextView)rootView.findViewById(R.id.potentialAbvLabel);
        String abvOutput = ( Double.toString(abv) == null ) ? "0" : new BigDecimal(Double.toString(abv)).setScale(2, RoundingMode.HALF_UP).toString();

        abvTextLabel.setText("Potential ABV: " + abvOutput + "%");
    }
    private void setAbwLabelText(double abw){
        TextView abwTextLabel = (TextView)rootView.findViewById(R.id.potentialAbwLabel);
        String abwOutput = (Double.toString(abw) == null ) ? "0" : new BigDecimal(Double.toString(abw)).setScale(2,RoundingMode.HALF_UP).toString();
        abwTextLabel.setText("Potential ABW: " + abwOutput + "%");
    }

}
