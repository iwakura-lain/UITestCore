package com.qalain.ui.core.page;

import com.qalain.ui.core.engine.EngineDriver;
import lombok.Data;

import javax.annotation.Resource;

/**
 * @author lain
 * @Description
 * @create 2022-01-22
 */
@Data
public class Page {

    /**
     * 页面地址
     */
    private String url;

    @Resource
    private EngineDriver engine;

    public void open() {
        engine.open(url);
    }

    public void close() {
        engine.close();
    }

    /**
     * 暂存数据
     * @param key 参数键
     * @param value 参数值
     */
    public void putData(String key, String value) {
        System.setProperty(key, value);
    }

    /**
     * 获取暂存数据
     * @param key 参数键
     * @return
     */
    public String getData(String key) {
        return System.getProperty(key);
    }
}
