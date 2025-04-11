package com.happy_hao.rums.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("permissions")
public class Permission {
    @TableId("permission_id")
    private Long permissionId; // 主键ID

    @TableField("permission_name")
    private String permissionName; // 权限名

    @TableField("create_at")
    private Date createAt; // 创建时间

    @TableField("update_at")
    private Date updateAt; // 更新时间

    @TableField("description")
    private String description;

    @TableField("extension")
    private String extension;
}
