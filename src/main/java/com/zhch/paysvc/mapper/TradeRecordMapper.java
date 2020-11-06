package com.zhch.paysvc.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhch.paysvc.dto.TradeRecordDto;
import com.zhch.paysvc.dto.TradeRecordQueryParams;
import com.zhch.paysvc.entity.TradeRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lumos
 * @since 2020-10-10
 */
@Repository
public interface TradeRecordMapper extends BaseMapper<TradeRecord> {

    TradeRecord getTradeRecord(@Param("tradeNo") String tradeNo, @Param("transactionId") String transactionId, @Param("channelCode") String channelCode, @Param("outTradeNo") String outTradeNo);


    TradeRecordDto getTradeDetail(@Param("id") Long id);

    List<TradeRecordDto> getTradeRecords(@Param("tradeNo") String tradeNo,
                                         @Param("transactionId") String transactionId,
                                         @Param("channelCode") String channelCode,
                                         @Param("hospitalCode") String hospitalCode,
                                         @Param("outTradeNo") String outTradeNo,
                                         @Param("phone") String phone,
                                         @Param("name") String name,
                                         @Param("payType") String payType,
                                         @Param("state") String state,
                                         @Param("createdStartAt") String createdStartAt,
                                         @Param("createdEndAt") String createdEndAt,
                                         Page<Object> builderPage);
}
