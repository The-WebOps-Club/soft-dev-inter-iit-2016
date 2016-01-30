package example.com.alerto;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by srivatsan on 27/1/16.
 */
public class HTTPPost {
    public HTTPPost(final String URL, final List<NameValuePair> nameValuePairs, final Context context){
        Thread t = new Thread() {

            public void run() {
                Looper.prepare(); //For Preparing Message Pool for the child Thread
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                HttpResponse response;


                try {
                    HttpPost post = new HttpPost(URL);
                    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    response = client.execute(post);


                    final InputStream in = response.getEntity().getContent();
                    final String s = convertStreamToString(in);
                    try {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gotResult(s);
                            }
                        });
                    }catch (Exception e) {
                        gotResult(s);
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                    gotResult("");
                }

                Looper.loop();
            }
        };

        t.start();
    }
    private String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public void gotResult(String s){

    }
}