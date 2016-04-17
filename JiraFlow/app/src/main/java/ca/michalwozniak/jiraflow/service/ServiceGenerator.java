package ca.michalwozniak.jiraflow.service;

import android.util.Base64;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by michal on 4/10/2016.
 */
public class ServiceGenerator {

    public static final String API_BASE_URL = "https://ewok390.atlassian.net/rest/api/2/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null, null);
    }

    public static <S> S createService(Class<S> serviceClass, String username, String password) {
        if (username != null && password != null) {
            String credentials = username + ":" + password;
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", basic)
                            .header("Accept", "application/json")
                            .method(original.method(), original.body());
                    Request request = requestBuilder.build();
                    Log.e("requestLog",request.toString());
                    return chain.proceed(request);
                }
            });
        }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

//    public static <S> S createService(Class<S> serviceClass, String username, final String password) {
//        if (username != null && password != null) {
//            String credentials = username + ":" + password;
//            final String basic =
//                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
//
//            final String body = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }";
//            Log.e("requestBody", body);
//
//            final RequestBody requestBody = RequestBody.create(MediaType.parse("JSON"), body);
//
//            httpClient.addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Interceptor.Chain chain) throws IOException {
//                    Request original = chain.request();
//
//                    Request.Builder requestBuilder = original.newBuilder()
//                            .header("Accept", "application/json")
//                            .post(requestBody);
//
//                    Request request = requestBuilder.build();
//                    Log.e("requestOutput", request.toString());
//                    return chain.proceed(request);
//                }
//            });
//        }
//
//        OkHttpClient client = httpClient.build();
//        Retrofit retrofit = builder.client(client).build();
//        return retrofit.create(serviceClass);
//    }

}