package tdls.todolistapps;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import java.util.Calendar;

/**
 * Created by My name Is Hardline on 2/26/2016.
 */
public class PickerDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)

    {   DateSettings dateSettings = new DateSettings(getActivity());
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayofmonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog;
        dialog = new DatePickerDialog(getActivity(),dateSettings,year,month,dayofmonth);
        return dialog;
    }
}


