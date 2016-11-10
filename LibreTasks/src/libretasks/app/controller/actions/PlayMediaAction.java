/*  
 * Copyright (c) 2016  LibreTasks - https://github.com/biotinker/LibreTasks  
 *  
 *  This file is free software: you may copy, redistribute and/or modify it  
 *  under the terms of the GNU General Public License as published by the  
 *  Free Software Foundation, either version 3 of the License, or (at your  
 *  option) any later version.  
 *  
 *  This file is distributed in the hope that it will be useful, but  
 *  WITHOUT ANY WARRANTY; without even the implied warranty of  
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU  
 *  General Public License for more details.  
 *  
 *  You should have received a copy of the GNU General Public License  
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.  
 *  
 * This file incorporates work covered by the following copyright and  
 * permission notice:  
 */
package libretasks.app.controller.actions;

import java.util.HashMap;

import android.content.Intent;
import libretasks.app.controller.Action;
import libretasks.app.controller.external.actions.MediaActionService;
import libretasks.app.controller.util.OmnidroidException;

/**
 * Simple action that turns wifi off
 *
 */
public class PlayMediaAction extends OmniAction {

  public static final String APP_NAME = "Media";
  public static final String ACTION_NAME = "Play Media";

  public PlayMediaAction(HashMap<String, String> parameters) throws OmnidroidException {
    super(MediaActionService.class.getName(), Action.BY_SERVICE);
  }

  @Override
  public Intent getIntent() {
    Intent intent = new Intent();
    intent.setClassName(LIBRETASKS_PACKAGE_NAME, MediaActionService.class.getName());
    intent.putExtra(MediaActionService.OPERATION_TYPE, MediaActionService.PLAY_MEDIA_ACTION);
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
  
