/*******************************************************************************
 * Copyright 2009 OmniDroid - http://code.google.com/p/omnidroid
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
package edu.nyu.cs.omnidroid.ExternalParameters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Intent that is being broadcasted when phone is falling.
 */
public class PhoneIsFallingIntent extends Intent {
  public static final String VALUE = "Value";
  public static final String ACTION_NAME = "PHONE_IS_FALLING";

  public PhoneIsFallingIntent() {
    super(ACTION_NAME);
  }

  public PhoneIsFallingIntent(Uri uri) {
    super(ACTION_NAME, uri);
  }

  public PhoneIsFallingIntent(Context packageContext, Class<?> cls) {
    super(packageContext, cls);
  }

  public PhoneIsFallingIntent(Uri uri, Context packageContext, Class<?> cls) {
    super(ACTION_NAME, uri, packageContext, cls);
  }
}
