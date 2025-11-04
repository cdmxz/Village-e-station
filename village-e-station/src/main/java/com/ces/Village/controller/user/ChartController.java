package com.ces.village.controller.user;

import com.ces.village.common.R;
import com.ces.village.constant.ChartTypeConstant;
import com.ces.village.constant.ErrorCodeEnum;
import com.ces.village.pojo.entity.Chart;
import com.ces.village.service.ChartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Tag(name = "轮播图")
@RestController
@RequestMapping("/api")
public class ChartController {
    @Autowired
    private ChartService chartService;

    /**
     * 获取各个模块的轮播图
     * @param type
     * @return
     */
    @GetMapping("/chart")
    public R<?> chart(@RequestParam("type") Integer type) {
        if (!ChartTypeConstant.isValid(type)) {
            return R.error(ErrorCodeEnum.CHART_TYPE_ERROR);
        }
        List<Chart> chartList = chartService.getChartList(type);
        List<String> urls = new ArrayList<>();
        chartList.forEach(chart -> urls.add(chart.getUrl()));
        Map<String, Object> info = new HashMap<>();
        info.put("img_url", urls);
        return R.success(info);
    }
}
