package com.pick;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.accounts.GoogleAccountManager;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTube;
import com.pick.common.PersonTeamValueObject;
import com.pick.common.PickCommon;
import com.pick.common.PropertyPickManager;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
  업로드 하는 서비스컴포넌트 객체
 */

public class YouTubeUploadService extends IntentService {
    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = new GsonFactory();
    GoogleAccountCredential credential;

    PersonTeamValueObject uploadValueObject;

    public YouTubeUploadService() {
        super("YouTubeUploadService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Uri fileUri = intent.getData();
        String oauthEmail = intent.getStringExtra("email");
        uploadValueObject = (PersonTeamValueObject) intent.getSerializableExtra("memberInfo");


        //인증여부 확인
        credential = GoogleAccountCredential.usingOAuth2(getApplicationContext(),
                Arrays.asList(DeveloperKey.SCOPES));
        credential.setSelectedAccountName(oauthEmail);
        credential.setBackOff(new ExponentialBackOff());

        GoogleAccountManager accountManager= credential.getGoogleAccountManager();
        Log.e("googleManager", String.valueOf(accountManager.getAccountByName(oauthEmail)));
        Log.e("email1111", oauthEmail);
        Log.e("credentialScope", String.valueOf(credential.getScope()));
        Log.e("credentialAccount", String.valueOf(credential.getSelectedAccount()));
        Log.e("SelectedAccountName", String.valueOf(credential.getSelectedAccountName()));
        /*try {
            Log.e("credentialToken", String.valueOf(credential.getToken().toString()));
        }catch(Exception e){
            Log.e("IOE", e.getMessage(), e);
        }*/
        //Log.e("email1111", oauthEmail);

        //credential.setBackOff

        final YouTube youtube =
                new YouTube.Builder(transport, jsonFactory, credential).setApplicationName(
                        "com.pick").build();

        uploadValueObject.videoKey = tryUpload(fileUri, youtube);
        Log.e("VideoKey", String.valueOf(uploadValueObject.videoKey));
        new Handler(Looper.getMainLooper()).
                post(new Runnable() {
                    @Override
                    public void run() {
                        //내 서버로 업로드 한다.
                        new PickYoutubeToNode().execute(uploadValueObject);
                    }
                });
    }

    private String tryUpload(Uri mFileUri, YouTube youtube) {
        long fileSize;
        InputStream fileInputStream = null;
        String videoKey = "";
        try {
            fileSize = getContentResolver().openFileDescriptor(mFileUri, "r").getStatSize();
            fileInputStream = getContentResolver().openInputStream(mFileUri);
            videoKey = StartUpload.upload(youtube, fileInputStream, fileSize, uploadValueObject.cont);

        } catch (FileNotFoundException e) {
            Log.e("tryUpload", e.getMessage(), e);
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
            }
        }
        return videoKey;
    }

    class PickYoutubeToNode extends AsyncTask<PersonTeamValueObject, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("success")){
                Toast.makeText(PickApplication.getItemContext(), "전체 성공", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(PickApplication.getItemContext(), "노드서버로드 실패",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(PersonTeamValueObject... personTeamValueObjects) {
            PersonTeamValueObject postParam = personTeamValueObjects[0];
            Response response=null;

            try{
                OkHttpClient toServer = PickCommon.getOkHttpClient();
                FormBody.Builder formBuidler = new FormBody.Builder();
                formBuidler.add("cont", postParam.cont);
                formBuidler.add("v_key", postParam.videoKey);
                formBuidler.add("u_id", PropertyPickManager.getPickInstance().getUserID());

                int genreSize = postParam.genres.size();
                String genreQuery = "";
                for(int i = 0 ; i < genreSize ; i++){
                    if(i == (genreSize - 1)){
                        genreQuery = postParam.genres.get(i);
                    }else{
                        genreQuery =  postParam.genres.get(i) + ",";
                    }
                }
                formBuidler.add("genre", genreQuery);

                int partSize = postParam.genres.size();
                String partQuery = "";
                for(int i = 0 ; i < partSize ; i++){
                    if(i == (partSize - 1)){
                        partQuery = postParam.parts.get(i);
                    }else{
                        partQuery =  postParam.parts.get(i) + ",";
                    }
                }
                formBuidler.add("part", partQuery);

                FormBody formBody = formBuidler.build();

                Request request =  new Request.Builder()
                                  .url(PickCommon.TARGET_URL + PickCommon.personVideoInfo)
                                  .post(formBody)
                                  .build();
                response = toServer.newCall(request).execute();
                boolean flag = response.isSuccessful();
                Log.e("responseCode", String.valueOf(response.code()));
                if(flag){
                    String resultJSON = response.body().string();
                    JSONObject jsonObject = new JSONObject(resultJSON);
                    if(jsonObject.getString("msg").equalsIgnoreCase("success")){
                        return "success";
                    }
                }
            }catch(Exception e){
                Log.e("유투브정보Node입력", e.toString(),e);
            }finally{
                if(response != null){
                    response.close();
                }
            }
            return "fail";

        }
    }
}
