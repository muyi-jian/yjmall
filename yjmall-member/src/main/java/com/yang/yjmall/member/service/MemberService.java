package com.yang.yjmall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.common.utils.PageUtils;
import com.yang.yjmall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author yangjian
 * @email yangjian2628168@163.com
 * @date 2021-10-27 22:10:03
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

