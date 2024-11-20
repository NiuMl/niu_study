package com.niuml.niu_study_security.common.service;

import java.util.Locale;

/***
 * @author niumengliang
 * Date:2024/11/20
 * Time:16:36
 */
public interface DatabaseI18nMessageSource {
    /**
     * 获取翻译结果
     * @param i18nKey key
     * @param args 动态参数
     * @param language 语言
     */
    String getMessage(String i18nKey, Object[] args, Locale language);

}
