package com.boxtrade.gpt.aliyun;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationMessage;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalMessageItemImage;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalMessageItemText;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.exception.UploadFileException;

import io.reactivex.Flowable;

/**
 * @author 1763113879@qq.com
 * @version V2.1
 * @since 2.1.0 2024/1/5 15:35
 */
public class QwenVLTest extends DashScopeTest{

    /**
     * 通义千问VL支持灵活的交互方式，包括多图、多轮问答、创作等能力的模型。
     * @throws ApiException
     * @throws NoApiKeyException
     * @throws UploadFileException
     */
    @Test
    public   void simpleMultiModalConversationCall() throws ApiException, NoApiKeyException, UploadFileException {
        MultiModalConversation conv = new MultiModalConversation();
        MultiModalMessageItemImage userImage = new MultiModalMessageItemImage(
            "https://dashscope.oss-cn-beijing.aliyuncs.com/images/dog_and_girl.jpeg");
        MultiModalMessageItemText userText = new MultiModalMessageItemText("这是什么?");
        MultiModalConversationMessage userMessage =
            MultiModalConversationMessage.builder().role(Role.USER.getValue())
                .content(Arrays.asList(userImage, userText)).build();
        MultiModalConversationParam param = MultiModalConversationParam.builder()
            .model(MultiModalConversation.Models.QWEN_VL_CHAT_V1)
            .message(userMessage).build();
        MultiModalConversationResult result = conv.call(param);
        System.out.print(result);
    }

    /*
     * sample of use local file
     * Windows file format: file:///D:/test/images/test.png
     * Linux & Mac format: file://The_absolute_local_path
     *
     */
    @Test
    public   void callWithLocalFile()
    throws ApiException, NoApiKeyException, UploadFileException {
        String localFilePath = "file:///Users/chenzhongqiang/Downloads/dog_and_girl.jpeg";
        MultiModalConversation conv = new MultiModalConversation();
        MultiModalMessageItemImage userImage = new MultiModalMessageItemImage(
            localFilePath);
        MultiModalMessageItemText userText = new MultiModalMessageItemText("这是什么?");
        MultiModalConversationMessage userMessage = MultiModalConversationMessage.builder()
            .role(Role.USER.getValue()).content(Arrays.asList(userImage, userText)).build();
        MultiModalConversationParam param = MultiModalConversationParam.builder()
            .model(MultiModalConversation.Models.QWEN_VL_CHAT_V1)
            .message(userMessage)
            .build();
        Flowable<MultiModalConversationResult> result = conv.streamCall(param);
        result.blockingForEach(item -> {
            System.out.println(item);
        });
    }

    /**
     * 多轮对话
     * @throws ApiException
     * @throws NoApiKeyException
     * @throws UploadFileException
     */
    @Test
    public   void multiRoundConversationCall() throws ApiException, NoApiKeyException, UploadFileException {
        MultiModalConversation conv = new MultiModalConversation();
        MultiModalMessageItemText systemText = new MultiModalMessageItemText("你是生活助手机器人。");
        MultiModalConversationMessage systemMessage = MultiModalConversationMessage.builder()
            .role(Role.SYSTEM.getValue()).content(Arrays.asList(systemText)).build();
        MultiModalMessageItemImage userImage = new MultiModalMessageItemImage(
            "https://dashscope.oss-cn-beijing.aliyuncs.com/images/dog_and_girl.jpeg");
        MultiModalMessageItemText userText = new MultiModalMessageItemText("这是什么?");
        MultiModalConversationMessage userMessage =
            MultiModalConversationMessage.builder().role(Role.USER.getValue())
                .content(Arrays.asList(userImage, userText)).build();
        List<Object> messages = new ArrayList<>();
        messages.add(systemMessage);
        messages.add(userMessage);
        MultiModalConversationParam param = MultiModalConversationParam.builder()
            .model(MultiModalConversation.Models.QWEN_VL_CHAT_V1)
            .messages(messages)
            .build();
        MultiModalConversationResult result = conv.call(param);
        System.out.println(result);
        MultiModalMessageItemText assistentText = new MultiModalMessageItemText(
            result.getOutput().getChoices().get(0).getMessage().getContent().toString());
        MultiModalConversationMessage assistentMessage = MultiModalConversationMessage.builder()
            .role(Role.ASSISTANT.getValue()).content(Arrays.asList(assistentText)).build();
        messages.add(assistentMessage);
        userText = new MultiModalMessageItemText("他们在干什么？");
        messages.add(MultiModalConversationMessage.builder().role(Role.USER.getValue())
            .content(Arrays.asList(userText)).build());
        param.setMessages(messages);
        result = conv.call(param);
        System.out.print(result);
    }

}
