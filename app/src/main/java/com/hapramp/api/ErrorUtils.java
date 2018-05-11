package com.hapramp.api;

import com.hapramp.datamodels.error.GeneralErrorModel;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by Ankit on 10/22/2017.
 */

public class ErrorUtils {

    public static GeneralErrorModel parseError(Response<?> response) {
        Converter<ResponseBody, GeneralErrorModel> converter = HaprampApiClient
                .getClient("")
                .responseBodyConverter(GeneralErrorModel.class, new Annotation[0]);

        GeneralErrorModel error = null;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
        }
        return error;
    }
}
