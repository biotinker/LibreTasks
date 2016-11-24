package libretasks.app.controller.actions;

import android.content.Intent;

import java.util.HashMap;

/**
 * Created by jf on 09/11/16.
 */

public class PowerOffAction extends OmniAction {
    public static final String APP_NAME = "Signals";
    public static final String ACTION_NAME = "Power Off Device";

    public PowerOffAction(HashMap<String, String> parameters) throws libretasks.app.controller.util.OmnidroidException {
        super(libretasks.app.controller.external.actions.SignalsActionService.class.getName(),
                libretasks.app.controller.Action.BY_SERVICE);
    }

    @Override
    public Intent getIntent() {
        Intent intent = new Intent();
        intent.setClassName(LIBRETASKS_PACKAGE_NAME, libretasks.app.controller.external.actions.SignalsActionService.class.getName());
        intent.putExtra(libretasks.app.controller.external.actions.SignalsActionService.OPERATION_TYPE,
                libretasks.app.controller.external.actions.SignalsActionService.POWER_OFF_DEVICE);
        intent.putExtra(DATABASE_ID, databaseId);
        intent.putExtra(ACTION_TYPE, actionType);
        intent.putExtra(NOTIFICATION, showNotification);
        return intent;
    }

    @Override
    public String getDescription() {
        return APP_NAME + "-" + ACTION_NAME;
    }


}
