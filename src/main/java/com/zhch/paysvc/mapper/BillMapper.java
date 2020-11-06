package com.zhch.paysvc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhch.paysvc.entity.Bill;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lumos
 * @since 2020-10-10
 */
@Repository
public interface BillMapper extends BaseMapper<Bill> {

}
