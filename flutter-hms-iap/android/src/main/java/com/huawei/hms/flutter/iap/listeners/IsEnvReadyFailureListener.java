/*
    Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.huawei.hms.flutter.iap.listeners;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hms.flutter.iap.MethodCallHandlerImpl;
import com.huawei.hms.flutter.iap.utils.Constants;
import com.huawei.hms.iap.IapApiException;
import com.huawei.hms.support.api.client.Status;

import io.flutter.plugin.common.MethodChannel.Result;

public class IsEnvReadyFailureListener implements OnFailureListener {
    private final Result mResult;
    private final int mRequestCode;
    private final MethodCallHandlerImpl mMethodCallHandler;

    public IsEnvReadyFailureListener(MethodCallHandlerImpl methodCallHandler, Result result, int requestCode) {
        mResult = result;
        mRequestCode = requestCode;
        mMethodCallHandler = methodCallHandler;
    }

    @Override
    public void onFailure(Exception e) {
        if (e instanceof IapApiException) {
            final IapApiException apiException = (IapApiException) e;
            final Status status = apiException.getStatus();
            if (status.getStatusCode() == Constants.orderHwidNotLogin.getErrorCode()) {
                if (status.hasResolution()) {
                    mMethodCallHandler.handleResolution(status, mResult, mRequestCode);
                }
            } else {
                mResult.error(Integer.toString(status.getStatusCode()), status.getStatusMessage(), null);
            }
        }
    }
}