package com.ces.village.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2023-12-21
 */
@Data
@Accessors(chain = true)
@TableName("chart")
@Schema(name="Chart对象", description="轮播图")
public class Chart implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

     @Schema(name = "轮播图id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Integer id;

     @Schema(name = "轮播图url地址")
    @TableField("url")
    private String url;

     @Schema(name = "轮播图类型：1.首页、2.就业、3.商城")
    @TableField("type")
    private Integer type;


}
