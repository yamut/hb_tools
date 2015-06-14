package com.danmattern.hbtools;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.FrameLayout;

import java.util.List;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private int firstRun = 1;

    private String lastFragment;

    private android.support.v4.app.FragmentManager fragmentManager = null;

    private android.support.v4.app.Fragment lastFragmentResource = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }
    public void getActiveFragments(){
        List<Fragment> fragments = fragmentManager.getFragments();
        Fragment thisFragment = null;
        int length = fragments.size();
        for( int i = 0; i < length; i++){
            thisFragment = fragments.get(i);
            switch(thisFragment.getClass().getSimpleName().toString()){
                case "NavigationDrawerFragment":
                case "PlaceholderFragment":
                    break;
                default:
                    android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.remove(thisFragment).commit();
            }
        }
    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        android.support.v4.app.Fragment fragment = null;
        if(fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();

        switch(position){
            case 1:
                fragment = brixToSg.newInstance("","");
                setTitle(R.string.title_brix_to_sg);
                lastFragment = "brixToSg";
                lastFragmentResource = fragment;
                break;
            case 2:
                fragment = ogEstimates.newInstance("","");
                setTitle(R.string.title_og_estimates);
                lastFragment = "ogEstimates";
                lastFragmentResource = fragment;
                break;
            case 3:
                fragment = temperatureConversion.newInstance("","");
                setTitle(R.string.title_temperature_conversion);
                lastFragment = "temperatureConversion";
                lastFragmentResource = fragment;
                break;
            case 4:
                fragment = alphaAcidUnitConversion.newInstance("","");
                setTitle(R.string.title_aa_to_oz);
                lastFragment = "alphaAcidUnitConversion";
                lastFragmentResource = fragment;
                break;
            case 5:
            case 6:
            case 7:
                break;
            default:
                fragment = PlaceholderFragment.newInstance(position+1);
                break;

        }

        int foo = R.id.container;
        FrameLayout container = (FrameLayout)findViewById(R.id.container);
        if( container != null ) {
            container=container;
        }
        ft.replace(R.id.container, fragment, lastFragment).setTransitionStyle(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
    }



    public void onSectionAttached(int number) {
        /*switch (number) {
            case 1:
                mTitle = getString(R.string.title_activity_brix_to_sg);
                break;
            case 2:
                //mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_temperature_conversion);
                break;
        }*/
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends android.support.v4.app.Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }



}
