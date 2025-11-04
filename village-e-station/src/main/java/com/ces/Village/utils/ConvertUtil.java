package com.ces.village.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 将entity转为vo
 */
@Log4j2
public class ConvertUtil {

    /**
     * 将一个对象的属性复制到另一个对象
     *
     * @param sourceObject
     * @param targetClass
     * @param <T>
     * @return
     */
    private static <T> T copyBean(Object sourceObject, Class<T> targetClass) {
        if (sourceObject == null)
            return null;
        T targetObject = null;
        try {
            targetObject = targetClass.getConstructor().newInstance();
            BeanUtils.copyProperties(sourceObject, targetObject);
        } catch (Exception e) {
            log.error(ConvertUtil.class + " convert error :", e);
        }
        return targetObject;
    }

    /**
     * 将对象集合转为另一种类型的对象集合
     *
     * @param sourceObjects
     * @param targetClass
     * @param <T>
     * @return
     */
    private static <T> List<T> copyBeanList(Collection<?> sourceObjects, Class<T> targetClass) {
        if (sourceObjects == null)
            return null;
        List<T> targetObjects = new ArrayList<>();
        try {
            for (Object sourceObject : sourceObjects) {
                T targetObject = targetClass.getConstructor().newInstance();
                BeanUtils.copyProperties(sourceObject, targetObject);
                targetObjects.add(targetObject);
            }
        } catch (Exception e) {
            log.error(ConvertUtil.class + " convert error :", e);
        }
        return targetObjects;
    }

    /**
     * 将entity转为vo
     *
     * @param source
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T entityToVo(Object source, Class<T> target) {

        return copyBean(source, target);
    }


    /**
     * 将entityList转为voList
     *
     * @param sourceList
     * @param target
     * @param <T>
     * @return
     */
    public static <T> List<T> entityToVoList(Collection<?> sourceList, Class<T> target) {
        return copyBeanList(sourceList, target);
    }

    /**
     * 将DTO转为entity
     *
     * @param source
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T dtoToEntity(Object source, Class<T> target) {

        return copyBean(source, target);
    }


    /**
     * 将entityList转为voList
     *
     * @param sourceList
     * @param target
     * @param <T>
     * @return
     */
    public static <T> List<T> dtoToEntityList(Collection<?> sourceList, Class<T> target) {
        return copyBeanList(sourceList, target);
    }
}
