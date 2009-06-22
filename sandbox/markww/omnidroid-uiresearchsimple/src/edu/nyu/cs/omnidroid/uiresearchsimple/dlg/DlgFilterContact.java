package edu.nyu.cs.omnidroid.uiresearchsimple.dlg;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import edu.nyu.cs.omnidroid.uiresearchsimple.R;
import edu.nyu.cs.omnidroid.uiresearchsimple.Util;
import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelFilter;
import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelFilterContact;
import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelItem;


/**
 * Get input from the user to create a <code>ModelFilterContact</code>.
 */
public class DlgFilterContact extends Dialog
	implements IDlgFilter
{
	private ModelFilterContact mFilter;
	private EditText mEditContactName;
 
	
	public DlgFilterContact(Context context) {
		super(context);
		commonInit(context);
	}
	
	public DlgFilterContact(Context context, ModelItem filter) {
		super(context);
		commonInit(context);
		ModelFilterContact fc = (ModelFilterContact)filter;
		mEditContactName.setText(fc.getContactName());
	}
	
	private void commonInit(Context context) {
		setContentView(R.layout.dlg_filter_contact);
		setTitle("Contact Filter");
		
		mEditContactName = (EditText)findViewById(R.id.dlg_filtercontact_edit);

		Button btnOk = (Button)findViewById(R.id.dlg_filtercontact_btnOk);
        btnOk.setOnClickListener(listenerBtnClickOk);
        Button btnInfo = (Button)findViewById(R.id.dlg_filtercontact_btnInfo);
        btnInfo.setOnClickListener(listenerBtnClickInfo);
        Button btnCancel = (Button)findViewById(R.id.dlg_filtercontact_btnCancel);
        btnCancel.setOnClickListener(listenerBtnClickCancel);

		Util.inflateDialog(context, (LinearLayout)findViewById(R.id.dlg_filtercontact_ll));
	}
	
	@Override
	protected void onStart() {
	}

	public ModelFilter getConstructedFilter() {
		return mFilter;
	}
	
	public boolean getDidUserConstructFilter() {
		return mFilter != null;
	}
	
    private android.view.View.OnClickListener listenerBtnClickOk = new android.view.View.OnClickListener() {
        public void onClick(View v) {
        	// If everything went ok, we dismiss ourselves and notify our parent 
        	// with the newly constructed filter object.
        	if (/*check inputs ok*/true) {
        		mFilter = new ModelFilterContact(mEditContactName.getText().toString());
        		dismiss();
        	}
        	else {
        		Util.showAlert(v.getContext(), "Sorry!", "We'll implement info dialogs about each filter soon.");
        	}
        }
    };
    
    private android.view.View.OnClickListener listenerBtnClickInfo = new android.view.View.OnClickListener() {
        public void onClick(View v) {
        	Util.showAlert(v.getContext(), "Sorry!", "We'll implement an info dialog about contact filter soon.");
        }
    };
    
    private android.view.View.OnClickListener listenerBtnClickCancel = new android.view.View.OnClickListener() {
        public void onClick(View v) {
        	dismiss();
        }
    };
}