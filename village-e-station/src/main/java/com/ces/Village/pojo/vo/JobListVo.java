package com.ces.Village.pojo.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.Village.constant.JobTypeConstant;
import com.ces.Village.utils.ConvertUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "就业信息列表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobListVo {
    @JsonProperty("pagecount")
    private Long pageCount;

    @JsonProperty("type")
    private Integer type;

    @JsonProperty("list")
    private List<?> list;

    public static JobListVo createByEntity(Page<?> page, Integer type) {
        List<?> list;
        if (type == JobTypeConstant.GIG_JOB_HUNTING) {
            list = ConvertUtil.entityToVoList(page.getRecords(), MyGigJobHuntingVo.class);
        } else {
            list = ConvertUtil.entityToVoList(page.getRecords(), MyJobVo.class);
        }
        return new JobListVo(page.getPages(), type, list);
    }
}
