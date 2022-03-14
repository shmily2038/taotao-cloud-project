package com.taotao.cloud.promotion.api.dto.search;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * 砍价活动参与实体类
 */
@Data
@Schema(description = "砍价活动参与记录查询对象")
public class KanJiaActivityLogQuery {


    private static final long serialVersionUID = -1583030890805926292L;

    @Schema(description =  "砍价发起活动id")
    private String kanJiaActivityId;


    public <T> QueryWrapper<T> wrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();

        if (CharSequenceUtil.isNotEmpty(kanJiaActivityId)) {
            queryWrapper.like("kanjia_activity_id", kanJiaActivityId);
        }
        queryWrapper.eq("delete_flag", false);
        queryWrapper.orderByDesc("create_time");
        return queryWrapper;
    }
}
