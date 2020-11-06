package com.zhch.paysvc.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhch.paysvc.dto.ChannelConfigDto;
import com.zhch.paysvc.entity.ChannelConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lumos
 * @since 2020-10-10
 */
@Repository
public interface ChannelConfigMapper extends BaseMapper<ChannelConfig> {

    List<ChannelConfigDto> getChannelConfigs(@Param("hospitalName") String hospitalName,
                                             @Param("channelCode") String channelCode,
                                             @Param("payType") String payType,
                                             @Param("active") Boolean active,
                                             @Param("createdStartAt") String createdStartAt,
                                             @Param("createdEndAt") String createdEndAt,
                                             Page<ChannelConfigDto> page);

    @Select("select a.*, b.hospital_name\n" +
            "        from channel_config a\n" +
            "        inner join hospital_config b on a.hospital_code = b.hospital_code where a.id = #{id}")
    ChannelConfigDto getChannelConfigById(@Param("id") Long id);
}
