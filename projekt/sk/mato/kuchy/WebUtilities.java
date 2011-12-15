package sk.mato.kuchy;



import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.SyncStateContract.Constants;
import android.util.Log;

public class WebUtilities {
	private static HttpClient mHttpClient = new DefaultHttpClient();
	
	public static Thread performOnBackgroundThread(Runnable runnable) {
		Thread thread = new Thread(runnable);
		thread.start();
		return thread;
	}
	
	public static HttpEntity getUrlContent(String url) throws Exception {
		// TODO: specific exceptions
		Log.i(Constants._ID, "Fetching url: " + url);
		
		HttpGet get = new HttpGet(url);
		HttpResponse httpResponse = mHttpClient.execute(get);

		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if(statusCode != HttpStatus.SC_OK) {
			throw new Exception("Bad HTTP status code: " + statusCode);
		}
		return httpResponse.getEntity();
	}
	
	public static HttpEntity post(String url, List<NameValuePair> data) throws Exception {
		// TODO: specific exceptions
		Log.i(Constants._ID, "Posting to url: " + url);
		
		
		HttpPost post = new HttpPost(url);
		
		
		post.setEntity(new UrlEncodedFormEntity(data));
		HttpResponse httpResponse = mHttpClient.execute(post);
		
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		
		
		if(statusCode != HttpStatus.SC_OK) {
			throw new Exception("Bad HTTP status code: " + statusCode);
		}
		
		return httpResponse.getEntity();
	}
	
	public static String getStringFromUrl(String url) throws Exception {
		HttpEntity entity = getUrlContent(url);
		return EntityUtils.toString(entity);
	}
	
	public static Bitmap getImageFromUrl(String url) throws Exception {
		HttpEntity entity = getUrlContent(url);
		return BitmapFactory.decodeStream(entity.getContent());
	}
	
	public static byte[] getBytesFromUrl(String url) throws Exception {
		HttpEntity entity = getUrlContent(url);
		return EntityUtils.toByteArray(entity);
	}
}
