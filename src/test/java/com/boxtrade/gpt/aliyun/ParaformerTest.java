package com.boxtrade.gpt.aliyun;


import com.alibaba.dashscope.audio.asr.transcription.*;
import com.boxtrade.gpt.constant.DashScope;
import com.google.gson.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Paraformer语音识别API基于通义实验室新一代非自回归端到端模型，提供基于实时音频流的语音识别以及对输入的各类音视频文件进行语音识别的能力。可被应用于：
 *
 *     对语音识别结果返回的即时性有严格要求的实时场景，如实时会议记录、实时直播字幕、电话客服等。
 *
 *     对音视频文件中语音内容的识别，从而进行内容理解分析、字幕生成等。
 *
 *     对电话客服呼叫中心录音进行识别，从而进行客服质检等。
 *
 * @author 1763113879@qq.com
 * @version V2.1
 * @since 2.1.0 2024/1/5 16:06
 */
public class ParaformerTest extends DashScopeTest{

    /**
     * 音视频文件转写
     *
     * 用以进行语音识别的具体模型通过 model 参数指定。需要进行语音识别的音视频文件通过 file_urls 参数指定，支持HTTP / HTTPS协议的URL。
     */
    @Test
    public   void main( ) {
        // 创建转写请求参数，需要用真实apikey替换your-dashscope-api-key
        TranscriptionParam param =
            TranscriptionParam.builder()
                .apiKey(DashScope.API_KEY)
                .model("paraformer-v1")
                .fileUrls(
                    Collections.singletonList(
                        "https://dashscope.oss-cn-beijing.aliyuncs.com/samples/audio/paraformer/hello_world_female2.wav"))
                .build();
        try {
            Transcription transcription = new Transcription();
            // 提交转写请求
            TranscriptionResult result = transcription.asyncCall(param);
            // 等待转写完成
            result =
                transcription.wait(
                    TranscriptionQueryParam.FromTranscriptionParam(param, result.getTaskId()));
            // 获取转写结果
            List<TranscriptionTaskResult> taskResultList = result.getResults();
            if (taskResultList != null && taskResultList.size() > 0) {
                TranscriptionTaskResult taskResult = taskResultList.get(0);
                // 获取转写结果的url
                String transcriptionUrl = taskResult.getTranscriptionUrl();
                // 通过Http获取url内对应的结果
                HttpURLConnection connection =
                    (HttpURLConnection) new URL(transcriptionUrl).openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                BufferedReader reader =
                    new BufferedReader(new InputStreamReader(connection.getInputStream()));
                // 格式化输出json结果
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                System.out.println(gson.toJson(gson.fromJson(reader, JsonObject.class)));
            }
        } catch (Exception e) {
            System.out.println("error: " + e);
        }
        System.exit(0);
    }

}
