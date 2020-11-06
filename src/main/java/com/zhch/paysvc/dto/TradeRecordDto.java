package com.zhch.paysvc.dto;

import cn.hutool.core.date.DateUtil;
import com.zhch.paysvc.config.TradeStateEnum;
import com.zhch.paysvc.entity.TradeRecord;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author lumos
 * @date 2020/10/18 07
 */
@Setter
@Getter
public class TradeRecordDto extends TradeRecord {

    private String hospitalName;

    private String stateLabel;

    private String timeStartLabel;

    private String timeEndLabel;

    public String getStateLabel() {
        return TradeStateEnum.getLabelByState(this.getState());
    }

    public String getTimeStartLabel() {
        if (StringUtils.hasLength(getTimeStart())) {
            Date date = DateUtil.parse(getTimeStart(),"yyyyMMddHHmmss");
            return DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
        }
        return null;
    }

    public String getTimeEndLabel() {
        if (StringUtils.hasLength(getTimeEnd())) {
            Date date = DateUtil.parse(getTimeEnd(),"yyyyMMddHHmmss");
            return DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
        }
        return null;
    }
}
