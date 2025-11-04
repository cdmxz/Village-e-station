package com.ces.village.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

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
@Schema(name="reason对象", description="审核失败的理由")
public class Reason implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

     @Schema(name = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

     @Schema(name = "1、企业招聘2、零工招聘3、零工求职")
    private Integer type;

     @Schema(name = "招聘信息id")
    private Long informationId;

     @Schema(name = "审核失败理由")
    private String reason;
}
