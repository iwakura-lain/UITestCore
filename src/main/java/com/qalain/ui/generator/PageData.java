package com.qalain.ui.generator;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
@Data
public class PageData {
    /**
     * 包名
     */
    private String packageName;

    /**
     * 类名
     */
    private String name;

    /**
     * 类注释
     */
    private String comment;

    private boolean containsText;

    private boolean containsButton;

    private boolean containsSelect;

    private boolean containsCheckBox;

    private boolean containsTable;

    /**
     * 字段
     */
    private List<Field> fields = new ArrayList<>();

    @Getter
    @Setter
    public static class Field {
        /**
         * 字段类型
         */
        private String type;

        /**
         * 字段名称
         */
        private String name;

        /**
         * 字段注释
         */
        private String comment;

        public Field(String type, String name, String comment) {
            this.type = type;
            this.name = name;
            this.comment = comment;
        }
    }

    public void addField(String type, String name, String comment) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(name)) {
            fields.add(new Field(type, name, comment));
        }
    }
}
