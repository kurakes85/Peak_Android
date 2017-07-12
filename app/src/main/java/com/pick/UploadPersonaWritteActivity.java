package com.pick;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.pick.common.PersonTeamValueObject;
import com.pick.common.PickCommon;
import com.pick.common.PropertyPickManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ccei on 2016-08-12.
 * 개인 글쓰기 화면
 */
public class UploadPersonaWritteActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    //내장된 계정에서 이메일을 얻어올 때
    static final int REQUEST_CODE_PICK_ACCOUNT = 1001;
    //인증시 에러발생할 때
    static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1002;
    static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1003;
    private static final int RESULT_PICK_VIDEO = 100;
    private static final int REQUEST_ACCOUNT_PICKER = 2001;
    private static final int PERMISSION_GET_ACCOUNT = 100;
    //유튜브 전체 스코프
    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/youtube ";
    FrameLayout uploadVideoKind;
    ImageView selectedVideoIV;
    TextView memberName, genderTV, ageTV, areaCity, areaGu;
    Button addBtn, cancelBtn;
    EditText editText;
    Spinner genreSpinner, partSpinner;
    PersonTeamValueObject videoInfo;
    String oauthEmail = "";
    VideoView videoView;
    MediaController mc;
    Uri mFileUri;
    boolean videoFlag = true;
    GoogleAccountCredential credential;
    private GoogleApiClient mGoogleApiClient;

    public void setProfileInfo() {
        //not sure if mGoogleapiClient.isConnect is appropriate...
        if (!mGoogleApiClient.isConnected() || Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) == null) {
            Toast.makeText(UploadPersonaWritteActivity.this, "구글 서비스에 연결이 되지 않음", Toast.LENGTH_LONG).show();
        } else {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            if (currentPerson.hasImage()) {
                Toast.makeText(UploadPersonaWritteActivity.this, "구글 서비스에 연결되고 사용자 이미지 있음", Toast.LENGTH_LONG).show();
            }
            if (currentPerson.hasDisplayName()) {
                Toast.makeText(UploadPersonaWritteActivity.this, "구글 서비스 연결된 사용자 이름은 " +
                        currentPerson.getDisplayName() + "", Toast.LENGTH_LONG).show();
                ;
            }
        }
    }
    @Override
    public void onConnected(Bundle bundle) {

        if (ContextCompat.checkSelfPermission(UploadPersonaWritteActivity.this, Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {
            // We do not have this permission. Let's ask the user
            ActivityCompat.requestPermissions(UploadPersonaWritteActivity.this,
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    PERMISSION_GET_ACCOUNT);

        } else {
            setProfileInfo();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_GET_ACCOUNT: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(UploadPersonaWritteActivity.this, "허락해 주셔서 감사합니다.", Toast.LENGTH_SHORT).show();
                    setProfileInfo();
                } else {
                    // permission denied
                    Toast.makeText(UploadPersonaWritteActivity.this, "거절하셔서 더이상 앱의 진행이 불가합니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            Toast.makeText(UploadPersonaWritteActivity.this, "Connection to Play Services Failed, error", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_personal_write);


        videoView = (VideoView) findViewById(R.id.videoView);

        /*videoView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try {
                    if( videoFlag == true) {
                        videoView = (VideoView) findViewById(R.id.videoView);
                        videoView.start();
                        videoFlag = false;
                    }else{
                        videoView.stopPlayback();
                        videoFlag = true;
                    }
                } catch (Exception e) {
                    Log.e(UploadPersonaWritteActivity.this.getLocalClassName(), e.toString(),e);
                }
            }
        });*/

        //PropertyPickManager.getPickInstance().deleteEmail();
        oauthEmail = PropertyPickManager.getPickInstance().getYoutubeEmail();


        uploadVideoKind = (FrameLayout) findViewById(R.id.personal_video_add_frame_layout);
        selectedVideoIV = (ImageView) findViewById(R.id.personal_write_video_btn);
        memberName = (TextView) findViewById(R.id.personal_name_writte);
        genderTV = (TextView) findViewById(R.id.personal_gender_writte);
        ageTV = (TextView) findViewById(R.id.personal_age_writte);

        addBtn = (Button) findViewById(R.id.personal_add_button);
        cancelBtn = (Button) findViewById(R.id.personal_cancel_button);

        //toolbar section,
        Toolbar toolbar = (Toolbar) findViewById(R.id.person_upload_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar conInform = getSupportActionBar();
        conInform.setDisplayHomeAsUpEnabled(true);
        conInform.setTitle("개인 구인 등록");

        if (oauthEmail != null && oauthEmail.length() > 1) {

            credential = GoogleAccountCredential.usingOAuth2(
                    getApplicationContext(), Arrays.asList(DeveloperKey.SCOPES));
            credential.setSelectedAccountName(oauthEmail);
            credential.setBackOff(new ExponentialBackOff());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.e("If(oauthToken)", String.valueOf(credential.getToken()));
                    } catch (UserRecoverableAuthException e) {
                        startActivityForResult(e.getIntent(), REQUEST_ACCOUNT_PICKER);
                    } catch (Exception e) {
                        Log.e("if----", e.getMessage(), e);
                    }
                }
            }).start();
            mGoogleApiClient = new GoogleApiClient.Builder(UploadPersonaWritteActivity.this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Plus.API)
                    .addScope(Plus.SCOPE_PLUS_PROFILE)
                    .build();
            mGoogleApiClient.connect();
        } else {
            credential = GoogleAccountCredential.usingOAuth2(
                    getApplicationContext(), Arrays.asList(DeveloperKey.SCOPES));
            credential.setSelectedAccountName(oauthEmail);
            credential.setBackOff(new ExponentialBackOff());
            startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
        }

        //비디오 버튼 클릭 리스너
        selectedVideoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("video/*");
                startActivityForResult(intent, RESULT_PICK_VIDEO);
            }
        });
        //저장하기(유효성 체크 안함)
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (oauthEmail == null) {
                    return;
                }
                String description = editText.getText().toString();
                if (description.length() > 0) {
                    videoInfo.cont = description;
                }
                if (mFileUri != null) {

                    Intent uploadIntent = new Intent(UploadPersonaWritteActivity.this, YouTubeUploadService.class);
                    uploadIntent.setData(mFileUri);
                    uploadIntent.putExtra("email", oauthEmail);
                    uploadIntent.putExtra("memberInfo", videoInfo);
                    startService(uploadIntent);
                    //Main으로 돌아감
                    //finish();
                }

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editText = (EditText) findViewById(R.id.personal_writte_text);

        //스피너 부분
        //장르 스피너
        genreSpinner = (Spinner) findViewById(R.id.personal_spinner_genre);
        genreSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedGenre = (String) adapterView.getItemAtPosition(i);
                if (videoInfo == null) {
                    videoInfo = new PersonTeamValueObject();
                }
                int inputedSize = videoInfo.genres.size();
                if (inputedSize > 0) {
                    for (int j = 0; i < inputedSize; j++) {
                        String item = videoInfo.genres.get(j);
                        if (item.equals(selectedGenre)) {
                            return;
                        } else {
                            videoInfo.genres.add(selectedGenre);
                        }
                    }

                } else {
                    videoInfo.genres.add(selectedGenre);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (videoInfo == null) {
                    videoInfo = new PersonTeamValueObject();
                }
                String genre = (String) adapterView.getItemAtPosition(0);
                if (!(videoInfo.genres.size() > 0)) {
                    videoInfo.genres.add(genre);
                }
            }
        });


        //지원파트
        partSpinner = (Spinner) findViewById(R.id.personal_spinner_part);
        partSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedPart = (String) adapterView.getItemAtPosition(i);
                if (videoInfo == null) {
                    videoInfo = new PersonTeamValueObject();
                }
                int inputedSize = videoInfo.parts.size();
                if (inputedSize > 0) {
                    for (int j = 0; i < inputedSize; j++) {
                        String item = videoInfo.parts.get(j);
                        if (item.equals(selectedPart)) {
                            return;
                        } else {
                            videoInfo.parts.add(selectedPart);
                        }
                    }
                } else {
                    videoInfo.parts.add(selectedPart);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (videoInfo == null) {
                    videoInfo = new PersonTeamValueObject();
                }
                String part = (String) adapterView.getItemAtPosition(0);
                if (!(videoInfo.genres.size() > 0)) {
                    videoInfo.genres.add(part);
                }
            }
        });


        areaCity = (TextView) findViewById(R.id.personal_text_city);
        areaGu = (TextView) findViewById(R.id.personal_text_gu);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //현재 사용자의 정보를 뿌려주는 쓰레드
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    OkHttpClient toServer = PickCommon.getOkHttpClient();
                    String userId = PropertyPickManager.getPickInstance().getUserID();
                    FormBody formBody = new FormBody.Builder()
                            .add("userId", userId)
                            .build();
                    Request req = new Request.Builder()
                            .url(PickCommon.TARGET_URL + PickCommon.userInfo)
                            .post(formBody)
                            .build();
                    response = toServer.newCall(req).execute();
                    boolean flag = response.isSuccessful();
                    if (flag) {
                        String personalJSON = response.body().string();
                        JSONObject obj = new JSONObject(personalJSON);
                        if (obj.getString("msg").equalsIgnoreCase("success")) {
                            videoInfo = new PersonTeamValueObject();
                            JSONObject data = obj.getJSONObject("data");
                            videoInfo.name = data.getString("name");
                            videoInfo.age = data.getString("age");
                            videoInfo.areaDo = data.getString("area_do");
                            videoInfo.areaGu = data.getString("area_gu");
                            videoInfo.gender = data.getString("gender");

                            JSONArray genre = data.getJSONArray("genre");
                            int genreSize = genre.length();
                            for (int i = 0; i < genreSize; i++) {
                                videoInfo.genres.add(genre.getString(i));
                            }

                            JSONArray part = data.getJSONArray("part");
                            int partSize = genre.length();
                            for (int i = 0; i < partSize; i++) {
                                videoInfo.parts.add(part.getString(i));
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    memberName.setText(videoInfo.name);
                                    if (videoInfo.gender.equals("f")) {
                                        genderTV.setText("여자");
                                    } else {
                                        genderTV.setText("남자");
                                    }
                                    ageTV.setText(String.valueOf(videoInfo.age));
                                    areaCity.setText(videoInfo.areaDo);
                                    areaGu.setText(videoInfo.areaGu);
                                }
                            });
                        }
                    }

                } catch (Exception e) {
                    Log.e("개인정보 연결중  발생", e.getMessage(), e);
                } finally {
                    if (response != null) {
                        response.close();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri mFileURI = null;
        switch (requestCode) {
            case RESULT_PICK_VIDEO:
                if (resultCode == RESULT_OK) {
                    mFileURI = data.getData();
                    mFileUri = mFileURI;

                    if (mFileURI != null) {
                        videoView.setVisibility(View.VISIBLE);
                        mc = new MediaController(UploadPersonaWritteActivity.this);
                        videoView.setMediaController(mc);
                        videoView.setVideoURI(mFileUri);
                        mc.show();
                        videoView.start();
                        selectedVideoIV.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            case REQUEST_CODE_PICK_ACCOUNT:
                if (resultCode == RESULT_OK) {
                    String email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    PropertyPickManager.getPickInstance().setYoutubeEmail(email);
                    //new YoutubeOAuthTask().execute(email);
                }
                break;
            case REQUEST_CODE_RECOVER_FROM_AUTH_ERROR:
            case REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR:
                if (requestCode == RESULT_OK) {
                    String email = PropertyPickManager.getPickInstance().getYoutubeEmail();
                    //new YoutubeOAuthTask().execute(email);
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == Activity.RESULT_OK && data != null
                        && data.getExtras() != null) {
                    String accountName = data.getExtras().getString(
                            AccountManager.KEY_ACCOUNT_NAME);
                    Log.e("accountName", accountName);
                    if (accountName != null) {
                        oauthEmail = accountName;
                        credential = GoogleAccountCredential.usingOAuth2(
                                getApplicationContext(), Arrays.asList(DeveloperKey.SCOPES));
                        credential.setSelectedAccountName(accountName);
                        credential.setBackOff(new ExponentialBackOff());

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Log.e("oauthToken", String.valueOf(credential.getToken()));
                                } catch (UserRecoverableAuthException e) {
                                    startActivityForResult(e.getIntent(), REQUEST_ACCOUNT_PICKER);
                                } catch (Exception e) {
                                    Log.e("onActivityResult", e.getMessage(), e);
                                }
                            }
                        }).start();

                        PropertyPickManager.getPickInstance().setYoutubeEmail(oauthEmail);
                        mGoogleApiClient = new GoogleApiClient.Builder(UploadPersonaWritteActivity.this)
                                .addConnectionCallbacks(this)
                                .addOnConnectionFailedListener(this)
                                .addApi(Plus.API)
                                .addScope(Plus.SCOPE_PLUS_PROFILE)
                                .build();
                        mGoogleApiClient.connect();
                    }
                }
                break;
        }
    }

    //인증 예외처리
    public void handleException(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e instanceof GooglePlayServicesAvailabilityException) {

                    int statusCode = ((GooglePlayServicesAvailabilityException) e)
                            .getConnectionStatusCode();
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                            statusCode, UploadPersonaWritteActivity.this,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                    dialog.show();

                } else if (e instanceof UserRecoverableAuthException) {
                    Intent intent = ((UserRecoverableAuthException) e)
                            .getIntent();
                    startActivityForResult(intent,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                }
            }
        });
    }

    /*//비디오 썸네일 가져오는 곳
    public  Bitmap createVideoThumbnail(Context context, Uri uri, int kind) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {

            retriever.setDataSource(context, uri);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {

        } catch (RuntimeException ex) {

        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {

            }
        }
        int height = uploadVideoKind.getHeight();
        int width = uploadVideoKind.getWidth();
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null){
            bitmap = ThumbnailUtils.extractThumbnail(bitmap,
                    width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }*/
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class YoutubeOAuthTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            try {

                String token = GoogleAuthUtil.getToken(UploadPersonaWritteActivity.this,
                        email, SCOPE);

                Log.e("YouTubeToken", String.valueOf(token));
              /*  URL url = new URL("https://www.googleapis.com/youtube/v3/playlists?part=id,snippet"); //&mine=true");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Authorization", "Bearer " + token);
                int responseCode = conn.getResponseCode();
                String responseMessage = conn.getResponseMessage();
                Log.e("responseCode", responseCode + "" + responseMessage);
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream is = conn.getInputStream();
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\r\n");
                    }
                    Log.e("OauthResponse", String.valueOf(sb.toString()));
                    //이메일을 저장한다.
                    PropertyPickManager.getPickInstance().setYoutubeEmail(params[0]);
                    return sb.toString();
                } else {
                    Log.e("OauthResponse", "에러발생");
                    GoogleAuthUtil.clearToken(UploadPersonaWritteActivity.this, token);
                }*/
            } catch (UserRecoverableAuthException e) {
                e.printStackTrace();
                handleException(e);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GoogleAuthException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            /*if (result != null) {
                Toast.makeText(UploadPersonaWritteActivity.this, "구글 유튜브 인증을 완료했습니다",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UploadPersonaWritteActivity.this, "유튜브 인증 중 에러 ",
                        Toast.LENGTH_SHORT).show();
            }*/
        }
    }

    ;
}
