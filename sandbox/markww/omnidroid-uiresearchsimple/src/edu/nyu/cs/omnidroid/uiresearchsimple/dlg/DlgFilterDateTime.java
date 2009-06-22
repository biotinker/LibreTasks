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
import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelFilterDateTime;
import edu.nyu.cs.omnidroid.uiresearchsimple.model.ModelItem;


/**
 * Get input from the user to create a <code>ModelFilterDateTime</code>.
 */
public class DlgFilterDateTime extends Dialog
	implements IDlgFilter
{
	private ModelFilterDateTime mFilter;
	private EditText mEditDateTimeRange;
 
	
	public DlgFilterDateTime(Context context)
	{
		super(context);
		commonInit(context);
	}
	
	public DlgFilterDateTime(Context context, ModelItem filter) {
		super(context);
		commonInit(context);
		ModelFilterDateTime fdt = (ModelFilterDateTime)filter;
		mEditDateTimeRange.setText(fdt.getDateTimeRange());
	}
	
	private void commonInit(Context context) {
		setContentView(R.layout.dlg_filter_datetime);
		setTitle("Date/Time Filter");
		
		mEditDateTimeRange = (EditText)findViewById(R.id.dlg_filterdatetime_edit);

		Button btnOk = (Button)findViewById(R.id.dlg_filterdatetime_btnOk);
        btnOk.setOnClickListener(listenerBtnClickOk);
        Button btnInfo = (Button)findViewById(R.id.dlg_filterdatetime_btnInfo);
        btnInfo.setOnClickListener(listenerBtnClickInfo);
        Button btnCancel = (Button)findViewById(R.id.dlg_filterdatetime_btnCancel);
        btnCancel.setOnClickListener(listenerBtnClickCancel);

		Util.inflateDialog(context, (LinearLayout)findViewById(R.id.dlg_filterdatetime_ll));
	}
	
	@Override
	protected void onStart() {
	}

	public ModelFilter getConstructedFilter()
	{
		return mFilter;
	}
	
	public boolean getDidUserConstructFilter()
	{
		return mFilter != null;
	}
	
    private android.view.View.OnClickListener listenerBtnClickOk = new android.view.View.OnClickListener() {
        public void onClick(View v) {
        	// If everything went ok, we dismiss ourselves and notify our parent 
        	// with the newly constructed filter object.
        	if (/*check inputs ok*/true) {
        		mFilter = new ModelFilterDateTime(mEditDateTimeRange.getText().toString());
        		dismiss();
        	}
        	else {
        		Util.showAlert(v.getContext(), "Sorry!", "We'll implement info dialogs about each filter soon.");
        	}
        }
    };
    
    private android.view.View.OnClickListener listenerBtnClickInfo = new android.view.View.OnClickListener() {
        public void onClick(View v) {
        	Util.showAlert(v.getContext(), "Sorry!", "We'll implement an info dialog about date/time filter soon.");
        }
    };
    
    private android.view.View.OnClickListener listenerBtnClickCancel = new android.view.View.OnClickListener() {
        public void onClick(View v) {
        	dismiss();
        }
    };
}