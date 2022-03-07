package com.taotao.cloud.core.sensitive.word.support.data;


import com.taotao.cloud.core.heaven.annotation.ThreadSafe;
import com.taotao.cloud.core.heaven.util.guava.Guavas;
import com.taotao.cloud.core.heaven.util.io.StreamUtil;
import com.taotao.cloud.core.heaven.util.util.CollectionUtil;
import com.taotao.cloud.core.sensitive.word.api.IWordData;
import com.taotao.cloud.core.sensitive.word.constant.AppConst;
import java.util.List;

/**
 * 数据加载使用单例的模式，只需要加载一次即可。
 *
 */
@ThreadSafe
public class SensitiveWordData implements IWordData {

    /**
     * 默认的内置行
     *
     * @since 0.0.1
     */
    private static List<String> defaultLines;

    static {
        synchronized (SensitiveWordData.class) {
            long start = System.currentTimeMillis();
            defaultLines = Guavas.newArrayList(AppConst.DICT_SIZE+AppConst.DICT_EN_SIZE);
            defaultLines = StreamUtil.readAllLines("/dict.txt");
            defaultLines.addAll(StreamUtil.readAllLines("/dict_en.txt"));

            // 用户自定义
            List<String> denyList = StreamUtil.readAllLines("/sensitive_word_deny.txt");
            defaultLines.addAll(denyList);

            // 移除白名单词语
            List<String> allowList = StreamUtil.readAllLines("/sensitive_word_allow.txt");
            defaultLines = CollectionUtil.difference(defaultLines, allowList);

            long end = System.currentTimeMillis();
            System.out.println("Sensitive data loaded!, cost time: " + (end - start) + "ms");
        }
    }


    @Override
    public List<String> getWordData() {
        return defaultLines;
    }

}
