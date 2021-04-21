package com.yzk18.GUI;

import com.github.lgooddatepicker.components.TimePickerSettings;

class DateTimePickersUtils {
    public static TimePickerSettings createTimePickerSettings()
    {
        TimePickerSettings timePickerSettings = new TimePickerSettings();
        timePickerSettings.setFormatForDisplayTime("HH:mm:ss");
        timePickerSettings.setFormatForMenuTimes("HH:mm:ss");
        return timePickerSettings;
    }
}
