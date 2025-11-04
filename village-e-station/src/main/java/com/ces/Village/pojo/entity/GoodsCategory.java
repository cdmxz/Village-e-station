package com.ces.village.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 商品分类
 * </p>
 *
 * @author author
 * @since 2023-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("goods_category")
@Schema(name="GoodsCategory对象", description="商品分类")
public class GoodsCategory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

     @Schema(name = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    @JsonProperty("category_id")
    private Long id;

     @Schema(name = "分类名称")
    @TableField("name")
    @JsonProperty("name")
    private String name;


}
