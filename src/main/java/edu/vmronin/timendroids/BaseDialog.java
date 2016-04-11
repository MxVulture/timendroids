package edu.vmronin.timendroids;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by vmacias on 4/9/16.
 */
public class BaseDialog extends DialogFragment {

//	Comentario de prueba

	public static AlertDialog.Builder getThemedAlertBuilder(Context context, Bundle extras) {

		int theme = 0;

		if (extras != null) {
			theme = extras.getInt(TimePickerDialog.THEME_RES);
		}

		if (theme == 0) {
			new AlertDialog.Builder(context);
		}

		return new AlertDialog.Builder(context, theme);


	}

}
