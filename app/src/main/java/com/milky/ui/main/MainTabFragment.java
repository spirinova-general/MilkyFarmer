package com.milky.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.milky.R;

/**
 * Created by Neha on 11/17/2015.
 */
public class MainTabFragment extends Fragment {
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*
         call the method setHasOptionsMenu, to have access to the menu from your fragment
        * */
        setHasOptionsMenu(true);

        /**
         *Inflate tab_layout and setup Views.
         */
        View x = inflater.inflate(R.layout.tab_layout, null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
//        BadgeView badgeView = new BadgeView(getActivity(),tabLayout,5);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);
        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                MainActivity.POSITION = viewPager.getCurrentItem();
                getActivity().supportInvalidateOptionsMenu();
//                switch (MainActivity.POSITION)
//                {
//                    case 0:
//                        calenderFragment.onTabChange();
//                        break;
//
//                    case 1:
//                        customersFragment.onTabChange();
//                        break;
//
//                    case 2:
//                        billingFragment.onTabChange();
//                        break;
//                }
            }
        });
        return x;

    }

    public static CalenderFragment calenderFragment;
    public static CustomersFragment customersFragment;
    public static BillingFragment billingFragment;

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    calenderFragment = new CalenderFragment();
                    return calenderFragment;
                case 1:
                    customersFragment = new CustomersFragment();
                    return customersFragment;
                case 2:
                    billingFragment = new BillingFragment();
                    return billingFragment;
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Delivery";
                case 1:
                    return "Customers";
                case 2:
                    return "Billing";
            }
            return null;
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }
}
