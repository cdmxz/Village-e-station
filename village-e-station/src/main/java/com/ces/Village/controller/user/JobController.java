package com.ces.village.controller.user;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.village.annotation.LoginRequired;
import com.ces.village.common.BaseContext;
import com.ces.village.common.CurrentUser;
import com.ces.village.common.R;
import com.ces.village.constant.ErrorCodeEnum;
import com.ces.village.constant.JobStatusConstant;
import com.ces.village.pojo.dto.JobAddDTO;
import com.ces.village.pojo.dto.JobDTO;
import com.ces.village.pojo.dto.ReviewPushDTO;
import com.ces.village.pojo.vo.JobListVo;
import com.ces.village.service.BaseJobService;
import com.ces.village.service.WxMsgService;
import com.ces.village.service.factory.JobServiceFactory;
import com.ces.village.utils.StringUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Log4j2
@Tag(name = "就业接口")
@RestController
@RequestMapping("/api/job")
public class JobController {

    @Autowired
    private JobServiceFactory jobServiceFactory;
    @Autowired
    private WxMsgService wxMsgService;

    /**
     * 发布招聘
     */
    @LoginRequired
    @PostMapping("/publish")
    public R<?> publish(@RequestBody JobAddDTO jobAddDTO) {
        log.info("发布招聘：{}", jobAddDTO);
        CurrentUser currentUser = BaseContext.getCurrentUser();
        jobAddDTO.setUserId(currentUser.getId());
        Integer type = jobAddDTO.getType();
        BaseJobService<?> baseJobService = jobServiceFactory.create(type);
        baseJobService.publish(jobAddDTO);
        return R.success();
    }

    /**
     * 查询招聘信息列表
     *
     * @param type 招聘信息类型
     * @return 只返回状态为审核通过的招聘信息
     */
    @GetMapping("/list")
    public R<?> list(@RequestParam("type") Integer type,
                     @RequestParam(value = "page", required = false) Integer currentPage, @RequestParam(value = "keyword", required = false) String keyword) {
        log.info("查询求职信息列表 就业页面的接口...type={}", type);
        BaseJobService<?> baseJobService = jobServiceFactory.create(type);
        Page<?> page = baseJobService.listReviewSuccess(currentPage, keyword);
        JobListVo jobListVo = JobListVo.createByEntity(page, type);
        return R.success(jobListVo);
    }

    /**
     * 查询招聘信息列表(管理员)
     *
     * @param type   招聘信息类型
     * @param status 招聘信息状态
     * @return
     */
    @LoginRequired(requireAdmin = true)
    @GetMapping("/admin/list")
    public R<?> adminList(@RequestParam("type") Integer type,
                          @RequestParam(value = "page", required = false) Integer currentPage,
                          @RequestParam(value = "status") Integer status,
                          @RequestParam(value = "keyword", required = false) String keyword) {
        log.info("查询招聘信息列表(管理员)...type={}，status={}", type, status);
        // 判断状态是否合法
        if (!JobStatusConstant.isValid(status)) {
            return R.error(ErrorCodeEnum.JOB_STATUS_NOT_FOUND);
        }
        BaseJobService<?> baseJobService = jobServiceFactory.create(type);
        Page<?> page = baseJobService.listByStatus(currentPage, status, keyword);
        JobListVo jobListVo = JobListVo.createByEntity(page, type);
        return R.success(jobListVo);
    }


    /**
     * 查询我发布的招聘列表
     *
     * @param type        招聘信息类型
     * @param currentPage 当前页
     * @param status      招聘信息状态
     * @return
     */
    @LoginRequired
    @GetMapping("/mypublish/list")
    public R<?> mypublish(@RequestParam("type") Integer type,
                          @RequestParam(value = "page", required = false) Integer currentPage,
                          @RequestParam(value = "status", required = false) Integer status,
                          @RequestParam(value = "keyword", required = false) String keyword) {
        log.info("查询我发布的招聘列表...type={}，status={}", type, status);
        CurrentUser currentUser = BaseContext.getCurrentUser();
        // 判断状态是否合法
        if (status != null) {
            if (!JobStatusConstant.isValid(status)) {
                return R.error(ErrorCodeEnum.JOB_STATUS_NOT_FOUND);
            }
        }
        BaseJobService<?> baseJobService = jobServiceFactory.create(type);
        Page<?> page = baseJobService.listByUserIdAndStatus(currentPage, currentUser.getId(), status, keyword);
        JobListVo jobListVo = JobListVo.createByEntity(page, type);
        return R.success(jobListVo);
    }

    /**
     * 根据id查询信息详情
     *
     * @param type
     * @param informationId
     * @return
     */
    @GetMapping("/details")
    public R<?> details(@RequestParam("type") Integer type,
                        @RequestParam("information_id") Long informationId) {
        log.info("查询招聘信息详情...type={}，informationId={}", type, informationId);
        BaseJobService<?> baseJobService = jobServiceFactory.create(type);
        Object details = baseJobService.getDetailsById(informationId);
        return R.success(details);
    }

    /**
     * 审核就业内容
     *
     * @param jobDTO
     * @return
     */
    @LoginRequired(requireAdmin = true)
    @PostMapping("/admin/review")
    public R<?> review(@RequestBody JobDTO jobDTO) {
        log.info("审核就业：{}", jobDTO);
        Integer type = jobDTO.getType();
        // 判断状态是否合法
        if (!JobStatusConstant.isValid(jobDTO.getStatus())) {
            return R.error(ErrorCodeEnum.PARAM_IS_INVALID);
        }
        BaseJobService<?> baseJobService = jobServiceFactory.create(type);
        boolean result = baseJobService.updateStatus(jobDTO);
        if (!result) {
            return R.error(ErrorCodeEnum.UPDATE_FAILED);
        }
        // 发送审核通知
        String reason = jobDTO.getReason();
        ReviewPushDTO data = baseJobService.getReviewPushById(jobDTO.getInformationId());
        data.setResult(JobStatusConstant.toString(jobDTO.getStatus()));
        if (!StringUtils.isEmptyOrNull(reason)) {
            data.setNotes(reason);
        }
        wxMsgService.reviewPush(data);
        return R.success();
    }

    /**
     * 修改招聘信息
     *
     * @param
     * @return
     */
    @LoginRequired
    @PutMapping("/update")
    public R<?> update(@RequestBody JobAddDTO jobAddDTO) {
        log.info("修改招聘信息：{}", jobAddDTO);
        Integer type = jobAddDTO.getType();
        BaseJobService<?> baseJobService = jobServiceFactory.create(type);
        baseJobService.updateMsg(jobAddDTO);
        return R.success();
    }


    /**
     * 根据id删除招聘信息
     */
    @LoginRequired
    @DeleteMapping("/delete")
    public R<?> delete(@RequestParam("type") Integer type,
                       @RequestParam("information_id") Long informationId) {
        log.info("根据id删除招聘信息：type={},information_id={}", type, informationId);
        BaseJobService<?> baseJobService = jobServiceFactory.create(type);
        boolean result = baseJobService.deleteInfo(informationId);
        if (result) {
            return R.success();
        } else {
            return R.error(ErrorCodeEnum.DELETE_FAILED);
        }
    }
}
