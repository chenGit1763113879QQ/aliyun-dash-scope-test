package com.boxtrade.gpt.aliyun;

import org.junit.jupiter.api.Test;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.MessageManager;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;

/**
 * LLAMA2模型API调用需"申请体验"并通过后才可使用，否则API调用将返回错误状态码。
 *
 * Llama 2系列是来自Meta开发并公开发布的大型语言模型（LLMs）。该系列模型提供了多种参数大小（7B、13B和70B等）的版本，并同时提供了预训练和针对对话场景的微调版本。 Llama 2系列使用了2T token进行训练，相比于LLama多出40%，上下文长度从LLama的2048升级到4096，可以理解更长的文本， 在多个公开基准测试上超过了已有的开源模型。 采用了高质量的数据进行微调和基于人工反馈的强化学习训练，具有较高的可靠性和安全性。
 *
 * 当前在灵积平台部署的服务分别来自于ModelScope社区模型：
 *
 *     llama2-7b-chat-v2，模型版本 : v1.0.2
 *
 *     llama2-13b-chat-v2，模型版本：v1.0.2
 * @author 1763113879@qq.com
 * @version V2.1
 * @since 2.1.0 2024/1/5 15:28
 */
public class Llama2Test extends DashScopeTest {

    @Test
    public void usage() throws NoApiKeyException, ApiException, InputRequiredException {
        MessageManager msgManager = new MessageManager(10);
        Message systemMsg = Message.builder()
            .role(Role.SYSTEM.getValue())
            .content("You are a helpful assistant.")
            .build();
        Message userMsg = Message.builder()
            .role(Role.USER.getValue())
            .content("介绍下杭州")
            .build();
        msgManager.add(systemMsg);
        msgManager.add(userMsg);

        GenerationParam param = GenerationParam.builder()
            .model("llama2-13b-chat-v2")
            .messages(msgManager.get())
            .build();
        Generation gen = new Generation();
        GenerationResult result = gen.call(param);
        System.out.println(JsonUtils.toJson(result));
    }
}
