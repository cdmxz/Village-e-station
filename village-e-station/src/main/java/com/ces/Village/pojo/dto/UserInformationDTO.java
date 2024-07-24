package com.ces.Village.pojo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 修改用户信息dto
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInformationDTO {

    @JsonProperty("id_number")
    private String idNumber;

    @NotBlank(message = "乡村不能为空")
    @JsonProperty("village")
    private String village;

    @NotBlank(message = "手机号不能为空")
    @JsonProperty("phone")
    private String phone;

    @NotBlank(message = "头像不能为空")
    @JsonProperty("avatar_url")
    private String avatarUrl;

    @NotBlank(message = "昵称不能为空")
    @JsonProperty("nick_name")
    private String nickName;

    @NotBlank(message = "姓名不能为空")
    @JsonProperty("name")
    private String name;

    @JsonProperty("password")
    private String password;
}
