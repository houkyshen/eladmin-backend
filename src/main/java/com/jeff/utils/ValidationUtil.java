package com.jeff.utils;

import cn.hutool.core.util.ObjectUtil;
import com.jeff.exception.BadRequestException;

/**
 * 验证工具
 */
public class ValidationUtil {

    /**
     * 验证空
     */
    public static void isNull(Object obj, String entity, String parameter , Object value){
        if(ObjectUtil.isNull(obj)){
            String msg = entity + " 不存在: "+ parameter +" is "+ value;
            throw new BadRequestException(msg);
        }
    }
}
