package com.ces.Village.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

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
@ApiModel(value="Chart对象", description="轮播图")
public class Chart implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "轮播图id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Integer id;

    @ApiModelProperty(value = "轮播图url地址")
    @TableField("url")
    private String url;

    @ApiModelProperty(value = "轮播图类型：1.首页、2.就业、3.商城")
    @TableField("type")
    private Integer type;


}
