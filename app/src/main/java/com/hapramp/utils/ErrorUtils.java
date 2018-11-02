package com.hapramp.utils;

import com.hapramp.api.HaprampApiClient;
import com.hapramp.models.error.ErrorResponse;
import com.hapramp.preferences.HaprampPreferenceManager;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {

  public static ErrorResponse parseError(Response<?> response) {
    String tk = HaprampPreferenceManager.getInstance().getUserToken();
    Converter<ResponseBody, ErrorResponse> converter = HaprampApiClient.getClient(tk)
      .responseBodyConverter(ErrorResponse.class, new Annotation[0]);
    ErrorResponse errorResponse;
    try {
      errorResponse = converter.convert(response.errorBody());
    }
    catch (IOException e) {
      return new ErrorResponse();
    }
    return errorResponse;
  }
}
