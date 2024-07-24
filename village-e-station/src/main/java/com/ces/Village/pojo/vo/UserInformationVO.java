package com.ces.Village.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInformationVO {

    @JsonProperty("id_number")
    private String idNumber;

    @JsonProperty("village")
    private String village;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("nick_name")
    private String nickName;

    @JsonProperty("name")
    private String name;

    /**
     * 当前用户是否为管理员 1是2否
     */
    @JsonProperty("is_admin")
    private Integer isAdmin;
}
