package com.happy_hao.rums.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("group_permissions")
public class GroupPermission {
    @TableField("group_id")
    private Long groupId;

    @TableField("permission_id")
    private Long permissionId;

    @TableField("create_at")
    private Date createAt; // 创建时间

    @TableField("update_at")
    private Date updateAt; // 更新时间

    @TableField("extension")
    private String extension;
}
