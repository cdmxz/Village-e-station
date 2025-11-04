package com.ces.village.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表示当前用户
 */
@Data
// 提供无参和有参构造函数
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUser {
    private Long id;
    private boolean isAdmin;
}
