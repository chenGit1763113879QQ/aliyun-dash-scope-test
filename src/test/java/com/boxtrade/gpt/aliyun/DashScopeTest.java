package com.boxtrade.gpt.aliyun;

/**
 * 本文以通义千问大模型（qwen-turbo）为例，介绍通过DashScope玩转大语言模型的基本使用方法。
 *
 * 大语言模型可以与人类就几乎任何话题进行海阔天空的聊天。小明周末在家想做一顿美餐，但由于他是厨房新手，不知道该怎么烹饪。他希望聊天大模型能够帮到他，于是向大模型提出：“用萝卜、土豆、茄子做饭，给我个菜谱”。
 *
 * @author 1763113879@qq.com
 * @version V2.1
 * @since 2.1.0 2024/1/5 14:43
 */

// Copyright (c) Alibaba, Inc. and its affiliates.

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.alibaba.dashscope.aigc.conversation.Conversation;
import com.alibaba.dashscope.aigc.conversation.ConversationParam;
import com.alibaba.dashscope.aigc.conversation.ConversationResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.Constants;
import com.alibaba.dashscope.utils.JsonUtils;
import com.boxtrade.gpt.constant.DashScope;

import io.reactivex.Flowable;

public class DashScopeTest {

    @BeforeAll
    public static void setUp() {
        Constants.apiKey = DashScope.API_KEY;
    }

    /**
     * 从最简单的指令开始
     * @throws ApiException
     * @throws NoApiKeyException
     */
    public static void quickStart() throws ApiException, NoApiKeyException, InputRequiredException {
        Conversation conversation = new Conversation();
        String prompt = "用萝卜、土豆、茄子做饭，给我个菜谱。";
        ConversationParam param = ConversationParam.builder()
            .model(Conversation.Models.QWEN_TURBO)
            .prompt(prompt)
            .build();
        ConversationResult result = conversation.call(param);
        System.out.println(JsonUtils.toJson(result));
    }

    @Test
    public void testquickStart() {
        try {
            quickStart();
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            System.out.println(e.getMessage());
        }
        System.exit(0);
    }

    /**
     * 接收流式输出
     *
     * 上述代码的会在整体文本生成完成后，一次性返回所有输出结果。
     * 小明修改了代码，让大模型一边生成一边输出，即通过流式输出的方式尽快的将中间结果显示在屏幕上。
     * @throws ApiException
     * @throws NoApiKeyException
     */
    @Test
    public  void testStreamCall() throws ApiException, NoApiKeyException {
        Conversation conversation = new Conversation();
        String prompt = "用萝卜、土豆、茄子做饭，给我个菜谱。";
        ConversationParam param = ConversationParam
            .builder()
            .model(Conversation.Models.QWEN_TURBO)
            .prompt(prompt)
            .build();
        try{
            Flowable<ConversationResult> result = conversation.streamCall(param);
            result.blockingForEach(msg->{
                System.out.print(msg.getOutput());
            });
        }catch(ApiException ex){
            System.out.println(ex.getMessage());
        } catch (InputRequiredException e) {
            e.printStackTrace();
        }
    }

    @Test
    public  void quickStartText() throws ApiException, NoApiKeyException, InputRequiredException {
        Conversation conversation = new Conversation();
        String prompt = "用萝卜、土豆、茄子做饭，给我个菜谱。";
        ConversationParam param = ConversationParam
            .builder()
            .model(Conversation.Models.QWEN_TURBO)
            .prompt(prompt)
            .build();
        ConversationResult result = conversation.call(param);
        System.out.println(result.getOutput().getText());
    }

}
