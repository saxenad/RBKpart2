/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android.result;

import android.app.Activity;
import android.view.View;

import com.RBK.R;
import com.google.zxing.Result;
import com.google.zxing.client.result.ExpandedProductParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ProductParsedResult;

/**
 * Handles generic products which are not books.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ProductResultHandler extends ResultHandler {
  private static final int[] buttons = {
      R.string.button_product_search,
      R.string.button_web_search,
      R.string.button_custom_product_search
  };

  public ProductResultHandler(Activity activity, ParsedResult result, Result rawResult) {
    super(activity, result, rawResult);
    showGoogleShopperButton(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ProductParsedResult productResult = (ProductParsedResult) getResult();
        openGoogleShopper(productResult.getNormalizedProductID());
      }
    });
  }

  @Override
  public int getButtonCount() {
    return hasCustomProductSearch() ? buttons.length : buttons.length - 1;
  }

  @Override
  public int getButtonText(int index) {
    return buttons[index];
  }

  @Override
  public void handleButtonPress(int index) {
    ParsedResult rawResult = getResult();
    String productID;
    if (rawResult instanceof ProductParsedResult) {
      productID = ((ProductParsedResult) rawResult).getNormalizedProductID();
    } else if (rawResult instanceof ExpandedProductParsedResult) {
      productID = ((ExpandedProductParsedResult) rawResult).getRawText();
    } else {
      throw new IllegalArgumentException(rawResult.getClass().toString());
    }
    switch (index) {
      case 0:
        openProductSearch(productID);
        break;
      case 1:
        webSearch(productID);
        break;
      case 2:
        openURL(fillInCustomSearchURL(productID));
        break;
    }
  }

  @Override
  public int getDisplayTitle() {
    return R.string.result_product;
  }
}
