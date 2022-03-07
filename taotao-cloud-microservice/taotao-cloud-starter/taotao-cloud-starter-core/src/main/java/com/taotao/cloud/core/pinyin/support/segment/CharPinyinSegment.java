package com.taotao.cloud.core.pinyin.support.segment;


import com.taotao.cloud.core.heaven.annotation.ThreadSafe;
import com.taotao.cloud.core.heaven.support.handler.IHandler;
import com.taotao.cloud.core.heaven.util.util.ArrayPrimitiveUtil;
import com.taotao.cloud.core.pinyin.spi.IPinyinSegment;
import java.util.List;

/**
 * 直接基于字符切分
 * （1）不具有分词准确性
 * （2）性能较好
 *
 * TODO: 后期替换为分词框架
 *
 * 分词应该保持中立，因为涉及到繁简体转换。
 *
 */
@ThreadSafe
public class CharPinyinSegment implements IPinyinSegment {

    @Override
    public List<String> segment(String string) {
        char[] chars = string.toCharArray();

        return ArrayPrimitiveUtil.toList(chars, new IHandler<Character, String>() {
            @Override
            public String handle(Character character) {
                return String.valueOf(character);
            }
        });
    }

}
