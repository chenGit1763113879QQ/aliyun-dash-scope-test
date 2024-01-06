package com.boxtrade.gpt.aliyun;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

import org.junit.jupiter.api.Test;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.MessageManager;
import com.alibaba.dashscope.common.ResultCallback;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;

import io.reactivex.Flowable;

/**
 * @author 1763113879@qq.com
 * @version V2.1
 * @since 2.1.0 2024/1/5 15:05
 */
public class QwenTest extends DashScopeTest {

    @Test
    public void callWithMessage() throws NoApiKeyException, ApiException, InputRequiredException {
        Generation gen = new Generation();
        MessageManager msgManager = new MessageManager(10);
        Message systemMsg = Message.builder()
            .role(Role.SYSTEM.getValue())
            .content("You are a helpful assistant.")
            .build();
        Message userMsg = Message.builder()
            .role(Role.USER.getValue())
            .content("如何做西红柿鸡蛋？")
            .build();
        msgManager.add(systemMsg);
        msgManager.add(userMsg);
        QwenParam param = QwenParam.builder()
            .model(Generation.Models.QWEN_TURBO)
            .messages(msgManager.get())
            .resultFormat(QwenParam.ResultFormat.MESSAGE)
            .build();
        GenerationResult result = gen.call(param);
        System.out.println(result);
    }

    private final static String PROMPT = "就当前的海洋污染的情况，写一份限塑的倡议书提纲，需要有理有据地号召大家克制地使用塑料制品";

    public static void qwenQuickStart() throws NoApiKeyException, ApiException, InputRequiredException {
        Generation gen = new Generation();
        QwenParam param = QwenParam.builder()
            .model(Generation.Models.QWEN_TURBO)
            .prompt(PROMPT)
            .topP(0.8)
            .build();
        GenerationResult result = gen.call(param);
        System.out.println(JsonUtils.toJson(result));
    }

    public static void qwenQuickStartCallback()
    throws NoApiKeyException, ApiException, InputRequiredException, InterruptedException {
        Generation gen = new Generation();
        QwenParam param = QwenParam.builder()
            .model(Generation.Models.QWEN_TURBO)
            .prompt(PROMPT)
            .topP(0.8)
            .build();
        Semaphore semaphore = new Semaphore(0);
        gen.call(param, new ResultCallback<GenerationResult>() {

            @Override
            public void onEvent(GenerationResult message) {
                System.out.println(message);
            }

            @Override
            public void onError(Exception ex) {
                System.out.println(ex.getMessage());
                semaphore.release();
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
                semaphore.release();
            }

        });
        semaphore.acquire();
    }

    /**
     * 通过prompt调用
     */
    @Test
    public void testqwenQuickStartprompt() {
        try {
            qwenQuickStart();
            qwenQuickStartCallback();
        } catch (ApiException | NoApiKeyException | InputRequiredException | InterruptedException e) {
            System.out.println(String.format("Exception %s", e.getMessage()));
        }
        System.exit(0);
    }

    /**
     * 通过messages调用
     */
    @Test
    public void callWithMessage2() throws NoApiKeyException, ApiException, InputRequiredException {
        Generation gen = new Generation();
        MessageManager msgManager = new MessageManager(10);
        Message systemMsg = Message.builder()
            .role(Role.SYSTEM.getValue())
            .content("You are a helpful assistant.")
            .build();
        Message userMsg = Message.builder()
            .role(Role.USER.getValue())
            .content("你好，周末去哪里玩？我在宁波江北区")
            .build();
        msgManager.add(systemMsg);
        msgManager.add(userMsg);
        QwenParam param = QwenParam.builder()
            .model(Generation.Models.QWEN_PLUS)
            .messages(msgManager.get())
            .resultFormat(QwenParam.ResultFormat.MESSAGE)
            .topP(0.8)
            .enableSearch(true)
            .build();
        GenerationResult result = gen.call(param);
        System.out.println(result);
        msgManager.add(result);
        System.out.println(JsonUtils.toJson(result));
        param.setPrompt("找个近点的");
        param.setMessages(msgManager.get());
        result = gen.call(param);
        System.out.println(result);
        System.out.println(JsonUtils.toJson(result));
    }

    /**
     * 多轮会话
     * @throws NoApiKeyException
     * @throws ApiException
     * @throws InputRequiredException
     */
    @Test
    public void callWithMessage3() throws NoApiKeyException, ApiException, InputRequiredException {
        Generation gen = new Generation();
        MessageManager msgManager = new MessageManager(10);
        Message systemMsg = Message.builder()
            .role(Role.SYSTEM.getValue())
            .content("你是智能助手机器人")
            .build();
        Message userMsg = Message.builder()
            .role(Role.USER.getValue())
            .content("如何做西红柿炖牛腩？")
            .build();
        msgManager.add(systemMsg);
        msgManager.add(userMsg);
        QwenParam param = QwenParam.builder()
            .model(Generation.Models.QWEN_PLUS)
            .messages(msgManager.get())
            .resultFormat(QwenParam.ResultFormat.MESSAGE)
            .topP(0.8)
            .enableSearch(true)
            .build();
        GenerationResult result = gen.call(param);
        System.out.println(result);
        msgManager.add(result);
        System.out.println(JsonUtils.toJson(result));
        param.setPrompt("不放糖可以吗？");
        param.setMessages(msgManager.get());
        result = gen.call(param);
        System.out.println(result);
        System.out.println(JsonUtils.toJson(result));
    }

    public static void streamCallWithMessage()
    throws NoApiKeyException, ApiException, InputRequiredException {
        Generation gen = new Generation();
        Message userMsg = Message
            .builder()
            .role(Role.USER.getValue())
            .content("如何做西红柿炖牛腩？")
            .build();
        QwenParam param =
            QwenParam.builder().model(Generation.Models.QWEN_PLUS).messages(Arrays.asList(userMsg))
                .resultFormat(QwenParam.ResultFormat.MESSAGE)
                .topP(0.8)
                .enableSearch(true)
                .incrementalOutput(true) // get streaming output incrementally
                .build();
        Flowable<GenerationResult> result = gen.streamCall(param);
        StringBuilder fullContent = new StringBuilder();
        result.blockingForEach(message -> {
            fullContent.append(message.getOutput().getChoices().get(0).getMessage().getContent());
            System.out.println(JsonUtils.toJson(message));
        });
        System.out.println("Full content: \n" + fullContent.toString());
    }

    public static void streamCallWithCallback()
    throws NoApiKeyException, ApiException, InputRequiredException,InterruptedException {
        Generation gen = new Generation();
        Message userMsg = Message
            .builder()
            .role(Role.USER.getValue())
            .content("如何做西红柿炖牛腩？")
            .build();
        QwenParam param = QwenParam
            .builder()
            .model(Generation.Models.QWEN_PLUS)
            .resultFormat(QwenParam.ResultFormat.MESSAGE)
            .messages(Arrays.asList(userMsg))
            .topP(0.8)
            .incrementalOutput(true) // get streaming output incrementally
            .build();
        Semaphore semaphore = new Semaphore(0);
        StringBuilder fullContent = new StringBuilder();
        gen.streamCall(param, new ResultCallback<GenerationResult>() {

            @Override
            public void onEvent(GenerationResult message) {
                fullContent.append(message.getOutput().getChoices().get(0).getMessage().getContent());
                System.out.println(message);
            }
            @Override
            public void onError(Exception err){
                System.out.println(String.format("Exception: %s", err.getMessage()));
                semaphore.release();
            }

            @Override
            public void onComplete(){
                System.out.println("Completed");
                semaphore.release();
            }

        });
        semaphore.acquire();
        System.out.println("Full content: \n" + fullContent.toString());
    }

    /**
     * 流式输出
     */
    @Test
    public   void streamCallWithMessageTest() {
        try {
            streamCallWithMessage();
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            System.out.println(e.getMessage());
        }
        try {
            streamCallWithCallback();
        } catch (ApiException | NoApiKeyException | InputRequiredException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        System.exit(0);
    }
}
