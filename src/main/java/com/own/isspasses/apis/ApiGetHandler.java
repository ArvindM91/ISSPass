package com.own.isspasses.apis;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.own.isspasses.globals.AppController;
import com.own.isspasses.interfaces.ApiResponseListener;

import java.util.Map;

public class ApiGetHandler {

    private static final int MAX_RETRIES = 3;
    private static final String LOG_TAG = ApiGetHandler.class.getSimpleName();

    public static void makeGetApiRequest(final Context context,
                                         final String url, final String apiTag,
                                         final Map<String, String> queryParams,
                                         final Map<String, String> headerParams,
                                         final ApiResponseListener apiResponseListener) {
        final String finalUrl = constructUrlWithQueryParams(url, queryParams);


        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, finalUrl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(LOG_TAG, "Success Response : " + response);
                        if (apiResponseListener != null) {
                            apiResponseListener.onSuccessResponse(apiTag, response);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if(apiResponseListener != null)
                    apiResponseListener.onErrorResponse(apiTag, error);
            }
        }) {

            /**
             * Passing some request headers
             * */
          /*  @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headerParams;
            }*/
        };

        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 30000;
            }

            @Override
            public int getCurrentRetryCount() {
                return MAX_RETRIES;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                Log.e("Service Failure","Error: " + error.toString());
            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        jsonObjReq.setShouldCache(false);
        AppController.getInstance().cancelPendingRequests(apiTag);
        AppController.getInstance().addToRequestQueue(jsonObjReq, apiTag);
    }

    private static String constructUrlWithQueryParams(String url, Map<String, String> queryParams){
        if(queryParams != null && queryParams.size() > 0){
            StringBuilder sb = new StringBuilder(url);
            sb.append("?");
            for (String key: queryParams.keySet()) {
                sb.append(key);
                sb.append("=");
                sb.append(queryParams.get(key));
                sb.append("&");
            }
            return sb.substring(0, sb.length()-1);
        }
        return url;
    }

}
