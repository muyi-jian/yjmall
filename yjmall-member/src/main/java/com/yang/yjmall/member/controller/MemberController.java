package com.yang.yjmall.member.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;


import com.yang.yjmall.member.feign.CouponFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yang.yjmall.member.entity.MemberEntity;
import com.yang.yjmall.member.service.MemberService;
import com.yang.common.utils.PageUtils;
import com.yang.common.utils.R;



/**
 * 会员
 *
 * @author yangjian
 * @email yangjian2628168@163.com
 * @date 2021-10-27 22:10:03
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    CouponFeignService couponFeignService;

    @RequestMapping("/coupons")
    public R test(){
        MemberEntity member = new MemberEntity();
        member.setNickname("张三");
        R membercoupon = couponFeignService.membercoupon();
        return Objects.requireNonNull(R.ok().put("member", member)).put("coupons",membercoupon.get("coupons"));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
   
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
