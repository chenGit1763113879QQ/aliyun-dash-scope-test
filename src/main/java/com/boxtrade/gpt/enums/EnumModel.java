package com.boxtrade.gpt.enums;

/**
 * 模型列表
 *
 * @author boxtrade
 * @see https://help.aliyun.com/zh/dashscope/developer-reference/model-square/?spm=a2c4g.11186623.0.0.46fd5d88gUyD0R
 */
public enum EnumModel {

    QWEN_TURBO("qwen-turbo", "通义千问超大规模语言模型，支持中文、英文等不同语言输入。", "模型支持8k tokens上下文，API限定用户输入为6k tokens。"),

    QWEN_PLUS("qwen-plus", "通义千问超大规模语言模型增强版，支持中文、英文等不同语言输入。", "模型支持32k tokens上下文，API限定用户输入为30k tokens。"),

    QWEN_MAX("qwen-max",
        "通义千问千亿级别超大规模语言模型（限时免费开放中），支持中文、英文等不同语言输入。随着模型的升级，qwen-max将滚动更新升级，如果希望使用稳定版本，请使用qwen-max-1201。",
        "模型支持8k tokens上下文，API限定用户输入为6k tokens。"),

    QWEN_MAX_1201("qwen-max-1201", "通义千问千亿级别超大规模语言模型（限时免费开放中），该模型为qwen-max的快照稳定版本，预期维护到下个快照版本发布时间（待定）后一个月。",
        "模型支持8k tokens上下文，API限定用户输入为6k tokens。"),

    QWEN_MAX_LONGCONTEXT("qwen-max-longcontext", "通义千问千亿级别超大规模语言模型（限时免费开放中），支持中文、英文等不同语言输入。",
        "模型支持30k tokens上下文，API限定用户输入为28k tokens。"),

    // LLaMa2 大语言模型是Meta开发并公开发布的系列大型语言模型（LLMs）。
    LLAMA2_7B_CHAT_V2("llama2-7b-chat-v2",
        "LLaMa2系列大语言模型由Meta开发并公开发布，其规模从70亿到700亿参数不等。在灵积上提供的llama2-7b-chat-v2和llama2-13b-chat-v2，分别为7B和13B规模的LLaMa2模型，针对对话场景微调优化后的版本。",
        ""),
    LLAMA2_13B_CHAT_V2("llama2-13b-chat-v2",
        "LLaMa2系列大语言模型由Meta开发并公开发布，其规模从70亿到700亿参数不等。在灵积上提供的llama2-7b-chat-v2和llama2-13b-chat-v2，分别为7B和13B规模的LLaMa2模型，针对对话场景微调优化后的版本。",
        ""),

    // 通义千问系列模型为阿里云研发的大语言模型。千问模型基于Transformer架构，在超大规模的预训练数据上进行训练得到。预训练数据类型多样，覆盖广泛，包括大量网络文本、专业书籍、代码等。同时，在预训练模型的基础之上，使用对齐机制打造了模型的chat版本。其中千问-1.8B是18亿参数规模的模型，千问-7B是70亿参数规模的模型，千问-14B是140亿参数规模的模型，千问-72B是720亿参数规模的模型。
    // 灵积平台上提供的千问开源模型，进行了针对性的推理性能优化，为广大开发者提供便捷的API服务。其中1.8B模型基于最新在魔搭社区开源的最新版本，7B模型基于最新在魔搭社区上开源的V1.1版本，而14B模型同样基于魔搭社区上最新版本提供， 72B模型基于魔搭社区开源的最新版本提供。
    QWEN_72B_CHAT("qwen-72b-chat", "通义千问对外开源的72B规模参数量的经过人类指令对齐的chat模型",
        "支持32k tokens上下文，输入最大30k tokens，输出最大2k tokens。"),

    QWEN_14B_CHAT("qwen-14b-chat", "通义千问对外开源的14B规模参数量的经过人类指令对齐的chat模型", "模型支持8k tokens上下文，API限定用户输入为6k Tokens。"),

    QWEN_7B_CHAT("qwen-7b-chat", "通义千问对外开源的7B规模参数量的经过人类指令对齐的chat模型", "（输入输出限制未给出，请提供后补充）"),

    QWEN_1_8B_LONGCONTEXT_CHAT("qwen-1.8b-longcontext-chat", "通义千问对外开源的1.8B规模参数量的经过人类指令对齐的chat模型",
        "支持32k tokens上下文，输入最大30k tokens，输出最大2k tokens。"),

    QWEN_1_8B_CHAT("qwen-1.8b-chat", "通义千问对外开源的1.8B规模参数量的经过人类指令对齐的chat模型", "模型支持8k tokens上下文，API限定用户输入为6k Tokens。"),

    /**
     * 通义千问VL是阿里云研发的大规模视觉语言模型（Large Vision Language Model, LVLM），可以以图像、文本、检测框作为输入，并以文本和检测框作为输出，支持中文多模态对话及多图对话，并具有更好的性能，是首个支持中文开放域的通用定位模型和首个开源448分辨率的大规模视觉语言模型。
     *
     * 通义千问VL模型主要有以下特点：
     *
     *     强大的性能：在四大类多模态任务的标准英文测评中（Zero-shot Captioning/VQA/DocVQA/Grounding）上，均取得同等通用模型大小下最好效果；
     *
     *     多语言对话模型：天然支持英文、中文等多语言对话，端到端支持图片里中英双语的长文本识别；
     *
     *     多图交错对话：支持多图输入和比较，指定图片问答，多图文学创作等；
     *
     *     首个支持中文开放域定位的通用模型：通过中文开放域语言表达进行检测框标注；
     *
     *     细粒度识别和理解：相比于目前其它开源LVLM使用的224分辨率，Qwen-VL是首个开源的448分辨率的LVLM模型。更高分辨率可以提升细粒度的文字识别、文档问答和检测框标注。
     */
    QWEN_VL_V1("qwen-vl-v1", "以 Qwen-7B 语言模型初始化，添加图像模型，图像输入分辨率为448的预训练模型。", ""),
    QWEN_VL_CHAT_V1("qwen-vl-chat-v1", "通义千问VL支持灵活的交互方式，包括多图、多轮问答、创作等能力的模型。", ""),

    ;

    private final String modelName;
    private final String modelDescription;
    private final String inputOutputLimits;

    EnumModel(String modelName, String modelDescription, String inputOutputLimits) {
        this.modelName = modelName;
        this.modelDescription = modelDescription;
        this.inputOutputLimits = inputOutputLimits;
    }

    public String getModelName() {
        return modelName;
    }

    public String getModelDescription() {
        return modelDescription;
    }

    public String getInputOutputLimits() {
        return inputOutputLimits;
    }

}
