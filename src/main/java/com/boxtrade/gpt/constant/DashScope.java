package com.boxtrade.gpt.constant;

import com.alibaba.dashscope.aigc.generation.Generation;

/**
 * 前提条件
 *
 *     已开通服务并获得API-KEY：开通DashScope并创建API-KEY。
 *
 *     已安装最新版SDK：安装DashScope SDK。
 * @author 1763113879@qq.com
 * @version V2.1
 * @since 2.1.0 2024/1/5 14:48
 */
public class DashScope {

    /**
     * API-KEY
     *
     * DashScope灵积模型服务使用API-KEY作为调用API的密钥。API-KEY承担了调用鉴权、计量计费等功能。API-KEY被广泛应用于DashScope API中，通过 api-key 参数指定。DashScope中所有不同的模型API服务都可以使用一个API-KEY、以一致的编程方式进行调用，方便开发者进行跨模态的、多个模型的接续调用。
     *
     * 请妥善保存和使用API-KEY，如需进一步了解API-KEY有关的安全信息，请参考保护并正确使用API-KEY。
     */
    public static String API_KEY = "sk-xxxxxxxxxxx";

    /**
     * 模型列表
     *
     * 通义千问超大规模语言模型（限时免费开放中），支持中文、英文等不同语言输入。随着模型的升级，qwen-max将滚动更新升级，如果希望使用稳定版本，请使用qwen-max-1201。
     *
     * 通义千问千亿级别超大规模语言模型（限时免费开放中），支持中文、英文等不同语言
     */
    public static String[] ALL_MODELS = {
        Generation.Models.QWEN_TURBO,
        Generation.Models.BAILIAN_V1, Generation.Models.DOLLY_12B_V2,
        Generation.Models.QWEN_PLUS, Generation.Models.QWEN_MAX,
        "qwen-max-1201",
        "qwen-max-longcontext",
        "qwen-72b-chat",
        "qwen-14b-chat",
        "qwen-7b-chat",
        "qwen-1.8b-longcontext-chat",
        "qwen-1.8b-chat",

        "llama2-7b-chat-v2",
        "baichuan-7b-v1",
        "baichuan2-7b-chat-v1 " , "baichuan2-13b-chat-v1",
        "billa-7b-sft-v1","chatyuan-large-v2","moss-moon-003-sft-v1","belle-llama-13b-2m-v1"
        ,"dolly-12b-v2","ziya-llama-13b-v1"
        ,
    };

    /**
     * 模型列表 可用的 排序
     *
     * 通义千问超大规模语言模型（限时免费开放中），支持中文、英文等不同语言输入。随着模型的升级，qwen-max将滚动更新升级，如果希望使用稳定版本，请使用qwen-max-1201。
     *
     * 通义千问千亿级别超大规模语言模型（限时免费开放中），支持中文、英文等不同语言
     */
    public static String[] ALL_ENABLE_MODELS = {
        "qwen-max-longcontext",
        Generation.Models.QWEN_PLUS,//收费
        Generation.Models.QWEN_TURBO,
        Generation.Models.QWEN_MAX,
        "qwen-max-1201",
        "qwen-72b-chat",
        "qwen-14b-chat",
        "qwen-7b-chat",
        "qwen-1.8b-longcontext-chat",
        "qwen-1.8b-chat",

    };

}
