package com.ces.Village.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 审核失败的理由
 * </p>
 *
 * @author author
 * @since 2023-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("reason")
@ApiModel(value="reason对象", description="审核失败的理由")
public class Reason implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "1、企业招聘2、零工招聘3、零工求职")
    private Integer type;

    @ApiModelProperty(value = "招聘信息id")
    private Long informationId;

    @ApiModelProperty(value = "审核失败理由")
    private String reason;
}
