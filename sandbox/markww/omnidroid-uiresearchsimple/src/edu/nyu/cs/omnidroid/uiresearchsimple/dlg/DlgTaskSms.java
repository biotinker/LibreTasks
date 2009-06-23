package edu.nyu.cs.omnidroid.uiresearchsimple.dlg;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import edu.nyu.cs.omnidroid.uiresearchsimple.R;
import edu.nyu.cs.omnidroid.uiresearchsimple.Util;
import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelItem;
import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelTask;
import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelTaskSms;


/**
 * Get input from the user to create a <code>ModelFilterContact</code>.
 */
public class DlgTaskSms extends Dialog
	implements IDlgTask
{
	private ModelTaskSms mTask;
	private EditText mEditPhoneNumber;
	private EditText mEditText;
 
	
	public DlgTaskSms(Context context) {
		super(context);
		commonInit(context);
	}
	
	public DlgTaskSms(Context context, ModelItem task) {
		super(context);
		commonInit(context);
		ModelTaskSms mts = (ModelTaskSms)task;
		mEditPhoneNumber.setText(mts.getNumber());
		mEditText.setText(mts.getText());
	}
	
	private void commonInit(Context context) {
		setContentView(R.layout.dlg_filter_contact);
		setTitle("Contact Filter");
		
		mEditPhoneNumber = (EditText)findViewById(R.id.dlg_tasksms_editPhoneNumber);
		mEditText = (EditText)findViewById(R.id.dlg_tasksms_editText);

		Button btnOk = (Button)findViewById(R.id.dlg_tasksms_btnOk);
        btnOk.setOnClickListener(listenerBtnClickOk);
        Button btnInfo = (Button)findViewById(R.id.dlg_tasksms_btnInfo);
        btnInfo.setOnClickListener(listenerBtnClickInfo);
        Button btnCancel = (Button)findViewById(R.id.dlg_tasksms_btnCancel);
        btnCancel.setOnClickListener(listenerBtnClickCancel);

		Util.inflateDialog(context, (LinearLayout)findViewById(R.id.dlg_tasksms_ll));
	}
	
	@Override
	protected void onStart() {
	}

	public ModelTask getConstructedTask() {
		return mTask;
	}
	
	public boolean getDidUserConstructTask() {
		return mTask != null;
	}
	
    private android.view.View.OnClickListener listenerBtnClickOk = new android.view.View.OnClickListener() {
        public void onClick(View v) {
        	// If everything went ok, we dismiss ourselves and notify our parent 
        	// with the newly constructed task object.
        	if (/*check inputs ok*/true) {
        		mTask = new ModelTaskSms(mEditPhoneNumber.getText().toString(), mEditText.getText().toString());
        		dismiss();
        	}
        	else {
        		Util.showAlert(v.getContext(), "Sorry!", "We'll implement info dialogs about each filter soon.");
        	}
        }
    };
    
    private android.view.View.OnClickListener listenerBtnClickInfo = new android.view.View.OnClickListener() {
        public void onClick(View v) {
        	Util.showAlert(v.getContext(), "Sorry!", "We'll implement an info dialog about SMS task soon.");
        }
    };
    
    private android.view.View.OnClickListener listenerBtnClickCancel = new android.view.View.OnClickListener() {
        public void onClick(View v) {
        	dismiss();
        }
    };
}