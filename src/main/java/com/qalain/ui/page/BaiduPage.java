package com.qalain.ui.page;

import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import lombok.Getter;
import com.qalain.ui.core.page.Page;
import com.qalain.ui.core.entity.ui.Text;
import com.qalain.ui.core.entity.ui.Button;

/**
 * @author lain
 * 百度首页
 */
@Getter
@Component
public class BaiduPage extends Page {

    /**
     * 输入内容
     */
    @Resource
    private Text wordInput;

    /**
     * 点击搜索
     */
    @Resource
    private Button searchBtn;

}
