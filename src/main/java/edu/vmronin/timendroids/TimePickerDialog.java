package edu.vmronin.timendroids;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
	public void onStart() {
		super.onStart();
		final AlertDialog d = (AlertDialog) getDialog();

		if (d != null) {

			final Button positiveButton = d.getButton(DialogInterface.BUTTON_POSITIVE);

			final Button negativeButton = d.getButton(DialogInterface.BUTTON_NEGATIVE);

			final Bundle args = getArguments();

			negativeButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					if (viewSwitcher.getDisplayedChild() == 0) {

						ITimePickerListener listener = TimePickerDialog.this.listener.get();

						if (listener != null) {
							listener.onCancel();
						}

						dismissAllowingStateLoss();

					} else {

						int title = args.getInt(TITLE_DATE_TEXT);

						int next = args.getInt(NEXT_TEXT);
						int cancel = args.getInt(CANCEL_TEXT);

						positiveButton.setText(next);
						negativeButton.setText(cancel);

						d.setTitle(title);

						viewSwitcher.setDisplayedChild(0);
					}

				}

			});

			positiveButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (viewSwitcher.getDisplayedChild() == 0) {
						viewSwitcher.setDisplayedChild(1);

						int done = args.getInt(DONE_TEXT);
						int back = args.getInt(BACK_TEXT);

						int title = args.getInt(TITLE_TIME_TEXT);

						positiveButton.setText(done);
						negativeButton.setText(back);


						d.setTitle(title);

					} else {

						ITimePickerListener listener = TimePickerDialog.this.listener.get();

						if (listener != null) {

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

	interface ITimePickerListener {
		void onDateTimeSelected(Date date);

		void onCancel();
	}

	public static final String THEME_RES = "com.vmronin.timendroids.THEME_RES";

	public static final String TITLE_DATE_TEXT = "com.vmronin.timendroids.TITLE_DATE_TEXT";
	public static final String TITLE_TIME_TEXT = "com.vmronin.timendroids.TITLE_TIME_TEXT";
	public static final String NEXT_TEXT = "com.vmronin.timendroids.NEXT_TEXT";
	public static final String DONE_TEXT = "com.vmronin.timendroids.DONE_TEXT";
	public static final String BACK_TEXT = "com.vmronin.timendroids.BACK_TEXT";
	public static final String CANCEL_TEXT = "com.vmronin.timendroids.CANCEL_TEXT";

	public static final String IN_ANIMATION = "com.vmronin.timendroids.IN_ANIMATION";
	public static final String OUT_ANIMATION = "com.vmronin.timendroids.OUT_ANIMATION";

	private TimePicker timePicker;
	private DatePicker datePicker;
	private ViewSwitcher viewSwitcher;
	private WeakReference<ITimePickerListener> listener;

	public static TimePickerDialog newInstance(Bundle args, ITimePickerListener listener) {
		TimePickerDialog pirate = new TimePickerDialog();

		if (args == null) {
			args = new Bundle(); // Arg!
		}

		if (!args.containsKey(TITLE_DATE_TEXT)) {
			args.putInt(TITLE_DATE_TEXT, R.string.timendroids_title_date); // Args!
		}

		if (!args.containsKey(TITLE_TIME_TEXT)) {
			args.putInt(TITLE_TIME_TEXT, R.string.timendroids_title_time); // Args!
		}

		if (!args.containsKey(NEXT_TEXT)) {
			args.putInt(NEXT_TEXT, R.string.timendroids_next); // Args!
		}

		if (!args.containsKey(DONE_TEXT)) {
			args.putInt(DONE_TEXT, R.string.timendroids_done); // Args!
		}

		if (!args.containsKey(BACK_TEXT)) {
			args.putInt(BACK_TEXT, R.string.timendroids_back); // Args!
		}

		if (!args.containsKey(CANCEL_TEXT)) {
			args.putInt(CANCEL_TEXT, R.string.timendroids_cancel); // Args!
		}

		if (!args.containsKey(IN_ANIMATION)) {
			args.putInt(IN_ANIMATION, R.anim.abc_fade_in); // Args!
		}

		if (!args.containsKey(OUT_ANIMATION)) {
			args.putInt(OUT_ANIMATION, R.anim.abc_fade_out); // Args!
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

		Context act = getActivity();
		Context appCtx = act.getApplicationContext();

		AlertDialog.Builder builder = BaseDialog.getThemedAlertBuilder(act, args);

		View baseView = LayoutInflater.from(act).inflate(R.layout.timepickerfragment, null);

		viewSwitcher = (ViewSwitcher) baseView.findViewById(R.id.vSwitchTimePicker);
		timePicker = (TimePicker) baseView.findViewById(R.id.timePicker);
		datePicker = (DatePicker) baseView.findViewById(R.id.datePicker);

		int inAnim, outAnim;

		inAnim = args.getInt(IN_ANIMATION);
		outAnim = args.getInt(OUT_ANIMATION);

		Animation in = AnimationUtils.loadAnimation(appCtx, inAnim);
		Animation out = AnimationUtils.loadAnimation(appCtx, outAnim);

		viewSwitcher.setInAnimation(in);
		viewSwitcher.setOutAnimation(out);


		int titleID = args.getInt(TITLE_DATE_TEXT);
		int next = args.getInt(NEXT_TEXT);
		int cancel = args.getInt(CANCEL_TEXT);


		builder.setTitle(titleID);
		builder.setView(baseView);

		builder.setPositiveButton(next, this);
		builder.setNegativeButton(cancel, this);

		return builder.create();
	}


}
