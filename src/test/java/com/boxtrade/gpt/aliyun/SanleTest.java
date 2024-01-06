package com.boxtrade.gpt.aliyun;

import java.util.concurrent.Semaphore;

import org.junit.jupiter.api.Test;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.common.ResultCallback;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;

/**
 * 智海三乐教育大模型现处于内部测试阶段，暂不支持外部访问！
 * @author 1763113879@qq.com
 * @version V2.1
 * @since 2.1.0 2024/1/5 16:02
 */
public class SanleTest extends DashScopeTest {

    private static final String model = "sanle-v1";
    private static final String prompt = "请简要介绍一下浙江大学";

    public static void qwenQuickStart() throws NoApiKeyException, ApiException, InputRequiredException {
        Generation gen = new Generation();
        QwenParam param = QwenParam.builder()
            .model(model)
            .prompt(prompt)
            .build();
        GenerationResult result = gen.call(param);
        System.out.println(JsonUtils.toJson(result));
    }

    public static void qwenQuickStartCallback()
    throws NoApiKeyException, ApiException, InputRequiredException, InterruptedException {
        Generation gen = new Generation();
        QwenParam param = QwenParam.builder()
            .model(model)
            .prompt(prompt)
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

    @Test
    public void main() {
        try {
            qwenQuickStart();
            qwenQuickStartCallback();
        } catch (ApiException | NoApiKeyException | InputRequiredException | InterruptedException e) {
            System.out.println(String.format("Exception %s", e.getMessage()));
        }
        System.exit(0);
    }
}
