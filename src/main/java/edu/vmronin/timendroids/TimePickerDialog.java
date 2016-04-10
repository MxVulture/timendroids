package edu.vmronin.timendroids;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.ViewSwitcher;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by vmacias on 4/9/16.
 */

public class TimePickerDialog extends BaseDialog implements DialogInterface.OnClickListener {

	public static ITimePickerListener NO_OP_LISTENER = new ITimePickerListener() {
		@Override
		public void onDateTimeSelected(Date date) {
			System.out.println(date);
		}

		@Override
		public void onCancel() {
			System.out.println("Cancel");
		}
	};

	@Override
	public void onClick(DialogInterface dialog, int which) {
		System.out.println(which);

	}

	@Override
	public void onStart(){
		super.onStart();
		AlertDialog d = (AlertDialog)getDialog();
		if(d != null) {
			final Button positiveButton = d.getButton(DialogInterface.BUTTON_POSITIVE);

			final Button negativeButton = d.getButton(DialogInterface.BUTTON_NEGATIVE);

			negativeButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					if (viewSwitcher.getDisplayedChild() == 0) {

						ITimePickerListener listener = TimePickerDialog.this.listener.get();

						if (listener != null){
							listener.onCancel();
						}

						dismissAllowingStateLoss();

					} else {
						positiveButton.setText("Siguiente");
						negativeButton.setText("Cancelar");

						viewSwitcher.setDisplayedChild(0);
					}

				}

            });

			positiveButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (viewSwitcher.getDisplayedChild() == 0) {
						viewSwitcher.setDisplayedChild(1);

						positiveButton.setText("Continuar");
						negativeButton.setText("Volver");
					} else {

						ITimePickerListener listener = TimePickerDialog.this.listener.get();

						if (listener != null){

							Calendar c = Calendar.getInstance();

							int hour, min;
							int year, month, day;

							year = datePicker.getYear();
							month = datePicker.getMonth();
							day = datePicker.getDayOfMonth();

							// TODO Al movernos a API23 refactorizar las llamadas del timepicker

							hour = timePicker.getCurrentHour();
							min = timePicker.getCurrentMinute();

							c.set(Calendar.YEAR, year);
							c.set(Calendar.MONTH, month);
							c.set(Calendar.DAY_OF_MONTH, day);

							c.set(Calendar.HOUR_OF_DAY, hour);
							c.set(Calendar.MINUTE, min);
							c.set(Calendar.SECOND, 0);
							c.set(Calendar.MILLISECOND, 0);

							listener.onDateTimeSelected(c.getTime());
						}

						dismissAllowingStateLoss();

					}

				}
			});
		}


	}

	interface ITimePickerListener{
		void onDateTimeSelected(Date date);
		void onCancel();
	}

	public static final String TITLE_RES = "com.vmronin.timendroids.TITLE_RES";
	public static final String THEME_RES = "com.vmronin.timendroids.THEME_RES";

	private TimePicker timePicker;
	private DatePicker datePicker;
	private ViewSwitcher viewSwitcher;
	private WeakReference<ITimePickerListener> listener;

	public static TimePickerDialog newInstance(Bundle args, ITimePickerListener listener){
		TimePickerDialog pirate = new TimePickerDialog();

		if (args == null){
			args = new Bundle(); // Arg!
		}

		if (!args.containsKey(TITLE_RES)){
			args.putInt(TITLE_RES, R.string.timepicker_def_title); // Args!
		}


		pirate.setArguments(args); // Ahoy!

		pirate.listener = new WeakReference<>(listener);

		return pirate;

	}

	@NonNull
	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		Bundle args = getArguments();

		AlertDialog.Builder builder = BaseDialog.getThemedAlertBuilder(getActivity(), args);

		View baseView = LayoutInflater.from(getActivity()).inflate(R.layout.timepickerfragment, null);

		viewSwitcher = (ViewSwitcher) baseView.findViewById(R.id.vSwitchTimePicker);
		timePicker = (TimePicker) baseView.findViewById(R.id.timePicker);
		datePicker = (DatePicker) baseView.findViewById(R.id.datePicker);



		int titleID = args.getInt(TITLE_RES);
		builder.setTitle(titleID);
		builder.setView(baseView);


		builder.setPositiveButton("Siguiente", this);

		builder.setNegativeButton("Cancelar", this);

		return builder.create();
	}






}
