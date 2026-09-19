package com.campustrade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campustrade.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问层
 * <p>
 * 继承 BaseMapper 后自动拥有 insert / selectById / selectList / updateById / deleteById 等单表方法，
 * 复杂查询再另外加方法（注解或 XML）。
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
