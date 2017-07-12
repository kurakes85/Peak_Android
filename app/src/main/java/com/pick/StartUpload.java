package com.pick;

import android.util.Log;

import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by pyoinsoo on 2016-08-21.
 */
public class StartUpload {

    public static String upload(YouTube youtube, final InputStream fileInputStream,
                                final long fileSize, final String description) {
        String videoKey="";
        try {
            Video videoObjectDefiningMetadata = new Video();
            VideoStatus status = new VideoStatus();
            status.setPrivacyStatus("public");
            videoObjectDefiningMetadata.setStatus(status);
            //업로드할 동영상의 세부정보
            VideoSnippet snippet = new VideoSnippet();

            Calendar cal = Calendar.getInstance();
            snippet.setTitle("Android App Pick  " + cal.getTime());
            snippet.setDescription(description + cal.getTime());
            snippet.setTags(Arrays.asList("Pick", "PLPick"));
            videoObjectDefiningMetadata.setSnippet(snippet);

            InputStreamContent mediaContent =
                    new InputStreamContent("video/*", new BufferedInputStream(fileInputStream));
            mediaContent.setLength(fileSize);

            //업로드할 비디오 정보를 세팅
            YouTube.Videos.Insert videoInsert =
                    youtube.videos().insert("snippet,statistics,status", videoObjectDefiningMetadata, mediaContent);
            MediaHttpUploader uploader = videoInsert.getMediaHttpUploader();
            //순서적/일괄적으로 업로드 할 것인지를 결정
            uploader.setDirectUploadEnabled(false);

            //업로드 실행
            Video returnedVideo = videoInsert.execute();
            videoKey = returnedVideo.getId();

        }catch (Exception e) {
            Log.e("업로드중 문제발생", e.toString(), e);
        }
        return videoKey;
    }
}
