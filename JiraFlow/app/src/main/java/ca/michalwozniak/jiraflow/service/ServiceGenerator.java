package ca.michalwozniak.jiraflow.service;

import android.util.Base64;
import android.util.Log;

import java.io.IOException;

import ca.michalwozniak.jiraflow.service.ErrorHTTP.RxErrorHandlingCallAdapterFactory;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by Michal Wozniak on 4/10/2016.
 */
public class ServiceGenerator {

    private static OkHttpClient.Builder httpClient = getHttpClient();

    private static OkHttpClient.Builder getHttpClient()
    {
        //created this function if we later want to add some custom options
        // specific timeout ...
        OkHttpClient.Builder client =   new OkHttpClient.Builder();
        return  client;
    }

    private static  Retrofit.Builder getBuilder(String jiraBaseUrl)
    {
        return new Retrofit.Builder()
                .baseUrl(jiraBaseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create());
    }

    private static  Retrofit.Builder getBuilderXML(String jiraBaseUrl)
    {
       return new Retrofit.Builder()
                .baseUrl(jiraBaseUrl)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create());
    }

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null, null,null);
    }

    public static <S> S createService(final Class<S> serviceClass, String username, String password, String url) {
        if (username != null && password != null) {
            String credentials = username + ":" + password;
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Log.e("url", String.valueOf(original.url()));
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", basic)
                            .header("Accept", "application/json")
                            .method(original.method(), original.body());
                    Request request = requestBuilder.build();

                    Log.e("url",request.headers().toString());

                    return chain.proceed(request);
                }
            });

        }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = getBuilder(url).client(client).build();

        return retrofit.create(serviceClass);
    }

    public static <S> S createServiceXML(Class<S> serviceClass, String username, String password, String url) {
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
                    return chain.proceed(request);
                }
            });
        }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = getBuilderXML(url).client(client).build();
        return retrofit.create(serviceClass);
    }

}
