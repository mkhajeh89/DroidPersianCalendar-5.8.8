package com.mahzadiran.persiancalendar.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mahzadiran.persiancalendar.R;
import com.mahzadiran.persiancalendar.util.Utils;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import calendar.CivilDate;
import calendar.DateConverter;
import calendar.IslamicDate;
import calendar.PersianDate;

/**
 * Program activity for android
 *
 * @author ebraminio
 */
public class ConverterFragment extends Fragment implements
        AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Spinner calendarTypeSpinner;
    private Spinner yearSpinner;
    private Spinner monthSpinner;
    private Spinner daySpinner;
    private int startingYearOnYearSpinner = 0;
    private TextView weekDayName;
    private TextView shamsiDateLinear;
    private TextView shamsiDateDay;
    private TextView shamsiDate;
    private TextView gregorianDateLinear;
    private TextView gregorianDateDay;
    private TextView gregorianDate;
    private TextView islamicDateLinear;
    private TextView islamicDateDay;
    private TextView islamicDate;
    private TextView diffDate;
    private CardView calendarsCard;

    LinearLayoutCompat shamsiContainer;
    LinearLayoutCompat gregorianContainer;
    LinearLayoutCompat islamicContainer;

    private TextView todayText;
    private AppCompatImageView todayIcon;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Utils.setActivityTitleAndSubtitle(getActivity(), getString(R.string.date_converter), "");

        View view = inflater.inflate(R.layout.fragment_converter, container, false);

        AppCompatImageView iv = view.findViewById(R.id.calendars_card_icon);
        iv.setImageResource(R.drawable.ic_swap_vertical_circle);

        todayText = view.findViewById(R.id.today);
        todayIcon = view.findViewById(R.id.today_icon);
        todayText.setVisibility(View.GONE);
        todayIcon.setVisibility(View.GONE);
        todayText.setOnClickListener(this);
        todayIcon.setOnClickListener(this);

        // fill members
        calendarTypeSpinner = view.findViewById(R.id.calendarTypeSpinner);
        yearSpinner = view.findViewById(R.id.yearSpinner);
        monthSpinner = view.findViewById(R.id.monthSpinner);
        daySpinner = view.findViewById(R.id.daySpinner);

        weekDayName = view.findViewById(R.id.week_day_name);

        shamsiContainer = view.findViewById(R.id.shamsi_container);
        gregorianContainer = view.findViewById(R.id.gregorian_container);
        islamicContainer = view.findViewById(R.id.islamic_container);

        shamsiDateLinear = view.findViewById(R.id.shamsi_date_linear);
        shamsiDateDay = view.findViewById(R.id.shamsi_date_day);
        shamsiDate = view.findViewById(R.id.shamsi_date);
        gregorianDateLinear = view.findViewById(R.id.gregorian_date_linear);
        gregorianDateDay = view.findViewById(R.id.gregorian_date_day);
        gregorianDate = view.findViewById(R.id.gregorian_date);
        islamicDateLinear = view.findViewById(R.id.islamic_date_linear);
        islamicDateDay = view.findViewById(R.id.islamic_date_day);
        islamicDate = view.findViewById(R.id.islamic_date);
        // Hide the button, we don't need it here
        view.findViewById(R.id.more_calendar).setVisibility(View.GONE);

        shamsiDateLinear.setOnClickListener(this);
        shamsiDateDay.setOnClickListener(this);
        shamsiDate.setOnClickListener(this);
        gregorianDateLinear.setOnClickListener(this);
        gregorianDateDay.setOnClickListener(this);
        gregorianDate.setOnClickListener(this);
        islamicDateLinear.setOnClickListener(this);
        islamicDateDay.setOnClickListener(this);
        islamicDate.setOnClickListener(this);

        calendarsCard = view.findViewById(R.id.calendars_card);

        diffDate = view.findViewById(R.id.diff_date);

        // fill views
        calendarTypeSpinner.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.calendar_type)));

        calendarTypeSpinner.setSelection(Utils.positionFromCalendarType(Utils.getMainCalendar()));
        startingYearOnYearSpinner = Utils.fillYearMonthDaySpinners(getContext(),
                calendarTypeSpinner, yearSpinner, monthSpinner, daySpinner);

        calendarTypeSpinner.setOnItemSelectedListener(this);

        yearSpinner.setOnItemSelectedListener(this);
        monthSpinner.setOnItemSelectedListener(this);
        daySpinner.setOnItemSelectedListener(this);
        //
        return view;
    }

    private void fillCalendarInfo() {
        int year = startingYearOnYearSpinner + yearSpinner.getSelectedItemPosition();
        int month = monthSpinner.getSelectedItemPosition() + 1;
        int day = daySpinner.getSelectedItemPosition() + 1;

        CivilDate civilDate = null;
        PersianDate persianDate;
        IslamicDate hijriDate;

        try {
            shamsiContainer.setVisibility(View.VISIBLE);
            gregorianContainer.setVisibility(View.VISIBLE);
            islamicContainer.setVisibility(View.VISIBLE);

            switch (Utils.calendarTypeFromPosition(calendarTypeSpinner.getSelectedItemPosition())) {
                case GREGORIAN:
                    civilDate = new CivilDate(year, month, day);
                    hijriDate = DateConverter.civilToIslamic(civilDate, 0);
                    persianDate = DateConverter.civilToPersian(civilDate);
                    gregorianContainer.setVisibility(View.GONE);
                    break;

                case ISLAMIC:
                    hijriDate = new IslamicDate(year, month, day);
                    civilDate = DateConverter.islamicToCivil(hijriDate);
                    persianDate = DateConverter.islamicToPersian(hijriDate);
                    islamicContainer.setVisibility(View.GONE);
                    break;

                case SHAMSI:
                default:
                    persianDate = new PersianDate(year, month, day);
                    civilDate = DateConverter.persianToCivil(persianDate);
                    hijriDate = DateConverter.persianToIslamic(persianDate);
                    shamsiContainer.setVisibility(View.GONE);
                    break;
            }


            weekDayName.setText(Utils.getWeekDayName(persianDate));

            shamsiDateLinear.setText(Utils.toLinearDate(persianDate));
            shamsiDateDay.setText(Utils.formatNumber(persianDate.getDayOfMonth()));
            shamsiDate.setText(Utils.getMonthName(persianDate) + "\n" + Utils.formatNumber(persianDate.getYear()));

            gregorianDateLinear.setText(Utils.toLinearDate(civilDate));
            gregorianDateDay.setText(Utils.formatNumber(civilDate.getDayOfMonth()));
            gregorianDate.setText(Utils.getMonthName(civilDate) + "\n" + Utils.formatNumber(civilDate.getYear()));

            islamicDateLinear.setText(Utils.toLinearDate(hijriDate));
            islamicDateDay.setText(Utils.formatNumber(hijriDate.getDayOfMonth()));
            islamicDate.setText(Utils.getMonthName(hijriDate) + "\n" + Utils.formatNumber(hijriDate.getYear()));

            calendarsCard.setVisibility(View.VISIBLE);

            PersianDate today = Utils.getToday();
            long diffDays = Math.abs(DateConverter.persianToJdn(today) - DateConverter.persianToJdn(persianDate));
            CivilDate civilBase = new CivilDate(2000, 1, 1);
            CivilDate civilOffset = DateConverter.jdnToCivil(diffDays + DateConverter.civilToJdn(civilBase));
            int yearDiff = civilOffset.getYear() - 2000;
            int monthDiff = civilOffset.getMonth() - 1;
            int dayOfMonthDiff = civilOffset.getDayOfMonth() - 1;
            diffDate.setText(String.format(getString(R.string.date_diff_text),
                    Utils.formatNumber((int) diffDays),
                    Utils.formatNumber(yearDiff),
                    Utils.formatNumber(monthDiff),
                    Utils.formatNumber(dayOfMonthDiff)));
            diffDate.setVisibility(diffDays == 0 ? View.GONE : View.VISIBLE);
            this.todayText.setVisibility(diffDays == 0 ? View.GONE : View.VISIBLE);
            todayIcon.setVisibility(diffDays == 0 ? View.GONE : View.VISIBLE);

        } catch (RuntimeException e) {
            calendarsCard.setVisibility(View.GONE);
            Toast.makeText(getContext(), getString(R.string.date_exception), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.yearSpinner:
            case R.id.monthSpinner:
            case R.id.daySpinner:
                fillCalendarInfo();
                break;

            case R.id.calendarTypeSpinner:
                startingYearOnYearSpinner = Utils.fillYearMonthDaySpinners(getContext(),
                        calendarTypeSpinner, yearSpinner, monthSpinner, daySpinner);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.shamsi_date:
            case R.id.shamsi_date_day:
                Utils.copyToClipboard(getContext(), shamsiDateDay.getText() + " " +
                        shamsiDate.getText().toString().replace("\n", " "));
                break;

            case R.id.shamsi_date_linear:
                Utils.copyToClipboard(getContext(), shamsiDateLinear.getText());
                break;

            case R.id.gregorian_date:
            case R.id.gregorian_date_day:
                Utils.copyToClipboard(getContext(), gregorianDateDay.getText() + " " +
                        gregorianDate.getText().toString().replace("\n", " "));
                break;

            case R.id.gregorian_date_linear:
                Utils.copyToClipboard(getContext(), gregorianDateLinear.getText());
                break;

            case R.id.islamic_date:
            case R.id.islamic_date_day:
                Utils.copyToClipboard(getContext(), islamicDateDay.getText() + " " +
                        islamicDate.getText().toString().replace("\n", " "));
                break;

            case R.id.islamic_date_linear:
                Utils.copyToClipboard(getContext(), islamicDateLinear.getText());
                break;

            case R.id.today:
            case R.id.today_icon:
                startingYearOnYearSpinner = Utils.fillYearMonthDaySpinners(getContext(),
                        calendarTypeSpinner, yearSpinner, monthSpinner, daySpinner);
                break;
        }
    }
}
