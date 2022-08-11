package ${page.packageName};

import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import lombok.Getter;
import com.qalain.ui.core.page.Page;
<#if page.containsText>
import com.qalain.ui.core.entity.ui.Text;
</#if>
<#if page.containsButton>
import com.qalain.ui.core.entity.ui.Button;
</#if>
<#if page.containsSelect>
import com.qalain.ui.core.entity.ui.Selector;
</#if>
<#if page.containsCheckBox>
import com.qalain.ui.core.entity.ui.CheckBox;
</#if>
<#if page.containsTable>
import com.qalain.ui.core.entity.ui.Table;
</#if>

/**
 * @author lain
 * ${page.comment}
 */
@Getter
@Component
public class ${page.name} extends Page {

    <#list page.fields as field>
    /**
     * ${field.comment}
     */
    @Resource
    private ${field.type} ${field.name};

    </#list>
}
