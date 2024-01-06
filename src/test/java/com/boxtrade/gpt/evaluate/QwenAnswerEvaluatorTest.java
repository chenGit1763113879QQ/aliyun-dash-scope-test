package com.boxtrade.gpt.evaluate;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationOutput;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.MessageManager;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import com.boxtrade.gpt.aliyun.DashScopeTest;
import com.boxtrade.gpt.constant.DashScope;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 1763113879@qq.com
 * @version V2.1
 * @since 2.1.0 2024/1/5 16:46
 */
@Slf4j
public class QwenAnswerEvaluatorTest extends DashScopeTest {

    String fileName = "answers.txt";

    @Test
    public void testAnswer() throws NoApiKeyException, InputRequiredException {

        String answersMsg = readFile();

        Generation gen = new Generation();
        MessageManager msgManager = new MessageManager(100);
        String systemMsgContent = "You are an equity expert.";
        String userMsgMsgContent = "分析一下宁波银行的股票和投资建议";

        qwenAnswerEvaluatorCallWithMessageByMax(gen, msgManager, systemMsgContent + userMsgMsgContent, answersMsg);

    }

    @Nullable
    private String readFile() {
        String answersMsg = null;
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(fileName));
            answersMsg = new String(bytes, StandardCharsets.UTF_8);
            log.info("answersMsg:{}", answersMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answersMsg;
    }

    private void qwenAnswerEvaluatorCallWithMessageByMax(Generation gen, MessageManager msgManager, String questionMsg,
        String answersMsg) throws NoApiKeyException, InputRequiredException {
        Message systemMsg = Message.builder()
            .role(Role.SYSTEM.getValue())
            .content("You are a large model supervision expert.")
            .build();
        Message userMsg = Message.builder()
            .role(Role.USER.getValue())
            .content(String.format("分析下面模型对问题[%s]的反馈，请给出每个模型回答问题的优缺点和评分，并筛选出最优的回答，评分卡为0-100分，评分卡内容以表格形式展示；所有模型回复问题如下\n", questionMsg)
                + readFile())
            .build();
        msgManager.add(systemMsg);
        msgManager.add(userMsg);
        QwenParam param = QwenParam.builder()
            .model("qwen-max-longcontext")
            .messages(msgManager.get())
            .resultFormat(QwenParam.ResultFormat.MESSAGE)
            .topP(0.8)
            .enableSearch(true)
            .build();
        GenerationResult result = gen.call(param);
        System.out.println(result.getOutput());
        System.out.println(result.getOutput().getChoices());

        String mdFileName = "模型评分.md";
        //写入md
        writeFile(result.getOutput().getChoices().get(0).getMessage().getContent(),mdFileName);

        msgManager.add(result);
        System.out.println(JsonUtils.toJson(result));
    }

    @Test
    public void testCase() throws NoApiKeyException, ApiException, InputRequiredException {
        Generation gen = new Generation();
        MessageManager msgManager = new MessageManager(100);
        String systemMsgContent = "You are an equity expert.";
        String userMsgMsgContent = "分析一下宁波银行的股票和投资建议";
        Message systemMsg = Message.builder()
            .role(Role.SYSTEM.getValue())
            .content(systemMsgContent)
            .build();
        Message userMsg = Message.builder()
            .role(Role.USER.getValue())
            .content(userMsgMsgContent)
            .build();
        msgManager.add(systemMsg);
        msgManager.add(userMsg);
        // QwenParam param =
        //     QwenParam.builder().model("qwen-max-longcontext").messages(msgManager.get())
        //         .resultFormat(QwenParam.ResultFormat.MESSAGE)
        //         .topP(0.8)
        //         .enableSearch(true)
        //         .build();
        // GenerationResult result = gen.call(param);
        // System.out.println(result.getOutput());
        // msgManager.add(result);
        // System.out.println(JsonUtils.toJson(result));

        MessageManager msgManagerStatic = new MessageManager(100);

        System.out.println("Content appended successfully.");

        deletIfExists(fileName);

        foreachGpts(gen, msgManager, msgManagerStatic);
        qwenAnswerEvaluatorCallWithMessageByMax(gen, msgManagerStatic, systemMsgContent + userMsgMsgContent, null);

    }

    private void deletIfExists(String fileName) {
        try {
            Files.deleteIfExists(Paths.get(fileName));
            log.info("Content deleted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void foreachGpts(Generation gen, MessageManager msgManager, MessageManager msgManagerStatic)
    throws NoApiKeyException, InputRequiredException {
        String[] models = DashScope.ALL_MODELS;
        for (int i = 0; i < models.length; i++) {
            String model = models[i];
            String message = execModelMessage(msgManager, gen, model);
            String botmessage = String.format("第【%d】个模型 [%s]  的回答为 %s", i + 1, model, message);
            log.info("第【{}】个模型 {} 的回答为 {}", i + 1, model, message);
            writeFile(botmessage,fileName);
            msgManagerStatic.add(Message.builder()
                .role(Role.ATTACHMENT.getValue())
                .content(botmessage)
                .build());
            log.info("========================================================");
        }
    }

    private void writeFile(String message, String fileName) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            Files.write(Paths.get(fileName), (message + System.lineSeparator()).getBytes(),
                StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String execModelMessage(MessageManager msgManager, Generation gen, String model)
    throws NoApiKeyException, InputRequiredException {
        String messageContent = null;
        try {
            QwenParam param = QwenParam.builder()
                .model(model)
                .messages(msgManager.get())
                .resultFormat(QwenParam.ResultFormat.MESSAGE)
                .build();
            GenerationResult result = gen.call(param);
            GenerationOutput output = result.getOutput();
            Message message = output.getChoices()
                .get(0)
                .getMessage();
            log.info("模型 {}- 的回答为 {}", model, message);

            return message.getContent();
        } catch (ApiException e) {
            messageContent = e.getMessage();
            e.printStackTrace();
        } catch (NoApiKeyException e) {
            messageContent = e.getMessage();
            e.printStackTrace();
        } catch (InputRequiredException e) {
            messageContent = e.getMessage();
            e.printStackTrace();
        }
        return messageContent;
    }

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
