package com.hewentian.util;

import java.lang.reflect.Field;

/**
 * <p>
 * <b>MyBatisUtil</b> 是
 * </p>
 *
 * @author Tim Ho
 * @since 2021-04-20 17:53:19
 */
public class MyBatisUtil {
    public static void genInsertXml(Object objet) {
        Class clazz = objet.getClass();

        Field[] fields = clazz.getDeclaredFields(); // 仅本类，包括public, protected, default, private修饰的field
        if (fields.length == 0) {
            return;
        }

        StringBuilder xml = new StringBuilder();
        xml.append("<insert id=\"insertSelective\" parameterType=\"\" useGeneratedKeys=\"true\" keyProperty=\"id\">").append(System.getProperty("line.separator"));
        xml.append("    insert into table_name").append(System.getProperty("line.separator"));
        xml.append("    <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">").append(System.getProperty("line.separator"));

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            xml.append("        <if test=\"").append(field.getName()).append(" != null\">").append(System.getProperty("line.separator"));
            xml.append("            ").append(CommonUtil.camelCase2camelCaseUnderscore(field.getName())).append(",").append(System.getProperty("line.separator"));
            xml.append("        </if>").append(System.getProperty("line.separator"));
        }

        xml.append("    </trim>").append(System.getProperty("line.separator"));
        xml.append("    <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">").append(System.getProperty("line.separator"));

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            xml.append("        <if test=\"").append(field.getName()).append(" != null\">").append(System.getProperty("line.separator"));
            xml.append("            #{").append(field.getName()).append(",jdbcType=").append(getJdbcType(field.getType().getSimpleName()));
            xml.append("},").append(System.getProperty("line.separator"));
            xml.append("        </if>").append(System.getProperty("line.separator"));
        }

        xml.append("    </trim>").append(System.getProperty("line.separator"));
        xml.append("</insert>").append(System.getProperty("line.separator"));

        System.out.println(xml.toString());
    }

    public static void genUpdateXml(Object objet) {
        Class clazz = objet.getClass();

        Field[] fields = clazz.getDeclaredFields(); // 仅本类，包括public, protected, default, private修饰的field
        if (fields.length == 0) {
            return;
        }

        StringBuilder xml = new StringBuilder();
        xml.append("<update id=\"updateByPrimaryKeySelective\" parameterType=\"\">").append(System.getProperty("line.separator"));
        xml.append("    update table_name").append(System.getProperty("line.separator"));
        xml.append("    <set>").append(System.getProperty("line.separator"));

        Field idField = null;
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if ("id".equals(field.getName())) {
                idField = field;
                continue;
            }

            xml.append("        <if test=\"").append(field.getName()).append(" != null\">").append(System.getProperty("line.separator"));
            xml.append("            ").append(CommonUtil.camelCase2camelCaseUnderscore(field.getName())).append(" = #{")
                    .append(field.getName()).append(",jdbcType=").append(getJdbcType(field.getType().getSimpleName()));
            xml.append("},").append(System.getProperty("line.separator"));
            xml.append("        </if>").append(System.getProperty("line.separator"));
        }

        xml.append("    </set>").append(System.getProperty("line.separator"));

        xml.append("    where id = #{id,jdbcType=").append(getJdbcType(idField.getType().getSimpleName()));
        xml.append("}").append(System.getProperty("line.separator"));

        xml.append("</update>").append(System.getProperty("line.separator"));

        System.out.println(xml.toString());
    }

    private static String getJdbcType(String javaType) {
        switch (javaType) {
            case "String":
                return "VARCHAR";
            case "Integer":
                return "INTEGER";
            case "Date":
                return "DATE";
            case "Double":
                return "DOUBLE";
            case "BigDecimal":
                return "DECIMAL";
            case "Byte":
                return "TINYINT";
        }

        return null;
    }

    /**
     * 为实体对象生成 select columns
     *
     * @param objet            实体对象，例如：User
     * @param tableAliasPrefix 表前缀，例如：u.
     */
    public static void genSelectColumnXml(Object objet, String tableAliasPrefix) {
        Class clazz = objet.getClass();

        Field[] fields = clazz.getDeclaredFields();
        if (fields.length == 0) {
            return;
        }

        StringBuilder xml = new StringBuilder();

        for (int i = 0, len = fields.length; i < len; i++) {
            Field field = fields[i];

            xml.append(tableAliasPrefix);
            xml.append(CommonUtil.camelCase2camelCaseUnderscore(field.getName()));
            xml.append(" as ");
            xml.append(field.getName());

            if (i < len - 1) {
                xml.append(",");
            }

            xml.append(System.getProperty("line.separator"));
        }

        System.out.println(xml.toString());
    }


    public static void genResultMapXml(Object objet) {
        Class clazz = objet.getClass();

        Field[] fields = clazz.getDeclaredFields();
        if (fields.length == 0) {
            return;
        }

        StringBuilder xml = new StringBuilder();
        xml.append("<resultMap id=\"\" type=\"\">").append(System.getProperty("line.separator"));

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if ("id".equals(field.getName())) {
                xml.append("    <id");
            } else {
                xml.append("    <result");
            }

            xml.append(" column=\"").append(CommonUtil.camelCase2camelCaseUnderscore(field.getName()))
                    .append("\" property=\"").append(field.getName())
                    .append("\" jdbcType=\"").append(getJdbcType(field.getType().getSimpleName())).append("\"/>")
                    .append(System.getProperty("line.separator"));
        }

        xml.append("</resultMap>").append(System.getProperty("line.separator"));

        System.out.println(xml.toString());
    }

}
