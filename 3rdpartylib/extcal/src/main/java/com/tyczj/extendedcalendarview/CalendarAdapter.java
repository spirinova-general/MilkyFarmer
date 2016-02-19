package com.tyczj.extendedcalendarview;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter {

    static final int FIRST_DAY_OF_WEEK = 0;
    Context context;
    Calendar cal, deletedCal;
    public String[] days;
    public static boolean isPost = false;

    //	OnAddNewEventClick mAddEvent;
    SharedPreferences preferences;
    ExtcalDatabaseHelper db;
    ArrayList<Day> dayList = new ArrayList<Day>();

    public CalendarAdapter(Context context, Calendar cal) {
        this.cal = cal;
        this.context = context;
        cal.set(Calendar.DAY_OF_MONTH, 1);
        db = new ExtcalDatabaseHelper(context);
        refreshDays();
    }

    @Override
    public int getCount() {
        return days.length;
    }

    @Override
    public Object getItem(int position) {
        return dayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public int getPrevMonth() {
        if (cal.get(Calendar.MONTH) == cal.getActualMinimum(Calendar.MONTH)) {
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR - 1));
        } else {

        }
        int month = cal.get(Calendar.MONTH);
        if (month == 0) {
            return month = 11;
        }

        return month - 1;
    }

    public int getMonth() {
        return cal.get(Calendar.MONTH);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (position >= 0 && position < 7) {
            v = vi.inflate(R.layout.day_of_week, null);
            TextView day = (TextView) v.findViewById(R.id.textView1);

            if (position == 0) {
                day.setText(R.string.sunday);
            } else if (position == 1) {
                day.setText(R.string.monday);
            } else if (position == 2) {
                day.setText(R.string.tuesday);
            } else if (position == 3) {
                day.setText(R.string.wednesday);
            } else if (position == 4) {
                day.setText(R.string.thursday);
            } else if (position == 5) {
                day.setText(R.string.friday);
            } else if (position == 6) {
                day.setText(R.string.saturday);
            }

        } else {

            v = vi.inflate(R.layout.day_view, null);
            TextView dayTV = (TextView) v.findViewById(R.id.textView1);
            TextView quantitiTV = (TextView) v.findViewById(R.id.milk_quantity);
            FrameLayout today = (FrameLayout) v.findViewById(R.id.today_frame);
            Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
            Day d = dayList.get(position);
//today
            if (d.getYear() == cal.get(Calendar.YEAR) && d.getMonth() == cal.get(Calendar.MONTH) && d.getDay() == cal.get(Calendar.DAY_OF_MONTH)) {
                today.setVisibility(View.VISIBLE);
                if (getQuantity(d) == 0)
                    quantitiTV.setText("0.0L");
                else
                    quantitiTV.setText(String.valueOf(getQuantity(d)) + "L");


            } else if (d.getYear() <= cal.get(Calendar.YEAR) && d.getMonth() <= cal.get(Calendar.MONTH) && d.getDay() < cal.get(Calendar.DAY_OF_MONTH)
                    ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    today.setBackground(context.getResources().getDrawable(R.drawable.past_days));
                }
                if (getQuantity(d) == 0)
                    quantitiTV.setText("0.0L");
                else
                    quantitiTV.setText(String.valueOf(getQuantity(d)) + "L");

                today.setVisibility(View.VISIBLE);
            } else if (d.getYear() >= cal.get(Calendar.YEAR) && d.getMonth() >= cal.get(Calendar.MONTH) && d.getDay() > cal.get(Calendar.DAY_OF_MONTH)
                    ) {
                if (getQuantity(d) == 0)
                    quantitiTV.setText("0.0L");
                else
                    quantitiTV.setText(String.valueOf(getQuantity(d)) + "L");

                today.setVisibility(View.GONE);
            }


// else if (d.getYear() == cal.get(Calendar.YEAR) && d.getMonth() == cal.get(Calendar.MONTH) && d.getDay() < cal.get(Calendar.DAY_OF_MONTH)
//                    && d.getDay() >= registrationDate) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    today.setBackground(context.getResources().getDrawable(R.drawable.past_days));
//                }
//                quantitiTV.setText(String.valueOf(getQuantity(d)) + "L");
//
//                today.setVisibility(View.VISIBLE);
//            } else if (d.getYear() == cal.get(Calendar.YEAR) && d.getMonth() == cal.get(Calendar.MONTH) && (d.getDay() == 1 || d.getDay() < cal.get(Calendar.DAY_OF_MONTH))
//                    && d.getDay() < registrationDate) {
//                today.setVisibility(View.GONE);
//                quantitiTV.setText(String.valueOf(getQuantity(d)) + "L");
//            } else if (d.getYear() == cal.get(Calendar.YEAR) && d.getMonth() < cal.get(Calendar.MONTH)) {
//                today.setVisibility(View.GONE);
//                quantitiTV.setText(String.valueOf(getQuantity(d)) + "L");
//            } else if ((d.getYear() == cal.get(Calendar.YEAR) && d.getMonth() > cal.get(Calendar.MONTH)) ||d.getYear() > cal.get(Calendar.YEAR)) {
//                quantitiTV.setText(String.valueOf(getQuantity(d)) + "L");
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    today.setBackground(context.getResources().getDrawable(R.drawable.past_days));
//                }
//            } else if (d.getYear() < cal.get(Calendar.YEAR)) {
//                quantitiTV.setText("");
//                today.setVisibility(View.GONE);
//            }
            else {
                if (getQuantity(d) == 0)
                    quantitiTV.setText("0L");
                else
                    quantitiTV.setText(String.valueOf(getQuantity(d)) + "L");
                today.setVisibility(View.GONE);
            }

            RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.rl);
            ImageView iv = (ImageView) v.findViewById(R.id.imageView1);
            ImageView blue = (ImageView) v.findViewById(R.id.imageView2);
            ImageView purple = (ImageView) v.findViewById(R.id.imageView3);
            ImageView green = (ImageView) v.findViewById(R.id.imageView4);
            ImageView orange = (ImageView) v.findViewById(R.id.imageView5);
            ImageView red = (ImageView) v.findViewById(R.id.imageView6);

            blue.setVisibility(View.VISIBLE);
            purple.setVisibility(View.VISIBLE);
            green.setVisibility(View.VISIBLE);
            purple.setVisibility(View.VISIBLE);
            orange.setVisibility(View.VISIBLE);
            red.setVisibility(View.VISIBLE);

            iv.setVisibility(View.VISIBLE);
            dayTV.setVisibility(View.VISIBLE);
            rl.setVisibility(View.VISIBLE);

            Day day = dayList.get(position);

            if (day.getNumOfEvenets() > 0) {
                Set<Integer> colors = day.getColors();

                iv.setVisibility(View.INVISIBLE);
                blue.setVisibility(View.INVISIBLE);
                purple.setVisibility(View.INVISIBLE);
                green.setVisibility(View.INVISIBLE);
                purple.setVisibility(View.INVISIBLE);
                orange.setVisibility(View.INVISIBLE);
                red.setVisibility(View.INVISIBLE);

                if (colors.contains(0)) {
                    iv.setVisibility(View.VISIBLE);
                }
                if (colors.contains(2)) {
                    blue.setVisibility(View.VISIBLE);
                }
                if (colors.contains(4)) {
                    purple.setVisibility(View.VISIBLE);
                }
                if (colors.contains(5)) {
                    green.setVisibility(View.VISIBLE);
                }
                if (colors.contains(3)) {
                    orange.setVisibility(View.VISIBLE);
                }
                if (colors.contains(1)) {
                    red.setVisibility(View.VISIBLE);
                }

            } else {
                iv.setVisibility(View.INVISIBLE);
                blue.setVisibility(View.INVISIBLE);
                purple.setVisibility(View.INVISIBLE);
                green.setVisibility(View.INVISIBLE);
                purple.setVisibility(View.INVISIBLE);
                orange.setVisibility(View.INVISIBLE);
                red.setVisibility(View.INVISIBLE);
            }

            if (day.getDay() == 0) {
                rl.setVisibility(View.GONE);
            } else {
                dayTV.setVisibility(View.VISIBLE);
                dayTV.setText(String.valueOf(day.getDay()));
            }
        }

        return v;
    }

    public void refreshDays() {
        // clear items
        dayList.clear();

        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH) + 7;
        int firstDay = (int) cal.get(Calendar.DAY_OF_WEEK);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        TimeZone tz = TimeZone.getDefault();

        // figure size of the array
        if (firstDay == 1) {
            days = new String[lastDay + (FIRST_DAY_OF_WEEK * 6)];
        } else {
            days = new String[lastDay + firstDay - (FIRST_DAY_OF_WEEK + 1)];
        }

        int j = FIRST_DAY_OF_WEEK;

        // populate empty days before first real day
        if (firstDay > 1) {
            for (j = 0; j < (firstDay - FIRST_DAY_OF_WEEK) + 7; j++) {
                days[j] = "";
                Day d = new Day(context, 0, 0, 0);
                dayList.add(d);
            }
        } else {
            for (j = 0; j < (FIRST_DAY_OF_WEEK * 6) + 7; j++) {
                days[j] = "";
                Day d = new Day(context, 0, 0, 0);
                dayList.add(d);
            }
            j = FIRST_DAY_OF_WEEK * 6 + 1; // sunday => 1, monday => 7
        }

        // populate days
        int dayNumber = 1;

        if (j > 0 && dayList.size() > 0 && j != 1) {
            dayList.remove(j - 1);
        }

        for (int i = j - 1; i < days.length; i++) {
            Day d = new Day(context, dayNumber, year, month);

            Calendar cTemp = Calendar.getInstance();
            cTemp.set(year, month, dayNumber);
            int startDay = Time.getJulianDay(cTemp.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cTemp.getTimeInMillis())));

            d.setAdapter(this);
            d.setStartDay(startDay);

            days[i] = "" + dayNumber;
            dayNumber++;
            dayList.add(d);
        }
    }

    //	public abstract static class OnAddNewEventClick{
//		public abstract void onAddNewEventClick();
//	}
    private static String quantityText;
    private static int registrationDate = 0, registeredMonth = 0, registeredYear = 0;

    public static void setQuantity(final String value) {
        quantityText = value;
    }

    public static void setRegistrationTime(final int value) {
        registrationDate = value;
    }

    //    static float totalQuantity;
    static boolean isForCustomers = false;
    static ArrayList<DateQuantityModel> quantityByDateList, totalData;

//    public static void totalData(final float totalList) {
//        totalQuantity = totalList;
//    }

    public static void quantityByDate(final ArrayList<DateQuantityModel> totalList) {
        quantityByDateList = totalList;
    }

    public static void totalDataList(final ArrayList<DateQuantityModel> totalList) {
        totalData = totalList;
    }

    public static void isForCustomerDelivery(final boolean customer_delivery) {
        isForCustomers = customer_delivery;
    }

    public static void setRegisteredMonth(final int month) {
        registeredMonth = month;
    }

//    static ArrayList<DateQuantityModel> customerMillkQuantity;

//    public static void customersMilkQuantity(ArrayList<DateQuantityModel> list) {
//        customerMillkQuantity = list;
//    }

    public static void setRegisteredYear(final int year) {
        registeredYear = year;
    }

    ArrayList<String> arrayCustId, dateList;
    String FORMAT_DATETIME = "yyyy-MM-dd";
    SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATETIME);
    java.util.Date date = null, deletedDate = null;

//    private BigDecimal getQuantity(Day d) {
//        float quantity = 0;
//        BigDecimal bigDecimal = null;
//        arrayCustId = new ArrayList<>();
//        dateList = new ArrayList<>();
//        //If setting table has daTa
//        if (totalData != null && totalData.size() > 0) {
//            for (int i = 0; i < totalData.size(); ++i) {
//                //If delivery table has data
//                if (quantityByDateList != null && quantityByDateList.size() > 0) {
//
//                    for (int j = 0; j < quantityByDateList.size(); ++j) {
//
//                        try {
//                            date = sdf.parse(quantityByDateList.get(j).getDeliveryDate());
//
//                            cal.setTime(date);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        if (d.getDay() == cal.get(Calendar.DAY_OF_MONTH) && d.getMonth() == cal
//                                .get(Calendar.MONTH) && d.getYear() == cal.get(Calendar.YEAR)) {
//                            if (!dateList.contains(quantityByDateList.get(j).getDeliveryDate() + quantityByDateList.get(j).getCustomerId())) {
//                                quantity = Float.parseFloat(quantityByDateList.get(j).getQuantity()) + quantity;
//                                bigDecimal = round(quantity, 2);
//                                arrayCustId.add(quantityByDateList.get(j).getCustomerId());
//                                dateList.add(quantityByDateList.get(j).getDeliveryDate() + quantityByDateList.get(j).getCustomerId());
//                            } else {
//                                quantity = Float.parseFloat(quantityByDateList.get(j).getQuantity()) + quantity;
//                                bigDecimal = round(quantity, 2);
//                                arrayCustId.add(quantityByDateList.get(j).getCustomerId());
//                                dateList.add(quantityByDateList.get(j).getDeliveryDate() + quantityByDateList.get(j).getCustomerId());
//                            }
//                        } else {
//                            Calendar cal_start = Calendar.getInstance();
//                            Calendar cal_end = Calendar.getInstance();
//                            Calendar delivery_date = Calendar.getInstance();
//                            java.util.Date date_start = null, date_end = null, delivery_end = null;
//
//                            try {
//                                date_start = sdf.parse(totalData.get(i).getStartDate());
//
//                                delivery_end = sdf.parse(totalData.get(i).getDeliveryDate());
//
//                                cal_start.setTime(date_start);
//
//                                delivery_date.setTime(delivery_end);
//
//                                if (!totalData.get(i).getEndDate().equals("0")) {
//                                    date_end = sdf.parse(totalData.get(i).getEndDate());
//                                    cal_end.setTime(date_end);
//                                }
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//
//                            if (d.getDay() >= delivery_date.get(Calendar.DAY_OF_MONTH) && d.getMonth() >= delivery_date.get(Calendar.MONTH) &&
//                                    d.getYear() >= delivery_date.get(Calendar.YEAR)) {
//                                if (arrayCustId.size() > 0 && !arrayCustId.contains(totalData.get(i).getCustomerId())) {
//                                    if (!totalData.get(i).getEndDate().equals("0")) {
//                                        if ((d.getDay() >= cal_start.get(Calendar.DAY_OF_MONTH) && d.getMonth() == cal_start
//                                                .get(Calendar.MONTH) && d.getYear() == cal_start.get(Calendar.YEAR)
//                                                &&
//                                                d.getDay() < cal_end.get(Calendar.DAY_OF_MONTH) && d.getMonth() == cal_end
//                                                .get(Calendar.MONTH) && d.getYear() == cal_end.get(Calendar.YEAR))) {
//                                            quantity = Float.parseFloat(totalData.get(i).getQuantity()) + quantity;
//                                            arrayCustId.add(totalData.get(i).getCustomerId());
//                                        }
//                                    } else if (d.getDay() >= cal_start.get(Calendar.DAY_OF_MONTH) && d.getMonth() == cal_start
//                                            .get(Calendar.MONTH) && d.getYear() == cal_start.get(Calendar.YEAR)) {
//                                        quantity = Float.parseFloat(totalData.get(i).getQuantity()) + quantity;
//                                        bigDecimal = round(quantity, 2);
//                                        arrayCustId.add(totalData.get(i).getCustomerId());
//                                    }
//                                } else {
//                                    //array has no customer info
//                                    if (!totalData.get(i).getEndDate().equals("0")) {
//                                        if ((d.getDay() >= cal_start.get(Calendar.DAY_OF_MONTH) && d.getMonth() == cal_start
//                                                .get(Calendar.MONTH) && d.getYear() == cal_start.get(Calendar.YEAR)
//                                                &&
//                                                d.getDay() < cal_end.get(Calendar.DAY_OF_MONTH) && d.getMonth() == cal_end
//                                                .get(Calendar.MONTH) && d.getYear() == cal_end.get(Calendar.YEAR))) {
//                                            quantity = Float.parseFloat(totalData.get(i).getQuantity()) + quantity;
//                                            bigDecimal = round(quantity, 2);
//                                            arrayCustId.add(totalData.get(i).getCustomerId());
//                                        }
//                                    } else if (d.getDay() >= cal_start.get(Calendar.DAY_OF_MONTH) && d.getMonth() >= cal_start
//                                            .get(Calendar.MONTH) && d.getYear() >= cal_start.get(Calendar.YEAR)) {
//                                        quantity = Float.parseFloat(totalData.get(i).getQuantity()) + quantity;
//                                        bigDecimal = round(quantity, 2);
//                                        arrayCustId.add(totalData.get(i).getCustomerId());
//                                    }
//                                }
//                            }
//                        }
//
//                    }
//                } else {
//                    //get date from settings
//
//                    Calendar cal_start = Calendar.getInstance();
//                    Calendar cal_end = Calendar.getInstance();
//                    Calendar delivery_date = Calendar.getInstance();
//                    java.util.Date date_start = null, date_end = null, delivery_end = null;
//
//                    try {
//                        date_start = sdf.parse(totalData.get(i).getStartDate());
//
//                        delivery_end = sdf.parse(totalData.get(i).getDeliveryDate());
//
//                        cal_start.setTime(date_start);
//
//                        delivery_date.setTime(delivery_end);
//
//                        if (!totalData.get(i).getEndDate().equals("0")) {
//                            date_end = sdf.parse(totalData.get(i).getEndDate());
//                            cal_end.setTime(date_end);
//                        }
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//                    if (d.getDay() >= delivery_date.get(Calendar.DAY_OF_MONTH) && d.getMonth() >= delivery_date.get(Calendar.MONTH) &&
//                            d.getYear() >= delivery_date.get(Calendar.YEAR)) {
//                        if (arrayCustId.size() > 0 && !arrayCustId.contains(totalData.get(i).getCustomerId())) {
//                            if (!totalData.get(i).getEndDate().equals("0")) {
//                                if ((d.getDay() >= cal_start.get(Calendar.DAY_OF_MONTH) && d.getMonth() == cal_start
//                                        .get(Calendar.MONTH) && d.getYear() == cal_start.get(Calendar.YEAR)
//                                        &&
//                                        d.getDay() < cal_end.get(Calendar.DAY_OF_MONTH) && d.getMonth() == cal_end
//                                        .get(Calendar.MONTH) && d.getYear() == cal_end.get(Calendar.YEAR))) {
//                                    quantity = Float.parseFloat(totalData.get(i).getQuantity()) + quantity;
//                                    bigDecimal = round(quantity, 2);
//                                    arrayCustId.add(totalData.get(i).getCustomerId());
//                                }
//                            } else if (d.getDay() >= cal_start.get(Calendar.DAY_OF_MONTH) && d.getMonth() == cal_start
//                                    .get(Calendar.MONTH) && d.getYear() == cal_start.get(Calendar.YEAR)) {
//                                quantity = Float.parseFloat(totalData.get(i).getQuantity()) + quantity;
//                                bigDecimal = round(quantity, 2);
//                                arrayCustId.add(totalData.get(i).getCustomerId());
//                            }
//                        } else {
//                            //array has no customer info
//                            if (!totalData.get(i).getEndDate().equals("0")) {
//                                if ((d.getDay() >= cal_start.get(Calendar.DAY_OF_MONTH) && d.getMonth() == cal_start
//                                        .get(Calendar.MONTH) && d.getYear() == cal_start.get(Calendar.YEAR)
//                                        &&
//                                        d.getDay() < cal_end.get(Calendar.DAY_OF_MONTH) && d.getMonth() == cal_end
//                                        .get(Calendar.MONTH) && d.getYear() == cal_end.get(Calendar.YEAR))) {
//                                    quantity = Float.parseFloat(totalData.get(i).getQuantity()) + quantity;
//                                    arrayCustId.add(totalData.get(i).getCustomerId());
//                                }
//                            } else if (d.getDay() >= cal_start.get(Calendar.DAY_OF_MONTH) && d.getMonth() == cal_start
//                                    .get(Calendar.MONTH) && d.getYear() == cal_start.get(Calendar.YEAR)) {
//                                quantity = Float.parseFloat(totalData.get(i).getQuantity()) + quantity;
//                                bigDecimal = round(quantity, 2);
//                                arrayCustId.add(totalData.get(i).getCustomerId());
//                            }
//                        }
//                    }
//                }
//
//
//            }
//
//
//        }
//
//        if (bigDecimal == null) {
//            bigDecimal = round(0, 2);
//        }
//        return bigDecimal;
//    }


//    private BigDecimal getQuantity(final Day d) {
//        float quantity = 0;
//        BigDecimal bigDecimal = round(quantity, 1);
//        arrayCustId = new ArrayList<>();
//        dateList = new ArrayList<>();
//        Calendar deletedCal = Calendar.getInstance();
//
////If setting table has data
//        if (!isForCustomers) {
//            if (totalData != null && totalData.size() > 0) {
//                for (int i = 0; i < totalData.size(); ++i) {
//                    //Check if deliverydate is same as of calander
//
//                    try {
//                        date = sdf.parse(totalData.get(i).getDeliveryDate());
//
//                        cal.setTime(date);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    if (d.getDay() >= cal.get(Calendar.DAY_OF_MONTH) && d.getMonth() >= cal
//                            .get(Calendar.MONTH) && d.getYear() >= cal.get(Calendar.YEAR)
//                            ) {
//                        if (totalData.get(i).getIs_deleted().equals("0")) {
//                            //If delivery table has data
//                            if (quantityByDateList != null && quantityByDateList.size() > 0) {
//                                for (int j = 0; j < quantityByDateList.size(); j++) {
//                                    try {
//                                        date = sdf.parse(quantityByDateList.get(j).getDeliveryDate());
//
//                                        cal.setTime(date);
//                                    } catch (ParseException e) {
//                                        e.printStackTrace();
//                                    }
//                                    if (d.getDay() == cal.get(Calendar.DAY_OF_MONTH) && d.getMonth() == cal
//                                            .get(Calendar.MONTH) && d.getYear() == cal.get(Calendar.YEAR)
//                                            && totalData.get(i).getCustomerId().equals(quantityByDateList.get(j).getCustomerId())) {
//                                        quantity = Float.parseFloat(quantityByDateList.get(j).getQuantity());
//                                        bigDecimal = round(quantity, 1);
//                                        arrayCustId.add(totalData.get(i).getCustomerId());
//                                    } else {
//                                        //get data from customer setting table
//                                        Calendar cal_start = Calendar.getInstance();
//                                        Calendar cal_end = Calendar.getInstance();
//                                        Calendar delivery_date = Calendar.getInstance();
//                                        java.util.Date date_start = null, date_end = null, delivery_end = null;
//
//                                        try {
//                                            date_start = sdf.parse(totalData.get(i).getStartDate());
//
//                                            delivery_end = sdf.parse(totalData.get(i).getDeliveryDate());
//
//                                            cal_start.setTime(date_start);
//
//                                            delivery_date.setTime(delivery_end);
//
//                                            if (!totalData.get(i).getEndDate().equals("0")) {
//                                                date_end = sdf.parse(totalData.get(i).getEndDate());
//                                                cal_end.setTime(date_end);
//                                            }
//                                        } catch (ParseException e) {
//                                            e.printStackTrace();
//                                        }
//                                        //array has no customer info
//                                        if (totalData.get(i).getEndDate().equals("0")) {
//                                            if (!arrayCustId.contains(totalData.get(i).getCustomerId())) {
//                                                if (d.getDay() >= cal_start.get(Calendar.DAY_OF_MONTH) && d.getMonth() == cal_start
//                                                        .get(Calendar.MONTH) && d.getYear() == cal_start.get(Calendar.YEAR)) {
//                                                    quantity = Float.parseFloat(totalData.get(i).getQuantity()) + quantity;
//                                                    bigDecimal = round(quantity, 1);
//                                                    arrayCustId.add(totalData.get(i).getCustomerId());
//                                                }
//                                            }
//                                        } else if ((d.getDay() >= cal_start.get(Calendar.DAY_OF_MONTH) && d.getMonth() == cal_start
//                                                .get(Calendar.MONTH) && d.getYear() == cal_start.get(Calendar.YEAR)
//                                                &&
//                                                d.getDay() < cal_end.get(Calendar.DAY_OF_MONTH) && d.getMonth() == cal_end
//                                                .get(Calendar.MONTH) && d.getYear() == cal_end.get(Calendar.YEAR))) {
//                                            quantity = Float.parseFloat(totalData.get(i).getQuantity()) + quantity;
//                                            arrayCustId.add(totalData.get(i).getCustomerId());
//
//                                        }
//                                    }
//
//                                }
//                            } else {
//                                //get data from customer setting table
//                                Calendar cal_start = Calendar.getInstance();
//                                Calendar cal_end = Calendar.getInstance();
//                                Calendar delivery_date = Calendar.getInstance();
//                                java.util.Date date_start = null, date_end = null, delivery_end = null;
//
//                                try {
//                                    date_start = sdf.parse(totalData.get(i).getStartDate());
//
//                                    delivery_end = sdf.parse(totalData.get(i).getDeliveryDate());
//
//                                    cal_start.setTime(date_start);
//
//                                    delivery_date.setTime(delivery_end);
//
//                                    if (!totalData.get(i).getEndDate().equals("0")) {
//                                        date_end = sdf.parse(totalData.get(i).getEndDate());
//                                        cal_end.setTime(date_end);
//                                    }
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                                //array has no customer info
//                                if (totalData.get(i).getEndDate().equals("0")) {
//                                    if (d.getDay() >= cal_start.get(Calendar.DAY_OF_MONTH) && d.getMonth() == cal_start
//                                            .get(Calendar.MONTH) && d.getYear() == cal_start.get(Calendar.YEAR)) {
//                                        quantity = Float.parseFloat(totalData.get(i).getQuantity()) + quantity;
//                                        bigDecimal = round(quantity, 1);
//                                        arrayCustId.add(totalData.get(i).getCustomerId());
//                                    }
//                                } else if ((d.getDay() >= cal_start.get(Calendar.DAY_OF_MONTH) && d.getMonth() == cal_start
//                                        .get(Calendar.MONTH) && d.getYear() == cal_start.get(Calendar.YEAR)
//                                        &&
//                                        d.getDay() < cal_end.get(Calendar.DAY_OF_MONTH) && d.getMonth() == cal_end
//                                        .get(Calendar.MONTH) && d.getYear() == cal_end.get(Calendar.YEAR))) {
//                                    quantity = Float.parseFloat(totalData.get(i).getQuantity()) + quantity;
//                                    arrayCustId.add(totalData.get(i).getCustomerId());
//
//                                }
//                            }
//                        } else {/*Customer has been deletd
//                */
//                            try {
//                                deletedDate = sdf.parse(totalData.get(i).getDateModified());
//
//                                deletedCal.setTime(deletedDate);
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                            if (quantityByDateList != null && quantityByDateList.size() > 0) {
//                                for (int j = 0; j < quantityByDateList.size(); j++) {
//                                    try {
//                                        date = sdf.parse(quantityByDateList.get(j).getDeliveryDate());
//
//                                        cal.setTime(date);
//                                    } catch (ParseException e) {
//                                        e.printStackTrace();
//                                    }
//                                    if (d.getDay() < deletedCal.get(Calendar.DAY_OF_MONTH) && d.getMonth() <= deletedCal.get(Calendar.MONTH)
//                                            && d.getYear() <= deletedCal.get(Calendar.YEAR)) {
//                                        if (d.getDay() == cal.get(Calendar.DAY_OF_MONTH) && d.getMonth() == cal
//                                                .get(Calendar.MONTH) && d.getYear() == cal.get(Calendar.YEAR)
//                                                && totalData.get(i).getCustomerId().equals(quantityByDateList.get(j).getCustomerId())) {
//                                            quantity = Float.parseFloat(quantityByDateList.get(j).getQuantity());
//                                            bigDecimal = round(quantity, 1);
//                                        }
//
//
//                                    }
//
//                                }
//                            }
//
//
//                        }
//                    } else {
//                        try {
//                            date = sdf.parse(totalData.get(i).getDeliveryDate());
//
//                            cal.setTime(date);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        /*set previous dates 0*/
//                        if ((d.getDay() == 1 || d.getDay() < cal.get(Calendar.DAY_OF_MONTH)) && d.getMonth() == cal.get(Calendar.MONTH)
//                                && d.getYear() == cal.get(Calendar.YEAR)) {
//                            bigDecimal = round(0, 1);
//                        }
//                    }
//
//
//                }
//            } else {
//                //No data is in datanbase
//            }
//        } else {
//            /*For customer's delivery*/
//            if (customerMillkQuantity != null && customerMillkQuantity.size() > 0) {
//
//                for (int i = 0; i < customerMillkQuantity.size(); i++) {
//                    Calendar cal_start = Calendar.getInstance();
//                    java.util.Date deliveryDate = null;
//
//                    try {
//                        deliveryDate = sdf.parse(customerMillkQuantity.get(i).getDate());
//
//
//                        cal_start.setTime(deliveryDate);
//
//
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    // Date matches
//                    if (d.getDay() == cal_start.get(Calendar.DAY_OF_MONTH) && d.getMonth() == cal_start
//                            .get(Calendar.MONTH) && d.getYear() == cal_start.get(Calendar.YEAR)) {
//                        quantity = Float.parseFloat(customerMillkQuantity.get(i).getQuantity());
//                        bigDecimal = round(quantity, 1);
//
//                    } else {
//                        quantity = totalQuantity;
//                        bigDecimal = round(quantity, 1);
//                    }
//                }
//
//            } else {
//                quantity = totalQuantity;
//                bigDecimal = round(quantity, 1);
//            }
//
//        }
//


///*//Check if deleted customer
//        if(totaldata.getIsDeletedCustomer().equals("1"))
//        {
//
//        }*/
//
//
//        return bigDecimal;
//    }
    BigDecimal bigDecimal = null;

//    private BigDecimal getQuantity(final Day d) {
//        Calendar cal = Calendar.getInstance();
//        if (!isForCustomers) {
//            if (totalData != null)
//                for (int i = 0; i < totalData.size(); ++i) {
//                    //Check if deliverydate is same as of calander
//
//                    try {
//                        date = sdf.parse(totalData.get(i).getDeliveryDate());
//
//                        cal.setTime(date);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    if (d.getDay() == cal.get(Calendar.DAY_OF_MONTH) && d.getMonth() == cal
//                            .get(Calendar.MONTH) && d.getYear() == cal.get(Calendar.YEAR)
//                            ) {
//                        bigDecimal = totalData.get(i).getCalculatedQuqantity();
//                        return bigDecimal;
//                    } else {
//
//                    }
//                }
//        } else if (customerMillkQuantity != null) {
//            for (int i = 0; i < customerMillkQuantity.size(); ++i) {
//                //Check if deliverydate is same as of calander
//
//                try {
//                    date = sdf.parse(customerMillkQuantity.get(i).getDeliveryDate());
//
//                    cal.setTime(date);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                if (d.getDay() == cal.get(Calendar.DAY_OF_MONTH) && d.getMonth() == cal
//                        .get(Calendar.MONTH) && d.getYear() == cal.get(Calendar.YEAR)
//                        ) {
//                    bigDecimal = customerMillkQuantity.get(i).getCalculatedQuqantity();
//                    return bigDecimal;
//                } else {
//
//                }
//            }
//        }
//
//        return bigDecimal;
//    }

    private static String custId = "";

    public static void setcustId(String id) {
        custId = id;
    }

    private double getQuantity(Day day) {
        double qty = 0, adjustedQty = 0;
        DeliveryTableManagement.custIds.clear();
        String date = day.getYear() + "-" + String.format("%02d", day.getMonth() + 1) + "-" + String.format("%02d", day.getDay());
        if (!isForCustomers) {
            if (DeliveryTableManagement.isDeletedCustomer(db.getReadableDatabase(), date))
                qty = DeliveryTableManagement.getQuantityOfDayByDate(db.getReadableDatabase(), date);

            if (db.isTableNotEmpty("customers")) {
                if (DeliveryTableManagement.custIds != null) {
                    if (DeliveryTableManagement.custIds.size() > 0)
                        for (int i = 0; i < DeliveryTableManagement.custIds.size(); i++)
                            //TODO ExtCal SETTINGS DB
                            adjustedQty += ExtcalCustomerSettingTableManagement.getAllCustomersByCustId(db.getReadableDatabase(), date
                                    , DeliveryTableManagement.custIds.get(i));
                    //            adjustedQty += CustomerSettingTableManagement.getAllCustomersByCustId(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), day
                    //                    , custIds.get(i));
                    //TODO ExtCal SETTINGS DB
                    //            qty += CustomerSettingTableManagement.getAllCustomersByDay(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), day) - adjustedQty;
                    qty += ExtcalCustomerSettingTableManagement.getAllCustomersByDay(db.getReadableDatabase(), date) - adjustedQty;
                }

            }

//            bigDecimal = BigDecimal.valueOf(ExtcalCustomerSettingTableManagement.getAllCustomersByDay(db.getReadableDatabase(), String.valueOf(day.getYear()) + "-"
//                    + String.format("%02d", day.getMonth() + 1) + "-" + String.format("%02d", day.getDay())));
        } else {
            if (db.isTableNotEmpty("delivery")) {
                if (DeliveryTableManagement.getQuantityOfDayByDateForCustomer(db.getReadableDatabase(), date, custId) == 0) {
                    if (db.isTableNotEmpty("customers")) {
                    //TODO ExtCal SETTINGS DB
                    //                    qty = CustomerSettingTableManagement.getAllCustomersByCustId(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), day
                    //                            , custId);
                        qty = ExtcalCustomerSettingTableManagement.getAllCustomersByCustId(db.getReadableDatabase(), date
                                , custId);

                    }
                } else
                    qty = DeliveryTableManagement.getQuantityOfDayByDateForCustomer(db.getReadableDatabase(), date, custId);


            } else if (db.isTableNotEmpty("customers")) {

//            qty = CustomerSettingTableManagement.getAllCustomersByCustId(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), day
//                    , custId);
                //TODO ExtCal SETTINGS DB
                qty = ExtcalCustomerSettingTableManagement.getAllCustomersByCustId(db.getReadableDatabase(), date
                        , custId);

            }


        }
        return qty;
    }


}
