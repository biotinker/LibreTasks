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
 /*******************************************************************************
 * Copyright 2009 Omnidroid - http://code.google.com/p/omnidroid 
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *     
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 *******************************************************************************/
package libretasks.app.view.simple.model;

import java.util.ArrayList;

import libretasks.app.controller.datatypes.DataType;

/**
 * An instance of an action whose data has been set by the end-user. When a user picks a
 * <code>ModelAction</code> to be executed by a rule, we prompt them for input data about how to
 * execute the action. Their supplied information is stored in <code>mActionDatas</code> and the
 * original action type is stored in <code>mModelAction</code>.
 * 
 * So this class contains the action template, and whatever data the user supplied for it.
 */
public class ModelRuleAction extends ModelItem {

  /** Our filter type. */
  private final ModelAction modelAction;

  /** Action data entered by the end user. */
  private final ArrayList<DataType> actionDatas;

  public ModelRuleAction(long databaseId, ModelAction modelAction, ArrayList<DataType> actionDatas) {
    super(modelAction.getTypeName(), modelAction.getDescription(), modelAction.getIconResId(),
        databaseId);
    this.modelAction = modelAction;
    this.actionDatas = actionDatas;
  }

  public ModelAction getModelAction() {
    return modelAction;
  }

  public ArrayList<DataType> getDatas() {
    return actionDatas;
  }

  @Override
  public String getDescriptionShort() {
    return modelAction.getDescriptionShort();
  }
}
