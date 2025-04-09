package com.happy_hao.rums.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("group_users")
public class GroupUser {

    @TableField("group_id")
    private Long groupId;

    @TableField("user_id")
    private Long userId;

    @TableField("create_at")
    private LocalDateTime createAt; // 创建时间

    @TableField("update_at")
    private LocalDateTime updateAt; // 更新时间

    @TableField("extension")
    private String extension;
}
